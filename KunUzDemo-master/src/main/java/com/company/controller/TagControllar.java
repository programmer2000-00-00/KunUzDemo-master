package com.company.controller;

import com.company.dto.TagDTO;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.service.TagService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tag")
public class TagControllar {
    @Autowired
    private TagService tagService;

    @PostMapping("/adm")
    public ResponseEntity<?> create(@RequestBody TagDTO dto, HttpServletRequest request){
        Integer pId = JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(tagService.create(dto, pId));
    }

    @PutMapping("/adm/update/{id}")
    public ResponseEntity<?> update(@RequestBody TagDTO dto,@PathVariable("id") Integer rId, HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(tagService.update(dto,rId ));
    }

    @GetMapping("/adm")
    public ResponseEntity<?> list(HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(tagService.getList());
    }

    @GetMapping("/adm/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer rId, HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(tagService.getById(rId));
    }

    @GetMapping("/public/list/{lang}")
    public ResponseEntity<?> list(@PathVariable("lang")LangEnum lang){
        return ResponseEntity.ok(tagService.list(lang));
    }

    @DeleteMapping("/adm/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer rId, HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(tagService.delete(rId));
    }
}
