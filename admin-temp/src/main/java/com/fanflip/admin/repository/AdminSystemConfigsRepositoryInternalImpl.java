package com.fanflip.admin.repository;

import com.fanflip.admin.domain.AdminSystemConfigs;
import com.fanflip.admin.repository.rowmapper.AdminSystemConfigsRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the AdminSystemConfigs entity.
 */
@SuppressWarnings("unused")
class AdminSystemConfigsRepositoryInternalImpl
    extends SimpleR2dbcRepository<AdminSystemConfigs, Long>
    implements AdminSystemConfigsRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final AdminSystemConfigsRowMapper adminsystemconfigsMapper;

    private static final Table entityTable = Table.aliased("admin_system_configs", EntityManager.ENTITY_ALIAS);

    public AdminSystemConfigsRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        AdminSystemConfigsRowMapper adminsystemconfigsMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(AdminSystemConfigs.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.adminsystemconfigsMapper = adminsystemconfigsMapper;
    }

    @Override
    public Flux<AdminSystemConfigs> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<AdminSystemConfigs> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = AdminSystemConfigsSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, AdminSystemConfigs.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<AdminSystemConfigs> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<AdminSystemConfigs> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private AdminSystemConfigs process(Row row, RowMetadata metadata) {
        AdminSystemConfigs entity = adminsystemconfigsMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends AdminSystemConfigs> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
