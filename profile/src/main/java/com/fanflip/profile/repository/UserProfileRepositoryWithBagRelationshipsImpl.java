package com.fanflip.profile.repository;

import com.fanflip.profile.domain.UserProfile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class UserProfileRepositoryWithBagRelationshipsImpl implements UserProfileRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserProfile> fetchBagRelationships(Optional<UserProfile> userProfile) {
        return userProfile
            .map(this::fetchFolloweds)
            .map(this::fetchBlockedLists)
            .map(this::fetchLoyaLists)
            .map(this::fetchSubscribeds)
            .map(this::fetchJoinedEvents)
            .map(this::fetchHashtags);
    }

    @Override
    public Page<UserProfile> fetchBagRelationships(Page<UserProfile> userProfiles) {
        return new PageImpl<>(
            fetchBagRelationships(userProfiles.getContent()),
            userProfiles.getPageable(),
            userProfiles.getTotalElements()
        );
    }

    @Override
    public List<UserProfile> fetchBagRelationships(List<UserProfile> userProfiles) {
        return Optional
            .of(userProfiles)
            .map(this::fetchFolloweds)
            .map(this::fetchBlockedLists)
            .map(this::fetchLoyaLists)
            .map(this::fetchSubscribeds)
            .map(this::fetchJoinedEvents)
            .map(this::fetchHashtags)
            .orElse(Collections.emptyList());
    }

    UserProfile fetchFolloweds(UserProfile result) {
        return entityManager
            .createQuery(
                "select userProfile from UserProfile userProfile left join fetch userProfile.followeds where userProfile.id = :id",
                UserProfile.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<UserProfile> fetchFolloweds(List<UserProfile> userProfiles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, userProfiles.size()).forEach(index -> order.put(userProfiles.get(index).getId(), index));
        List<UserProfile> result = entityManager
            .createQuery(
                "select userProfile from UserProfile userProfile left join fetch userProfile.followeds where userProfile in :userProfiles",
                UserProfile.class
            )
            .setParameter("userProfiles", userProfiles)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    UserProfile fetchBlockedLists(UserProfile result) {
        return entityManager
            .createQuery(
                "select userProfile from UserProfile userProfile left join fetch userProfile.blockedLists where userProfile.id = :id",
                UserProfile.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<UserProfile> fetchBlockedLists(List<UserProfile> userProfiles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, userProfiles.size()).forEach(index -> order.put(userProfiles.get(index).getId(), index));
        List<UserProfile> result = entityManager
            .createQuery(
                "select userProfile from UserProfile userProfile left join fetch userProfile.blockedLists where userProfile in :userProfiles",
                UserProfile.class
            )
            .setParameter("userProfiles", userProfiles)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    UserProfile fetchLoyaLists(UserProfile result) {
        return entityManager
            .createQuery(
                "select userProfile from UserProfile userProfile left join fetch userProfile.loyaLists where userProfile.id = :id",
                UserProfile.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<UserProfile> fetchLoyaLists(List<UserProfile> userProfiles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, userProfiles.size()).forEach(index -> order.put(userProfiles.get(index).getId(), index));
        List<UserProfile> result = entityManager
            .createQuery(
                "select userProfile from UserProfile userProfile left join fetch userProfile.loyaLists where userProfile in :userProfiles",
                UserProfile.class
            )
            .setParameter("userProfiles", userProfiles)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    UserProfile fetchSubscribeds(UserProfile result) {
        return entityManager
            .createQuery(
                "select userProfile from UserProfile userProfile left join fetch userProfile.subscribeds where userProfile.id = :id",
                UserProfile.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<UserProfile> fetchSubscribeds(List<UserProfile> userProfiles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, userProfiles.size()).forEach(index -> order.put(userProfiles.get(index).getId(), index));
        List<UserProfile> result = entityManager
            .createQuery(
                "select userProfile from UserProfile userProfile left join fetch userProfile.subscribeds where userProfile in :userProfiles",
                UserProfile.class
            )
            .setParameter("userProfiles", userProfiles)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    UserProfile fetchJoinedEvents(UserProfile result) {
        return entityManager
            .createQuery(
                "select userProfile from UserProfile userProfile left join fetch userProfile.joinedEvents where userProfile.id = :id",
                UserProfile.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<UserProfile> fetchJoinedEvents(List<UserProfile> userProfiles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, userProfiles.size()).forEach(index -> order.put(userProfiles.get(index).getId(), index));
        List<UserProfile> result = entityManager
            .createQuery(
                "select userProfile from UserProfile userProfile left join fetch userProfile.joinedEvents where userProfile in :userProfiles",
                UserProfile.class
            )
            .setParameter("userProfiles", userProfiles)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    UserProfile fetchHashtags(UserProfile result) {
        return entityManager
            .createQuery(
                "select userProfile from UserProfile userProfile left join fetch userProfile.hashtags where userProfile.id = :id",
                UserProfile.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<UserProfile> fetchHashtags(List<UserProfile> userProfiles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, userProfiles.size()).forEach(index -> order.put(userProfiles.get(index).getId(), index));
        List<UserProfile> result = entityManager
            .createQuery(
                "select userProfile from UserProfile userProfile left join fetch userProfile.hashtags where userProfile in :userProfiles",
                UserProfile.class
            )
            .setParameter("userProfiles", userProfiles)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
