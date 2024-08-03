package com.mms_backend.repository.AR;

import com.mms_backend.entity.AR.Medical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ARMedicalRepo extends JpaRepository<Medical,Integer> {


    //Get all medical list with matching year
    @Query(nativeQuery = true, value = "select * from medical where academic_year= :academic_year")
    List<Medical> getAllMedicalSubmissionsByYear(String academic_year);


    //Get all medical list
    @Query(nativeQuery = true, value = "select * from medical")
    List<Medical> getAllMedicalSubmissions();



    //Get medical details of selected one student for one exam in selected year
    @Query(nativeQuery = true, value = "select * from medical where student_id=:student_id AND course_id = :course_id AND academic_year=:academic_year AND  exam_type= :exam_type")
    List<Medical> getSelectedStudentMedicalDetails(String student_id, String course_id, String academic_year, String exam_type);

}
