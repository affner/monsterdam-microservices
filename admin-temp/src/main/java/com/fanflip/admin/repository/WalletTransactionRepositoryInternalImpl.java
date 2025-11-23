package com.fanflip.admin.repository;

import com.fanflip.admin.domain.WalletTransaction;
import com.fanflip.admin.repository.rowmapper.PaymentTransactionRowMapper;
import com.fanflip.admin.repository.rowmapper.UserProfileRowMapper;
import com.fanflip.admin.repository.rowmapper.WalletTransactionRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the WalletTransaction entity.
 */
@SuppressWarnings("unused")
class WalletTransactionRepositoryInternalImpl
    extends SimpleR2dbcRepository<WalletTransaction, Long>
    implements WalletTransactionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PaymentTransactionRowMapper paymenttransactionMapper;
    private final UserProfileRowMapper userprofileMapper;
    private final WalletTransactionRowMapper wallettransactionMapper;

    private static final Table entityTable = Table.aliased("wallet_transaction", EntityManager.ENTITY_ALIAS);
    private static final Table paymentTable = Table.aliased("payment_transaction", "payment");
    private static final Table viewerTable = Table.aliased("user_profile", "viewer");

    public WalletTransactionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PaymentTransactionRowMapper paymenttransactionMapper,
        UserProfileRowMapper userprofileMapper,
        WalletTransactionRowMapper wallettransactionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(WalletTransaction.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.paymenttransactionMapper = paymenttransactionMapper;
        this.userprofileMapper = userprofileMapper;
        this.wallettransactionMapper = wallettransactionMapper;
    }

    @Override
    public Flux<WalletTransaction> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<WalletTransaction> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = WalletTransactionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PaymentTransactionSqlHelper.getColumns(paymentTable, "payment"));
        columns.addAll(UserProfileSqlHelper.getColumns(viewerTable, "viewer"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(paymentTable)
            .on(Column.create("payment_id", entityTable))
            .equals(Column.create("id", paymentTable))
            .leftOuterJoin(viewerTable)
            .on(Column.create("viewer_id", entityTable))
            .equals(Column.create("id", viewerTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, WalletTransaction.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<WalletTransaction> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<WalletTransaction> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<WalletTransaction> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<WalletTransaction> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<WalletTransaction> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private WalletTransaction process(Row row, RowMetadata metadata) {
        WalletTransaction entity = wallettransactionMapper.apply(row, "e");
        entity.setPayment(paymenttransactionMapper.apply(row, "payment"));
        entity.setViewer(userprofileMapper.apply(row, "viewer"));
        return entity;
    }

    @Override
    public <S extends WalletTransaction> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
