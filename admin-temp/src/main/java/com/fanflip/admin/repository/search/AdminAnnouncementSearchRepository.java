package com.monsterdam.admin.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.admin.domain.AdminAnnouncement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link AdminAnnouncement} entity.
 */
public interface AdminAnnouncementSearchRepository
    extends ReactiveElasticsearchRepository<AdminAnnouncement, Long>, AdminAnnouncementSearchRepositoryInternal {}

interface AdminAnnouncementSearchRepositoryInternal {
    Flux<AdminAnnouncement> search(String query, Pageable pageable);

    Flux<AdminAnnouncement> search(Query query);
}

class AdminAnnouncementSearchRepositoryInternalImpl implements AdminAnnouncementSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    AdminAnnouncementSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<AdminAnnouncement> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<AdminAnnouncement> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, AdminAnnouncement.class).map(SearchHit::getContent);
    }
}
