package com.mms_backend.repository.Student;

import com.mms_backend.dto.AR.ARCourseRelatedDepartmentsDTO;
import com.mms_backend.entity.AR.ARCourseRelatedDepartments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentCourseRelatedDepartmentsRepo extends JpaRepository<ARCourseRelatedDepartments,Integer> {

    //Get credits of selected course
    @Query(nativeQuery = true, value = "select * from courses_related_departments where course_id =:course_id")
    List<ARCourseRelatedDepartments> get_course_credits(String course_id);
}
