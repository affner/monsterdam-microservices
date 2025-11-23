package com.fanflip.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.fanflip.admin.domain.SpecialTitle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link SpecialTitle} entity.
 */
public interface SpecialTitleSearchRepository
    extends ReactiveElasticsearchRepository<SpecialTitle, Long>, SpecialTitleSearchRepositoryInternal {}

interface SpecialTitleSearchRepositoryInternal {
    Flux<SpecialTitle> search(String query, Pageable pageable);

    Flux<SpecialTitle> search(Query query);
}

class SpecialTitleSearchRepositoryInternalImpl implements SpecialTitleSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    SpecialTitleSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<SpecialTitle> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<SpecialTitle> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, SpecialTitle.class).map(SearchHit::getContent);
    }
}
