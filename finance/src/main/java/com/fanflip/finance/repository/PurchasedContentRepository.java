package com.fanflip.finance.repository;

import com.fanflip.finance.domain.PurchasedContent;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PurchasedContent entity.
 */
@Repository
public interface PurchasedContentRepository extends JpaRepository<PurchasedContent, Long> {
    default Optional<PurchasedContent> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PurchasedContent> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PurchasedContent> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select purchasedContent from PurchasedContent purchasedContent left join fetch purchasedContent.payment left join fetch purchasedContent.walletTransaction left join fetch purchasedContent.creatorEarning",
        countQuery = "select count(purchasedContent) from PurchasedContent purchasedContent"
    )
    Page<PurchasedContent> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select purchasedContent from PurchasedContent purchasedContent left join fetch purchasedContent.payment left join fetch purchasedContent.walletTransaction left join fetch purchasedContent.creatorEarning"
    )
    List<PurchasedContent> findAllWithToOneRelationships();

    @Query(
        "select purchasedContent from PurchasedContent purchasedContent left join fetch purchasedContent.payment left join fetch purchasedContent.walletTransaction left join fetch purchasedContent.creatorEarning where purchasedContent.id =:id"
    )
    Optional<PurchasedContent> findOneWithToOneRelationships(@Param("id") Long id);
}
