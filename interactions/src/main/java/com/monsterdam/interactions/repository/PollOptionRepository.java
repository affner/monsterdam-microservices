package com.monsterdam.interactions.repository;

import com.monsterdam.interactions.domain.PollOption;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PollOption entity.
 */
@Repository
public interface PollOptionRepository extends JpaRepository<PollOption, Long> {
    default Optional<PollOption> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PollOption> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PollOption> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select pollOption from PollOption pollOption left join fetch pollOption.poll",
        countQuery = "select count(pollOption) from PollOption pollOption"
    )
    Page<PollOption> findAllWithToOneRelationships(Pageable pageable);

    @Query("select pollOption from PollOption pollOption left join fetch pollOption.poll")
    List<PollOption> findAllWithToOneRelationships();

    @Query("select pollOption from PollOption pollOption left join fetch pollOption.poll where pollOption.id =:id")
    Optional<PollOption> findOneWithToOneRelationships(@Param("id") Long id);
}
