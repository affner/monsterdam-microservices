package com.monsterdam.admin.repository.rowmapper;

import com.monsterdam.admin.domain.TaxDeclaration;
import com.monsterdam.admin.domain.enumeration.TaxDeclarationStatus;
import com.monsterdam.admin.domain.enumeration.TaxDeclarationType;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TaxDeclaration}, with proper type conversions.
 */
@Service
public class TaxDeclarationRowMapper implements BiFunction<Row, String, TaxDeclaration> {

    private final ColumnConverter converter;

    public TaxDeclarationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TaxDeclaration} stored in the database.
     */
    @Override
    public TaxDeclaration apply(Row row, String prefix) {
        TaxDeclaration entity = new TaxDeclaration();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setYear(converter.fromRow(row, prefix + "_year", Integer.class));
        entity.setDeclarationType(converter.fromRow(row, prefix + "_declaration_type", TaxDeclarationType.class));
        entity.setSubmittedDate(converter.fromRow(row, prefix + "_submitted_date", Instant.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", TaxDeclarationStatus.class));
        entity.setTotalIncome(converter.fromRow(row, prefix + "_total_income", BigDecimal.class));
        entity.setTotalTaxableIncome(converter.fromRow(row, prefix + "_total_taxable_income", BigDecimal.class));
        entity.setTotalTaxPaid(converter.fromRow(row, prefix + "_total_tax_paid", BigDecimal.class));
        entity.setSupportingDocumentsKey(converter.fromRow(row, prefix + "_supporting_documents_key", String.class));
        return entity;
    }
}
