package com.mms_backend.repository.AR;

import com.mms_backend.entity.AttendanceEligibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ARAttendanceEligibilityRepo extends JpaRepository<AttendanceEligibility,Integer> {
    @Query(nativeQuery=true, value ="select * from attendance_eligibility where student_id=:studentId and course_id=:courseId limit 1")
    AttendanceEligibility getAttendanceEligibilityByStudentIdAndCourseId(String studentId, String courseId);
}
