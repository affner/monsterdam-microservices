package com.fanflip.admin.repository;

import com.fanflip.admin.domain.Notification;
import com.fanflip.admin.repository.rowmapper.NotificationRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Notification entity.
 */
@SuppressWarnings("unused")
class NotificationRepositoryInternalImpl extends SimpleR2dbcRepository<Notification, Long> implements NotificationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserProfileRowMapper userprofileMapper;
    private final NotificationRowMapper notificationMapper;

    private static final Table entityTable = Table.aliased("notification", EntityManager.ENTITY_ALIAS);
    private static final Table commentedUserTable = Table.aliased("user_profile", "commentedUser");
    private static final Table messagedUserTable = Table.aliased("user_profile", "messagedUser");
    private static final Table mentionerUserInPostTable = Table.aliased("user_profile", "mentionerUserInPost");
    private static final Table mentionerUserInCommentTable = Table.aliased("user_profile", "mentionerUserInComment");

    public NotificationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserProfileRowMapper userprofileMapper,
        NotificationRowMapper notificationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Notification.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userprofileMapper = userprofileMapper;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public Flux<Notification> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Notification> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = NotificationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserProfileSqlHelper.getColumns(commentedUserTable, "commentedUser"));
        columns.addAll(UserProfileSqlHelper.getColumns(messagedUserTable, "messagedUser"));
        columns.addAll(UserProfileSqlHelper.getColumns(mentionerUserInPostTable, "mentionerUserInPost"));
        columns.addAll(UserProfileSqlHelper.getColumns(mentionerUserInCommentTable, "mentionerUserInComment"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(commentedUserTable)
            .on(Column.create("commented_user_id", entityTable))
            .equals(Column.create("id", commentedUserTable))
            .leftOuterJoin(messagedUserTable)
            .on(Column.create("messaged_user_id", entityTable))
            .equals(Column.create("id", messagedUserTable))
            .leftOuterJoin(mentionerUserInPostTable)
            .on(Column.create("mentioner_user_in_post_id", entityTable))
            .equals(Column.create("id", mentionerUserInPostTable))
            .leftOuterJoin(mentionerUserInCommentTable)
            .on(Column.create("mentioner_user_in_comment_id", entityTable))
            .equals(Column.create("id", mentionerUserInCommentTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Notification.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Notification> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Notification> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Notification process(Row row, RowMetadata metadata) {
        Notification entity = notificationMapper.apply(row, "e");
        entity.setCommentedUser(userprofileMapper.apply(row, "commentedUser"));
        entity.setMessagedUser(userprofileMapper.apply(row, "messagedUser"));
        entity.setMentionerUserInPost(userprofileMapper.apply(row, "mentionerUserInPost"));
        entity.setMentionerUserInComment(userprofileMapper.apply(row, "mentionerUserInComment"));
        return entity;
    }

    @Override
    public <S extends Notification> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
