package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.PurchasedTip;
import com.monsterdam.admin.repository.rowmapper.CreatorEarningRowMapper;
import com.monsterdam.admin.repository.rowmapper.DirectMessageRowMapper;
import com.monsterdam.admin.repository.rowmapper.PaymentTransactionRowMapper;
import com.monsterdam.admin.repository.rowmapper.PurchasedTipRowMapper;
import com.monsterdam.admin.repository.rowmapper.WalletTransactionRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the PurchasedTip entity.
 */
@SuppressWarnings("unused")
class PurchasedTipRepositoryInternalImpl extends SimpleR2dbcRepository<PurchasedTip, Long> implements PurchasedTipRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PaymentTransactionRowMapper paymenttransactionMapper;
    private final WalletTransactionRowMapper wallettransactionMapper;
    private final CreatorEarningRowMapper creatorearningMapper;
    private final DirectMessageRowMapper directmessageMapper;
    private final PurchasedTipRowMapper purchasedtipMapper;

    private static final Table entityTable = Table.aliased("purchased_tip", EntityManager.ENTITY_ALIAS);
    private static final Table paymentTable = Table.aliased("payment_transaction", "payment");
    private static final Table walletTransactionTable = Table.aliased("wallet_transaction", "walletTransaction");
    private static final Table creatorEarningTable = Table.aliased("creator_earning", "creatorEarning");
    private static final Table messageTable = Table.aliased("direct_message", "message");

    public PurchasedTipRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PaymentTransactionRowMapper paymenttransactionMapper,
        WalletTransactionRowMapper wallettransactionMapper,
        CreatorEarningRowMapper creatorearningMapper,
        DirectMessageRowMapper directmessageMapper,
        PurchasedTipRowMapper purchasedtipMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(PurchasedTip.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.paymenttransactionMapper = paymenttransactionMapper;
        this.wallettransactionMapper = wallettransactionMapper;
        this.creatorearningMapper = creatorearningMapper;
        this.directmessageMapper = directmessageMapper;
        this.purchasedtipMapper = purchasedtipMapper;
    }

    @Override
    public Flux<PurchasedTip> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<PurchasedTip> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PurchasedTipSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PaymentTransactionSqlHelper.getColumns(paymentTable, "payment"));
        columns.addAll(WalletTransactionSqlHelper.getColumns(walletTransactionTable, "walletTransaction"));
        columns.addAll(CreatorEarningSqlHelper.getColumns(creatorEarningTable, "creatorEarning"));
        columns.addAll(DirectMessageSqlHelper.getColumns(messageTable, "message"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(paymentTable)
            .on(Column.create("payment_id", entityTable))
            .equals(Column.create("id", paymentTable))
            .leftOuterJoin(walletTransactionTable)
            .on(Column.create("wallet_transaction_id", entityTable))
            .equals(Column.create("id", walletTransactionTable))
            .leftOuterJoin(creatorEarningTable)
            .on(Column.create("creator_earning_id", entityTable))
            .equals(Column.create("id", creatorEarningTable))
            .leftOuterJoin(messageTable)
            .on(Column.create("message_id", entityTable))
            .equals(Column.create("id", messageTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, PurchasedTip.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<PurchasedTip> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<PurchasedTip> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<PurchasedTip> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<PurchasedTip> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<PurchasedTip> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private PurchasedTip process(Row row, RowMetadata metadata) {
        PurchasedTip entity = purchasedtipMapper.apply(row, "e");
        entity.setPayment(paymenttransactionMapper.apply(row, "payment"));
        entity.setWalletTransaction(wallettransactionMapper.apply(row, "walletTransaction"));
        entity.setCreatorEarning(creatorearningMapper.apply(row, "creatorEarning"));
        entity.setMessage(directmessageMapper.apply(row, "message"));
        return entity;
    }

    @Override
    public <S extends PurchasedTip> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
