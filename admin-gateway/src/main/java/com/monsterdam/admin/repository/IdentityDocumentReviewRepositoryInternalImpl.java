package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.IdentityDocumentReview;
import com.monsterdam.admin.repository.rowmapper.AssistanceTicketRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the IdentityDocumentReview entity.
 */
@SuppressWarnings("unused")
class IdentityDocumentReviewRepositoryInternalImpl
    extends SimpleR2dbcRepository<IdentityDocumentReview, Long>
    implements IdentityDocumentReviewRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final AssistanceTicketRowMapper assistanceticketMapper;
    private final IdentityDocumentReviewRowMapper identitydocumentreviewMapper;

    private static final Table entityTable = Table.aliased("identity_document_review", EntityManager.ENTITY_ALIAS);
    private static final Table ticketTable = Table.aliased("assistance_ticket", "ticket");

    public IdentityDocumentReviewRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        AssistanceTicketRowMapper assistanceticketMapper,
        IdentityDocumentReviewRowMapper identitydocumentreviewMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(IdentityDocumentReview.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.assistanceticketMapper = assistanceticketMapper;
        this.identitydocumentreviewMapper = identitydocumentreviewMapper;
    }

    @Override
    public Flux<IdentityDocumentReview> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<IdentityDocumentReview> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = IdentityDocumentReviewSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(AssistanceTicketSqlHelper.getColumns(ticketTable, "ticket"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(ticketTable)
            .on(Column.create("ticket_id", entityTable))
            .equals(Column.create("id", ticketTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, IdentityDocumentReview.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<IdentityDocumentReview> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<IdentityDocumentReview> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private IdentityDocumentReview process(Row row, RowMetadata metadata) {
        IdentityDocumentReview entity = identitydocumentreviewMapper.apply(row, "e");
        entity.setTicket(assistanceticketMapper.apply(row, "ticket"));
        return entity;
    }

    @Override
    public <S extends IdentityDocumentReview> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
