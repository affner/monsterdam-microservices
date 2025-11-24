package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.AccountingRecord;
import com.monsterdam.admin.domain.enumeration.AccountingType;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AccountingRecord}, with proper type conversions.
 */
@Service
public class AccountingRecordRowMapper implements BiFunction<Row, String, AccountingRecord> {

    private final ColumnConverter converter;

    public AccountingRecordRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AccountingRecord} stored in the database.
     */
    @Override
    public AccountingRecord apply(Row row, String prefix) {
        AccountingRecord entity = new AccountingRecord();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDate(converter.fromRow(row, prefix + "_date", Instant.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setDebit(converter.fromRow(row, prefix + "_debit", BigDecimal.class));
        entity.setCredit(converter.fromRow(row, prefix + "_credit", BigDecimal.class));
        entity.setBalance(converter.fromRow(row, prefix + "_balance", BigDecimal.class));
        entity.setAccountType(converter.fromRow(row, prefix + "_account_type", AccountingType.class));
        entity.setPaymentId(converter.fromRow(row, prefix + "_payment_id", Long.class));
        entity.setBudgetId(converter.fromRow(row, prefix + "_budget_id", Long.class));
        entity.setAssetId(converter.fromRow(row, prefix + "_asset_id", Long.class));
        entity.setLiabilityId(converter.fromRow(row, prefix + "_liability_id", Long.class));
        return entity;
    }
}
