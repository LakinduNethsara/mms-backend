package com.mms_backend.repository;

import com.mms_backend.entity.Marks_edit_log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface MarksEditLogRepo extends JpaRepository<Marks_edit_log,Integer> {

    @Query(nativeQuery = true,value = "select * from marks_edit_log where course_id=:course_id and student_id=:student_id")
    List<Marks_edit_log> getEditLogsforaStudentCourse(@Param("course_id") String course_id, @Param("student_id") String student_id);

    @Procedure(procedureName = "updateEndMarks")
    void updateEndMarksProcedure(
            @Param("p_assignment_name") String assignment_name,
            @Param("p_course_id") String course_id,
            @Param("p_academic_year") String academic_year,
            @Param("p_mark") String end_mark,
            @Param("p_student_id") String student_id
    );
}
