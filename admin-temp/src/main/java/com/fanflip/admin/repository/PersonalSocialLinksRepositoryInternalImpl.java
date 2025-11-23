package com.fanflip.admin.repository;

import com.fanflip.admin.domain.PersonalSocialLinks;
import com.fanflip.admin.repository.rowmapper.PersonalSocialLinksRowMapper;
import com.fanflip.admin.repository.rowmapper.SocialNetworkRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the PersonalSocialLinks entity.
 */
@SuppressWarnings("unused")
class PersonalSocialLinksRepositoryInternalImpl
    extends SimpleR2dbcRepository<PersonalSocialLinks, Long>
    implements PersonalSocialLinksRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SocialNetworkRowMapper socialnetworkMapper;
    private final UserProfileRowMapper userprofileMapper;
    private final PersonalSocialLinksRowMapper personalsociallinksMapper;

    private static final Table entityTable = Table.aliased("personal_social_links", EntityManager.ENTITY_ALIAS);
    private static final Table socialNetworkTable = Table.aliased("social_network", "socialNetwork");
    private static final Table userTable = Table.aliased("user_profile", "e_user");

    public PersonalSocialLinksRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SocialNetworkRowMapper socialnetworkMapper,
        UserProfileRowMapper userprofileMapper,
        PersonalSocialLinksRowMapper personalsociallinksMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(PersonalSocialLinks.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.socialnetworkMapper = socialnetworkMapper;
        this.userprofileMapper = userprofileMapper;
        this.personalsociallinksMapper = personalsociallinksMapper;
    }

    @Override
    public Flux<PersonalSocialLinks> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<PersonalSocialLinks> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PersonalSocialLinksSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SocialNetworkSqlHelper.getColumns(socialNetworkTable, "socialNetwork"));
        columns.addAll(UserProfileSqlHelper.getColumns(userTable, "user"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(socialNetworkTable)
            .on(Column.create("social_network_id", entityTable))
            .equals(Column.create("id", socialNetworkTable))
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, PersonalSocialLinks.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<PersonalSocialLinks> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<PersonalSocialLinks> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<PersonalSocialLinks> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<PersonalSocialLinks> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<PersonalSocialLinks> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private PersonalSocialLinks process(Row row, RowMetadata metadata) {
        PersonalSocialLinks entity = personalsociallinksMapper.apply(row, "e");
        entity.setSocialNetwork(socialnetworkMapper.apply(row, "socialNetwork"));
        entity.setUser(userprofileMapper.apply(row, "user"));
        return entity;
    }

    @Override
    public <S extends PersonalSocialLinks> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
