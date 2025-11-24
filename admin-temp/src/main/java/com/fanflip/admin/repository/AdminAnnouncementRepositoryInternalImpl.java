package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.AdminAnnouncement;
import com.monsterdam.admin.repository.rowmapper.AdminAnnouncementRowMapper;
import com.monsterdam.admin.repository.rowmapper.AdminUserProfileRowMapper;
import com.monsterdam.admin.repository.rowmapper.DirectMessageRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the AdminAnnouncement entity.
 */
@SuppressWarnings("unused")
class AdminAnnouncementRepositoryInternalImpl
    extends SimpleR2dbcRepository<AdminAnnouncement, Long>
    implements AdminAnnouncementRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final DirectMessageRowMapper directmessageMapper;
    private final AdminUserProfileRowMapper adminuserprofileMapper;
    private final AdminAnnouncementRowMapper adminannouncementMapper;

    private static final Table entityTable = Table.aliased("admin_announcement", EntityManager.ENTITY_ALIAS);
    private static final Table announcerMessageTable = Table.aliased("direct_message", "announcerMessage");
    private static final Table adminTable = Table.aliased("admin_user_profile", "e_admin");

    public AdminAnnouncementRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        DirectMessageRowMapper directmessageMapper,
        AdminUserProfileRowMapper adminuserprofileMapper,
        AdminAnnouncementRowMapper adminannouncementMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(AdminAnnouncement.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.directmessageMapper = directmessageMapper;
        this.adminuserprofileMapper = adminuserprofileMapper;
        this.adminannouncementMapper = adminannouncementMapper;
    }

    @Override
    public Flux<AdminAnnouncement> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<AdminAnnouncement> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = AdminAnnouncementSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(DirectMessageSqlHelper.getColumns(announcerMessageTable, "announcerMessage"));
        columns.addAll(AdminUserProfileSqlHelper.getColumns(adminTable, "admin"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(announcerMessageTable)
            .on(Column.create("announcer_message_id", entityTable))
            .equals(Column.create("id", announcerMessageTable))
            .leftOuterJoin(adminTable)
            .on(Column.create("admin_id", entityTable))
            .equals(Column.create("id", adminTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, AdminAnnouncement.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<AdminAnnouncement> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<AdminAnnouncement> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private AdminAnnouncement process(Row row, RowMetadata metadata) {
        AdminAnnouncement entity = adminannouncementMapper.apply(row, "e");
        entity.setAnnouncerMessage(directmessageMapper.apply(row, "announcerMessage"));
        entity.setAdmin(adminuserprofileMapper.apply(row, "admin"));
        return entity;
    }

    @Override
    public <S extends AdminAnnouncement> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
