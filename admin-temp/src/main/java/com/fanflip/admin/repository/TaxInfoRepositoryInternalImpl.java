package com.fanflip.admin.repository;

import com.fanflip.admin.domain.TaxInfo;
import com.fanflip.admin.repository.rowmapper.CountryRowMapper;
import com.fanflip.admin.repository.rowmapper.TaxInfoRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the TaxInfo entity.
 */
@SuppressWarnings("unused")
class TaxInfoRepositoryInternalImpl extends SimpleR2dbcRepository<TaxInfo, Long> implements TaxInfoRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CountryRowMapper countryMapper;
    private final TaxInfoRowMapper taxinfoMapper;

    private static final Table entityTable = Table.aliased("tax_info", EntityManager.ENTITY_ALIAS);
    private static final Table countryTable = Table.aliased("country", "country");

    public TaxInfoRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CountryRowMapper countryMapper,
        TaxInfoRowMapper taxinfoMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(TaxInfo.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.countryMapper = countryMapper;
        this.taxinfoMapper = taxinfoMapper;
    }

    @Override
    public Flux<TaxInfo> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<TaxInfo> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = TaxInfoSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CountrySqlHelper.getColumns(countryTable, "country"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(countryTable)
            .on(Column.create("country_id", entityTable))
            .equals(Column.create("id", countryTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, TaxInfo.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<TaxInfo> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<TaxInfo> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<TaxInfo> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<TaxInfo> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<TaxInfo> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private TaxInfo process(Row row, RowMetadata metadata) {
        TaxInfo entity = taxinfoMapper.apply(row, "e");
        entity.setCountry(countryMapper.apply(row, "country"));
        return entity;
    }

    @Override
    public <S extends TaxInfo> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
