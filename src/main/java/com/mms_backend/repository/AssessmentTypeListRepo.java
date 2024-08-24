package com.mms_backend.repository;

import com.mms_backend.entity.AssessmentTypeListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssessmentTypeListRepo extends JpaRepository<AssessmentTypeListEntity,Integer> {

    @Query(nativeQuery = true, value = "select distinct assessment_type_list.assessment_type_name from assessment_type_list inner join evaluationcriteria on evaluationcriteria.assessment_type = assessment_type_list.assessment_type_name inner join marks on marks.evaluation_criteria_id = evaluationcriteria.evaluationcriteria_id where marks.student_id=:student_id and marks.course_id=:course_id and marks.academic_year=:academic_year and evaluationcriteria.evaluationcriteria_id=:evaluationcriteria_id and assessment_type_list.ca_mid_end='Mid' limit 1;")
    List<String> findStudentMarksByCourseID(@Param("student_id") String student_id,@Param("course_id") String course_id,@Param("academic_year") String academic_year,@Param("evaluationcriteria_id") String evaluationcriteria_id);

}
