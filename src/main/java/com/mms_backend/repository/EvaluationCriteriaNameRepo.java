package com.mms_backend.repository;

import com.mms_backend.entity.EvaluationCriteriaNameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluationCriteriaNameRepo extends JpaRepository<EvaluationCriteriaNameEntity,Integer> {
    @Query(nativeQuery = true, value="SELECT DISTINCT a.id,a.evaluationcriteria_id,a.assignment_name,a.course_id FROM evaluationcriteria_name a INNER JOIN evaluationcriteria b ON b.evaluationcriteria_id = a.evaluationcriteria_id WHERE b.course_id =:course_id AND b.type =:type AND a.assignment_name NOT IN ( SELECT assignment_name FROM marks WHERE course_id =:course_id );")
    List<EvaluationCriteriaNameEntity> getAssessmentTypes(@Param("course_id") String course_id,@Param("type") String type);

    @Query (nativeQuery = true, value = "select evaluationcriteria_name.* from marks inner join evaluationcriteria on evaluationcriteria.evaluationcriteria_id = marks.evaluation_criteria_id inner join assessment_type_list on assessment_type_list.assessment_type_name= evaluationcriteria.assessment_type inner join evaluationcriteria_name on evaluationcriteria.evaluationcriteria_id = evaluationcriteria_name.evaluationcriteria_id and marks.assignment_name=evaluationcriteria_name.assignment_name where marks.student_id=:student_id and marks.course_id =:course_id and assessment_type_list.ca_mid_end='Mid' and marks.assignment_score='MC' and marks.academic_year=:prev_academic_year;")
    List<EvaluationCriteriaNameEntity> getMCMidName(@Param("student_id") String student_id,@Param("course_id") String course_id,@Param("prev_academic_year") String academic_year);
}
