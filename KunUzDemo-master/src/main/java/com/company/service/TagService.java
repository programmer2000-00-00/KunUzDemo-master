package com.company.service;

import com.company.dto.TagDTO;
import com.company.entity.ProfileEntity;
import com.company.entity.TagEntity;
import com.company.enums.LangEnum;
import com.company.exp.ItemNotFoundException;
import com.company.exp.TagAlreadyExsistException;
import com.company.repository.TagRepository;
import com.company.validation.TagValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ProfileService profileService;

    public TagDTO create(TagDTO dto, Integer pId) {
        TagValidation.isValid(dto);

        ProfileEntity profileEntity = profileService.get(pId);
        Optional<TagEntity> optional = tagRepository.findByKey(dto.getKey());
        if (optional.isPresent()) {
            TagEntity entity = optional.get();
            if (entity.getVisible()) {
                throw new TagAlreadyExsistException("Tag Already Exsists");
            } else {
                tagRepository.updateVisible(true, entity.getId());
                return toDTO(entity);
            }
        }
        TagEntity entity = new TagEntity();
        entity.setKey(dto.getKey());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setProfile(profileEntity);
        tagRepository.save(entity);
        dto.setId(entity.getId());
        return toDTO(entity);
    }

    public TagDTO update(TagDTO dto, Integer regionId) {
        TagValidation.isValid(dto);
        Optional<TagEntity> optional = tagRepository.findById(regionId);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Tag Not Found");
        }
        TagEntity entity = optional.get();
        if (!entity.getVisible()){
            tagRepository.updateVisible(true, entity.getId());
            return toDTO(entity);
        }

        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setKey(dto.getKey());
        tagRepository.save(entity);
        return toDTO(entity);
    }

    public String delete(Integer regionId){
        TagEntity entity = tagRepository.findById(regionId).orElseThrow(()-> new ItemNotFoundException("Tag Not Found"));
        tagRepository.updateVisible(false, entity.getId());
        return "Success";
    }

    public List<TagDTO> getList(){
        List<TagDTO> list = new ArrayList<>();
        tagRepository.findAllByVisible(true).forEach(entity -> {
            list.add(toDTO(entity));
        });
        if (list.isEmpty()) {
            throw new ItemNotFoundException("Not Found!");
        }
        return list;
    }

    public TagDTO getById(Integer regionId){
        TagEntity entity = get(regionId);
        return toDTO(entity);
    }

    public TagEntity get(Integer regionId){
        return tagRepository.findByIdAndVisible(regionId, true).orElseThrow(() -> new ItemNotFoundException("Tag Not Found"));
    }

    private TagDTO toDTO(TagEntity entity) {
        TagDTO dto = new TagDTO();
        dto.setId(entity.getId());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public List<TagDTO> list(LangEnum lang) {
        List<TagEntity> entityList = tagRepository.findAllByVisible(true);
        List<TagDTO> dtoList = new ArrayList<>();
        for (TagEntity entity : entityList){
            TagDTO dto = new TagDTO();
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
    private TagDTO toDTO(TagEntity entity, LangEnum lang) {
        TagDTO dto = new TagDTO();
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

    public List<TagDTO> getTagList(List<Integer> tagList, LangEnum lang) {
        List<TagEntity> entityList = tagRepository.findAllById(tagList);
        List<TagDTO> dtoList = new LinkedList<>();
        for (TagEntity entity : entityList) {
            dtoList.add(toDTO(entity, lang));
        }
        return dtoList;
    }
}
