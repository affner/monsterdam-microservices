package com.fanflip.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.fanflip.admin.domain.AssistanceTicket;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link AssistanceTicket} entity.
 */
public interface AssistanceTicketSearchRepository
    extends ReactiveElasticsearchRepository<AssistanceTicket, Long>, AssistanceTicketSearchRepositoryInternal {}

interface AssistanceTicketSearchRepositoryInternal {
    Flux<AssistanceTicket> search(String query);

    Flux<AssistanceTicket> search(Query query);
}

class AssistanceTicketSearchRepositoryInternalImpl implements AssistanceTicketSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    AssistanceTicketSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<AssistanceTicket> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Flux<AssistanceTicket> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, AssistanceTicket.class).map(SearchHit::getContent);
    }
}
