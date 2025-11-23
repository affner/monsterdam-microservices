package com.fanflip.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.fanflip.admin.domain.EmojiType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link EmojiType} entity.
 */
public interface EmojiTypeSearchRepository extends ReactiveElasticsearchRepository<EmojiType, Long>, EmojiTypeSearchRepositoryInternal {}

interface EmojiTypeSearchRepositoryInternal {
    Flux<EmojiType> search(String query, Pageable pageable);

    Flux<EmojiType> search(Query query);
}

class EmojiTypeSearchRepositoryInternalImpl implements EmojiTypeSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    EmojiTypeSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<EmojiType> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<EmojiType> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, EmojiType.class).map(SearchHit::getContent);
    }
}
