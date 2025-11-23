package com.fanflip.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.fanflip.admin.domain.WalletTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link WalletTransaction} entity.
 */
public interface WalletTransactionSearchRepository
    extends ReactiveElasticsearchRepository<WalletTransaction, Long>, WalletTransactionSearchRepositoryInternal {}

interface WalletTransactionSearchRepositoryInternal {
    Flux<WalletTransaction> search(String query, Pageable pageable);

    Flux<WalletTransaction> search(Query query);
}

class WalletTransactionSearchRepositoryInternalImpl implements WalletTransactionSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    WalletTransactionSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<WalletTransaction> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<WalletTransaction> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, WalletTransaction.class).map(SearchHit::getContent);
    }
}
