package com.fanflip.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.fanflip.admin.domain.SpecialReward;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link SpecialReward} entity.
 */
public interface SpecialRewardSearchRepository
    extends ReactiveElasticsearchRepository<SpecialReward, Long>, SpecialRewardSearchRepositoryInternal {}

interface SpecialRewardSearchRepositoryInternal {
    Flux<SpecialReward> search(String query, Pageable pageable);

    Flux<SpecialReward> search(Query query);
}

class SpecialRewardSearchRepositoryInternalImpl implements SpecialRewardSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    SpecialRewardSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<SpecialReward> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<SpecialReward> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, SpecialReward.class).map(SearchHit::getContent);
    }
}
