package com.monsterdam.catalogs.repository;

import com.monsterdam.catalogs.domain.HelpQuestion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface HelpQuestionRepositoryWithBagRelationships {
    Optional<HelpQuestion> fetchBagRelationships(Optional<HelpQuestion> helpQuestion);

    List<HelpQuestion> fetchBagRelationships(List<HelpQuestion> helpQuestions);

    Page<HelpQuestion> fetchBagRelationships(Page<HelpQuestion> helpQuestions);
}
