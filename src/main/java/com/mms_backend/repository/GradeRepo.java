package com.mms_backend.repository;

import com.mms_backend.entity.AR.Grade;
import com.mms_backend.entity.EvaluationCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GradeRepo extends JpaRepository<Grade,Integer> {
    @Query(nativeQuery = true , value = "select * from grade where  student_id =:student_id and course_id=:course_id limit 1;")
    public Grade getGradeDetailsBY_SIID_CID(@Param( "student_id")String student_id,@Param( "course_id")String course_id);

    @Query(nativeQuery = true, value = "select grade.* from grade where student_id in\n" +
            "    (select student_id from studentregcourses where course_id=:course_id and academic_year=:academic_year and is_repeat=0) and\n" +
            "    course_id=:course_id and total_rounded_mark is null")
    public List<Grade> isFinalized(String course_id, String academic_year);

}
