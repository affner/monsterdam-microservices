package com.fanflip.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.fanflip.admin.domain.PollOption;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link PollOption} entity.
 */
public interface PollOptionSearchRepository extends ReactiveElasticsearchRepository<PollOption, Long>, PollOptionSearchRepositoryInternal {}

interface PollOptionSearchRepositoryInternal {
    Flux<PollOption> search(String query, Pageable pageable);

    Flux<PollOption> search(Query query);
}

class PollOptionSearchRepositoryInternalImpl implements PollOptionSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    PollOptionSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<PollOption> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<PollOption> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, PollOption.class).map(SearchHit::getContent);
    }
}
