package com.mms_backend.repository;

import com.mms_backend.entity.EvaluationCriteriaNameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluationCriteriaNameRepo extends JpaRepository<EvaluationCriteriaNameEntity,Integer> {
    @Query(nativeQuery = true, value="SELECT DISTINCT a.id,a.evaluationcriteria_id,a.assignment_name,a.course_id FROM evaluationcriteria_name a INNER JOIN evaluationcriteria b ON b.evaluationcriteria_id = a.evaluationcriteria_id WHERE b.course_id =:course_id AND b.type =:type AND a.assignment_name NOT IN ( SELECT assignment_name FROM marks WHERE course_id =:course_id );")
    List<EvaluationCriteriaNameEntity> getAssessmentTypes(@Param("course_id") String course_id,@Param("type") String type);
}
