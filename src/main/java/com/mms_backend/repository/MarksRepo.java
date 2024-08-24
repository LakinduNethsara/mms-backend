package com.mms_backend.repository;

import com.mms_backend.entity.MarksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MarksRepo extends JpaRepository<MarksEntity,Integer> {
    @Query(nativeQuery = true, value = "select * from marks  where course_id=:course_id ")
    List<MarksEntity> findStudentMarksByCourseID(@Param("course_id") String course_id);

    @Query(nativeQuery = true, value = "select * from marks where student_id=:student_id")
    List<MarksEntity> getScoreByStudent_ID(@Param("student_id") String student_id);

    @Query(nativeQuery = true, value = "select * from marks inner join courses on course.course_id=marks.course_id where level=:level and semester=:semester")
    List<MarksEntity> getScoreByLS(@Param("level") String level,@Param("semester") String semester);

    @Query(nativeQuery = true, value = "select * from marks where course_id=:course_id and student_id=:student_id")
    List<MarksEntity> getScoreByStuIDCourseID(@Param("course_id") String course_id,@Param("student_id") String student_id);

    @Modifying
    @Query(nativeQuery = true, value = "update marks set assignment_score=:value where student_id=:student_id and course_id=:course_id and assignment_name=:key")
    void updateEndMarks(@Param("value")String value,@Param("student_id")String student_id,@Param("course_id")String course_id,@Param("key")String key);

    @Query(nativeQuery = true,value = "select marks.id, marks.student_id ,marks.course_id ,marks.academic_year ,course.level ,course.semester ,marks.assignment_name ,marks.assignment_score ,marks.evaluation_criteria_id  from marks inner join evaluationcriteria on marks.evaluation_criteria_id=evaluationcriteria.evaluationcriteria_id inner join course on course.course_id = marks.course_id where evaluationcriteria.type='CA' and evaluationcriteria.course_id=:course_id and marks.academic_year=:academic_year;")
    List<MarksEntity> getCAMarks(@Param("course_id") String course_id ,@Param("academic_year") String academic_year);

    @Query(nativeQuery = true,value = "select marks.id, marks.student_id ,marks.course_id ,marks.academic_year ,course.level ,course.semester ,marks.assignment_name ,marks.assignment_score ,marks.evaluation_criteria_id  from marks inner join evaluationcriteria on marks.evaluation_criteria_id=evaluationcriteria.evaluationcriteria_id inner join course on course.course_id = marks.course_id where evaluationcriteria.type='End' and evaluationcriteria.course_id=:course_id and marks.academic_year=:academic_year;")
    List<MarksEntity> getFAMarks(@Param("course_id") String course_id ,@Param("academic_year") String academic_year);

    @Query (nativeQuery = true, value = "select *  from marks inner join evaluationcriteria on marks.evaluation_criteria_id = evaluationcriteria.evaluationcriteria_id inner join studentregcourses on marks.student_id = studentregcourses.student_id and marks.course_id = studentregcourses.course_id and marks.academic_year = studentregcourses.academic_year where marks.course_id=:course_id and marks.academic_year=:academic_year and evaluationcriteria.type='CA'")
    List<Object> getMarksForCA(@Param("course_id") String course_id,@Param("academic_year") String academic_year);

    @Query(nativeQuery=true ,value = "select * from marks where student_id=:student_id and course_id=:course_id and assignment_name=:assignment_name and academic_year=:academic_year")
    List<MarksEntity> getMarksByStuIDCourseIDAssignmentName(@Param("student_id") String student_id,@Param("course_id") String course_id,@Param("assignment_name") String assignment_name,@Param("academic_year") String academic_year);

    @Query(nativeQuery=true ,value = "select marks.* from marks inner join evaluationcriteria on evaluationcriteria.evaluationcriteria_id = marks.evaluation_criteria_id inner join assessment_type_list on assessment_type_list.assessment_type_name = evaluationcriteria.assessment_type where marks.student_id=:student_id and marks.course_id=:course_id and marks.academic_year=:academic_year and assessment_type_list.ca_mid_end='CA'")
    List<MarksEntity> getOLDCAByStuIDCourseID_AY(@Param("student_id") String student_id,@Param("course_id") String course_id,@Param("academic_year") String academic_year);

    @Query(nativeQuery=true ,value = "select marks.* from marks inner join evaluationcriteria on evaluationcriteria.evaluationcriteria_id = marks.evaluation_criteria_id where marks.student_id=:student_id and marks.course_id=:course_id and marks.academic_year=:academic_year and evaluationcriteria.evaluationcriteria_id=:evaluationcriteria_id;")
    List<MarksEntity> getCurrentCAByStuIDCourseID_AY(@Param("student_id") String student_id,@Param("course_id") String course_id,@Param("academic_year") String academic_year,@Param("evaluationcriteria_id") String evaluationcriteria_id);

    @Query(nativeQuery=true ,value = "select marks.* from marks inner join evaluationcriteria on evaluationcriteria.evaluationcriteria_id = marks.evaluation_criteria_id inner join assessment_type_list on assessment_type_list.assessment_type_name = evaluationcriteria.assessment_type where marks.student_id=:student_id and marks.course_id=:course_id and marks.academic_year=:academic_year and assessment_type_list.ca_mid_end='Mid'")
    List<MarksEntity> getCurrentMIDValue(@Param("student_id") String student_id,@Param("course_id") String course_id,@Param("academic_year") String academic_year);



}
