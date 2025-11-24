package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.State;
import com.monsterdam.admin.repository.rowmapper.CountryRowMapper;
import com.monsterdam.admin.repository.rowmapper.StateRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the State entity.
 */
@SuppressWarnings("unused")
class StateRepositoryInternalImpl extends SimpleR2dbcRepository<State, Long> implements StateRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CountryRowMapper countryMapper;
    private final StateRowMapper stateMapper;

    private static final Table entityTable = Table.aliased("state", EntityManager.ENTITY_ALIAS);
    private static final Table countryTable = Table.aliased("country", "country");

    public StateRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CountryRowMapper countryMapper,
        StateRowMapper stateMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(State.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.countryMapper = countryMapper;
        this.stateMapper = stateMapper;
    }

    @Override
    public Flux<State> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<State> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = StateSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CountrySqlHelper.getColumns(countryTable, "country"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(countryTable)
            .on(Column.create("country_id", entityTable))
            .equals(Column.create("id", countryTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, State.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<State> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<State> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<State> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<State> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<State> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private State process(Row row, RowMetadata metadata) {
        State entity = stateMapper.apply(row, "e");
        entity.setCountry(countryMapper.apply(row, "country"));
        return entity;
    }

    @Override
    public <S extends State> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
