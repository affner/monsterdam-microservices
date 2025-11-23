package com.fanflip.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.fanflip.admin.domain.LikeMark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link LikeMark} entity.
 */
public interface LikeMarkSearchRepository extends ReactiveElasticsearchRepository<LikeMark, Long>, LikeMarkSearchRepositoryInternal {}

interface LikeMarkSearchRepositoryInternal {
    Flux<LikeMark> search(String query, Pageable pageable);

    Flux<LikeMark> search(Query query);
}

class LikeMarkSearchRepositoryInternalImpl implements LikeMarkSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    LikeMarkSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<LikeMark> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<LikeMark> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, LikeMark.class).map(SearchHit::getContent);
    }
}
