package com.fanflip.catalogs.repository;

import com.fanflip.catalogs.domain.State;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the State entity.
 */
@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    default Optional<State> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<State> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<State> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select state from State state left join fetch state.country", countQuery = "select count(state) from State state")
    Page<State> findAllWithToOneRelationships(Pageable pageable);

    @Query("select state from State state left join fetch state.country")
    List<State> findAllWithToOneRelationships();

    @Query("select state from State state left join fetch state.country where state.id =:id")
    Optional<State> findOneWithToOneRelationships(@Param("id") Long id);
}
