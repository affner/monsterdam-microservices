package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.UserMention;
import com.monsterdam.admin.repository.rowmapper.PostCommentRowMapper;
import com.monsterdam.admin.repository.rowmapper.PostFeedRowMapper;
import com.monsterdam.admin.repository.rowmapper.UserMentionRowMapper;
import com.monsterdam.admin.repository.rowmapper.UserProfileRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the UserMention entity.
 */
@SuppressWarnings("unused")
class UserMentionRepositoryInternalImpl extends SimpleR2dbcRepository<UserMention, Long> implements UserMentionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PostFeedRowMapper postfeedMapper;
    private final PostCommentRowMapper postcommentMapper;
    private final UserProfileRowMapper userprofileMapper;
    private final UserMentionRowMapper usermentionMapper;

    private static final Table entityTable = Table.aliased("user_mention", EntityManager.ENTITY_ALIAS);
    private static final Table originPostTable = Table.aliased("post_feed", "originPost");
    private static final Table originPostCommentTable = Table.aliased("post_comment", "originPostComment");
    private static final Table mentionedUserTable = Table.aliased("user_profile", "mentionedUser");

    public UserMentionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PostFeedRowMapper postfeedMapper,
        PostCommentRowMapper postcommentMapper,
        UserProfileRowMapper userprofileMapper,
        UserMentionRowMapper usermentionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UserMention.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.postfeedMapper = postfeedMapper;
        this.postcommentMapper = postcommentMapper;
        this.userprofileMapper = userprofileMapper;
        this.usermentionMapper = usermentionMapper;
    }

    @Override
    public Flux<UserMention> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UserMention> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UserMentionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PostFeedSqlHelper.getColumns(originPostTable, "originPost"));
        columns.addAll(PostCommentSqlHelper.getColumns(originPostCommentTable, "originPostComment"));
        columns.addAll(UserProfileSqlHelper.getColumns(mentionedUserTable, "mentionedUser"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(originPostTable)
            .on(Column.create("origin_post_id", entityTable))
            .equals(Column.create("id", originPostTable))
            .leftOuterJoin(originPostCommentTable)
            .on(Column.create("origin_post_comment_id", entityTable))
            .equals(Column.create("id", originPostCommentTable))
            .leftOuterJoin(mentionedUserTable)
            .on(Column.create("mentioned_user_id", entityTable))
            .equals(Column.create("id", mentionedUserTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UserMention.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UserMention> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UserMention> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<UserMention> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<UserMention> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<UserMention> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private UserMention process(Row row, RowMetadata metadata) {
        UserMention entity = usermentionMapper.apply(row, "e");
        entity.setOriginPost(postfeedMapper.apply(row, "originPost"));
        entity.setOriginPostComment(postcommentMapper.apply(row, "originPostComment"));
        entity.setMentionedUser(userprofileMapper.apply(row, "mentionedUser"));
        return entity;
    }

    @Override
    public <S extends UserMention> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
