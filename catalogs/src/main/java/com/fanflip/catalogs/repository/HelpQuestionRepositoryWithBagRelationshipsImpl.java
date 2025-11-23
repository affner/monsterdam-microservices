package com.fanflip.catalogs.repository;

import com.fanflip.catalogs.domain.HelpQuestion;
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
public class HelpQuestionRepositoryWithBagRelationshipsImpl implements HelpQuestionRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<HelpQuestion> fetchBagRelationships(Optional<HelpQuestion> helpQuestion) {
        return helpQuestion.map(this::fetchQuestions);
    }

    @Override
    public Page<HelpQuestion> fetchBagRelationships(Page<HelpQuestion> helpQuestions) {
        return new PageImpl<>(
            fetchBagRelationships(helpQuestions.getContent()),
            helpQuestions.getPageable(),
            helpQuestions.getTotalElements()
        );
    }

    @Override
    public List<HelpQuestion> fetchBagRelationships(List<HelpQuestion> helpQuestions) {
        return Optional.of(helpQuestions).map(this::fetchQuestions).orElse(Collections.emptyList());
    }

    HelpQuestion fetchQuestions(HelpQuestion result) {
        return entityManager
            .createQuery(
                "select helpQuestion from HelpQuestion helpQuestion left join fetch helpQuestion.questions where helpQuestion.id = :id",
                HelpQuestion.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<HelpQuestion> fetchQuestions(List<HelpQuestion> helpQuestions) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, helpQuestions.size()).forEach(index -> order.put(helpQuestions.get(index).getId(), index));
        List<HelpQuestion> result = entityManager
            .createQuery(
                "select helpQuestion from HelpQuestion helpQuestion left join fetch helpQuestion.questions where helpQuestion in :helpQuestions",
                HelpQuestion.class
            )
            .setParameter("helpQuestions", helpQuestions)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
