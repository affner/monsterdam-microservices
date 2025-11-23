package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.AccountingRecord;
import com.fanflip.admin.domain.FinancialStatement;
import com.fanflip.admin.service.dto.AccountingRecordDTO;
import com.fanflip.admin.service.dto.FinancialStatementDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FinancialStatement} and its DTO {@link FinancialStatementDTO}.
 */
@Mapper(componentModel = "spring")
public interface FinancialStatementMapper extends EntityMapper<FinancialStatementDTO, FinancialStatement> {
    @Mapping(target = "accountingRecords", source = "accountingRecords", qualifiedByName = "accountingRecordIdSet")
    FinancialStatementDTO toDto(FinancialStatement s);

    @Mapping(target = "removeAccountingRecords", ignore = true)
    FinancialStatement toEntity(FinancialStatementDTO financialStatementDTO);

    @Named("accountingRecordId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AccountingRecordDTO toDtoAccountingRecordId(AccountingRecord accountingRecord);

    @Named("accountingRecordIdSet")
    default Set<AccountingRecordDTO> toDtoAccountingRecordIdSet(Set<AccountingRecord> accountingRecord) {
        return accountingRecord.stream().map(this::toDtoAccountingRecordId).collect(Collectors.toSet());
    }
}
