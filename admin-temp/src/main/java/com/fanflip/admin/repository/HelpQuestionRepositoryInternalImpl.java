package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.HelpQuestion;
import com.monsterdam.admin.domain.HelpRelatedArticle;
import com.monsterdam.admin.repository.rowmapper.HelpQuestionRowMapper;
import com.monsterdam.admin.repository.rowmapper.HelpSubcategoryRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the HelpQuestion entity.
 */
@SuppressWarnings("unused")
class HelpQuestionRepositoryInternalImpl extends SimpleR2dbcRepository<HelpQuestion, Long> implements HelpQuestionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final HelpSubcategoryRowMapper helpsubcategoryMapper;
    private final HelpQuestionRowMapper helpquestionMapper;

    private static final Table entityTable = Table.aliased("help_question", EntityManager.ENTITY_ALIAS);
    private static final Table subcategoryTable = Table.aliased("help_subcategory", "subcategory");

    private static final EntityManager.LinkTable questionLink = new EntityManager.LinkTable(
        "rel_help_question__question",
        "help_question_id",
        "question_id"
    );

    public HelpQuestionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        HelpSubcategoryRowMapper helpsubcategoryMapper,
        HelpQuestionRowMapper helpquestionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(HelpQuestion.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.helpsubcategoryMapper = helpsubcategoryMapper;
        this.helpquestionMapper = helpquestionMapper;
    }

    @Override
    public Flux<HelpQuestion> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<HelpQuestion> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = HelpQuestionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(HelpSubcategorySqlHelper.getColumns(subcategoryTable, "subcategory"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(subcategoryTable)
            .on(Column.create("subcategory_id", entityTable))
            .equals(Column.create("id", subcategoryTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, HelpQuestion.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<HelpQuestion> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<HelpQuestion> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<HelpQuestion> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<HelpQuestion> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<HelpQuestion> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private HelpQuestion process(Row row, RowMetadata metadata) {
        HelpQuestion entity = helpquestionMapper.apply(row, "e");
        entity.setSubcategory(helpsubcategoryMapper.apply(row, "subcategory"));
        return entity;
    }

    @Override
    public <S extends HelpQuestion> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends HelpQuestion> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(questionLink, entity.getId(), entity.getQuestions().stream().map(HelpRelatedArticle::getId))
            .then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(questionLink, entityId);
    }
}
