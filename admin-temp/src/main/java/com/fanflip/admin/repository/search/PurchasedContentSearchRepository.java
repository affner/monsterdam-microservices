package com.monsterdam.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.admin.domain.PurchasedContent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link PurchasedContent} entity.
 */
public interface PurchasedContentSearchRepository
    extends ReactiveElasticsearchRepository<PurchasedContent, Long>, PurchasedContentSearchRepositoryInternal {}

interface PurchasedContentSearchRepositoryInternal {
    Flux<PurchasedContent> search(String query, Pageable pageable);

    Flux<PurchasedContent> search(Query query);
}

class PurchasedContentSearchRepositoryInternalImpl implements PurchasedContentSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    PurchasedContentSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<PurchasedContent> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<PurchasedContent> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, PurchasedContent.class).map(SearchHit::getContent);
    }
}
