package com.monsterdam.app.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.app.domain.UserUIPreferences;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link UserUIPreferences} entity.
 */
public interface UserUIPreferencesSearchRepository
    extends ReactiveElasticsearchRepository<UserUIPreferences, Long>, UserUIPreferencesSearchRepositoryInternal {}

interface UserUIPreferencesSearchRepositoryInternal {
    Flux<UserUIPreferences> search(String query, Pageable pageable);

    Flux<UserUIPreferences> search(Query query);
}

class UserUIPreferencesSearchRepositoryInternalImpl implements UserUIPreferencesSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    UserUIPreferencesSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<UserUIPreferences> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<UserUIPreferences> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, UserUIPreferences.class).map(SearchHit::getContent);
    }
}
