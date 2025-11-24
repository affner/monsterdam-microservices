package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.DocumentReviewObservation;
import com.monsterdam.admin.repository.rowmapper.DocumentReviewObservationRowMapper;
import com.monsterdam.admin.repository.rowmapper.IdentityDocumentReviewRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the DocumentReviewObservation entity.
 */
@SuppressWarnings("unused")
class DocumentReviewObservationRepositoryInternalImpl
    extends SimpleR2dbcRepository<DocumentReviewObservation, Long>
    implements DocumentReviewObservationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final IdentityDocumentReviewRowMapper identitydocumentreviewMapper;
    private final DocumentReviewObservationRowMapper documentreviewobservationMapper;

    private static final Table entityTable = Table.aliased("document_review_observation", EntityManager.ENTITY_ALIAS);
    private static final Table reviewTable = Table.aliased("identity_document_review", "review");

    public DocumentReviewObservationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        IdentityDocumentReviewRowMapper identitydocumentreviewMapper,
        DocumentReviewObservationRowMapper documentreviewobservationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(
                converter.getMappingContext().getRequiredPersistentEntity(DocumentReviewObservation.class)
            ),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.identitydocumentreviewMapper = identitydocumentreviewMapper;
        this.documentreviewobservationMapper = documentreviewobservationMapper;
    }

    @Override
    public Flux<DocumentReviewObservation> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<DocumentReviewObservation> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = DocumentReviewObservationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(IdentityDocumentReviewSqlHelper.getColumns(reviewTable, "review"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(reviewTable)
            .on(Column.create("review_id", entityTable))
            .equals(Column.create("id", reviewTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, DocumentReviewObservation.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<DocumentReviewObservation> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<DocumentReviewObservation> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private DocumentReviewObservation process(Row row, RowMetadata metadata) {
        DocumentReviewObservation entity = documentreviewobservationMapper.apply(row, "e");
        entity.setReview(identitydocumentreviewMapper.apply(row, "review"));
        return entity;
    }

    @Override
    public <S extends DocumentReviewObservation> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
