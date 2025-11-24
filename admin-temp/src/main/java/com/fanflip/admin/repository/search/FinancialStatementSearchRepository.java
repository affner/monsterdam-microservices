package com.monsterdam.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.admin.domain.FinancialStatement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link FinancialStatement} entity.
 */
public interface FinancialStatementSearchRepository
    extends ReactiveElasticsearchRepository<FinancialStatement, Long>, FinancialStatementSearchRepositoryInternal {}

interface FinancialStatementSearchRepositoryInternal {
    Flux<FinancialStatement> search(String query, Pageable pageable);

    Flux<FinancialStatement> search(Query query);
}

class FinancialStatementSearchRepositoryInternalImpl implements FinancialStatementSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    FinancialStatementSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<FinancialStatement> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<FinancialStatement> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, FinancialStatement.class).map(SearchHit::getContent);
    }
}
