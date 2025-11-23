package com.fanflip.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.fanflip.admin.domain.HelpRelatedArticle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link HelpRelatedArticle} entity.
 */
public interface HelpRelatedArticleSearchRepository
    extends ReactiveElasticsearchRepository<HelpRelatedArticle, Long>, HelpRelatedArticleSearchRepositoryInternal {}

interface HelpRelatedArticleSearchRepositoryInternal {
    Flux<HelpRelatedArticle> search(String query, Pageable pageable);

    Flux<HelpRelatedArticle> search(Query query);
}

class HelpRelatedArticleSearchRepositoryInternalImpl implements HelpRelatedArticleSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    HelpRelatedArticleSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<HelpRelatedArticle> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<HelpRelatedArticle> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, HelpRelatedArticle.class).map(SearchHit::getContent);
    }
}
