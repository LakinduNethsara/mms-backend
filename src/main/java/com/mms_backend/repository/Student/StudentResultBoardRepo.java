package com.mms_backend.repository.Student;

import com.mms_backend.entity.AR.ResultBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentResultBoardRepo extends JpaRepository<ResultBoard,Integer> {

    @Query(nativeQuery = true, value="SELECT DISTINCT  result_board.* FROM (result_board INNER JOIN result_board_member ON result_board.id= result_board_member.result_board_id) INNER JOIN mark_approved_level ON mark_approved_level.course_id= result_board_member.course_id AND mark_approved_level.academic_year= result_board.academic_year AND result_board.department=mark_approved_level.department_id WHERE mark_approved_level.approval_level =:approval_level AND result_board.status =:status and result_board.department=:department_id and result_board.level=:level and result_board.semester=:semester order by result_board.academic_year desc , result_board.level asc, result_board.semester asc limit 1")
    ResultBoard getPublishedMarkSheets(String approval_level, String status, String department_id, String level, String semester);

    @Query(nativeQuery = true, value="SELECT DISTINCT  result_board.* FROM (result_board INNER JOIN result_board_member ON result_board.id= result_board_member.result_board_id) INNER JOIN mark_approved_level ON mark_approved_level.course_id= result_board_member.course_id AND mark_approved_level.academic_year= result_board.academic_year AND result_board.department=mark_approved_level.department_id WHERE mark_approved_level.approval_level =:approval_level AND result_board.status =:status order by result_board.academic_year desc , result_board.level asc, result_board.semester asc")
    List<ResultBoard> getPublishedMarksSheetList(String approval_level, String status);      //Get result board details where marks published

}
