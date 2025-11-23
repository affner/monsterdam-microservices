package com.fanflip.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.fanflip.admin.domain.BookMark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link BookMark} entity.
 */
public interface BookMarkSearchRepository extends ReactiveElasticsearchRepository<BookMark, Long>, BookMarkSearchRepositoryInternal {}

interface BookMarkSearchRepositoryInternal {
    Flux<BookMark> search(String query, Pageable pageable);

    Flux<BookMark> search(Query query);
}

class BookMarkSearchRepositoryInternalImpl implements BookMarkSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    BookMarkSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<BookMark> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<BookMark> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, BookMark.class).map(SearchHit::getContent);
    }
}
