package com.monsterdam.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.admin.domain.PaymentProvider;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link PaymentProvider} entity.
 */
public interface PaymentProviderSearchRepository
    extends ReactiveElasticsearchRepository<PaymentProvider, Long>, PaymentProviderSearchRepositoryInternal {}

interface PaymentProviderSearchRepositoryInternal {
    Flux<PaymentProvider> search(String query, Pageable pageable);

    Flux<PaymentProvider> search(Query query);
}

class PaymentProviderSearchRepositoryInternalImpl implements PaymentProviderSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    PaymentProviderSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<PaymentProvider> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<PaymentProvider> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, PaymentProvider.class).map(SearchHit::getContent);
    }
}
