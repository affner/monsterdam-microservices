package com.monsterdam.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.admin.domain.SingleVideo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link SingleVideo} entity.
 */
public interface SingleVideoSearchRepository
    extends ReactiveElasticsearchRepository<SingleVideo, Long>, SingleVideoSearchRepositoryInternal {}

interface SingleVideoSearchRepositoryInternal {
    Flux<SingleVideo> search(String query, Pageable pageable);

    Flux<SingleVideo> search(Query query);
}

class SingleVideoSearchRepositoryInternalImpl implements SingleVideoSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    SingleVideoSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<SingleVideo> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<SingleVideo> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, SingleVideo.class).map(SearchHit::getContent);
    }
}
