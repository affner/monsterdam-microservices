package com.monsterdam.catalogs.repository;

import com.monsterdam.catalogs.domain.HelpQuestion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HelpQuestion entity.
 *
 * When extending this class, extend HelpQuestionRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface HelpQuestionRepository extends HelpQuestionRepositoryWithBagRelationships, JpaRepository<HelpQuestion, Long> {
    default Optional<HelpQuestion> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<HelpQuestion> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<HelpQuestion> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
