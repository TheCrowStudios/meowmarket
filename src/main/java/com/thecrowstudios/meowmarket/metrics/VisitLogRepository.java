package com.thecrowstudios.meowmarket.metrics;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitLogRepository extends CrudRepository<VisitLog, Integer> {
    Long countByIsBotAndTimestampBetween(boolean isBot, LocalDateTime start, LocalDateTime end);
    List<VisitLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}