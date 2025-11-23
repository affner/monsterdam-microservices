package com.fanflip.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.fanflip.admin.domain.PayoutMethod;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link PayoutMethod} entity.
 */
public interface PayoutMethodSearchRepository
    extends ReactiveElasticsearchRepository<PayoutMethod, Long>, PayoutMethodSearchRepositoryInternal {}

interface PayoutMethodSearchRepositoryInternal {
    Flux<PayoutMethod> search(String query, Pageable pageable);

    Flux<PayoutMethod> search(Query query);
}

class PayoutMethodSearchRepositoryInternalImpl implements PayoutMethodSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    PayoutMethodSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<PayoutMethod> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<PayoutMethod> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, PayoutMethod.class).map(SearchHit::getContent);
    }
}
