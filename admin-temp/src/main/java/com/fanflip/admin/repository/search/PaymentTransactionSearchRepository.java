package com.monsterdam.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.admin.domain.PaymentTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link PaymentTransaction} entity.
 */
public interface PaymentTransactionSearchRepository
    extends ReactiveElasticsearchRepository<PaymentTransaction, Long>, PaymentTransactionSearchRepositoryInternal {}

interface PaymentTransactionSearchRepositoryInternal {
    Flux<PaymentTransaction> search(String query, Pageable pageable);

    Flux<PaymentTransaction> search(Query query);
}

class PaymentTransactionSearchRepositoryInternalImpl implements PaymentTransactionSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    PaymentTransactionSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<PaymentTransaction> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<PaymentTransaction> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, PaymentTransaction.class).map(SearchHit::getContent);
    }
}
