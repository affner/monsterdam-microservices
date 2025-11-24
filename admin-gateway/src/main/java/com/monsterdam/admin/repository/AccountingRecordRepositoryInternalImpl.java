package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.AccountingRecord;
import com.monsterdam.admin.repository.rowmapper.AccountingRecordRowMapper;
import com.monsterdam.admin.repository.rowmapper.AssetRowMapper;
import com.monsterdam.admin.repository.rowmapper.BudgetRowMapper;
import com.monsterdam.admin.repository.rowmapper.LiabilityRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the AccountingRecord entity.
 */
@SuppressWarnings("unused")
class AccountingRecordRepositoryInternalImpl
    extends SimpleR2dbcRepository<AccountingRecord, Long>
    implements AccountingRecordRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final BudgetRowMapper budgetMapper;
    private final AssetRowMapper assetMapper;
    private final LiabilityRowMapper liabilityMapper;
    private final AccountingRecordRowMapper accountingrecordMapper;

    private static final Table entityTable = Table.aliased("accounting_record", EntityManager.ENTITY_ALIAS);
    private static final Table budgetTable = Table.aliased("budget", "budget");
    private static final Table assetTable = Table.aliased("asset", "asset");
    private static final Table liabilityTable = Table.aliased("liability", "liability");

    public AccountingRecordRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        BudgetRowMapper budgetMapper,
        AssetRowMapper assetMapper,
        LiabilityRowMapper liabilityMapper,
        AccountingRecordRowMapper accountingrecordMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(AccountingRecord.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.budgetMapper = budgetMapper;
        this.assetMapper = assetMapper;
        this.liabilityMapper = liabilityMapper;
        this.accountingrecordMapper = accountingrecordMapper;
    }

    @Override
    public Flux<AccountingRecord> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<AccountingRecord> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = AccountingRecordSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(BudgetSqlHelper.getColumns(budgetTable, "budget"));
        columns.addAll(AssetSqlHelper.getColumns(assetTable, "asset"));
        columns.addAll(LiabilitySqlHelper.getColumns(liabilityTable, "liability"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(budgetTable)
            .on(Column.create("budget_id", entityTable))
            .equals(Column.create("id", budgetTable))
            .leftOuterJoin(assetTable)
            .on(Column.create("asset_id", entityTable))
            .equals(Column.create("id", assetTable))
            .leftOuterJoin(liabilityTable)
            .on(Column.create("liability_id", entityTable))
            .equals(Column.create("id", liabilityTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, AccountingRecord.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<AccountingRecord> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<AccountingRecord> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private AccountingRecord process(Row row, RowMetadata metadata) {
        AccountingRecord entity = accountingrecordMapper.apply(row, "e");
        entity.setBudget(budgetMapper.apply(row, "budget"));
        entity.setAsset(assetMapper.apply(row, "asset"));
        entity.setLiability(liabilityMapper.apply(row, "liability"));
        return entity;
    }

    @Override
    public <S extends AccountingRecord> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
