package com.fanflip.interactions.repository;

import com.fanflip.interactions.domain.PollVote;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PollVote entity.
 */
@Repository
public interface PollVoteRepository extends JpaRepository<PollVote, Long> {
    default Optional<PollVote> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PollVote> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PollVote> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select pollVote from PollVote pollVote left join fetch pollVote.pollOption",
        countQuery = "select count(pollVote) from PollVote pollVote"
    )
    Page<PollVote> findAllWithToOneRelationships(Pageable pageable);

    @Query("select pollVote from PollVote pollVote left join fetch pollVote.pollOption")
    List<PollVote> findAllWithToOneRelationships();

    @Query("select pollVote from PollVote pollVote left join fetch pollVote.pollOption where pollVote.id =:id")
    Optional<PollVote> findOneWithToOneRelationships(@Param("id") Long id);
}
