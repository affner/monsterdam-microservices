package com.fanflip.admin.repository;

import com.fanflip.admin.domain.ContentPackage;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.repository.rowmapper.ContentPackageRowMapper;
import com.fanflip.admin.repository.rowmapper.SingleAudioRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the ContentPackage entity.
 */
@SuppressWarnings("unused")
class ContentPackageRepositoryInternalImpl extends SimpleR2dbcRepository<ContentPackage, Long> implements ContentPackageRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SingleAudioRowMapper singleaudioMapper;
    private final ContentPackageRowMapper contentpackageMapper;

    private static final Table entityTable = Table.aliased("content_package", EntityManager.ENTITY_ALIAS);
    private static final Table audioTable = Table.aliased("single_audio", "audio");

    private static final EntityManager.LinkTable usersTaggedLink = new EntityManager.LinkTable(
        "rel_content_package__users_tagged",
        "content_package_id",
        "users_tagged_id"
    );

    public ContentPackageRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SingleAudioRowMapper singleaudioMapper,
        ContentPackageRowMapper contentpackageMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ContentPackage.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.singleaudioMapper = singleaudioMapper;
        this.contentpackageMapper = contentpackageMapper;
    }

    @Override
    public Flux<ContentPackage> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ContentPackage> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ContentPackageSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SingleAudioSqlHelper.getColumns(audioTable, "audio"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(audioTable)
            .on(Column.create("audio_id", entityTable))
            .equals(Column.create("id", audioTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ContentPackage.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ContentPackage> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ContentPackage> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<ContentPackage> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<ContentPackage> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<ContentPackage> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private ContentPackage process(Row row, RowMetadata metadata) {
        ContentPackage entity = contentpackageMapper.apply(row, "e");
        entity.setAudio(singleaudioMapper.apply(row, "audio"));
        return entity;
    }

    @Override
    public <S extends ContentPackage> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends ContentPackage> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(usersTaggedLink, entity.getId(), entity.getUsersTaggeds().stream().map(UserProfile::getId))
            .then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(usersTaggedLink, entityId);
    }
}
