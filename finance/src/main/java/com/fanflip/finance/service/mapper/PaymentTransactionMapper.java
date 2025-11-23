package com.fanflip.finance.service.mapper;

import com.fanflip.finance.domain.PaymentTransaction;
import com.fanflip.finance.service.dto.PaymentTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentTransaction} and its DTO {@link PaymentTransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentTransactionMapper extends EntityMapper<PaymentTransactionDTO, PaymentTransaction> {}
