package com.mms_backend.repository;

import com.mms_backend.entity.FinalMarksselection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FinalSelectionRepo extends JpaRepository<FinalMarksselection,Integer> {
    @Query(nativeQuery = true,value="SELECT selected FROM scoredb.finalmarksselection where course_id=:course_id and academic_year=:academic_year")
    String getSelected(@Param("course_id") String course_id,@Param("academic_year") String academic_year);
}
