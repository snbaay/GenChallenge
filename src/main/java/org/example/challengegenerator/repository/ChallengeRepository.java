package org.example.challengegenerator.repository;

import org.example.challengegenerator.models.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    // Исправь createdDate на createdAt!
//    List<Challenge> findAllByOrderByCreatedAtDesc(); // БЫЛО: createdDate

    List<Challenge> findByCompleted(boolean completed);
    List<Challenge> findByType(String type);
    int countByCompleted(boolean completed);
}