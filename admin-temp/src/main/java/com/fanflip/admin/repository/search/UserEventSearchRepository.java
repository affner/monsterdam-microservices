package com.fanflip.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.fanflip.admin.domain.UserEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link UserEvent} entity.
 */
public interface UserEventSearchRepository extends ReactiveElasticsearchRepository<UserEvent, Long>, UserEventSearchRepositoryInternal {}

interface UserEventSearchRepositoryInternal {
    Flux<UserEvent> search(String query, Pageable pageable);

    Flux<UserEvent> search(Query query);
}

class UserEventSearchRepositoryInternalImpl implements UserEventSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    UserEventSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<UserEvent> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<UserEvent> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, UserEvent.class).map(SearchHit::getContent);
    }
}
