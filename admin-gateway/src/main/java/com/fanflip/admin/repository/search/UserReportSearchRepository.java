package com.fanflip.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.fanflip.admin.domain.UserReport;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link UserReport} entity.
 */
public interface UserReportSearchRepository extends ReactiveElasticsearchRepository<UserReport, Long>, UserReportSearchRepositoryInternal {}

interface UserReportSearchRepositoryInternal {
    Flux<UserReport> search(String query);

    Flux<UserReport> search(Query query);
}

class UserReportSearchRepositoryInternalImpl implements UserReportSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    UserReportSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<UserReport> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Flux<UserReport> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, UserReport.class).map(SearchHit::getContent);
    }
}
