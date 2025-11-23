package com.fanflip.finance.repository;

import com.fanflip.finance.domain.WalletTransaction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WalletTransaction entity.
 */
@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
    default Optional<WalletTransaction> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<WalletTransaction> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<WalletTransaction> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select walletTransaction from WalletTransaction walletTransaction left join fetch walletTransaction.payment",
        countQuery = "select count(walletTransaction) from WalletTransaction walletTransaction"
    )
    Page<WalletTransaction> findAllWithToOneRelationships(Pageable pageable);

    @Query("select walletTransaction from WalletTransaction walletTransaction left join fetch walletTransaction.payment")
    List<WalletTransaction> findAllWithToOneRelationships();

    @Query(
        "select walletTransaction from WalletTransaction walletTransaction left join fetch walletTransaction.payment where walletTransaction.id =:id"
    )
    Optional<WalletTransaction> findOneWithToOneRelationships(@Param("id") Long id);
}
