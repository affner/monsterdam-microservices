package com.monsterdam.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.admin.domain.Currency;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link Currency} entity.
 */
public interface CurrencySearchRepository extends ReactiveElasticsearchRepository<Currency, Long>, CurrencySearchRepositoryInternal {}

interface CurrencySearchRepositoryInternal {
    Flux<Currency> search(String query, Pageable pageable);

    Flux<Currency> search(Query query);
}

class CurrencySearchRepositoryInternalImpl implements CurrencySearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    CurrencySearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<Currency> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<Currency> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, Currency.class).map(SearchHit::getContent);
    }
}
