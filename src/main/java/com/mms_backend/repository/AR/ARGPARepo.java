package com.mms_backend.repository.AR;
import com.mms_backend.entity.GPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ARGPARepo extends JpaRepository<GPA,Integer> {

    @Query(nativeQuery = true, value = "select gpa.* from gpa inner join user on user.user_id = gpa.student_id where user.department_id=:department_id AND gpa.acadamic_year=:academic_year AND gpa.level=:level and gpa.semester=:semester and user.role='student'")
    List<GPA> getGpaListForResultBoard(String department_id, String academic_year, int level, int semester);        //Get list of all the GPAs for result board ar view

    //Calculate GPA and insert SGPA to the table - Stored procedure
    @Procedure(procedureName = "calculate_SGPA")
    void calculateSGPA(
            @Param("level_p") String level,
            @Param("sem_p") String semester,
            @Param("department_id_p") String departmentId,
            @Param("academic_year") String academicYear
    );


}