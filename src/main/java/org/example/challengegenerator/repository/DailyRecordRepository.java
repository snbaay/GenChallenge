package org.example.challengegenerator.repository;

import org.example.challengegenerator.models.DailyRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyRecordRepository extends JpaRepository<DailyRecordEntity, Long> {

    // Все записи за конкретную дату (если захочешь показывать список "сегодня")
    List<DailyRecordEntity> findAllByDate(LocalDate date);

    // Последняя запись за сегодня (самый удобный фикс твоей ошибки)
    DailyRecordEntity findFirstByDateOrderByIdDesc(LocalDate date);

    // История
    List<DailyRecordEntity> findAllByOrderByDateDesc();
}
