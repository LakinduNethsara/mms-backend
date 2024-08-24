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

    @Query(nativeQuery = true , value = "select * from evaluationcriteria inner join assessment_type_list on assessment_type_list.assessment_type_name = evaluationcriteria.assessment_type where course_id=:course_id and evaluationcriteria.type='CA'")
    public List<Object> getCA(@Param( "course_id")String course_id);

    @Query(nativeQuery = true , value = "select * from evaluationcriteria where course_id=:course_id and type='CA'")
    public List<EvaluationCriteria> getECbyCourseIDCA(@Param( "course_id")String course_id);

    @Query(nativeQuery = true , value = "select evaluationcriteria.* from evaluationcriteria inner join scoredb.assessment_type_list on assessment_type_list.assessment_type_name=evaluationcriteria.assessment_type where evaluationcriteria.course_id=:course_id and evaluationcriteria.type='CA' and assessment_type_list.ca_mid_end='Mid';")
    public List<EvaluationCriteria> getEBMIDbyCourseID(@Param( "course_id")String course_id);

    @Query(nativeQuery = true , value = "select evaluationcriteria.* from evaluationcriteria inner join assessment_type_list on assessment_type_list.assessment_type_name = evaluationcriteria.assessment_type where evaluationcriteria.course_id=:course_id and evaluationcriteria.type='CA' and assessment_type_list.ca_mid_end='Mid';")
    public List<EvaluationCriteria> getCurrent_mid_Details(@Param( "course_id")String course_id);

    @Query(nativeQuery = true , value = "select distinct evaluationcriteria.* from evaluationcriteria inner join marks on marks.evaluation_criteria_id = evaluationcriteria.evaluationcriteria_id where marks.student_id=:student_id and marks.course_id=:course_id and marks.academic_year=:academic_year and evaluationcriteria.type='CA';")
    public List<EvaluationCriteria> getEvaluationCriteriaByStudentIDCourseID(@Param( "student_id")String student_id,@Param( "course_id")String course_id,@Param( "academic_year")String academic_year);
}
