package com.mms_backend.service;

import com.mms_backend.dto.MedicalDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.repository.MedicalRepo;
import com.mms_backend.entity.MedicalEntity;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class MedicalManageService {

    @Autowired
    MedicalRepo medicalRepo;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ResponseDTO responseDTO;
    public ResponseDTO getAllMedicals(){
        List<MedicalEntity> medicalEntities = medicalRepo.findAll();
        if (medicalEntities.isEmpty()){
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Medicals not found!");
        }else {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(modelMapper.map(medicalEntities, new TypeToken<ArrayList<MedicalDTO>>(){}.getType()));
            responseDTO.setMessage("Medicals found!");
        }
        return responseDTO;
    }
    public ResponseDTO insertMedicalsAsBulk(List<MedicalDTO> medicalDTOS){
        List<MedicalEntity> medicalsAsBulk = modelMapper.map(medicalDTOS,new TypeToken<ArrayList<MedicalEntity>>(){}.getType());
        try {
            medicalRepo.saveAll(medicalsAsBulk);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(medicalDTOS);
            responseDTO.setMessage("Medicals have been inserted");
        }catch (Exception e){
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(medicalDTOS);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }
    public ResponseDTO insertAMedical(MedicalDTO medicalDTO){
        MedicalEntity insertOneMedical = modelMapper.map(medicalDTO,MedicalEntity.class);
        try {
            medicalRepo.save(insertOneMedical);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(medicalDTO);
            responseDTO.setMessage("Medical has been inserted");
        }catch (Exception e){
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(medicalDTO);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }
    public ResponseDTO getAMedicalById(int id){
        if (medicalRepo.existsById(id)){

            Optional<MedicalEntity> medicalById = medicalRepo.findById(id);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(medicalById);
            responseDTO.setMessage("Data found");

        }else {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Data not found");
        }

        return responseDTO;
    }
    public ResponseDTO updateAMedicalById(MedicalDTO medicalDTO){
        MedicalEntity updateOneMedicalById = modelMapper.map(medicalDTO,MedicalEntity.class);
        try {
            medicalRepo.save(updateOneMedicalById);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(medicalDTO);
            responseDTO.setMessage("medical has been updated");
        }catch (Exception e){
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(medicalDTO);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }
    public ResponseDTO deleteAMedicalById(int id){
        if (medicalRepo.existsById(id)){
             medicalRepo.deleteById(id);
             responseDTO.setCode(VarList.RIP_SUCCESS);
             responseDTO.setContent(id);
             responseDTO.setMessage("medical has been deleted");
        }else {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(id);
            responseDTO.setMessage("medical id not found");
        }
        return responseDTO;
    }

}
