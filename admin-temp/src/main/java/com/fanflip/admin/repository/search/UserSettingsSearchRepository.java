package com.monsterdam.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.admin.domain.UserSettings;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link UserSettings} entity.
 */
public interface UserSettingsSearchRepository
    extends ReactiveElasticsearchRepository<UserSettings, Long>, UserSettingsSearchRepositoryInternal {}

interface UserSettingsSearchRepositoryInternal {
    Flux<UserSettings> search(String query, Pageable pageable);

    Flux<UserSettings> search(Query query);
}

class UserSettingsSearchRepositoryInternalImpl implements UserSettingsSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    UserSettingsSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<UserSettings> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<UserSettings> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, UserSettings.class).map(SearchHit::getContent);
    }
}
