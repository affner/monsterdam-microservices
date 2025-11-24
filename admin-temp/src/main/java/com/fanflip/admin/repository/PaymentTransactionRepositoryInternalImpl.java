package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.PaymentTransaction;
import com.monsterdam.admin.repository.rowmapper.PaymentMethodRowMapper;
import com.monsterdam.admin.repository.rowmapper.PaymentProviderRowMapper;
import com.monsterdam.admin.repository.rowmapper.PaymentTransactionRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the PaymentTransaction entity.
 */
@SuppressWarnings("unused")
class PaymentTransactionRepositoryInternalImpl
    extends SimpleR2dbcRepository<PaymentTransaction, Long>
    implements PaymentTransactionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PaymentMethodRowMapper paymentmethodMapper;
    private final PaymentProviderRowMapper paymentproviderMapper;
    private final UserProfileRowMapper userprofileMapper;
    private final PaymentTransactionRowMapper paymenttransactionMapper;

    private static final Table entityTable = Table.aliased("payment_transaction", EntityManager.ENTITY_ALIAS);
    private static final Table paymentMethodTable = Table.aliased("payment_method", "paymentMethod");
    private static final Table paymentProviderTable = Table.aliased("payment_provider", "paymentProvider");
    private static final Table viewerTable = Table.aliased("user_profile", "viewer");

    public PaymentTransactionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PaymentMethodRowMapper paymentmethodMapper,
        PaymentProviderRowMapper paymentproviderMapper,
        UserProfileRowMapper userprofileMapper,
        PaymentTransactionRowMapper paymenttransactionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(PaymentTransaction.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.paymentmethodMapper = paymentmethodMapper;
        this.paymentproviderMapper = paymentproviderMapper;
        this.userprofileMapper = userprofileMapper;
        this.paymenttransactionMapper = paymenttransactionMapper;
    }

    @Override
    public Flux<PaymentTransaction> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<PaymentTransaction> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PaymentTransactionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PaymentMethodSqlHelper.getColumns(paymentMethodTable, "paymentMethod"));
        columns.addAll(PaymentProviderSqlHelper.getColumns(paymentProviderTable, "paymentProvider"));
        columns.addAll(UserProfileSqlHelper.getColumns(viewerTable, "viewer"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(paymentMethodTable)
            .on(Column.create("payment_method_id", entityTable))
            .equals(Column.create("id", paymentMethodTable))
            .leftOuterJoin(paymentProviderTable)
            .on(Column.create("payment_provider_id", entityTable))
            .equals(Column.create("id", paymentProviderTable))
            .leftOuterJoin(viewerTable)
            .on(Column.create("viewer_id", entityTable))
            .equals(Column.create("id", viewerTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, PaymentTransaction.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<PaymentTransaction> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<PaymentTransaction> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<PaymentTransaction> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<PaymentTransaction> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<PaymentTransaction> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private PaymentTransaction process(Row row, RowMetadata metadata) {
        PaymentTransaction entity = paymenttransactionMapper.apply(row, "e");
        entity.setPaymentMethod(paymentmethodMapper.apply(row, "paymentMethod"));
        entity.setPaymentProvider(paymentproviderMapper.apply(row, "paymentProvider"));
        entity.setViewer(userprofileMapper.apply(row, "viewer"));
        return entity;
    }

    @Override
    public <S extends PaymentTransaction> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
