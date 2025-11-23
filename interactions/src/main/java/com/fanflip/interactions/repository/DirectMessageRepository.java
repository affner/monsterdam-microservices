package com.fanflip.interactions.repository;

import com.fanflip.interactions.domain.DirectMessage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DirectMessage entity.
 */
@Repository
public interface DirectMessageRepository extends JpaRepository<DirectMessage, Long> {
    default Optional<DirectMessage> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DirectMessage> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DirectMessage> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select directMessage from DirectMessage directMessage left join fetch directMessage.responseTo",
        countQuery = "select count(directMessage) from DirectMessage directMessage"
    )
    Page<DirectMessage> findAllWithToOneRelationships(Pageable pageable);

    @Query("select directMessage from DirectMessage directMessage left join fetch directMessage.responseTo")
    List<DirectMessage> findAllWithToOneRelationships();

    @Query("select directMessage from DirectMessage directMessage left join fetch directMessage.responseTo where directMessage.id =:id")
    Optional<DirectMessage> findOneWithToOneRelationships(@Param("id") Long id);
}
