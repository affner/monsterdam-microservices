package com.fanflip.multimedia.repository;

import com.fanflip.multimedia.domain.SinglePhoto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SinglePhoto entity.
 */
@Repository
public interface SinglePhotoRepository extends JpaRepository<SinglePhoto, Long> {
    default Optional<SinglePhoto> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SinglePhoto> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SinglePhoto> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select singlePhoto from SinglePhoto singlePhoto left join fetch singlePhoto.belongPackage",
        countQuery = "select count(singlePhoto) from SinglePhoto singlePhoto"
    )
    Page<SinglePhoto> findAllWithToOneRelationships(Pageable pageable);

    @Query("select singlePhoto from SinglePhoto singlePhoto left join fetch singlePhoto.belongPackage")
    List<SinglePhoto> findAllWithToOneRelationships();

    @Query("select singlePhoto from SinglePhoto singlePhoto left join fetch singlePhoto.belongPackage where singlePhoto.id =:id")
    Optional<SinglePhoto> findOneWithToOneRelationships(@Param("id") Long id);
}
