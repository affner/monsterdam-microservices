package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.PollVote;
import com.monsterdam.admin.repository.rowmapper.PollOptionRowMapper;
import com.monsterdam.admin.repository.rowmapper.PollVoteRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the PollVote entity.
 */
@SuppressWarnings("unused")
class PollVoteRepositoryInternalImpl extends SimpleR2dbcRepository<PollVote, Long> implements PollVoteRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PollOptionRowMapper polloptionMapper;
    private final UserProfileRowMapper userprofileMapper;
    private final PollVoteRowMapper pollvoteMapper;

    private static final Table entityTable = Table.aliased("poll_vote", EntityManager.ENTITY_ALIAS);
    private static final Table pollOptionTable = Table.aliased("poll_option", "pollOption");
    private static final Table votingUserTable = Table.aliased("user_profile", "votingUser");

    public PollVoteRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PollOptionRowMapper polloptionMapper,
        UserProfileRowMapper userprofileMapper,
        PollVoteRowMapper pollvoteMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(PollVote.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.polloptionMapper = polloptionMapper;
        this.userprofileMapper = userprofileMapper;
        this.pollvoteMapper = pollvoteMapper;
    }

    @Override
    public Flux<PollVote> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<PollVote> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PollVoteSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PollOptionSqlHelper.getColumns(pollOptionTable, "pollOption"));
        columns.addAll(UserProfileSqlHelper.getColumns(votingUserTable, "votingUser"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(pollOptionTable)
            .on(Column.create("poll_option_id", entityTable))
            .equals(Column.create("id", pollOptionTable))
            .leftOuterJoin(votingUserTable)
            .on(Column.create("voting_user_id", entityTable))
            .equals(Column.create("id", votingUserTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, PollVote.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<PollVote> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<PollVote> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<PollVote> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<PollVote> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<PollVote> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private PollVote process(Row row, RowMetadata metadata) {
        PollVote entity = pollvoteMapper.apply(row, "e");
        entity.setPollOption(polloptionMapper.apply(row, "pollOption"));
        entity.setVotingUser(userprofileMapper.apply(row, "votingUser"));
        return entity;
    }

    @Override
    public <S extends PollVote> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
