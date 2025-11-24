package com.monsterdam.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.admin.domain.SocialNetwork;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link SocialNetwork} entity.
 */
public interface SocialNetworkSearchRepository
    extends ReactiveElasticsearchRepository<SocialNetwork, Long>, SocialNetworkSearchRepositoryInternal {}

interface SocialNetworkSearchRepositoryInternal {
    Flux<SocialNetwork> search(String query, Pageable pageable);

    Flux<SocialNetwork> search(Query query);
}

class SocialNetworkSearchRepositoryInternalImpl implements SocialNetworkSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    SocialNetworkSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<SocialNetwork> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<SocialNetwork> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, SocialNetwork.class).map(SearchHit::getContent);
    }
}
