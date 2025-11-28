package com.monsterdam.profile.repository;

import com.monsterdam.profile.domain.PersonalSocialLinks;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PersonalSocialLinks entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonalSocialLinksRepository extends JpaRepository<PersonalSocialLinks, Long> {}
