package com.mms_backend.repository.Student;

import com.mms_backend.entity.EvaluationCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentEvaluationCriteriaRepo extends JpaRepository<EvaluationCriteria,Integer>{

    @Query(nativeQuery = true, value = "select * from evaluationcriteria where course_id = :course_id")
    List<EvaluationCriteria> getEvaluationCriteriaByCourseId(String course_id);     //Get list of all the evaluation criteria by selected course id
}
