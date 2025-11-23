package com.fanflip.interactions.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.fanflip.interactions.domain.PostComment;
import com.fanflip.interactions.repository.PostCommentRepository;
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
 * Spring Data Elasticsearch repository for the {@link PostComment} entity.
 */
public interface PostCommentSearchRepository extends ElasticsearchRepository<PostComment, Long>, PostCommentSearchRepositoryInternal {}

interface PostCommentSearchRepositoryInternal {
    Page<PostComment> search(String query, Pageable pageable);

    Page<PostComment> search(Query query);

    @Async
    void index(PostComment entity);

    @Async
    void deleteFromIndexById(Long id);
}

class PostCommentSearchRepositoryInternalImpl implements PostCommentSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final PostCommentRepository repository;

    PostCommentSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, PostCommentRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<PostComment> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<PostComment> search(Query query) {
        SearchHits<PostComment> searchHits = elasticsearchTemplate.search(query, PostComment.class);
        List<PostComment> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(PostComment entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), PostComment.class);
    }
}
