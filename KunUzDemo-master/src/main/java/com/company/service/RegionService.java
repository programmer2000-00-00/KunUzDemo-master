package com.company.service;

import com.company.dto.RegionDTO;
import com.company.entity.ProfileEntity;
import com.company.entity.RegionEntity;
import com.company.enums.LangEnum;
import com.company.exp.ItemNotFoundException;
import com.company.exp.RegionAlreadyExsistException;
import com.company.repository.RegionRepository;
import com.company.validation.RegionValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private ProfileService profileService;

    public RegionDTO create(RegionDTO dto, Integer pId) {
        RegionValidation.isValid(dto);

        ProfileEntity profileEntity = profileService.get(pId);
        Optional<RegionEntity> optional = regionRepository.findByKey(dto.getKey());
        if (optional.isPresent()) {
            RegionEntity entity = optional.get();
            if (entity.getVisible()) {
                throw new RegionAlreadyExsistException("Region Already Exsists");
            } else {
                regionRepository.updateVisible(true, entity.getId());
                return toDTO(entity);
            }
        }
        RegionEntity entity = new RegionEntity();
        entity.setKey(dto.getKey());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setProfile(profileEntity);
        regionRepository.save(entity);
        dto.setId(entity.getId());
        return toDTO(entity);
    }

    public RegionDTO update(RegionDTO dto, Integer regionId) {
        RegionValidation.isValid(dto);
        Optional<RegionEntity> optional = regionRepository.findById(regionId);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Region Not Found");
        }
        RegionEntity entity = optional.get();
        if (!entity.getVisible()){
            regionRepository.updateVisible(true, entity.getId());
            return toDTO(entity);
        }

        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setKey(dto.getKey());
        regionRepository.save(entity);
        return toDTO(entity);
    }

    public String delete(Integer regionId){
        RegionEntity entity = regionRepository.findById(regionId).orElseThrow(()-> new ItemNotFoundException("Region Not Found"));
        regionRepository.updateVisible(false, entity.getId());
        return "Success";
    }

    public List<RegionDTO> getList(){
        List<RegionDTO> list = new ArrayList<>();
        regionRepository.findAllByVisible(true).forEach(entity -> {
            list.add(toDTO(entity));
        });
        if (list.isEmpty()) {
            throw new ItemNotFoundException("Not Found!");
        }
        return list;
    }

    public RegionDTO getById(Integer regionId){
        RegionEntity entity = get(regionId);
        return toDTO(entity);
    }

    public RegionDTO getById(Integer regionId, LangEnum lang){
        RegionEntity entity = get(regionId);
        return toDTO(entity, lang);
    }

    public RegionEntity get(Integer regionId){
        return regionRepository.findByIdAndVisible(regionId, true).orElseThrow(() -> new ItemNotFoundException("Region Not Found"));
    }

    private RegionDTO toDTO(RegionEntity entity) {
        RegionDTO dto = new RegionDTO();
        dto.setId(entity.getId());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public List<RegionDTO> list(LangEnum lang) {
        List<RegionEntity> entityList = regionRepository.findAllByVisible(true);
        List<RegionDTO> dtoList = new ArrayList<>();
        for (RegionEntity entity : entityList){
            RegionDTO dto = new RegionDTO();
            dto.setKey(entity.getKey());
            dto.setId(entity.getId());

            switch (lang){
                case uz -> {
                    dto.setName(entity.getNameUz());
                }
                case ru -> {
                    dto.setName(entity.getNameRu());
                }
                case en -> {
                    dto.setName(entity.getNameEn());
                }
            }
            dtoList.add(dto);
        }
        return dtoList;
    }

    private RegionDTO toDTO(RegionEntity entity, LangEnum lang) {
        RegionDTO dto = new RegionDTO();
        dto.setId(entity.getId());
        dto.setKey(entity.getKey());
        switch (lang) {
            case uz:
                dto.setName(entity.getNameUz());
                break;
            case ru:
                dto.setName(entity.getNameRu());
                break;
            case en:
                dto.setName(entity.getNameEn());
                break;
        }
        return dto;
    }
}
