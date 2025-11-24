package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.ModerationAction;
import com.monsterdam.admin.repository.rowmapper.ModerationActionRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the ModerationAction entity.
 */
@SuppressWarnings("unused")
class ModerationActionRepositoryInternalImpl
    extends SimpleR2dbcRepository<ModerationAction, Long>
    implements ModerationActionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ModerationActionRowMapper moderationactionMapper;

    private static final Table entityTable = Table.aliased("moderation_action", EntityManager.ENTITY_ALIAS);

    public ModerationActionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ModerationActionRowMapper moderationactionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ModerationAction.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.moderationactionMapper = moderationactionMapper;
    }

    @Override
    public Flux<ModerationAction> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ModerationAction> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ModerationActionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ModerationAction.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ModerationAction> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ModerationAction> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private ModerationAction process(Row row, RowMetadata metadata) {
        ModerationAction entity = moderationactionMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends ModerationAction> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
