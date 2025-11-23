package com.fanflip.catalogs.repository;

import com.fanflip.catalogs.domain.TaxInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TaxInfo entity.
 */
@Repository
public interface TaxInfoRepository extends JpaRepository<TaxInfo, Long> {
    default Optional<TaxInfo> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TaxInfo> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TaxInfo> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select taxInfo from TaxInfo taxInfo left join fetch taxInfo.country",
        countQuery = "select count(taxInfo) from TaxInfo taxInfo"
    )
    Page<TaxInfo> findAllWithToOneRelationships(Pageable pageable);

    @Query("select taxInfo from TaxInfo taxInfo left join fetch taxInfo.country")
    List<TaxInfo> findAllWithToOneRelationships();

    @Query("select taxInfo from TaxInfo taxInfo left join fetch taxInfo.country where taxInfo.id =:id")
    Optional<TaxInfo> findOneWithToOneRelationships(@Param("id") Long id);
}
