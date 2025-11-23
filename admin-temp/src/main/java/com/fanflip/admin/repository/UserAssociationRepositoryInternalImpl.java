package com.fanflip.admin.repository;

import com.fanflip.admin.domain.UserAssociation;
import com.fanflip.admin.repository.rowmapper.UserAssociationRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the UserAssociation entity.
 */
@SuppressWarnings("unused")
class UserAssociationRepositoryInternalImpl
    extends SimpleR2dbcRepository<UserAssociation, Long>
    implements UserAssociationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserProfileRowMapper userprofileMapper;
    private final UserAssociationRowMapper userassociationMapper;

    private static final Table entityTable = Table.aliased("user_association", EntityManager.ENTITY_ALIAS);
    private static final Table ownerTable = Table.aliased("user_profile", "owner");

    public UserAssociationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserProfileRowMapper userprofileMapper,
        UserAssociationRowMapper userassociationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UserAssociation.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userprofileMapper = userprofileMapper;
        this.userassociationMapper = userassociationMapper;
    }

    @Override
    public Flux<UserAssociation> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UserAssociation> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UserAssociationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserProfileSqlHelper.getColumns(ownerTable, "owner"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(ownerTable)
            .on(Column.create("owner_id", entityTable))
            .equals(Column.create("id", ownerTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UserAssociation.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UserAssociation> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UserAssociation> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private UserAssociation process(Row row, RowMetadata metadata) {
        UserAssociation entity = userassociationMapper.apply(row, "e");
        entity.setOwner(userprofileMapper.apply(row, "owner"));
        return entity;
    }

    @Override
    public <S extends UserAssociation> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
