package de.csgr.repository;

import de.csgr.entity.YearPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YearPlanRepository extends JpaRepository<YearPlan, String> {
}
