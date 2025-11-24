package com.monsterdam.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.admin.domain.ContentPackage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link ContentPackage} entity.
 */
public interface ContentPackageSearchRepository
    extends ReactiveElasticsearchRepository<ContentPackage, Long>, ContentPackageSearchRepositoryInternal {}

interface ContentPackageSearchRepositoryInternal {
    Flux<ContentPackage> search(String query, Pageable pageable);

    Flux<ContentPackage> search(Query query);
}

class ContentPackageSearchRepositoryInternalImpl implements ContentPackageSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    ContentPackageSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<ContentPackage> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<ContentPackage> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, ContentPackage.class).map(SearchHit::getContent);
    }
}
