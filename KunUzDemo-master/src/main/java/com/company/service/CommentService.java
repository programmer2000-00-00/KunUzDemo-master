package com.company.service;


import com.company.dto.CommentDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.CommentEntity;
import com.company.enums.ProfileRole;
import com.company.exp.AppForbiddenException;
import com.company.exp.ItemNotFoundException;
import com.company.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleService articleService;

    public CommentDTO create(CommentDTO dto, Integer pId){
        ArticleEntity articleEntity = articleService.get(dto.getArticleId());

        CommentEntity entity = new CommentEntity();
        entity.setContent(dto.getContent());
        entity.setArticleId(dto.getArticleId());
        entity.setProfileId(pId);
        commentRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public String update(Integer commentId, CommentDTO dto, Integer pId){
        CommentEntity commentEntity = get(commentId);
        if (!commentEntity.getProfileId().equals(pId)){
            throw new AppForbiddenException("Not Access");
        }
        commentEntity.setContent(dto.getContent());
        commentEntity.setUpdatedDate(LocalDateTime.now());
        commentRepository.save(commentEntity);
        return "Success";
    }

    public String delete(Integer commentId, ProfileRole role, Integer pId){
        CommentEntity entity = get(commentId);
        if (entity.getProfileId().equals(pId) || role.equals(ProfileRole.ADMIN)){
            commentRepository.delete(entity);
            return "Success";
        }
        throw new AppForbiddenException("Not Access");
    }

    public PageImpl<CommentDTO> listByArticleId(Integer articleId, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        List<CommentDTO> dtoList = new ArrayList<>();
        Page<CommentEntity > pageList = commentRepository.findByArticleId(articleId, pageable);
        for (CommentEntity commentEntity : pageList){
            dtoList.add(toDTO(commentEntity));
        }
        return new PageImpl<CommentDTO>(dtoList, pageable, pageList.getTotalElements());
    }

    public PageImpl<CommentDTO> listByProfileId(Integer profileId, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        List<CommentDTO> dtoList = new ArrayList<>();
        Page<CommentEntity > pageList = commentRepository.findByProfileId(profileId, pageable);
        for (CommentEntity commentEntity : pageList){
            dtoList.add(toDTO(commentEntity));
        }
        return new PageImpl<CommentDTO>(dtoList, pageable, pageList.getTotalElements());
    }

    public PageImpl<CommentDTO> list( int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        List<CommentDTO> dtoList = new ArrayList<>();
        Page<CommentEntity > pageList = commentRepository.findAll(pageable);
        for (CommentEntity commentEntity : pageList){
            dtoList.add(toDTO(commentEntity));
        }
        return new PageImpl<CommentDTO>(dtoList, pageable, pageList.getTotalElements());
    }

    public CommentEntity get(Integer commentId){
        return commentRepository.findById(commentId).orElseThrow(() -> new ItemNotFoundException("Comment Not Found"));
    }

    private CommentDTO toDTO(CommentEntity entity){
        CommentDTO dto = new CommentDTO();
        dto.setContent(entity.getContent());
        dto.setProfileId(entity.getProfileId());
        dto.setArticleId(entity.getArticleId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
}
