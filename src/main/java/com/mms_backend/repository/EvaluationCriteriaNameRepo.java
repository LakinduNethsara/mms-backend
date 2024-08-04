package com.mms_backend.repository;

import com.mms_backend.entity.EvaluationCriteriaNameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluationCriteriaNameRepo extends JpaRepository<EvaluationCriteriaNameEntity,Integer> {
    @Query(nativeQuery = true, value="select a.id,a.evaluationcriteria_id,a.assignment_name,a.course_id from evaluationcriteria_name as a inner join   evaluationcriteria b on a.evaluationcriteria_id=b.evaluationcriteria_id where a.course_id=:course_id and type=:type ")
    List<EvaluationCriteriaNameEntity> getAssessmentTypes(@Param("course_id") String course_id,@Param("type") String type);
}
