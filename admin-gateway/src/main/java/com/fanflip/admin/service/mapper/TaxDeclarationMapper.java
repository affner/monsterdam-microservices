package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.AccountingRecord;
import com.fanflip.admin.domain.TaxDeclaration;
import com.fanflip.admin.service.dto.AccountingRecordDTO;
import com.fanflip.admin.service.dto.TaxDeclarationDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaxDeclaration} and its DTO {@link TaxDeclarationDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaxDeclarationMapper extends EntityMapper<TaxDeclarationDTO, TaxDeclaration> {
    @Mapping(target = "accountingRecords", source = "accountingRecords", qualifiedByName = "accountingRecordIdSet")
    TaxDeclarationDTO toDto(TaxDeclaration s);

    @Mapping(target = "removeAccountingRecords", ignore = true)
    TaxDeclaration toEntity(TaxDeclarationDTO taxDeclarationDTO);

    @Named("accountingRecordId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AccountingRecordDTO toDtoAccountingRecordId(AccountingRecord accountingRecord);

    @Named("accountingRecordIdSet")
    default Set<AccountingRecordDTO> toDtoAccountingRecordIdSet(Set<AccountingRecord> accountingRecord) {
        return accountingRecord.stream().map(this::toDtoAccountingRecordId).collect(Collectors.toSet());
    }
}
