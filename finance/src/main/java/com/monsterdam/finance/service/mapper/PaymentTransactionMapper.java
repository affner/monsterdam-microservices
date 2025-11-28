package com.monsterdam.finance.service.mapper;

import com.monsterdam.finance.domain.PaymentTransaction;
import com.monsterdam.finance.service.dto.PaymentTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentTransaction} and its DTO {@link PaymentTransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentTransactionMapper extends EntityMapper<PaymentTransactionDTO, PaymentTransaction> {}
