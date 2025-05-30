package com.leverx.learningmanagementsystem.courseSettings;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseSettingsRepository extends JpaRepository<CourseSettings, UUID> {
}
