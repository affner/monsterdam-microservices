package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.UserReport;
import com.monsterdam.admin.repository.rowmapper.AssistanceTicketRowMapper;
import com.monsterdam.admin.repository.rowmapper.DirectMessageRowMapper;
import com.monsterdam.admin.repository.rowmapper.PostCommentRowMapper;
import com.monsterdam.admin.repository.rowmapper.PostFeedRowMapper;
import com.monsterdam.admin.repository.rowmapper.SingleAudioRowMapper;
import com.monsterdam.admin.repository.rowmapper.SingleLiveStreamRowMapper;
import com.monsterdam.admin.repository.rowmapper.SinglePhotoRowMapper;
import com.monsterdam.admin.repository.rowmapper.SingleVideoRowMapper;
import com.monsterdam.admin.repository.rowmapper.UserProfileRowMapper;
import com.monsterdam.admin.repository.rowmapper.UserReportRowMapper;
import com.monsterdam.admin.repository.rowmapper.VideoStoryRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the UserReport entity.
 */
@SuppressWarnings("unused")
class UserReportRepositoryInternalImpl extends SimpleR2dbcRepository<UserReport, Long> implements UserReportRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final AssistanceTicketRowMapper assistanceticketMapper;
    private final UserProfileRowMapper userprofileMapper;
    private final VideoStoryRowMapper videostoryMapper;
    private final SingleVideoRowMapper singlevideoMapper;
    private final SinglePhotoRowMapper singlephotoMapper;
    private final SingleAudioRowMapper singleaudioMapper;
    private final SingleLiveStreamRowMapper singlelivestreamMapper;
    private final DirectMessageRowMapper directmessageMapper;
    private final PostFeedRowMapper postfeedMapper;
    private final PostCommentRowMapper postcommentMapper;
    private final UserReportRowMapper userreportMapper;

    private static final Table entityTable = Table.aliased("user_report", EntityManager.ENTITY_ALIAS);
    private static final Table ticketTable = Table.aliased("assistance_ticket", "ticket");
    private static final Table reporterTable = Table.aliased("user_profile", "reporter");
    private static final Table reportedTable = Table.aliased("user_profile", "reported");
    private static final Table storyTable = Table.aliased("video_story", "story");
    private static final Table videoTable = Table.aliased("single_video", "video");
    private static final Table photoTable = Table.aliased("single_photo", "photo");
    private static final Table audioTable = Table.aliased("single_audio", "audio");
    private static final Table liveStreamTable = Table.aliased("single_live_stream", "liveStream");
    private static final Table messageTable = Table.aliased("direct_message", "message");
    private static final Table postTable = Table.aliased("post_feed", "post");
    private static final Table postCommentTable = Table.aliased("post_comment", "postComment");

    public UserReportRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        AssistanceTicketRowMapper assistanceticketMapper,
        UserProfileRowMapper userprofileMapper,
        VideoStoryRowMapper videostoryMapper,
        SingleVideoRowMapper singlevideoMapper,
        SinglePhotoRowMapper singlephotoMapper,
        SingleAudioRowMapper singleaudioMapper,
        SingleLiveStreamRowMapper singlelivestreamMapper,
        DirectMessageRowMapper directmessageMapper,
        PostFeedRowMapper postfeedMapper,
        PostCommentRowMapper postcommentMapper,
        UserReportRowMapper userreportMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UserReport.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.assistanceticketMapper = assistanceticketMapper;
        this.userprofileMapper = userprofileMapper;
        this.videostoryMapper = videostoryMapper;
        this.singlevideoMapper = singlevideoMapper;
        this.singlephotoMapper = singlephotoMapper;
        this.singleaudioMapper = singleaudioMapper;
        this.singlelivestreamMapper = singlelivestreamMapper;
        this.directmessageMapper = directmessageMapper;
        this.postfeedMapper = postfeedMapper;
        this.postcommentMapper = postcommentMapper;
        this.userreportMapper = userreportMapper;
    }

    @Override
    public Flux<UserReport> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UserReport> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UserReportSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(AssistanceTicketSqlHelper.getColumns(ticketTable, "ticket"));
        columns.addAll(UserProfileSqlHelper.getColumns(reporterTable, "reporter"));
        columns.addAll(UserProfileSqlHelper.getColumns(reportedTable, "reported"));
        columns.addAll(VideoStorySqlHelper.getColumns(storyTable, "story"));
        columns.addAll(SingleVideoSqlHelper.getColumns(videoTable, "video"));
        columns.addAll(SinglePhotoSqlHelper.getColumns(photoTable, "photo"));
        columns.addAll(SingleAudioSqlHelper.getColumns(audioTable, "audio"));
        columns.addAll(SingleLiveStreamSqlHelper.getColumns(liveStreamTable, "liveStream"));
        columns.addAll(DirectMessageSqlHelper.getColumns(messageTable, "message"));
        columns.addAll(PostFeedSqlHelper.getColumns(postTable, "post"));
        columns.addAll(PostCommentSqlHelper.getColumns(postCommentTable, "postComment"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(ticketTable)
            .on(Column.create("ticket_id", entityTable))
            .equals(Column.create("id", ticketTable))
            .leftOuterJoin(reporterTable)
            .on(Column.create("reporter_id", entityTable))
            .equals(Column.create("id", reporterTable))
            .leftOuterJoin(reportedTable)
            .on(Column.create("reported_id", entityTable))
            .equals(Column.create("id", reportedTable))
            .leftOuterJoin(storyTable)
            .on(Column.create("story_id", entityTable))
            .equals(Column.create("id", storyTable))
            .leftOuterJoin(videoTable)
            .on(Column.create("video_id", entityTable))
            .equals(Column.create("id", videoTable))
            .leftOuterJoin(photoTable)
            .on(Column.create("photo_id", entityTable))
            .equals(Column.create("id", photoTable))
            .leftOuterJoin(audioTable)
            .on(Column.create("audio_id", entityTable))
            .equals(Column.create("id", audioTable))
            .leftOuterJoin(liveStreamTable)
            .on(Column.create("live_stream_id", entityTable))
            .equals(Column.create("id", liveStreamTable))
            .leftOuterJoin(messageTable)
            .on(Column.create("message_id", entityTable))
            .equals(Column.create("id", messageTable))
            .leftOuterJoin(postTable)
            .on(Column.create("post_id", entityTable))
            .equals(Column.create("id", postTable))
            .leftOuterJoin(postCommentTable)
            .on(Column.create("post_comment_id", entityTable))
            .equals(Column.create("id", postCommentTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UserReport.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UserReport> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UserReport> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<UserReport> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<UserReport> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<UserReport> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private UserReport process(Row row, RowMetadata metadata) {
        UserReport entity = userreportMapper.apply(row, "e");
        entity.setTicket(assistanceticketMapper.apply(row, "ticket"));
        entity.setReporter(userprofileMapper.apply(row, "reporter"));
        entity.setReported(userprofileMapper.apply(row, "reported"));
        entity.setStory(videostoryMapper.apply(row, "story"));
        entity.setVideo(singlevideoMapper.apply(row, "video"));
        entity.setPhoto(singlephotoMapper.apply(row, "photo"));
        entity.setAudio(singleaudioMapper.apply(row, "audio"));
        entity.setLiveStream(singlelivestreamMapper.apply(row, "liveStream"));
        entity.setMessage(directmessageMapper.apply(row, "message"));
        entity.setPost(postfeedMapper.apply(row, "post"));
        entity.setPostComment(postcommentMapper.apply(row, "postComment"));
        return entity;
    }

    @Override
    public <S extends UserReport> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
