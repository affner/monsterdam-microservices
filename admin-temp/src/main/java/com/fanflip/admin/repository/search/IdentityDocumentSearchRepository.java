package com.monsterdam.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.admin.domain.IdentityDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link IdentityDocument} entity.
 */
public interface IdentityDocumentSearchRepository
    extends ReactiveElasticsearchRepository<IdentityDocument, Long>, IdentityDocumentSearchRepositoryInternal {}

interface IdentityDocumentSearchRepositoryInternal {
    Flux<IdentityDocument> search(String query, Pageable pageable);

    Flux<IdentityDocument> search(Query query);
}

class IdentityDocumentSearchRepositoryInternalImpl implements IdentityDocumentSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    IdentityDocumentSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<IdentityDocument> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<IdentityDocument> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, IdentityDocument.class).map(SearchHit::getContent);
    }
}
