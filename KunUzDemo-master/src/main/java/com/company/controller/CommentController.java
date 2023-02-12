package com.company.controller;

import com.company.dto.CommentDTO;
import com.company.dto.ProfileJwtDTO;
import com.company.enums.ProfileRole;
import com.company.service.CommentService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("")
    public ResponseEntity<?> create (@RequestBody CommentDTO dto, HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(commentService.create(dto, pId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer commentId,
                                    @RequestBody CommentDTO dto,
                                    HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(commentService.update(commentId, dto, pId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer commentId, HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfileFromHeader(request);
        return ResponseEntity.ok(commentService.delete(commentId, jwtDTO.getRole(), jwtDTO.getPId()));
    }

    @GetMapping("/article/{id}")
    public ResponseEntity<?> findByArticleId(@PathVariable("id") Integer articleId,
                                             @RequestParam(value = "page") int page,
                                             @RequestParam(value = "size") int size,
                                             HttpServletRequest request){
        return ResponseEntity.ok(commentService.listByArticleId(articleId, page, size));
    }

    @GetMapping("/adm/profile/{id}")
    public ResponseEntity<?> findByProfileId(@PathVariable("id") Integer profileId,
                                             @RequestParam(value = "page") int page,
                                             @RequestParam(value = "size") int size,
                                             HttpServletRequest request){
        return ResponseEntity.ok(commentService.listByProfileId(profileId, page, size));
    }

    @GetMapping("/adm/pagination")
    public ResponseEntity<?> findAll(@RequestParam(value = "page") int page,
                                     @RequestParam(value = "size") int size,
                                     HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(commentService.list(page, size));
    }
}
