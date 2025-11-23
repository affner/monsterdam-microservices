package com.fanflip.admin.repository;

import com.fanflip.admin.domain.PollOption;
import com.fanflip.admin.repository.rowmapper.PollOptionRowMapper;
import com.fanflip.admin.repository.rowmapper.PostPollRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the PollOption entity.
 */
@SuppressWarnings("unused")
class PollOptionRepositoryInternalImpl extends SimpleR2dbcRepository<PollOption, Long> implements PollOptionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PostPollRowMapper postpollMapper;
    private final PollOptionRowMapper polloptionMapper;

    private static final Table entityTable = Table.aliased("poll_option", EntityManager.ENTITY_ALIAS);
    private static final Table pollTable = Table.aliased("post_poll", "poll");

    public PollOptionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PostPollRowMapper postpollMapper,
        PollOptionRowMapper polloptionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(PollOption.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.postpollMapper = postpollMapper;
        this.polloptionMapper = polloptionMapper;
    }

    @Override
    public Flux<PollOption> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<PollOption> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PollOptionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PostPollSqlHelper.getColumns(pollTable, "poll"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(pollTable)
            .on(Column.create("poll_id", entityTable))
            .equals(Column.create("id", pollTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, PollOption.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<PollOption> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<PollOption> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<PollOption> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<PollOption> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<PollOption> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private PollOption process(Row row, RowMetadata metadata) {
        PollOption entity = polloptionMapper.apply(row, "e");
        entity.setPoll(postpollMapper.apply(row, "poll"));
        return entity;
    }

    @Override
    public <S extends PollOption> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
