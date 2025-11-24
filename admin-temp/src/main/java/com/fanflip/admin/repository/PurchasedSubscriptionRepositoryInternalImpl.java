package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.PurchasedSubscription;
import com.monsterdam.admin.repository.rowmapper.CreatorEarningRowMapper;
import com.monsterdam.admin.repository.rowmapper.OfferPromotionRowMapper;
import com.monsterdam.admin.repository.rowmapper.PaymentTransactionRowMapper;
import com.monsterdam.admin.repository.rowmapper.PurchasedSubscriptionRowMapper;
import com.monsterdam.admin.repository.rowmapper.SubscriptionBundleRowMapper;
import com.monsterdam.admin.repository.rowmapper.UserProfileRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the PurchasedSubscription entity.
 */
@SuppressWarnings("unused")
class PurchasedSubscriptionRepositoryInternalImpl
    extends SimpleR2dbcRepository<PurchasedSubscription, Long>
    implements PurchasedSubscriptionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PaymentTransactionRowMapper paymenttransactionMapper;
    private final WalletTransactionRowMapper wallettransactionMapper;
    private final CreatorEarningRowMapper creatorearningMapper;
    private final SubscriptionBundleRowMapper subscriptionbundleMapper;
    private final OfferPromotionRowMapper offerpromotionMapper;
    private final UserProfileRowMapper userprofileMapper;
    private final PurchasedSubscriptionRowMapper purchasedsubscriptionMapper;

    private static final Table entityTable = Table.aliased("purchased_subscription", EntityManager.ENTITY_ALIAS);
    private static final Table paymentTable = Table.aliased("payment_transaction", "payment");
    private static final Table walletTransactionTable = Table.aliased("wallet_transaction", "walletTransaction");
    private static final Table creatorEarningTable = Table.aliased("creator_earning", "creatorEarning");
    private static final Table subscriptionBundleTable = Table.aliased("subscription_bundle", "subscriptionBundle");
    private static final Table appliedPromotionTable = Table.aliased("offer_promotion", "appliedPromotion");
    private static final Table viewerTable = Table.aliased("user_profile", "viewer");

    public PurchasedSubscriptionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PaymentTransactionRowMapper paymenttransactionMapper,
        WalletTransactionRowMapper wallettransactionMapper,
        CreatorEarningRowMapper creatorearningMapper,
        SubscriptionBundleRowMapper subscriptionbundleMapper,
        OfferPromotionRowMapper offerpromotionMapper,
        UserProfileRowMapper userprofileMapper,
        PurchasedSubscriptionRowMapper purchasedsubscriptionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(PurchasedSubscription.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.paymenttransactionMapper = paymenttransactionMapper;
        this.wallettransactionMapper = wallettransactionMapper;
        this.creatorearningMapper = creatorearningMapper;
        this.subscriptionbundleMapper = subscriptionbundleMapper;
        this.offerpromotionMapper = offerpromotionMapper;
        this.userprofileMapper = userprofileMapper;
        this.purchasedsubscriptionMapper = purchasedsubscriptionMapper;
    }

    @Override
    public Flux<PurchasedSubscription> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<PurchasedSubscription> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PurchasedSubscriptionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PaymentTransactionSqlHelper.getColumns(paymentTable, "payment"));
        columns.addAll(WalletTransactionSqlHelper.getColumns(walletTransactionTable, "walletTransaction"));
        columns.addAll(CreatorEarningSqlHelper.getColumns(creatorEarningTable, "creatorEarning"));
        columns.addAll(SubscriptionBundleSqlHelper.getColumns(subscriptionBundleTable, "subscriptionBundle"));
        columns.addAll(OfferPromotionSqlHelper.getColumns(appliedPromotionTable, "appliedPromotion"));
        columns.addAll(UserProfileSqlHelper.getColumns(viewerTable, "viewer"));
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
            .leftOuterJoin(subscriptionBundleTable)
            .on(Column.create("subscription_bundle_id", entityTable))
            .equals(Column.create("id", subscriptionBundleTable))
            .leftOuterJoin(appliedPromotionTable)
            .on(Column.create("applied_promotion_id", entityTable))
            .equals(Column.create("id", appliedPromotionTable))
            .leftOuterJoin(viewerTable)
            .on(Column.create("viewer_id", entityTable))
            .equals(Column.create("id", viewerTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, PurchasedSubscription.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<PurchasedSubscription> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<PurchasedSubscription> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<PurchasedSubscription> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<PurchasedSubscription> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<PurchasedSubscription> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private PurchasedSubscription process(Row row, RowMetadata metadata) {
        PurchasedSubscription entity = purchasedsubscriptionMapper.apply(row, "e");
        entity.setPayment(paymenttransactionMapper.apply(row, "payment"));
        entity.setWalletTransaction(wallettransactionMapper.apply(row, "walletTransaction"));
        entity.setCreatorEarning(creatorearningMapper.apply(row, "creatorEarning"));
        entity.setSubscriptionBundle(subscriptionbundleMapper.apply(row, "subscriptionBundle"));
        entity.setAppliedPromotion(offerpromotionMapper.apply(row, "appliedPromotion"));
        entity.setViewer(userprofileMapper.apply(row, "viewer"));
        return entity;
    }

    @Override
    public <S extends PurchasedSubscription> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
