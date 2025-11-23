package com.fanflip.profile.repository;

import com.fanflip.profile.domain.UserSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {}
