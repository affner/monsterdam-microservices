package com.fanflip.admin.repository.rowmapper;

import com.fanflip.admin.domain.PollOption;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PollOption}, with proper type conversions.
 */
@Service
public class PollOptionRowMapper implements BiFunction<Row, String, PollOption> {

    private final ColumnConverter converter;

    public PollOptionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PollOption} stored in the database.
     */
    @Override
    public PollOption apply(Row row, String prefix) {
        PollOption entity = new PollOption();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setOptionDescription(converter.fromRow(row, prefix + "_option_description", String.class));
        entity.setVoteCount(converter.fromRow(row, prefix + "_vote_count", Integer.class));
        entity.setPollId(converter.fromRow(row, prefix + "_poll_id", Long.class));
        return entity;
    }
}
