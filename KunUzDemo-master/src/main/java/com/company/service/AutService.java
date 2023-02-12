package com.company.service;

import com.company.dto.AttachDTO;
import com.company.dto.AuthDTO;
import com.company.dto.ProfileDTO;
import com.company.dto.RegistrationDTO;
import com.company.entity.AttachEntity;
import com.company.entity.EmailEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exp.AppBadRequestException;
import com.company.exp.AppForbiddenException;
import com.company.exp.EmailAlreadyExistsException;
import com.company.exp.PasswordOrEmailWrongException;
import com.company.repository.EmailRepository;
import com.company.repository.ProfileRepository;
import com.company.util.JwtUtil;
import com.company.validation.RegistrationValidation;
import io.jsonwebtoken.JwtException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AutService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private AttachService attachService;
    public ProfileDTO login(AuthDTO dto){
        String pswd = DigestUtils.md5Hex(dto.getPassword());
        ProfileEntity profileEntity = profileRepository.findByEmailAndPassword(dto.getEmail(), pswd);
        if (profileEntity == null){
            throw new PasswordOrEmailWrongException("Password or Email wrong!");
        }
        if (!profileEntity.getStatus().equals(ProfileStatus.ACTIVE)){
            throw new AppForbiddenException("No Access ");
        }
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(profileEntity.getId());
        profileDTO.setEmail(profileEntity.getEmail());
        profileDTO.setPassword(profileEntity.getPassword());
        profileDTO.setCreatedDate(profileEntity.getCreatedDate());
        profileDTO.setJwt(JwtUtil.encode(profileEntity.getId(), profileEntity.getRole()));
        AttachEntity attach = profileEntity.getAttach();
        if (attach != null){
           AttachDTO imageDTO = new AttachDTO();
           imageDTO.setUrl(attachService.toOpenURL(attach.getId()));
           profileDTO.setImage(imageDTO);
        }
        return profileDTO;
    }

    public void registration(RegistrationDTO dto){
        RegistrationValidation.isValid(dto);
        ProfileEntity profileEntity = profileRepository.findByEmail(dto.getEmail());

        if (profileEntity != null){
            if (!profileEntity.getStatus().equals(ProfileStatus.NOT_ACTIVE)){
                throw new EmailAlreadyExistsException("Email Already Exists");
            } else {
                profileRepository.delete(profileEntity);
            }
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        String pswd = DigestUtils.md5Hex(dto.getPassword());
        entity.setPassword(pswd);
        entity.setRole(ProfileRole.USER);
        entity.setStatus(ProfileStatus.NOT_ACTIVE);
        profileRepository.save(entity);

        Thread thread = new Thread(){
            @Override
            public void run() {
                sendVerificationEmail(entity);
            }
        };
        thread.start();


    }

    private void sendVerificationEmail(ProfileEntity entity) {
        StringBuilder builder = new StringBuilder();
        String jwt = JwtUtil.encode(entity.getId());
        builder.append("Salom bormsin \n");
        builder.append("To verify your registration click to next link.");
        builder.append("http://localhost:8080/auth/verification/").append(jwt);
        builder.append("\nMazgi!");
        String title = "Activate Your Registration";
        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setToEmail(entity.getEmail());
        emailEntity.setTitle(title);
        emailEntity.setContent(builder.toString());
        emailRepository.save(emailEntity);
        emailService.send(entity.getEmail(), title, builder.toString());
    }

    public void verification(String jwt){
        Integer userId = null;
        try {
            userId = JwtUtil.decode(jwt);
        } catch (JwtException e) {
            throw new AppBadRequestException("Verification not completed");
        }
        profileRepository.updateStatus(ProfileStatus.ACTIVE, userId);

    }
}
