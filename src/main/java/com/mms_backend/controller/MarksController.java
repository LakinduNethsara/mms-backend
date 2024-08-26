package com.mms_backend.controller;

import com.mms_backend.dto.MarksDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.entity.MarksEntity;
import com.mms_backend.service.MarksService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("/api/StudentAssessment")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MarksController {

    @Autowired
    private MarksService marksService;





    @GetMapping("get/score")
    public List<MarksDTO> getAllScore(){
       return marksService.getAllScore();
    }

    @GetMapping("get/scoreByCourseId/{course_id}/{academic_year}")
    public List<MarksDTO> getAllScoreByCourseId(@PathVariable String course_id,@PathVariable String academic_year){
        return marksService.getAllScoreByCourseId(course_id,academic_year);
    }

    @PutMapping("edit/score/{id}")
    public void editScore(@RequestBody MarksDTO marksDTO)
    {
        marksService.editScore(marksDTO);
    }


    @GetMapping("get/scorebyID/{id}")
    public Optional<MarksEntity> getScoreByID(@PathVariable int id){
        return marksService.getScoreByID(id);
    }

    @GetMapping("get/scorebyStudentID/{student_id}")
    public ResponseEntity getScoreByStudent_ID(@PathVariable String student_id){

        ResponseDTO responseDTO=marksService.getScoreByStudent_ID(student_id);

        if(responseDTO.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(responseDTO,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(responseDTO,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("get/scorebyStuIDCourseID/{course_id},{student_id}")
    public ResponseEntity getScoreByStudent_ID(@PathVariable String course_id, @PathVariable String student_id){

        List<MarksDTO> list=marksService.getScoreByStuIDCourseID(course_id,student_id);
        if(list.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity(list,HttpStatus.OK);
    }


    @GetMapping("get/scorebyLS/{level},{semester}")
    public ResponseEntity getScoreByLS(@PathVariable String level,@PathVariable String semester)
    {
        List<MarksDTO> list= marksService.getScoreByLS(level,semester);
        if(list.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity(list,HttpStatus.OK);
    }

    @GetMapping("getEnteredCAMarks/{course_id},{academic_year}")
    public ResponseEntity getenteredCAMarks(@PathVariable String course_id,@PathVariable String academic_year)
    {
        ResponseDTO responseDTO=marksService.getenteredCAMarks(course_id, academic_year);
        if(responseDTO.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(responseDTO,HttpStatus.OK);
        }
        else
            return new ResponseEntity(responseDTO,HttpStatus.NOT_FOUND);


    }

    @GetMapping("getEnteredFAMarks/{course_id},{academic_year}")
    public ResponseEntity getenteredFAMarks(@PathVariable String course_id, @PathVariable String academic_year)
    {
        ResponseDTO responseDTO=marksService.getenteredFAMarks(course_id, academic_year);
        if(responseDTO.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(responseDTO,HttpStatus.OK);
        }
        else
            return new ResponseEntity(responseDTO,HttpStatus.NOT_FOUND);
    }

    @PostMapping("inputCAMarks")
    public ResponseEntity enterCAMarks(@RequestBody List<MarksDTO> list)
    {
        ResponseDTO response=marksService.saveCAMarks(list);
        if(response.getCode().equals(VarList.RIP_SUCCESS))
        {
            return  new ResponseEntity(HttpStatus.OK);
        }
        else
            return  new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("getMarksForCA/{course_id},{academic_year}")
    public List<Object> getMarksForCA(@PathVariable String course_id, @PathVariable String academic_year)
    {
        List<Object> list=marksService.getMarksForCA(course_id,academic_year);
        return list;

    }



    //------------------------------------------------Select 1st , 2nd or average of end theory marks and insert
    @GetMapping("GenerateFinalMarksFromEnd/{assignment_name}/{course_id}/{selected}/{academic_year}")
    public void GenerateFinalMarksFromEnd(@PathVariable String assignment_name,@PathVariable String course_id,@PathVariable String selected,@PathVariable String academic_year){
        marksService.GenerateFinalMarksFromEnd(assignment_name,course_id,selected,academic_year);
    }
}
