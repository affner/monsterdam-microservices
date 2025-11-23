package com.fanflip.finance.repository;

import com.fanflip.finance.domain.MoneyPayout;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MoneyPayout entity.
 */
@Repository
public interface MoneyPayoutRepository extends JpaRepository<MoneyPayout, Long> {
    default Optional<MoneyPayout> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MoneyPayout> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MoneyPayout> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select moneyPayout from MoneyPayout moneyPayout left join fetch moneyPayout.creatorEarning",
        countQuery = "select count(moneyPayout) from MoneyPayout moneyPayout"
    )
    Page<MoneyPayout> findAllWithToOneRelationships(Pageable pageable);

    @Query("select moneyPayout from MoneyPayout moneyPayout left join fetch moneyPayout.creatorEarning")
    List<MoneyPayout> findAllWithToOneRelationships();

    @Query("select moneyPayout from MoneyPayout moneyPayout left join fetch moneyPayout.creatorEarning where moneyPayout.id =:id")
    Optional<MoneyPayout> findOneWithToOneRelationships(@Param("id") Long id);
}
