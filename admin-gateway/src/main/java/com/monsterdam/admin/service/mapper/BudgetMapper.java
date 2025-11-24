package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.Budget;
import com.monsterdam.admin.service.dto.BudgetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Budget} and its DTO {@link BudgetDTO}.
 */
@Mapper(componentModel = "spring")
public interface BudgetMapper extends EntityMapper<BudgetDTO, Budget> {}
