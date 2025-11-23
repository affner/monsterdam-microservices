package com.fanflip.admin.repository;

import com.fanflip.admin.domain.SingleVideo;
import com.fanflip.admin.repository.rowmapper.ContentPackageRowMapper;
import com.fanflip.admin.repository.rowmapper.SingleVideoRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the SingleVideo entity.
 */
@SuppressWarnings("unused")
class SingleVideoRepositoryInternalImpl extends SimpleR2dbcRepository<SingleVideo, Long> implements SingleVideoRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ContentPackageRowMapper contentpackageMapper;
    private final SingleVideoRowMapper singlevideoMapper;

    private static final Table entityTable = Table.aliased("single_video", EntityManager.ENTITY_ALIAS);
    private static final Table belongPackageTable = Table.aliased("content_package", "belongPackage");

    public SingleVideoRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ContentPackageRowMapper contentpackageMapper,
        SingleVideoRowMapper singlevideoMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(SingleVideo.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.contentpackageMapper = contentpackageMapper;
        this.singlevideoMapper = singlevideoMapper;
    }

    @Override
    public Flux<SingleVideo> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<SingleVideo> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SingleVideoSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ContentPackageSqlHelper.getColumns(belongPackageTable, "belongPackage"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(belongPackageTable)
            .on(Column.create("belong_package_id", entityTable))
            .equals(Column.create("id", belongPackageTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, SingleVideo.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<SingleVideo> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<SingleVideo> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<SingleVideo> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<SingleVideo> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<SingleVideo> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private SingleVideo process(Row row, RowMetadata metadata) {
        SingleVideo entity = singlevideoMapper.apply(row, "e");
        entity.setBelongPackage(contentpackageMapper.apply(row, "belongPackage"));
        return entity;
    }

    @Override
    public <S extends SingleVideo> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
