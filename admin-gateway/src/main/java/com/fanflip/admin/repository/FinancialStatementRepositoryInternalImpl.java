package com.fanflip.admin.repository;

import com.fanflip.admin.domain.AccountingRecord;
import com.fanflip.admin.domain.FinancialStatement;
import com.fanflip.admin.repository.rowmapper.FinancialStatementRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the FinancialStatement entity.
 */
@SuppressWarnings("unused")
class FinancialStatementRepositoryInternalImpl
    extends SimpleR2dbcRepository<FinancialStatement, Long>
    implements FinancialStatementRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final FinancialStatementRowMapper financialstatementMapper;

    private static final Table entityTable = Table.aliased("financial_statement", EntityManager.ENTITY_ALIAS);

    private static final EntityManager.LinkTable accountingRecordsLink = new EntityManager.LinkTable(
        "rel_financial_statement__accounting_records",
        "financial_statement_id",
        "accounting_records_id"
    );

    public FinancialStatementRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        FinancialStatementRowMapper financialstatementMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(FinancialStatement.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.financialstatementMapper = financialstatementMapper;
    }

    @Override
    public Flux<FinancialStatement> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<FinancialStatement> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = FinancialStatementSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, FinancialStatement.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<FinancialStatement> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<FinancialStatement> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<FinancialStatement> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<FinancialStatement> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<FinancialStatement> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private FinancialStatement process(Row row, RowMetadata metadata) {
        FinancialStatement entity = financialstatementMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends FinancialStatement> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends FinancialStatement> Mono<S> updateRelations(S entity) {
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
