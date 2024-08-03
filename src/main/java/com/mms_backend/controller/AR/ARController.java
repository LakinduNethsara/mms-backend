package com.mms_backend.controller.AR;

import com.mms_backend.dto.AR.AcademicYearDetailsDTO;
import com.mms_backend.service.AR.ARService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/AssistantRegistrar")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ARController {
    @Autowired
    private ARService arService;


    /*---------------------------------------------------------------------------------------- Controller for academic_year_details table ----------------------------START-------------*/
    @GetMapping("/getAcademicYearDetails")
    public List<AcademicYearDetailsDTO> getAcademicYearDetails(){
        return arService.getAcademicYearDetails();


        /*Usage
            UpdateABPage
            CreateResultBoard
         */
    }

    /*---------------------------------------------------------------------------------------- Controller for academic_year_details table ----------------------------END-------------*/

}
