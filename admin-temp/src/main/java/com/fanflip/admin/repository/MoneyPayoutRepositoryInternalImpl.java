package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.MoneyPayout;
import com.monsterdam.admin.repository.rowmapper.CreatorEarningRowMapper;
import com.monsterdam.admin.repository.rowmapper.MoneyPayoutRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the MoneyPayout entity.
 */
@SuppressWarnings("unused")
class MoneyPayoutRepositoryInternalImpl extends SimpleR2dbcRepository<MoneyPayout, Long> implements MoneyPayoutRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CreatorEarningRowMapper creatorearningMapper;
    private final UserProfileRowMapper userprofileMapper;
    private final MoneyPayoutRowMapper moneypayoutMapper;

    private static final Table entityTable = Table.aliased("money_payout", EntityManager.ENTITY_ALIAS);
    private static final Table creatorEarningTable = Table.aliased("creator_earning", "creatorEarning");
    private static final Table creatorTable = Table.aliased("user_profile", "creator");

    public MoneyPayoutRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CreatorEarningRowMapper creatorearningMapper,
        UserProfileRowMapper userprofileMapper,
        MoneyPayoutRowMapper moneypayoutMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(MoneyPayout.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.creatorearningMapper = creatorearningMapper;
        this.userprofileMapper = userprofileMapper;
        this.moneypayoutMapper = moneypayoutMapper;
    }

    @Override
    public Flux<MoneyPayout> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<MoneyPayout> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = MoneyPayoutSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CreatorEarningSqlHelper.getColumns(creatorEarningTable, "creatorEarning"));
        columns.addAll(UserProfileSqlHelper.getColumns(creatorTable, "creator"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(creatorEarningTable)
            .on(Column.create("creator_earning_id", entityTable))
            .equals(Column.create("id", creatorEarningTable))
            .leftOuterJoin(creatorTable)
            .on(Column.create("creator_id", entityTable))
            .equals(Column.create("id", creatorTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, MoneyPayout.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<MoneyPayout> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<MoneyPayout> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<MoneyPayout> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<MoneyPayout> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<MoneyPayout> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private MoneyPayout process(Row row, RowMetadata metadata) {
        MoneyPayout entity = moneypayoutMapper.apply(row, "e");
        entity.setCreatorEarning(creatorearningMapper.apply(row, "creatorEarning"));
        entity.setCreator(userprofileMapper.apply(row, "creator"));
        return entity;
    }

    @Override
    public <S extends MoneyPayout> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
