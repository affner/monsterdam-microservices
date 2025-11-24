package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.BookMark;
import com.monsterdam.admin.repository.rowmapper.BookMarkRowMapper;
import com.monsterdam.admin.repository.rowmapper.DirectMessageRowMapper;
import com.monsterdam.admin.repository.rowmapper.PostFeedRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the BookMark entity.
 */
@SuppressWarnings("unused")
class BookMarkRepositoryInternalImpl extends SimpleR2dbcRepository<BookMark, Long> implements BookMarkRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PostFeedRowMapper postfeedMapper;
    private final DirectMessageRowMapper directmessageMapper;
    private final UserProfileRowMapper userprofileMapper;
    private final BookMarkRowMapper bookmarkMapper;

    private static final Table entityTable = Table.aliased("book_mark", EntityManager.ENTITY_ALIAS);
    private static final Table postTable = Table.aliased("post_feed", "post");
    private static final Table messageTable = Table.aliased("direct_message", "message");
    private static final Table userTable = Table.aliased("user_profile", "e_user");

    public BookMarkRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PostFeedRowMapper postfeedMapper,
        DirectMessageRowMapper directmessageMapper,
        UserProfileRowMapper userprofileMapper,
        BookMarkRowMapper bookmarkMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(BookMark.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.postfeedMapper = postfeedMapper;
        this.directmessageMapper = directmessageMapper;
        this.userprofileMapper = userprofileMapper;
        this.bookmarkMapper = bookmarkMapper;
    }

    @Override
    public Flux<BookMark> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<BookMark> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = BookMarkSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PostFeedSqlHelper.getColumns(postTable, "post"));
        columns.addAll(DirectMessageSqlHelper.getColumns(messageTable, "message"));
        columns.addAll(UserProfileSqlHelper.getColumns(userTable, "user"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(postTable)
            .on(Column.create("post_id", entityTable))
            .equals(Column.create("id", postTable))
            .leftOuterJoin(messageTable)
            .on(Column.create("message_id", entityTable))
            .equals(Column.create("id", messageTable))
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, BookMark.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<BookMark> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<BookMark> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<BookMark> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<BookMark> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<BookMark> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private BookMark process(Row row, RowMetadata metadata) {
        BookMark entity = bookmarkMapper.apply(row, "e");
        entity.setPost(postfeedMapper.apply(row, "post"));
        entity.setMessage(directmessageMapper.apply(row, "message"));
        entity.setUser(userprofileMapper.apply(row, "user"));
        return entity;
    }

    @Override
    public <S extends BookMark> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
