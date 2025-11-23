package com.fanflip.admin.repository;

import com.fanflip.admin.domain.SinglePhoto;
import com.fanflip.admin.repository.rowmapper.ContentPackageRowMapper;
import com.fanflip.admin.repository.rowmapper.SinglePhotoRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the SinglePhoto entity.
 */
@SuppressWarnings("unused")
class SinglePhotoRepositoryInternalImpl extends SimpleR2dbcRepository<SinglePhoto, Long> implements SinglePhotoRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ContentPackageRowMapper contentpackageMapper;
    private final SinglePhotoRowMapper singlephotoMapper;

    private static final Table entityTable = Table.aliased("single_photo", EntityManager.ENTITY_ALIAS);
    private static final Table belongPackageTable = Table.aliased("content_package", "belongPackage");

    public SinglePhotoRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ContentPackageRowMapper contentpackageMapper,
        SinglePhotoRowMapper singlephotoMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(SinglePhoto.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.contentpackageMapper = contentpackageMapper;
        this.singlephotoMapper = singlephotoMapper;
    }

    @Override
    public Flux<SinglePhoto> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<SinglePhoto> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SinglePhotoSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ContentPackageSqlHelper.getColumns(belongPackageTable, "belongPackage"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(belongPackageTable)
            .on(Column.create("belong_package_id", entityTable))
            .equals(Column.create("id", belongPackageTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, SinglePhoto.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<SinglePhoto> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<SinglePhoto> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<SinglePhoto> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<SinglePhoto> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<SinglePhoto> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private SinglePhoto process(Row row, RowMetadata metadata) {
        SinglePhoto entity = singlephotoMapper.apply(row, "e");
        entity.setBelongPackage(contentpackageMapper.apply(row, "belongPackage"));
        return entity;
    }

    @Override
    public <S extends SinglePhoto> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
