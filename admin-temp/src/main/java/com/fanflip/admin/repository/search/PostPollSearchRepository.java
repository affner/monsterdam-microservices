package com.fanflip.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.fanflip.admin.domain.PostPoll;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link PostPoll} entity.
 */
public interface PostPollSearchRepository extends ReactiveElasticsearchRepository<PostPoll, Long>, PostPollSearchRepositoryInternal {}

interface PostPollSearchRepositoryInternal {
    Flux<PostPoll> search(String query, Pageable pageable);

    Flux<PostPoll> search(Query query);
}

class PostPollSearchRepositoryInternalImpl implements PostPollSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    PostPollSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<PostPoll> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<PostPoll> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, PostPoll.class).map(SearchHit::getContent);
    }
}
