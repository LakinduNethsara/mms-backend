package com.mms_backend.service.AR;
import com.mms_backend.dto.AR.AcademicYearDetailsDTO;
import com.mms_backend.entity.AR.AcademicYearDetails;
import com.mms_backend.repository.AR.ARAcademicYearDetailsRepo;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class ARService {

    @Autowired
    private ARAcademicYearDetailsRepo arAcademicYearDetailsRepo;

    private ModelMapper mp;




    /* ----------------------------------------------------------------------New Update Start -------------------------------------------------------------------------------------------------------------------------------------------------------*/


    /*---------------------------------------------------------------------------------------- Service for academic_year_details table ----------------------------START-------------*/
    public List<AcademicYearDetailsDTO> getAcademicYearDetails(){
        List<AcademicYearDetails> academicYearDetails= arAcademicYearDetailsRepo.findAll();
        return mp.map(academicYearDetails, new TypeToken<ArrayList<AcademicYearDetailsDTO>>(){}.getType());
    }


    /*---------------------------------------------------------------------------------------- Service for academic_year_details table ----------------------------END-------------*/


}

