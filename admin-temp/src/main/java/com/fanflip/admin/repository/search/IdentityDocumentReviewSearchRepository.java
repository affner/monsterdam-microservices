package com.monsterdam.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.admin.domain.IdentityDocumentReview;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link IdentityDocumentReview} entity.
 */
public interface IdentityDocumentReviewSearchRepository
    extends ReactiveElasticsearchRepository<IdentityDocumentReview, Long>, IdentityDocumentReviewSearchRepositoryInternal {}

interface IdentityDocumentReviewSearchRepositoryInternal {
    Flux<IdentityDocumentReview> search(String query, Pageable pageable);

    Flux<IdentityDocumentReview> search(Query query);
}

class IdentityDocumentReviewSearchRepositoryInternalImpl implements IdentityDocumentReviewSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    IdentityDocumentReviewSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<IdentityDocumentReview> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<IdentityDocumentReview> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, IdentityDocumentReview.class).map(SearchHit::getContent);
    }
}
