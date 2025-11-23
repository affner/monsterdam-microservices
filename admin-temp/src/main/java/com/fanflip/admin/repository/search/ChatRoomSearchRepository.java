package com.fanflip.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.fanflip.admin.domain.ChatRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link ChatRoom} entity.
 */
public interface ChatRoomSearchRepository extends ReactiveElasticsearchRepository<ChatRoom, Long>, ChatRoomSearchRepositoryInternal {}

interface ChatRoomSearchRepositoryInternal {
    Flux<ChatRoom> search(String query, Pageable pageable);

    Flux<ChatRoom> search(Query query);
}

class ChatRoomSearchRepositoryInternalImpl implements ChatRoomSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    ChatRoomSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<ChatRoom> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<ChatRoom> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, ChatRoom.class).map(SearchHit::getContent);
    }
}
