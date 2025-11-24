package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.PollVote;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PollVote}, with proper type conversions.
 */
@Service
public class PollVoteRowMapper implements BiFunction<Row, String, PollVote> {

    private final ColumnConverter converter;

    public PollVoteRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PollVote} stored in the database.
     */
    @Override
    public PollVote apply(Row row, String prefix) {
        PollVote entity = new PollVote();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setPollOptionId(converter.fromRow(row, prefix + "_poll_option_id", Long.class));
        entity.setVotingUserId(converter.fromRow(row, prefix + "_voting_user_id", Long.class));
        return entity;
    }
}
