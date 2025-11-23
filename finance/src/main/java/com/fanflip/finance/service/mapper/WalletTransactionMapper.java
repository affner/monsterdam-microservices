package com.fanflip.finance.service.mapper;

import com.fanflip.finance.domain.PaymentTransaction;
import com.fanflip.finance.domain.WalletTransaction;
import com.fanflip.finance.service.dto.PaymentTransactionDTO;
import com.fanflip.finance.service.dto.WalletTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WalletTransaction} and its DTO {@link WalletTransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface WalletTransactionMapper extends EntityMapper<WalletTransactionDTO, WalletTransaction> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentTransactionAmount")
    WalletTransactionDTO toDto(WalletTransaction s);

    @Named("paymentTransactionAmount")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "amount", source = "amount")
    PaymentTransactionDTO toDtoPaymentTransactionAmount(PaymentTransaction paymentTransaction);
}
