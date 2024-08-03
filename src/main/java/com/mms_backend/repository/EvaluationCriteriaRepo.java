package com.mms_backend.repository;

import com.mms_backend.entity.EvaluationCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluationCriteriaRepo extends JpaRepository<EvaluationCriteria,Integer>
{

    @Query(nativeQuery = true , value = "select * from evaluationcriteria where course_id=:course_id")
    public List<EvaluationCriteria> getEvaluationCriteria(@Param( "course_id")String course_id);
}
