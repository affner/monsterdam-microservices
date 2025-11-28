package com.monsterdam.profile.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.monsterdam.profile.domain.HashTag;
import com.monsterdam.profile.repository.HashTagRepository;
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
 * Spring Data Elasticsearch repository for the {@link HashTag} entity.
 */
public interface HashTagSearchRepository extends ElasticsearchRepository<HashTag, Long>, HashTagSearchRepositoryInternal {}

interface HashTagSearchRepositoryInternal {
    Page<HashTag> search(String query, Pageable pageable);

    Page<HashTag> search(Query query);

    @Async
    void index(HashTag entity);

    @Async
    void deleteFromIndexById(Long id);
}

class HashTagSearchRepositoryInternalImpl implements HashTagSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final HashTagRepository repository;

    HashTagSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, HashTagRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<HashTag> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<HashTag> search(Query query) {
        SearchHits<HashTag> searchHits = elasticsearchTemplate.search(query, HashTag.class);
        List<HashTag> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(HashTag entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), HashTag.class);
    }
}
