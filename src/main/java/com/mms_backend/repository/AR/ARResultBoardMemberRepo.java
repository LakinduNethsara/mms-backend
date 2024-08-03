package com.mms_backend.repository.AR;

import com.mms_backend.entity.AR.ResultBoardMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ARResultBoardMemberRepo extends JpaRepository<ResultBoardMember,Integer> {
    @Query(nativeQuery = true, value= "select result_board_member.id , user.user_id, user.name_with_initials, user.email, course.course_id, course.course_name from result_board_member inner join user on result_board_member.course_coordinator_id= user.user_id inner join course on result_board_member.course_id = course.course_id where result_board_id = :result_board_id AND user.role!='student'")     //Get all assigned marks sheets by result board id
    public List<Object> getAssignedMarksSheetsByResultBoardID(int result_board_id);

    @Query(nativeQuery = true, value= "select result_board_member.id , user.user_id, user.name_with_initials, user.email, course.course_id, course.course_name from result_board_member inner join user on result_board_member.course_coordinator_id= user.user_id inner join course on result_board_member.course_id = course.course_id where result_board_id = :result_board_id and result_board_member.course_coordinator_id= :course_coordinator_id")     //Get all assigned marks sheets by result board id and course coordinator id
    public List<Object> getAssignedMarksSheetsByExaminerIdAndResultBoardID(int result_board_id, String course_coordinator_id);


    @Modifying
    @Query(nativeQuery = true, value = "delete result_board_member from result_board_member inner join result_board on result_board_member.result_board_id= result_board.id  where result_board_member.result_board_id = :result_board_id and result_board.status='Not started'")     //Delete all assigned marks sheets by result board id
    int deleteAssignedMarksSheetsByResultBoardID(int result_board_id);
}
