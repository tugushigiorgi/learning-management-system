package com.leverx.learning_management_system.course;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
  @Query("SELECT c FROM Course c ORDER BY c.coinsPaid DESC LIMIT 5")
  List<Course> findTop5MostPopular();

  @Query("SELECT c FROM Course c WHERE c.settings.startDate BETWEEN :start AND :end")
  List<Course> findAllByStartDateBetween(LocalDateTime start, LocalDateTime end);
}
