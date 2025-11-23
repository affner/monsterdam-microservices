package com.fanflip.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.fanflip.admin.domain.DocumentReviewObservation;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link DocumentReviewObservation} entity.
 */
public interface DocumentReviewObservationSearchRepository
    extends ReactiveElasticsearchRepository<DocumentReviewObservation, Long>, DocumentReviewObservationSearchRepositoryInternal {}

interface DocumentReviewObservationSearchRepositoryInternal {
    Flux<DocumentReviewObservation> search(String query);

    Flux<DocumentReviewObservation> search(Query query);
}

class DocumentReviewObservationSearchRepositoryInternalImpl implements DocumentReviewObservationSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    DocumentReviewObservationSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<DocumentReviewObservation> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Flux<DocumentReviewObservation> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, DocumentReviewObservation.class).map(SearchHit::getContent);
    }
}
