package com.company.service;

import com.company.dto.EmailDTO;
import com.company.entity.EmailEntity;
import com.company.exp.ItemNotFoundException;
import com.company.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private EmailRepository emailRepository;
    public void send(String  toEmail, String title, String content){
        SimpleMailMessage simple = new SimpleMailMessage();
        simple.setTo(toEmail);
        simple.setSubject(title);
        simple.setText(content);
        javaMailSender.send(simple);
    }

    public List<EmailDTO> getPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC ,"createdDate"));
        List<EmailDTO> dtoList = new ArrayList<>();
        emailRepository.findAll(pageable).stream().forEach(emailEntity -> {
            dtoList.add(toDTO(emailEntity));
        });
        return dtoList;
    }

    public String delete(Integer id){
        EmailEntity entity = get(id);
        emailRepository.delete(entity);
        return "Success";
    }

    public EmailEntity get(Integer id){
        return emailRepository.findById(id).orElseThrow(() -> new  ItemNotFoundException("Email History Not Found"));
    }

    private EmailDTO toDTO(EmailEntity entity){
        EmailDTO dto = new EmailDTO();
        dto.setToEmail(entity.getToEmail());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
}
