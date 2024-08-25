package com.mms_backend.repository.AR;

import com.mms_backend.entity.StudentRegCourses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ARStudentRegCourses extends JpaRepository<StudentRegCourses,Integer> {
    @Query (nativeQuery = true, value =" select studentregcourses.* from studentregcourses where student_id=:student_id and course_id=:course_id and academic_year=:academic_year")
    List<StudentRegCourses> checkStudentRepeatStatus(String student_id, String course_id, String academic_year);
}
