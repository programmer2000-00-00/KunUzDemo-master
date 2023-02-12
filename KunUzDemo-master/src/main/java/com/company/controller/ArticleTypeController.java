package com.company.controller;

import com.company.dto.ArticleTypeDTO;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.service.ArticleTypeService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/article_type")
public class ArticleTypeController {
    @Autowired
    private ArticleTypeService articelTypeService;

    @PostMapping("/adm")
    public ResponseEntity<?> create(@RequestBody ArticleTypeDTO dto, HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articelTypeService.create(dto, pId));
    }

    @PutMapping("/adm/update/{id}")
    public ResponseEntity<?> update(@RequestBody ArticleTypeDTO dto, @PathVariable("id") Integer aId, HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articelTypeService.update(dto,aId ));
    }

    @GetMapping("/adm")
    public ResponseEntity<?> list(HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articelTypeService.getList());
    }

    @GetMapping("/adm/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer aId, HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articelTypeService.getById(aId));
    }

    @GetMapping("/public/list/{lang}")
    public ResponseEntity<?> list(@PathVariable("lang") LangEnum lang){
        return ResponseEntity.ok(articelTypeService.list(lang));
    }

    @DeleteMapping("/adm/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer rId, HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articelTypeService.delete(rId));
    }
}
