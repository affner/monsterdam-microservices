package com.monsterdam.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.admin.domain.HelpCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link HelpCategory} entity.
 */
public interface HelpCategorySearchRepository
    extends ReactiveElasticsearchRepository<HelpCategory, Long>, HelpCategorySearchRepositoryInternal {}

interface HelpCategorySearchRepositoryInternal {
    Flux<HelpCategory> search(String query, Pageable pageable);

    Flux<HelpCategory> search(Query query);
}

class HelpCategorySearchRepositoryInternalImpl implements HelpCategorySearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    HelpCategorySearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<HelpCategory> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<HelpCategory> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, HelpCategory.class).map(SearchHit::getContent);
    }
}
