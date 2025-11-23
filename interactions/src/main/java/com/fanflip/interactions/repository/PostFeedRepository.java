package com.fanflip.interactions.repository;

import com.fanflip.interactions.domain.PostFeed;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PostFeed entity.
 */
@Repository
public interface PostFeedRepository extends JpaRepository<PostFeed, Long> {
    default Optional<PostFeed> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PostFeed> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PostFeed> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select postFeed from PostFeed postFeed left join fetch postFeed.poll",
        countQuery = "select count(postFeed) from PostFeed postFeed"
    )
    Page<PostFeed> findAllWithToOneRelationships(Pageable pageable);

    @Query("select postFeed from PostFeed postFeed left join fetch postFeed.poll")
    List<PostFeed> findAllWithToOneRelationships();

    @Query("select postFeed from PostFeed postFeed left join fetch postFeed.poll where postFeed.id =:id")
    Optional<PostFeed> findOneWithToOneRelationships(@Param("id") Long id);
}
