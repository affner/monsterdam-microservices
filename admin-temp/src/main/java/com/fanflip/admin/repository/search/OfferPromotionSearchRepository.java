package com.monsterdam.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.admin.domain.OfferPromotion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link OfferPromotion} entity.
 */
public interface OfferPromotionSearchRepository
    extends ReactiveElasticsearchRepository<OfferPromotion, Long>, OfferPromotionSearchRepositoryInternal {}

interface OfferPromotionSearchRepositoryInternal {
    Flux<OfferPromotion> search(String query, Pageable pageable);

    Flux<OfferPromotion> search(Query query);
}

class OfferPromotionSearchRepositoryInternalImpl implements OfferPromotionSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    OfferPromotionSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<OfferPromotion> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<OfferPromotion> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, OfferPromotion.class).map(SearchHit::getContent);
    }
}
