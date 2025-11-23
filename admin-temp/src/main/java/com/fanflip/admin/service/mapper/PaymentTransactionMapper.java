package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.PaymentMethod;
import com.fanflip.admin.domain.PaymentProvider;
import com.fanflip.admin.domain.PaymentTransaction;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.service.dto.PaymentMethodDTO;
import com.fanflip.admin.service.dto.PaymentProviderDTO;
import com.fanflip.admin.service.dto.PaymentTransactionDTO;
import com.fanflip.admin.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentTransaction} and its DTO {@link PaymentTransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentTransactionMapper extends EntityMapper<PaymentTransactionDTO, PaymentTransaction> {
    @Mapping(target = "paymentMethod", source = "paymentMethod", qualifiedByName = "paymentMethodMethodName")
    @Mapping(target = "paymentProvider", source = "paymentProvider", qualifiedByName = "paymentProviderProviderName")
    @Mapping(target = "viewer", source = "viewer", qualifiedByName = "userProfileId")
    PaymentTransactionDTO toDto(PaymentTransaction s);

    @Named("paymentMethodMethodName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "methodName", source = "methodName")
    PaymentMethodDTO toDtoPaymentMethodMethodName(PaymentMethod paymentMethod);

    @Named("paymentProviderProviderName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "providerName", source = "providerName")
    PaymentProviderDTO toDtoPaymentProviderProviderName(PaymentProvider paymentProvider);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
