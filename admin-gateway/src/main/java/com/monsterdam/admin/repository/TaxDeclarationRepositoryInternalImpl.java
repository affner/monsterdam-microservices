package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.AccountingRecord;
import com.monsterdam.admin.domain.TaxDeclaration;
import com.monsterdam.admin.repository.rowmapper.TaxDeclarationRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the TaxDeclaration entity.
 */
@SuppressWarnings("unused")
class TaxDeclarationRepositoryInternalImpl extends SimpleR2dbcRepository<TaxDeclaration, Long> implements TaxDeclarationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TaxDeclarationRowMapper taxdeclarationMapper;

    private static final Table entityTable = Table.aliased("tax_declaration", EntityManager.ENTITY_ALIAS);

    private static final EntityManager.LinkTable accountingRecordsLink = new EntityManager.LinkTable(
        "rel_tax_declaration__accounting_records",
        "tax_declaration_id",
        "accounting_records_id"
    );

    public TaxDeclarationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TaxDeclarationRowMapper taxdeclarationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(TaxDeclaration.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.taxdeclarationMapper = taxdeclarationMapper;
    }

    @Override
    public Flux<TaxDeclaration> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<TaxDeclaration> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = TaxDeclarationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, TaxDeclaration.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<TaxDeclaration> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<TaxDeclaration> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<TaxDeclaration> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<TaxDeclaration> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<TaxDeclaration> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private TaxDeclaration process(Row row, RowMetadata metadata) {
        TaxDeclaration entity = taxdeclarationMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends TaxDeclaration> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends TaxDeclaration> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(accountingRecordsLink, entity.getId(), entity.getAccountingRecords().stream().map(AccountingRecord::getId))
            .then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(accountingRecordsLink, entityId);
    }
}
