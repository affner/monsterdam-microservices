package com.fanflip.finance.repository;

import com.fanflip.finance.domain.PurchasedSubscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PurchasedSubscription entity.
 */
@Repository
public interface PurchasedSubscriptionRepository extends JpaRepository<PurchasedSubscription, Long> {
    default Optional<PurchasedSubscription> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PurchasedSubscription> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PurchasedSubscription> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select purchasedSubscription from PurchasedSubscription purchasedSubscription left join fetch purchasedSubscription.payment left join fetch purchasedSubscription.walletTransaction left join fetch purchasedSubscription.creatorEarning left join fetch purchasedSubscription.appliedPromotion",
        countQuery = "select count(purchasedSubscription) from PurchasedSubscription purchasedSubscription"
    )
    Page<PurchasedSubscription> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select purchasedSubscription from PurchasedSubscription purchasedSubscription left join fetch purchasedSubscription.payment left join fetch purchasedSubscription.walletTransaction left join fetch purchasedSubscription.creatorEarning left join fetch purchasedSubscription.appliedPromotion"
    )
    List<PurchasedSubscription> findAllWithToOneRelationships();

    @Query(
        "select purchasedSubscription from PurchasedSubscription purchasedSubscription left join fetch purchasedSubscription.payment left join fetch purchasedSubscription.walletTransaction left join fetch purchasedSubscription.creatorEarning left join fetch purchasedSubscription.appliedPromotion where purchasedSubscription.id =:id"
    )
    Optional<PurchasedSubscription> findOneWithToOneRelationships(@Param("id") Long id);
}
