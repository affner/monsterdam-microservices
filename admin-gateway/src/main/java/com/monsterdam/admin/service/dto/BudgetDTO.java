package com.monsterdam.admin.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.admin.domain.Budget} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BudgetDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Integer year;

    @NotNull(message = "must not be null")
    private BigDecimal totalBudget;

    private BigDecimal spentAmount;

    private BigDecimal remainingAmount;

    private String budgetDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getBudgetDetails() {
        return budgetDetails;
    }

    public void setBudgetDetails(String budgetDetails) {
        this.budgetDetails = budgetDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BudgetDTO)) {
            return false;
        }

        BudgetDTO budgetDTO = (BudgetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, budgetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BudgetDTO{" +
            "id=" + getId() +
            ", year=" + getYear() +
            ", totalBudget=" + getTotalBudget() +
            ", spentAmount=" + getSpentAmount() +
            ", remainingAmount=" + getRemainingAmount() +
            ", budgetDetails='" + getBudgetDetails() + "'" +
            "}";
    }
}
