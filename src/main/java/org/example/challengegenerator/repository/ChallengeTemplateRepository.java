package org.example.challengegenerator.repository;

import org.example.challengegenerator.models.ChallengeTemplateEntity;
import org.example.challengegenerator.models.ChallengeType;
import org.example.challengegenerator.models.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChallengeTemplateRepository extends JpaRepository<ChallengeTemplateEntity, Long> {
    List<ChallengeTemplateEntity> findByTypeAndDifficulty(ChallengeType type, Difficulty difficulty);
}