package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.HelpSubcategory;
import com.monsterdam.admin.repository.rowmapper.HelpCategoryRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the HelpSubcategory entity.
 */
@SuppressWarnings("unused")
class HelpSubcategoryRepositoryInternalImpl
    extends SimpleR2dbcRepository<HelpSubcategory, Long>
    implements HelpSubcategoryRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final HelpCategoryRowMapper helpcategoryMapper;
    private final HelpSubcategoryRowMapper helpsubcategoryMapper;

    private static final Table entityTable = Table.aliased("help_subcategory", EntityManager.ENTITY_ALIAS);
    private static final Table categoryTable = Table.aliased("help_category", "category");

    public HelpSubcategoryRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        HelpCategoryRowMapper helpcategoryMapper,
        HelpSubcategoryRowMapper helpsubcategoryMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(HelpSubcategory.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.helpcategoryMapper = helpcategoryMapper;
        this.helpsubcategoryMapper = helpsubcategoryMapper;
    }

    @Override
    public Flux<HelpSubcategory> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<HelpSubcategory> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = HelpSubcategorySqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(HelpCategorySqlHelper.getColumns(categoryTable, "category"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(categoryTable)
            .on(Column.create("category_id", entityTable))
            .equals(Column.create("id", categoryTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, HelpSubcategory.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<HelpSubcategory> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<HelpSubcategory> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private HelpSubcategory process(Row row, RowMetadata metadata) {
        HelpSubcategory entity = helpsubcategoryMapper.apply(row, "e");
        entity.setCategory(helpcategoryMapper.apply(row, "category"));
        return entity;
    }

    @Override
    public <S extends HelpSubcategory> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
