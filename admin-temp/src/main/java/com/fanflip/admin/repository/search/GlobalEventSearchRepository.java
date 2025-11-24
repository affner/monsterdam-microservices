package com.monsterdam.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.admin.domain.GlobalEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link GlobalEvent} entity.
 */
public interface GlobalEventSearchRepository
    extends ReactiveElasticsearchRepository<GlobalEvent, Long>, GlobalEventSearchRepositoryInternal {}

interface GlobalEventSearchRepositoryInternal {
    Flux<GlobalEvent> search(String query, Pageable pageable);

    Flux<GlobalEvent> search(Query query);
}

class GlobalEventSearchRepositoryInternalImpl implements GlobalEventSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    GlobalEventSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<GlobalEvent> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<GlobalEvent> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, GlobalEvent.class).map(SearchHit::getContent);
    }
}
