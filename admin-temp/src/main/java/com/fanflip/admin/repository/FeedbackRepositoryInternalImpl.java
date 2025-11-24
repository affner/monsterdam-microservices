package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.Feedback;
import com.monsterdam.admin.repository.rowmapper.FeedbackRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Feedback entity.
 */
@SuppressWarnings("unused")
class FeedbackRepositoryInternalImpl extends SimpleR2dbcRepository<Feedback, Long> implements FeedbackRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserProfileRowMapper userprofileMapper;
    private final FeedbackRowMapper feedbackMapper;

    private static final Table entityTable = Table.aliased("feedback", EntityManager.ENTITY_ALIAS);
    private static final Table creatorTable = Table.aliased("user_profile", "creator");

    public FeedbackRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserProfileRowMapper userprofileMapper,
        FeedbackRowMapper feedbackMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Feedback.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userprofileMapper = userprofileMapper;
        this.feedbackMapper = feedbackMapper;
    }

    @Override
    public Flux<Feedback> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Feedback> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = FeedbackSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserProfileSqlHelper.getColumns(creatorTable, "creator"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(creatorTable)
            .on(Column.create("creator_id", entityTable))
            .equals(Column.create("id", creatorTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Feedback.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Feedback> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Feedback> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Feedback process(Row row, RowMetadata metadata) {
        Feedback entity = feedbackMapper.apply(row, "e");
        entity.setCreator(userprofileMapper.apply(row, "creator"));
        return entity;
    }

    @Override
    public <S extends Feedback> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
