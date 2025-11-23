package com.fanflip.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.fanflip.admin.domain.Feedback;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link Feedback} entity.
 */
public interface FeedbackSearchRepository extends ReactiveElasticsearchRepository<Feedback, Long>, FeedbackSearchRepositoryInternal {}

interface FeedbackSearchRepositoryInternal {
    Flux<Feedback> search(String query, Pageable pageable);

    Flux<Feedback> search(Query query);
}

class FeedbackSearchRepositoryInternalImpl implements FeedbackSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    FeedbackSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<Feedback> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<Feedback> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, Feedback.class).map(SearchHit::getContent);
    }
}
