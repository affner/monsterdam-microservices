package com.fanflip.finance.repository;

import com.fanflip.finance.domain.PurchasedTip;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PurchasedTip entity.
 */
@Repository
public interface PurchasedTipRepository extends JpaRepository<PurchasedTip, Long> {
    default Optional<PurchasedTip> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PurchasedTip> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PurchasedTip> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select purchasedTip from PurchasedTip purchasedTip left join fetch purchasedTip.payment left join fetch purchasedTip.walletTransaction left join fetch purchasedTip.creatorEarning",
        countQuery = "select count(purchasedTip) from PurchasedTip purchasedTip"
    )
    Page<PurchasedTip> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select purchasedTip from PurchasedTip purchasedTip left join fetch purchasedTip.payment left join fetch purchasedTip.walletTransaction left join fetch purchasedTip.creatorEarning"
    )
    List<PurchasedTip> findAllWithToOneRelationships();

    @Query(
        "select purchasedTip from PurchasedTip purchasedTip left join fetch purchasedTip.payment left join fetch purchasedTip.walletTransaction left join fetch purchasedTip.creatorEarning where purchasedTip.id =:id"
    )
    Optional<PurchasedTip> findOneWithToOneRelationships(@Param("id") Long id);
}
