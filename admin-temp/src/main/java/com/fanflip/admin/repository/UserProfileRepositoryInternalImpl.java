package com.fanflip.admin.repository;

import com.fanflip.admin.domain.HashTag;
import com.fanflip.admin.domain.State;
import com.fanflip.admin.domain.UserEvent;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.repository.rowmapper.CountryRowMapper;
import com.fanflip.admin.repository.rowmapper.StateRowMapper;
import com.fanflip.admin.repository.rowmapper.UserLiteRowMapper;
import com.fanflip.admin.repository.rowmapper.UserProfileRowMapper;
import com.fanflip.admin.repository.rowmapper.UserSettingsRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the UserProfile entity.
 */
@SuppressWarnings("unused")
class UserProfileRepositoryInternalImpl extends SimpleR2dbcRepository<UserProfile, Long> implements UserProfileRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserLiteRowMapper userliteMapper;
    private final UserSettingsRowMapper usersettingsMapper;
    private final CountryRowMapper countryMapper;
    private final StateRowMapper stateMapper;
    private final UserProfileRowMapper userprofileMapper;

    private static final Table entityTable = Table.aliased("user_profile", EntityManager.ENTITY_ALIAS);
    private static final Table userLiteTable = Table.aliased("user_lite", "userLite");
    private static final Table settingsTable = Table.aliased("user_settings", "settings");
    private static final Table countryOfBirthTable = Table.aliased("country", "countryOfBirth");
    private static final Table stateOfResidenceTable = Table.aliased("state", "stateOfResidence");

    private static final EntityManager.LinkTable followedLink = new EntityManager.LinkTable(
        "rel_user_profile__followed",
        "user_profile_id",
        "followed_id"
    );
    private static final EntityManager.LinkTable blockedListLink = new EntityManager.LinkTable(
        "rel_user_profile__blocked_list",
        "user_profile_id",
        "blocked_list_id"
    );
    private static final EntityManager.LinkTable loyaListsLink = new EntityManager.LinkTable(
        "rel_user_profile__loya_lists",
        "user_profile_id",
        "loya_lists_id"
    );
    private static final EntityManager.LinkTable subscribedLink = new EntityManager.LinkTable(
        "rel_user_profile__subscribed",
        "user_profile_id",
        "subscribed_id"
    );
    private static final EntityManager.LinkTable joinedEventsLink = new EntityManager.LinkTable(
        "rel_user_profile__joined_events",
        "user_profile_id",
        "joined_events_id"
    );
    private static final EntityManager.LinkTable blockedUbicationsLink = new EntityManager.LinkTable(
        "rel_user_profile__blocked_ubications",
        "user_profile_id",
        "blocked_ubications_id"
    );
    private static final EntityManager.LinkTable hashTagsLink = new EntityManager.LinkTable(
        "rel_user_profile__hash_tags",
        "user_profile_id",
        "hash_tags_id"
    );

    public UserProfileRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserLiteRowMapper userliteMapper,
        UserSettingsRowMapper usersettingsMapper,
        CountryRowMapper countryMapper,
        StateRowMapper stateMapper,
        UserProfileRowMapper userprofileMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UserProfile.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userliteMapper = userliteMapper;
        this.usersettingsMapper = usersettingsMapper;
        this.countryMapper = countryMapper;
        this.stateMapper = stateMapper;
        this.userprofileMapper = userprofileMapper;
    }

    @Override
    public Flux<UserProfile> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UserProfile> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UserProfileSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserLiteSqlHelper.getColumns(userLiteTable, "userLite"));
        columns.addAll(UserSettingsSqlHelper.getColumns(settingsTable, "settings"));
        columns.addAll(CountrySqlHelper.getColumns(countryOfBirthTable, "countryOfBirth"));
        columns.addAll(StateSqlHelper.getColumns(stateOfResidenceTable, "stateOfResidence"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(userLiteTable)
            .on(Column.create("user_lite_id", entityTable))
            .equals(Column.create("id", userLiteTable))
            .leftOuterJoin(settingsTable)
            .on(Column.create("settings_id", entityTable))
            .equals(Column.create("id", settingsTable))
            .leftOuterJoin(countryOfBirthTable)
            .on(Column.create("country_of_birth_id", entityTable))
            .equals(Column.create("id", countryOfBirthTable))
            .leftOuterJoin(stateOfResidenceTable)
            .on(Column.create("state_of_residence_id", entityTable))
            .equals(Column.create("id", stateOfResidenceTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UserProfile.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UserProfile> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UserProfile> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<UserProfile> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<UserProfile> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<UserProfile> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private UserProfile process(Row row, RowMetadata metadata) {
        UserProfile entity = userprofileMapper.apply(row, "e");
        entity.setUserLite(userliteMapper.apply(row, "userLite"));
        entity.setSettings(usersettingsMapper.apply(row, "settings"));
        entity.setCountryOfBirth(countryMapper.apply(row, "countryOfBirth"));
        entity.setStateOfResidence(stateMapper.apply(row, "stateOfResidence"));
        return entity;
    }

    @Override
    public <S extends UserProfile> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends UserProfile> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(followedLink, entity.getId(), entity.getFolloweds().stream().map(UserProfile::getId))
            .then();
        result =
            result.and(
                entityManager.updateLinkTable(blockedListLink, entity.getId(), entity.getBlockedLists().stream().map(UserProfile::getId))
            );
        result =
            result.and(
                entityManager.updateLinkTable(loyaListsLink, entity.getId(), entity.getLoyaLists().stream().map(UserProfile::getId))
            );
        result =
            result.and(
                entityManager.updateLinkTable(subscribedLink, entity.getId(), entity.getSubscribeds().stream().map(UserProfile::getId))
            );
        result =
            result.and(
                entityManager.updateLinkTable(joinedEventsLink, entity.getId(), entity.getJoinedEvents().stream().map(UserEvent::getId))
            );
        result =
            result.and(
                entityManager.updateLinkTable(
                    blockedUbicationsLink,
                    entity.getId(),
                    entity.getBlockedUbications().stream().map(State::getId)
                )
            );
        result = result.and(entityManager.updateLinkTable(hashTagsLink, entity.getId(), entity.getHashTags().stream().map(HashTag::getId)));
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager
            .deleteFromLinkTable(followedLink, entityId)
            .and(entityManager.deleteFromLinkTable(blockedListLink, entityId))
            .and(entityManager.deleteFromLinkTable(loyaListsLink, entityId))
            .and(entityManager.deleteFromLinkTable(subscribedLink, entityId))
            .and(entityManager.deleteFromLinkTable(joinedEventsLink, entityId))
            .and(entityManager.deleteFromLinkTable(blockedUbicationsLink, entityId))
            .and(entityManager.deleteFromLinkTable(hashTagsLink, entityId));
    }
}
