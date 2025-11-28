package com.monsterdam.multimedia.repository;

import com.monsterdam.multimedia.domain.SingleVideo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SingleVideo entity.
 */
@Repository
public interface SingleVideoRepository extends JpaRepository<SingleVideo, Long> {
    default Optional<SingleVideo> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SingleVideo> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SingleVideo> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select singleVideo from SingleVideo singleVideo left join fetch singleVideo.belongPackage",
        countQuery = "select count(singleVideo) from SingleVideo singleVideo"
    )
    Page<SingleVideo> findAllWithToOneRelationships(Pageable pageable);

    @Query("select singleVideo from SingleVideo singleVideo left join fetch singleVideo.belongPackage")
    List<SingleVideo> findAllWithToOneRelationships();

    @Query("select singleVideo from SingleVideo singleVideo left join fetch singleVideo.belongPackage where singleVideo.id =:id")
    Optional<SingleVideo> findOneWithToOneRelationships(@Param("id") Long id);
}
