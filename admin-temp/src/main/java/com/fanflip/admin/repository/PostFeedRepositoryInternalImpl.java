package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.HashTag;
import com.monsterdam.admin.domain.PostFeed;
import com.monsterdam.admin.repository.rowmapper.ContentPackageRowMapper;
import com.monsterdam.admin.repository.rowmapper.PostFeedRowMapper;
import com.monsterdam.admin.repository.rowmapper.PostPollRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the PostFeed entity.
 */
@SuppressWarnings("unused")
class PostFeedRepositoryInternalImpl extends SimpleR2dbcRepository<PostFeed, Long> implements PostFeedRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PostPollRowMapper postpollMapper;
    private final ContentPackageRowMapper contentpackageMapper;
    private final UserProfileRowMapper userprofileMapper;
    private final PostFeedRowMapper postfeedMapper;

    private static final Table entityTable = Table.aliased("post_feed", EntityManager.ENTITY_ALIAS);
    private static final Table pollTable = Table.aliased("post_poll", "poll");
    private static final Table contentPackageTable = Table.aliased("content_package", "contentPackage");
    private static final Table creatorTable = Table.aliased("user_profile", "creator");

    private static final EntityManager.LinkTable hashTagsLink = new EntityManager.LinkTable(
        "rel_post_feed__hash_tags",
        "post_feed_id",
        "hash_tags_id"
    );

    public PostFeedRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PostPollRowMapper postpollMapper,
        ContentPackageRowMapper contentpackageMapper,
        UserProfileRowMapper userprofileMapper,
        PostFeedRowMapper postfeedMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(PostFeed.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.postpollMapper = postpollMapper;
        this.contentpackageMapper = contentpackageMapper;
        this.userprofileMapper = userprofileMapper;
        this.postfeedMapper = postfeedMapper;
    }

    @Override
    public Flux<PostFeed> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<PostFeed> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PostFeedSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PostPollSqlHelper.getColumns(pollTable, "poll"));
        columns.addAll(ContentPackageSqlHelper.getColumns(contentPackageTable, "contentPackage"));
        columns.addAll(UserProfileSqlHelper.getColumns(creatorTable, "creator"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(pollTable)
            .on(Column.create("poll_id", entityTable))
            .equals(Column.create("id", pollTable))
            .leftOuterJoin(contentPackageTable)
            .on(Column.create("content_package_id", entityTable))
            .equals(Column.create("id", contentPackageTable))
            .leftOuterJoin(creatorTable)
            .on(Column.create("creator_id", entityTable))
            .equals(Column.create("id", creatorTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, PostFeed.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<PostFeed> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<PostFeed> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<PostFeed> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<PostFeed> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<PostFeed> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private PostFeed process(Row row, RowMetadata metadata) {
        PostFeed entity = postfeedMapper.apply(row, "e");
        entity.setPoll(postpollMapper.apply(row, "poll"));
        entity.setContentPackage(contentpackageMapper.apply(row, "contentPackage"));
        entity.setCreator(userprofileMapper.apply(row, "creator"));
        return entity;
    }

    @Override
    public <S extends PostFeed> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends PostFeed> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(hashTagsLink, entity.getId(), entity.getHashTags().stream().map(HashTag::getId))
            .then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(hashTagsLink, entityId);
    }
}
