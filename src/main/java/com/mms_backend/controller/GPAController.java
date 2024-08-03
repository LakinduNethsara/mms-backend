package com.mms_backend.controller;


import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.service.GPAService;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("/api/gpa")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GPAController
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String student_id;

    private String acadamic_year;

    private int level;

    private int semester;

    private double sgpa;

    private double cgpa;

    @Autowired
    private GPAService gpaService;

    @Autowired
    private ModelMapper modelMapper;
    @GetMapping("/GetGPAByLevelSemester/{level},{semester}")
    public ResponseEntity GetGPAByLevelSemester(@PathVariable("level")String level,@PathVariable("semester")String semester)
    {
       ResponseDTO responseDTO =gpaService.getGPAByLevelSemester(level,semester);

        if(responseDTO.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(responseDTO,HttpStatus.OK);
        }
        else {
            return new ResponseEntity(responseDTO,HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/GetGPAByStudent_Id/{student_id}")
    public ResponseEntity GetGPAByStudent_Id(@PathVariable("student_id")String student_id)
    {
        ResponseDTO responseDTO=gpaService.getGPAByStID(student_id);

        if(responseDTO.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        }
        return new ResponseEntity(responseDTO,HttpStatus.NOT_FOUND);

    }

}
