package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.PostPoll;
import io.r2dbc.spi.Row;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PostPoll}, with proper type conversions.
 */
@Service
public class PostPollRowMapper implements BiFunction<Row, String, PostPoll> {

    private final ColumnConverter converter;

    public PostPollRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PostPoll} stored in the database.
     */
    @Override
    public PostPoll apply(Row row, String prefix) {
        PostPoll entity = new PostPoll();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setQuestion(converter.fromRow(row, prefix + "_question", String.class));
        entity.setIsMultiChoice(converter.fromRow(row, prefix + "_is_multi_choice", Boolean.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        entity.setEndDate(converter.fromRow(row, prefix + "_end_date", LocalDate.class));
        entity.setPostPollDuration(converter.fromRow(row, prefix + "_post_poll_duration", Duration.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        return entity;
    }
}
