package com.monsterdam.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.admin.domain.AccountingRecord;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link AccountingRecord} entity.
 */
public interface AccountingRecordSearchRepository
    extends ReactiveElasticsearchRepository<AccountingRecord, Long>, AccountingRecordSearchRepositoryInternal {}

interface AccountingRecordSearchRepositoryInternal {
    Flux<AccountingRecord> search(String query);

    Flux<AccountingRecord> search(Query query);
}

class AccountingRecordSearchRepositoryInternalImpl implements AccountingRecordSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    AccountingRecordSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<AccountingRecord> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Flux<AccountingRecord> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, AccountingRecord.class).map(SearchHit::getContent);
    }
}
