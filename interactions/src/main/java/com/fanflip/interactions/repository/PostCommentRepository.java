package com.fanflip.interactions.repository;

import com.fanflip.interactions.domain.PostComment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PostComment entity.
 */
@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    default Optional<PostComment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PostComment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PostComment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select postComment from PostComment postComment left join fetch postComment.post left join fetch postComment.responseTo",
        countQuery = "select count(postComment) from PostComment postComment"
    )
    Page<PostComment> findAllWithToOneRelationships(Pageable pageable);

    @Query("select postComment from PostComment postComment left join fetch postComment.post left join fetch postComment.responseTo")
    List<PostComment> findAllWithToOneRelationships();

    @Query(
        "select postComment from PostComment postComment left join fetch postComment.post left join fetch postComment.responseTo where postComment.id =:id"
    )
    Optional<PostComment> findOneWithToOneRelationships(@Param("id") Long id);
}
