package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.PurchasedContent;
import com.monsterdam.admin.repository.rowmapper.ContentPackageRowMapper;
import com.monsterdam.admin.repository.rowmapper.CreatorEarningRowMapper;
import com.monsterdam.admin.repository.rowmapper.PaymentTransactionRowMapper;
import com.monsterdam.admin.repository.rowmapper.PurchasedContentRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the PurchasedContent entity.
 */
@SuppressWarnings("unused")
class PurchasedContentRepositoryInternalImpl
    extends SimpleR2dbcRepository<PurchasedContent, Long>
    implements PurchasedContentRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PaymentTransactionRowMapper paymenttransactionMapper;
    private final WalletTransactionRowMapper wallettransactionMapper;
    private final CreatorEarningRowMapper creatorearningMapper;
    private final UserProfileRowMapper userprofileMapper;
    private final ContentPackageRowMapper contentpackageMapper;
    private final PurchasedContentRowMapper purchasedcontentMapper;

    private static final Table entityTable = Table.aliased("purchased_content", EntityManager.ENTITY_ALIAS);
    private static final Table paymentTable = Table.aliased("payment_transaction", "payment");
    private static final Table walletTransactionTable = Table.aliased("wallet_transaction", "walletTransaction");
    private static final Table creatorEarningTable = Table.aliased("creator_earning", "creatorEarning");
    private static final Table viewerTable = Table.aliased("user_profile", "viewer");
    private static final Table purchasedContentPackageTable = Table.aliased("content_package", "purchasedContentPackage");

    public PurchasedContentRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PaymentTransactionRowMapper paymenttransactionMapper,
        WalletTransactionRowMapper wallettransactionMapper,
        CreatorEarningRowMapper creatorearningMapper,
        UserProfileRowMapper userprofileMapper,
        ContentPackageRowMapper contentpackageMapper,
        PurchasedContentRowMapper purchasedcontentMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(PurchasedContent.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.paymenttransactionMapper = paymenttransactionMapper;
        this.wallettransactionMapper = wallettransactionMapper;
        this.creatorearningMapper = creatorearningMapper;
        this.userprofileMapper = userprofileMapper;
        this.contentpackageMapper = contentpackageMapper;
        this.purchasedcontentMapper = purchasedcontentMapper;
    }

    @Override
    public Flux<PurchasedContent> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<PurchasedContent> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PurchasedContentSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PaymentTransactionSqlHelper.getColumns(paymentTable, "payment"));
        columns.addAll(WalletTransactionSqlHelper.getColumns(walletTransactionTable, "walletTransaction"));
        columns.addAll(CreatorEarningSqlHelper.getColumns(creatorEarningTable, "creatorEarning"));
        columns.addAll(UserProfileSqlHelper.getColumns(viewerTable, "viewer"));
        columns.addAll(ContentPackageSqlHelper.getColumns(purchasedContentPackageTable, "purchasedContentPackage"));
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
            .leftOuterJoin(viewerTable)
            .on(Column.create("viewer_id", entityTable))
            .equals(Column.create("id", viewerTable))
            .leftOuterJoin(purchasedContentPackageTable)
            .on(Column.create("purchased_content_package_id", entityTable))
            .equals(Column.create("id", purchasedContentPackageTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, PurchasedContent.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<PurchasedContent> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<PurchasedContent> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<PurchasedContent> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<PurchasedContent> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<PurchasedContent> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private PurchasedContent process(Row row, RowMetadata metadata) {
        PurchasedContent entity = purchasedcontentMapper.apply(row, "e");
        entity.setPayment(paymenttransactionMapper.apply(row, "payment"));
        entity.setWalletTransaction(wallettransactionMapper.apply(row, "walletTransaction"));
        entity.setCreatorEarning(creatorearningMapper.apply(row, "creatorEarning"));
        entity.setViewer(userprofileMapper.apply(row, "viewer"));
        entity.setPurchasedContentPackage(contentpackageMapper.apply(row, "purchasedContentPackage"));
        return entity;
    }

    @Override
    public <S extends PurchasedContent> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
