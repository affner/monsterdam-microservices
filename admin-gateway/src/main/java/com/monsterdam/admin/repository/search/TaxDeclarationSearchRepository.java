package com.monsterdam.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.admin.domain.TaxDeclaration;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link TaxDeclaration} entity.
 */
public interface TaxDeclarationSearchRepository
    extends ReactiveElasticsearchRepository<TaxDeclaration, Long>, TaxDeclarationSearchRepositoryInternal {}

interface TaxDeclarationSearchRepositoryInternal {
    Flux<TaxDeclaration> search(String query);

    Flux<TaxDeclaration> search(Query query);
}

class TaxDeclarationSearchRepositoryInternalImpl implements TaxDeclarationSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    TaxDeclarationSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<TaxDeclaration> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Flux<TaxDeclaration> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, TaxDeclaration.class).map(SearchHit::getContent);
    }
}
