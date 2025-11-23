package com.fanflip.admin.repository;

import com.fanflip.admin.domain.IdentityDocument;
import com.fanflip.admin.repository.rowmapper.IdentityDocumentReviewRowMapper;
import com.fanflip.admin.repository.rowmapper.IdentityDocumentRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the IdentityDocument entity.
 */
@SuppressWarnings("unused")
class IdentityDocumentRepositoryInternalImpl
    extends SimpleR2dbcRepository<IdentityDocument, Long>
    implements IdentityDocumentRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final IdentityDocumentReviewRowMapper identitydocumentreviewMapper;
    private final IdentityDocumentRowMapper identitydocumentMapper;

    private static final Table entityTable = Table.aliased("identity_document", EntityManager.ENTITY_ALIAS);
    private static final Table reviewTable = Table.aliased("identity_document_review", "review");

    public IdentityDocumentRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        IdentityDocumentReviewRowMapper identitydocumentreviewMapper,
        IdentityDocumentRowMapper identitydocumentMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(IdentityDocument.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.identitydocumentreviewMapper = identitydocumentreviewMapper;
        this.identitydocumentMapper = identitydocumentMapper;
    }

    @Override
    public Flux<IdentityDocument> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<IdentityDocument> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = IdentityDocumentSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(IdentityDocumentReviewSqlHelper.getColumns(reviewTable, "review"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(reviewTable)
            .on(Column.create("review_id", entityTable))
            .equals(Column.create("id", reviewTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, IdentityDocument.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<IdentityDocument> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<IdentityDocument> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private IdentityDocument process(Row row, RowMetadata metadata) {
        IdentityDocument entity = identitydocumentMapper.apply(row, "e");
        entity.setReview(identitydocumentreviewMapper.apply(row, "review"));
        return entity;
    }

    @Override
    public <S extends IdentityDocument> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
