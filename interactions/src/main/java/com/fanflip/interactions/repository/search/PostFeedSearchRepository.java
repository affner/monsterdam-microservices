package com.monsterdam.interactions.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.interactions.domain.PostFeed;
import com.monsterdam.interactions.repository.PostFeedRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link PostFeed} entity.
 */
public interface PostFeedSearchRepository extends ElasticsearchRepository<PostFeed, Long>, PostFeedSearchRepositoryInternal {}

interface PostFeedSearchRepositoryInternal {
    Page<PostFeed> search(String query, Pageable pageable);

    Page<PostFeed> search(Query query);

    @Async
    void index(PostFeed entity);

    @Async
    void deleteFromIndexById(Long id);
}

class PostFeedSearchRepositoryInternalImpl implements PostFeedSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final PostFeedRepository repository;

    PostFeedSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, PostFeedRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<PostFeed> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<PostFeed> search(Query query) {
        SearchHits<PostFeed> searchHits = elasticsearchTemplate.search(query, PostFeed.class);
        List<PostFeed> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(PostFeed entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), PostFeed.class);
    }
}
