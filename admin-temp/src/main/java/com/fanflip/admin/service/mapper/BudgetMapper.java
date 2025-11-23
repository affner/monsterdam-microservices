package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.Budget;
import com.fanflip.admin.service.dto.BudgetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Budget} and its DTO {@link BudgetDTO}.
 */
@Mapper(componentModel = "spring")
public interface BudgetMapper extends EntityMapper<BudgetDTO, Budget> {}
