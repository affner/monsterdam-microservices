package com.fanflip.admin.repository;

import com.fanflip.admin.domain.PostComment;
import com.fanflip.admin.repository.rowmapper.PostCommentRowMapper;
import com.fanflip.admin.repository.rowmapper.PostCommentRowMapper;
import com.fanflip.admin.repository.rowmapper.PostFeedRowMapper;
import com.fanflip.admin.repository.rowmapper.UserProfileRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the PostComment entity.
 */
@SuppressWarnings("unused")
class PostCommentRepositoryInternalImpl extends SimpleR2dbcRepository<PostComment, Long> implements PostCommentRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PostFeedRowMapper postfeedMapper;
    private final PostCommentRowMapper postcommentMapper;
    private final UserProfileRowMapper userprofileMapper;

    private static final Table entityTable = Table.aliased("post_comment", EntityManager.ENTITY_ALIAS);
    private static final Table postTable = Table.aliased("post_feed", "post");
    private static final Table responseToTable = Table.aliased("post_comment", "responseTo");
    private static final Table commenterTable = Table.aliased("user_profile", "commenter");

    public PostCommentRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PostFeedRowMapper postfeedMapper,
        PostCommentRowMapper postcommentMapper,
        UserProfileRowMapper userprofileMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(PostComment.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.postfeedMapper = postfeedMapper;
        this.postcommentMapper = postcommentMapper;
        this.userprofileMapper = userprofileMapper;
    }

    @Override
    public Flux<PostComment> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<PostComment> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PostCommentSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PostFeedSqlHelper.getColumns(postTable, "post"));
        columns.addAll(PostCommentSqlHelper.getColumns(responseToTable, "responseTo"));
        columns.addAll(UserProfileSqlHelper.getColumns(commenterTable, "commenter"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(postTable)
            .on(Column.create("post_id", entityTable))
            .equals(Column.create("id", postTable))
            .leftOuterJoin(responseToTable)
            .on(Column.create("response_to_id", entityTable))
            .equals(Column.create("id", responseToTable))
            .leftOuterJoin(commenterTable)
            .on(Column.create("commenter_id", entityTable))
            .equals(Column.create("id", commenterTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, PostComment.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<PostComment> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<PostComment> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<PostComment> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<PostComment> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<PostComment> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private PostComment process(Row row, RowMetadata metadata) {
        PostComment entity = postcommentMapper.apply(row, "e");
        entity.setPost(postfeedMapper.apply(row, "post"));
        entity.setResponseTo(postcommentMapper.apply(row, "responseTo"));
        entity.setCommenter(userprofileMapper.apply(row, "commenter"));
        return entity;
    }

    @Override
    public <S extends PostComment> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
