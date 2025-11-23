package com.fanflip.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.fanflip.admin.domain.AdminSystemConfigs;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link AdminSystemConfigs} entity.
 */
public interface AdminSystemConfigsSearchRepository
    extends ReactiveElasticsearchRepository<AdminSystemConfigs, Long>, AdminSystemConfigsSearchRepositoryInternal {}

interface AdminSystemConfigsSearchRepositoryInternal {
    Flux<AdminSystemConfigs> search(String query, Pageable pageable);

    Flux<AdminSystemConfigs> search(Query query);
}

class AdminSystemConfigsSearchRepositoryInternalImpl implements AdminSystemConfigsSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    AdminSystemConfigsSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<AdminSystemConfigs> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<AdminSystemConfigs> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, AdminSystemConfigs.class).map(SearchHit::getContent);
    }
}
