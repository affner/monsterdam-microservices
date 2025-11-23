package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.PaymentTransaction;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.domain.WalletTransaction;
import com.fanflip.admin.service.dto.PaymentTransactionDTO;
import com.fanflip.admin.service.dto.UserProfileDTO;
import com.fanflip.admin.service.dto.WalletTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WalletTransaction} and its DTO {@link WalletTransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface WalletTransactionMapper extends EntityMapper<WalletTransactionDTO, WalletTransaction> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentTransactionAmount")
    @Mapping(target = "viewer", source = "viewer", qualifiedByName = "userProfileId")
    WalletTransactionDTO toDto(WalletTransaction s);

    @Named("paymentTransactionAmount")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "amount", source = "amount")
    PaymentTransactionDTO toDtoPaymentTransactionAmount(PaymentTransaction paymentTransaction);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
