package com.fanflip.admin.repository;

import com.fanflip.admin.domain.SingleDocument;
import com.fanflip.admin.repository.rowmapper.SingleDocumentRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the SingleDocument entity.
 */
@SuppressWarnings("unused")
class SingleDocumentRepositoryInternalImpl extends SimpleR2dbcRepository<SingleDocument, Long> implements SingleDocumentRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserProfileRowMapper userprofileMapper;
    private final SingleDocumentRowMapper singledocumentMapper;

    private static final Table entityTable = Table.aliased("single_document", EntityManager.ENTITY_ALIAS);
    private static final Table userTable = Table.aliased("user_profile", "e_user");

    public SingleDocumentRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserProfileRowMapper userprofileMapper,
        SingleDocumentRowMapper singledocumentMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(SingleDocument.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userprofileMapper = userprofileMapper;
        this.singledocumentMapper = singledocumentMapper;
    }

    @Override
    public Flux<SingleDocument> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<SingleDocument> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SingleDocumentSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserProfileSqlHelper.getColumns(userTable, "user"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, SingleDocument.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<SingleDocument> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<SingleDocument> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private SingleDocument process(Row row, RowMetadata metadata) {
        SingleDocument entity = singledocumentMapper.apply(row, "e");
        entity.setUser(userprofileMapper.apply(row, "user"));
        return entity;
    }

    @Override
    public <S extends SingleDocument> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
