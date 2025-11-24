package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.ContentPackage;
import com.monsterdam.admin.domain.CreatorEarning;
import com.monsterdam.admin.domain.PaymentTransaction;
import com.monsterdam.admin.domain.PurchasedContent;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.domain.WalletTransaction;
import com.monsterdam.admin.service.dto.ContentPackageDTO;
import com.monsterdam.admin.service.dto.CreatorEarningDTO;
import com.monsterdam.admin.service.dto.PaymentTransactionDTO;
import com.monsterdam.admin.service.dto.PurchasedContentDTO;
import com.monsterdam.admin.service.dto.UserProfileDTO;
import com.monsterdam.admin.service.dto.WalletTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchasedContent} and its DTO {@link PurchasedContentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PurchasedContentMapper extends EntityMapper<PurchasedContentDTO, PurchasedContent> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentTransactionAmount")
    @Mapping(target = "walletTransaction", source = "walletTransaction", qualifiedByName = "walletTransactionAmount")
    @Mapping(target = "creatorEarning", source = "creatorEarning", qualifiedByName = "creatorEarningAmount")
    @Mapping(target = "viewer", source = "viewer", qualifiedByName = "userProfileId")
    @Mapping(target = "purchasedContentPackage", source = "purchasedContentPackage", qualifiedByName = "contentPackageId")
    PurchasedContentDTO toDto(PurchasedContent s);

    @Named("paymentTransactionAmount")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "amount", source = "amount")
    PaymentTransactionDTO toDtoPaymentTransactionAmount(PaymentTransaction paymentTransaction);

    @Named("walletTransactionAmount")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "amount", source = "amount")
    WalletTransactionDTO toDtoWalletTransactionAmount(WalletTransaction walletTransaction);

    @Named("creatorEarningAmount")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "amount", source = "amount")
    CreatorEarningDTO toDtoCreatorEarningAmount(CreatorEarning creatorEarning);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("contentPackageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContentPackageDTO toDtoContentPackageId(ContentPackage contentPackage);
}
