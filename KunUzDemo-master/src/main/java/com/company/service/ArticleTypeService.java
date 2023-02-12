package com.company.service;

import com.company.dto.ArticleTypeDTO;
import com.company.dto.ArticleTypeDTO;
import com.company.dto.ArticleTypeDTO;
import com.company.entity.ArticleTypeEntity;
import com.company.entity.ProfileEntity;
import com.company.entity.ArticleTypeEntity;
import com.company.entity.ArticleTypeEntity;
import com.company.enums.LangEnum;
import com.company.exp.ArticleTypeAlreadyExsistsException;
import com.company.exp.ItemNotFoundException;
import com.company.repository.ArticleTypeRepository;
import com.company.validation.ArticleTypeValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleTypeService {
    @Autowired
    private ArticleTypeRepository articleTypeRepository;
    @Autowired
    private ProfileService profileService;

    public ArticleTypeDTO create(ArticleTypeDTO dto, Integer pId){
        ArticleTypeValidation.isValid(dto);
        ProfileEntity profileEntity = profileService.get(pId);
        Optional<ArticleTypeEntity> optional = articleTypeRepository.findByKey(dto.getKey());
        if (optional.isPresent()){
            ArticleTypeEntity articleType = optional.get();
            if (articleType.getVisible()){
                throw new ArticleTypeAlreadyExsistsException("This type already exsists");
            } else {
                articleTypeRepository.updateVisible(true, articleType.getId());
                return toDTO(articleType);
            }
        }

        ArticleTypeEntity entity = new ArticleTypeEntity();
        entity.setKey(dto.getKey());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setProfile(profileEntity);
        articleTypeRepository.save(entity);
        return toDTO(entity);
    }

    public ArticleTypeDTO update(ArticleTypeDTO dto, Integer aId){
        ArticleTypeValidation.isValid(dto);
        Optional<ArticleTypeEntity> optional = articleTypeRepository.findById(aId);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Type Not Found");
        }
        ArticleTypeEntity entity = optional.get();
        if (!entity.getVisible()){
            articleTypeRepository.updateVisible(true, entity.getId());
            return toDTO(entity);
        }

        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setKey(dto.getKey());
        articleTypeRepository.save(entity);
        return toDTO(entity);
    }

    public String delete(Integer aId){
        ArticleTypeEntity entity = articleTypeRepository.findById(aId).orElseThrow(() -> new ItemNotFoundException("Type Not Found"));
        articleTypeRepository.updateVisible(false, entity.getId());
        return "Success";
    }

    public List<ArticleTypeDTO> getList(){
        List<ArticleTypeDTO> list = new ArrayList<>();
        articleTypeRepository.findAllByVisible(true).forEach(entity -> {
            list.add(toDTO(entity));
        });
        if (list.isEmpty()) {
            throw new ItemNotFoundException("Not Found!");
        }
        return list;
    }

    public ArticleTypeDTO getById(Integer articleTypeId){
        ArticleTypeEntity entity = get(articleTypeId);
        return toDTO(entity);
    }

    public ArticleTypeDTO getById(Integer articleTypeId, LangEnum lang){
        ArticleTypeEntity entity = get(articleTypeId);
        return toDTO(entity, lang);
    }

    public ArticleTypeEntity get(Integer articleTypeId){
        return articleTypeRepository.findByIdAndVisible(articleTypeId, true).orElseThrow(() -> new ItemNotFoundException("Tag Not Found"));
    }

    public List<ArticleTypeDTO> list(LangEnum lang) {
        List<ArticleTypeEntity> entityList = articleTypeRepository.findAllByVisible(true);
        List<ArticleTypeDTO> dtoList = new ArrayList<>();
        for (ArticleTypeEntity entity : entityList){
            ArticleTypeDTO dto = new ArticleTypeDTO();
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

    private ArticleTypeDTO toDTO(ArticleTypeEntity entity){
        ArticleTypeDTO dto = new ArticleTypeDTO();
        dto.setId(entity.getId());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
    private ArticleTypeDTO toDTO(ArticleTypeEntity entity, LangEnum lang) {
        ArticleTypeDTO dto = new ArticleTypeDTO();
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
