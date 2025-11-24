package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.AssistanceTicket;
import com.monsterdam.admin.repository.rowmapper.AdminUserProfileRowMapper;
import com.monsterdam.admin.repository.rowmapper.AssistanceTicketRowMapper;
import com.monsterdam.admin.repository.rowmapper.ModerationActionRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the AssistanceTicket entity.
 */
@SuppressWarnings("unused")
class AssistanceTicketRepositoryInternalImpl
    extends SimpleR2dbcRepository<AssistanceTicket, Long>
    implements AssistanceTicketRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ModerationActionRowMapper moderationactionMapper;
    private final AdminUserProfileRowMapper adminuserprofileMapper;
    private final UserProfileRowMapper userprofileMapper;
    private final AssistanceTicketRowMapper assistanceticketMapper;

    private static final Table entityTable = Table.aliased("assistance_ticket", EntityManager.ENTITY_ALIAS);
    private static final Table moderationActionTable = Table.aliased("moderation_action", "moderationAction");
    private static final Table assignedAdminTable = Table.aliased("admin_user_profile", "assignedAdmin");
    private static final Table userTable = Table.aliased("user_profile", "e_user");

    public AssistanceTicketRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ModerationActionRowMapper moderationactionMapper,
        AdminUserProfileRowMapper adminuserprofileMapper,
        UserProfileRowMapper userprofileMapper,
        AssistanceTicketRowMapper assistanceticketMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(AssistanceTicket.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.moderationactionMapper = moderationactionMapper;
        this.adminuserprofileMapper = adminuserprofileMapper;
        this.userprofileMapper = userprofileMapper;
        this.assistanceticketMapper = assistanceticketMapper;
    }

    @Override
    public Flux<AssistanceTicket> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<AssistanceTicket> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = AssistanceTicketSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ModerationActionSqlHelper.getColumns(moderationActionTable, "moderationAction"));
        columns.addAll(AdminUserProfileSqlHelper.getColumns(assignedAdminTable, "assignedAdmin"));
        columns.addAll(UserProfileSqlHelper.getColumns(userTable, "user"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(moderationActionTable)
            .on(Column.create("moderation_action_id", entityTable))
            .equals(Column.create("id", moderationActionTable))
            .leftOuterJoin(assignedAdminTable)
            .on(Column.create("assigned_admin_id", entityTable))
            .equals(Column.create("id", assignedAdminTable))
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, AssistanceTicket.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<AssistanceTicket> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<AssistanceTicket> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private AssistanceTicket process(Row row, RowMetadata metadata) {
        AssistanceTicket entity = assistanceticketMapper.apply(row, "e");
        entity.setModerationAction(moderationactionMapper.apply(row, "moderationAction"));
        entity.setAssignedAdmin(adminuserprofileMapper.apply(row, "assignedAdmin"));
        entity.setUser(userprofileMapper.apply(row, "user"));
        return entity;
    }

    @Override
    public <S extends AssistanceTicket> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
