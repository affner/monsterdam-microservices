package com.fanflip.admin.repository;

import com.fanflip.admin.domain.OfferPromotion;
import com.fanflip.admin.repository.rowmapper.OfferPromotionRowMapper;
import com.fanflip.admin.repository.rowmapper.UserProfileRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the OfferPromotion entity.
 */
@SuppressWarnings("unused")
class OfferPromotionRepositoryInternalImpl extends SimpleR2dbcRepository<OfferPromotion, Long> implements OfferPromotionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserProfileRowMapper userprofileMapper;
    private final OfferPromotionRowMapper offerpromotionMapper;

    private static final Table entityTable = Table.aliased("offer_promotion", EntityManager.ENTITY_ALIAS);
    private static final Table creatorTable = Table.aliased("user_profile", "creator");

    public OfferPromotionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserProfileRowMapper userprofileMapper,
        OfferPromotionRowMapper offerpromotionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(OfferPromotion.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userprofileMapper = userprofileMapper;
        this.offerpromotionMapper = offerpromotionMapper;
    }

    @Override
    public Flux<OfferPromotion> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<OfferPromotion> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = OfferPromotionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserProfileSqlHelper.getColumns(creatorTable, "creator"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(creatorTable)
            .on(Column.create("creator_id", entityTable))
            .equals(Column.create("id", creatorTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, OfferPromotion.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<OfferPromotion> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<OfferPromotion> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private OfferPromotion process(Row row, RowMetadata metadata) {
        OfferPromotion entity = offerpromotionMapper.apply(row, "e");
        entity.setCreator(userprofileMapper.apply(row, "creator"));
        return entity;
    }

    @Override
    public <S extends OfferPromotion> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
