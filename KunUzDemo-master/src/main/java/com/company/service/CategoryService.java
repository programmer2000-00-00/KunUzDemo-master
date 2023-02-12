package com.company.service;

import com.company.dto.CategoryDTO;
import com.company.entity.CategoryEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.LangEnum;
import com.company.exp.CategoryAlreadyExsistException;
import com.company.exp.ItemNotFoundException;
import com.company.repository.CategoryRepository;
import com.company.validation.CategoryValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProfileService profileService;

    public CategoryDTO create(CategoryDTO dto, Integer pId) {
        CategoryValidation.isValid(dto);

        ProfileEntity profileEntity = profileService.get(pId);
        Optional<CategoryEntity> optional = categoryRepository.findByKey(dto.getKey());
        if (optional.isPresent()) {
            CategoryEntity entity = optional.get();
            if (entity.getVisible()) {
                throw new CategoryAlreadyExsistException("Category Already Exsists");
            } else {
                categoryRepository.updateVisible(true, entity.getId());
                return toDTO(entity);
            }
        }
        CategoryEntity entity = new CategoryEntity();
        entity.setKey(dto.getKey());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setProfile(profileEntity);
        categoryRepository.save(entity);
        dto.setId(entity.getId());
        return toDTO(entity);
    }

    public CategoryDTO update(CategoryDTO dto, Integer regionId) {
        CategoryValidation.isValid(dto);
        Optional<CategoryEntity> optional = categoryRepository.findById(regionId);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Category Not Found");
        }
        CategoryEntity entity = optional.get();
        if (!entity.getVisible()){
            categoryRepository.updateVisible(true, entity.getId());
            return toDTO(entity);
        }

        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setKey(dto.getKey());
        categoryRepository.save(entity);
        return toDTO(entity);
    }

    public String delete(Integer regionId){
        CategoryEntity entity = categoryRepository.findById(regionId).orElseThrow(()-> new ItemNotFoundException("Category Not Found"));
        categoryRepository.updateVisible(false, entity.getId());
        return "Success";
    }

    public List<CategoryDTO> getList(){
        List<CategoryDTO> list = new ArrayList<>();
        categoryRepository.findAllByVisible(true).forEach(entity -> {
            list.add(toDTO(entity));
        });
        if (list.isEmpty()) {
            throw new ItemNotFoundException("Not Found!");
        }
        return list;
    }

    public CategoryDTO getById(Integer categoryId){
        CategoryEntity entity = get(categoryId);
        return toDTO(entity);
    }

    public CategoryDTO getById(Integer categoryId,LangEnum lang){
        CategoryEntity entity = get(categoryId);
        return toDTO(entity, lang);
    }

    public CategoryEntity get(Integer regionId){
        return categoryRepository.findByIdAndVisible(regionId, true).orElseThrow(() -> new ItemNotFoundException("Category Not Found"));
    }

    private CategoryDTO toDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    private CategoryDTO toDTO(CategoryEntity entity, LangEnum lang) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setKey(entity.getKey());
        switch (lang) {
            case uz -> dto.setName(entity.getNameUz());
            case ru -> dto.setName(entity.getNameRu());
            case en -> dto.setName(entity.getNameEn());
        }
        return dto;
    }

    public List<CategoryDTO> list(LangEnum lang) {
        List<CategoryEntity> entityList = categoryRepository.findAllByVisible(true);
        List<CategoryDTO> dtoList = new ArrayList<>();
        for (CategoryEntity entity : entityList){
            CategoryDTO dto = new CategoryDTO();
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
}
