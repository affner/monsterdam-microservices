package com.fanflip.interactions.repository;

import com.fanflip.interactions.domain.UserMention;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserMention entity.
 */
@Repository
public interface UserMentionRepository extends JpaRepository<UserMention, Long> {
    default Optional<UserMention> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UserMention> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UserMention> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select userMention from UserMention userMention left join fetch userMention.originPost left join fetch userMention.originPostComment",
        countQuery = "select count(userMention) from UserMention userMention"
    )
    Page<UserMention> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select userMention from UserMention userMention left join fetch userMention.originPost left join fetch userMention.originPostComment"
    )
    List<UserMention> findAllWithToOneRelationships();

    @Query(
        "select userMention from UserMention userMention left join fetch userMention.originPost left join fetch userMention.originPostComment where userMention.id =:id"
    )
    Optional<UserMention> findOneWithToOneRelationships(@Param("id") Long id);
}
