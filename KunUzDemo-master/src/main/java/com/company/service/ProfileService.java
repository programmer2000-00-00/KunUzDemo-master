package com.company.service;

import com.company.dto.AttachDTO;
import com.company.dto.ProfileDTO;
import com.company.entity.AttachEntity;
import com.company.entity.ProfileEntity;
import com.company.exp.EmailAlreadyExistsException;
import com.company.exp.ItemNotFoundException;
import com.company.repository.ProfileRepository;
import com.company.validation.ProfileValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AttachService attachService;
    public ProfileDTO create(ProfileDTO dto){
        ProfileValidation.isValid(dto);
        ProfileEntity profileEntity = profileRepository.findByEmail(dto.getEmail());
        if (profileEntity != null){
            if (profileEntity.getVisible()) {
                throw new EmailAlreadyExistsException("Email already exsists");
            } else {
                profileEntity.setVisible(true);
                profileRepository.updateVisible(true, dto.getId());
                return dto;
            }
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        profileRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public ProfileDTO update(ProfileDTO dto, Integer profileId){
        ProfileValidation.isValid(dto);
        ProfileEntity profileEntity = profileRepository.findByEmailAndPassword(dto.getEmail(), dto.getPassword());
        if (profileEntity == null || !profileEntity.getVisible()){
            throw new ItemNotFoundException("Email or Password wrong");
        }
        ProfileEntity entity = get(profileId);
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setUpdatedDate(LocalDateTime.now());
        profileRepository.save(entity);
        return toDTO(entity);
    }

    public String delete(Integer profileId){
        ProfileEntity entity = profileRepository.findByIdAndVisible(profileId, true).orElseThrow(() -> new ItemNotFoundException("Profile Not Found"));
        entity.setVisible(false);
        profileRepository.updateVisible(false, profileId);
        return "Success";
    }

    public ProfileDTO getById(Integer id){
        ProfileEntity entity = profileRepository.findByIdAndVisible(id, true).orElseThrow(() -> new  ItemNotFoundException("Profile Not Found"));
        return toDTO(entity);
    }

    public ProfileEntity get(Integer id){
        return profileRepository.findByIdAndVisible(id, true).orElseThrow(() -> new  ItemNotFoundException("Profile Not Found"));
    }

    public List<ProfileDTO> getPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<ProfileDTO> list = new ArrayList<>();
        profileRepository.findByVisible(true, pageable).forEach(profiles -> {
            list.add(toDTO(profiles));
        });
        if (list.isEmpty()){
            throw new ItemNotFoundException("Not found");
        }
        return list;
    }

    private ProfileDTO toDTO(ProfileEntity entity){
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public String updateImage(String attachId, Integer pId){
        ProfileEntity profile = get(pId);

        if (profile.getAttach() != null){
            attachService.delete(profile.getAttach().getId());
        }
        profileRepository.updateAttachId(attachId, pId);
        return "Success";
    }
}
