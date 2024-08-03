package com.mms_backend.repository.AR;

import com.mms_backend.entity.AR.ResultBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ARResultBoardRepo extends JpaRepository<ResultBoard,Integer>{

    //Get result board availability
    @Query(nativeQuery = true, value = "select * from result_board where department= :department AND level= :level AND semester= :semester AND academic_year= :academic_year")
    List<ResultBoard> isResultBoardAvailable(String department, String level, String semester, String academic_year);

    //Get created result board list
    @Query(nativeQuery = true, value="select * from result_board order by academic_year desc , semester desc, result_board.level asc, department desc")
    List <ResultBoard> getCreatedResultBoardList();

    @Query(nativeQuery = true, value = "select result_board.* from result_board  where status='Ended'")           //Get finished result board list
    List<ResultBoard> getFinishedResultBoardList();

    @Modifying
    @Query(nativeQuery = true, value="delete from result_board where result_board.id= :id and result_board.status='Not started'")       //Delete not started result board
     int deleteNotStartedResultBoard(int id);

    @Query(nativeQuery = true, value="SELECT DISTINCT  result_board.* FROM (result_board INNER JOIN result_board_member ON result_board.id= result_board_member.result_board_id) INNER JOIN mark_approved_level ON mark_approved_level.course_id= result_board_member.course_id AND mark_approved_level.academic_year= result_board.academic_year AND result_board.department=mark_approved_level.department_id WHERE mark_approved_level.approval_level =:approval_level AND result_board.status =:status order by result_board.academic_year desc , result_board.level asc, result_board.semester asc")
    List<ResultBoard> getCertifyPendingResultBoards(String approval_level, String status);      //Get result board details where AR can certify (Available for AR certification)
}
