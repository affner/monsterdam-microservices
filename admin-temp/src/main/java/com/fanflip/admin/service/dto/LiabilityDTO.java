package com.fanflip.admin.service.dto;

import com.fanflip.admin.domain.enumeration.LiabilityType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.fanflip.admin.domain.Liability} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LiabilityDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private BigDecimal amount;

    private LocalDate dueDate;

    @NotNull(message = "must not be null")
    private LiabilityType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LiabilityType getType() {
        return type;
    }

    public void setType(LiabilityType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LiabilityDTO)) {
            return false;
        }

        LiabilityDTO liabilityDTO = (LiabilityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, liabilityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LiabilityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", amount=" + getAmount() +
            ", dueDate='" + getDueDate() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
