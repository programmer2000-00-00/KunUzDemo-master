package com.company.controller;

import com.company.dto.LikeDTO;
import com.company.dto.ProfileJwtDTO;
import com.company.enums.ProfileRole;
import com.company.service.LikeService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PostMapping("")
    public ResponseEntity<?> create (@RequestBody LikeDTO dto, HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(likeService.create(dto, pId));
    }

   /* @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer commentId,
                                    @RequestBody LikeDTO dto,
                                    HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(likeService.update(commentId, dto, pId));
    }*/

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer likeId, HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfileFromHeader(request);
        return ResponseEntity.ok(likeService.delete(likeId, jwtDTO.getRole(), jwtDTO.getPId()));
    }

    @GetMapping("/article/{id}")
    public ResponseEntity<?> findByArticleId(@PathVariable("id") Integer articleId,
                                             @RequestParam(value = "page") int page,
                                             @RequestParam(value = "size") int size,
                                             HttpServletRequest request){
        return ResponseEntity.ok(likeService.listByArticleId(articleId, page, size));
    }

    @GetMapping("/adm/profile/{id}")
    public ResponseEntity<?> findByProfileId(@PathVariable("id") Integer profileId,
                                             @RequestParam(value = "page") int page,
                                             @RequestParam(value = "size") int size,
                                             HttpServletRequest request){
        return ResponseEntity.ok(likeService.listByProfileId(profileId, page, size));
    }

    @GetMapping("/adm/pagination")
    public ResponseEntity<?> findAll(@RequestParam(value = "page") int page,
                                     @RequestParam(value = "size") int size,
                                     HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(likeService.list(page, size));
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getByArticleId(@PathVariable("id") Integer articleId, HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request);
        return ResponseEntity.ok(likeService.getByArticleId(articleId, pId));
    }
}
