package com.fanflip.admin.repository;

import com.fanflip.admin.domain.DirectMessage;
import com.fanflip.admin.repository.rowmapper.ContentPackageRowMapper;
import com.fanflip.admin.repository.rowmapper.DirectMessageRowMapper;
import com.fanflip.admin.repository.rowmapper.DirectMessageRowMapper;
import com.fanflip.admin.repository.rowmapper.UserProfileRowMapper;
import com.fanflip.admin.repository.rowmapper.VideoStoryRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the DirectMessage entity.
 */
@SuppressWarnings("unused")
class DirectMessageRepositoryInternalImpl extends SimpleR2dbcRepository<DirectMessage, Long> implements DirectMessageRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ContentPackageRowMapper contentpackageMapper;
    private final DirectMessageRowMapper directmessageMapper;
    private final VideoStoryRowMapper videostoryMapper;
    private final UserProfileRowMapper userprofileMapper;

    private static final Table entityTable = Table.aliased("direct_message", EntityManager.ENTITY_ALIAS);
    private static final Table contentPackageTable = Table.aliased("content_package", "contentPackage");
    private static final Table responseToTable = Table.aliased("direct_message", "responseTo");
    private static final Table repliedStoryTable = Table.aliased("video_story", "repliedStory");
    private static final Table userTable = Table.aliased("user_profile", "e_user");

    public DirectMessageRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ContentPackageRowMapper contentpackageMapper,
        DirectMessageRowMapper directmessageMapper,
        VideoStoryRowMapper videostoryMapper,
        UserProfileRowMapper userprofileMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(DirectMessage.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.contentpackageMapper = contentpackageMapper;
        this.directmessageMapper = directmessageMapper;
        this.videostoryMapper = videostoryMapper;
        this.userprofileMapper = userprofileMapper;
    }

    @Override
    public Flux<DirectMessage> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<DirectMessage> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = DirectMessageSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ContentPackageSqlHelper.getColumns(contentPackageTable, "contentPackage"));
        columns.addAll(DirectMessageSqlHelper.getColumns(responseToTable, "responseTo"));
        columns.addAll(VideoStorySqlHelper.getColumns(repliedStoryTable, "repliedStory"));
        columns.addAll(UserProfileSqlHelper.getColumns(userTable, "user"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(contentPackageTable)
            .on(Column.create("content_package_id", entityTable))
            .equals(Column.create("id", contentPackageTable))
            .leftOuterJoin(responseToTable)
            .on(Column.create("response_to_id", entityTable))
            .equals(Column.create("id", responseToTable))
            .leftOuterJoin(repliedStoryTable)
            .on(Column.create("replied_story_id", entityTable))
            .equals(Column.create("id", repliedStoryTable))
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, DirectMessage.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<DirectMessage> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<DirectMessage> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<DirectMessage> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<DirectMessage> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<DirectMessage> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private DirectMessage process(Row row, RowMetadata metadata) {
        DirectMessage entity = directmessageMapper.apply(row, "e");
        entity.setContentPackage(contentpackageMapper.apply(row, "contentPackage"));
        entity.setResponseTo(directmessageMapper.apply(row, "responseTo"));
        entity.setRepliedStory(videostoryMapper.apply(row, "repliedStory"));
        entity.setUser(userprofileMapper.apply(row, "user"));
        return entity;
    }

    @Override
    public <S extends DirectMessage> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
