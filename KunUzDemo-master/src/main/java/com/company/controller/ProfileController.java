package com.company.controller;

import com.company.dto.AttachDTO;
import com.company.dto.ProfileDTO;
import com.company.enums.ProfileRole;
import com.company.service.ProfileService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;


    @PostMapping("/adm")
    public ResponseEntity<?> createProfile(@RequestBody ProfileDTO dto,
                                           HttpServletRequest request) {
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.create(dto));
    }

    @PutMapping("/adm/update/{profileId}")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileDTO dto, @PathVariable("profileId") Integer profileId,
                                           HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.update(dto, profileId));
    }

    @GetMapping("/adm/pagination")
    public ResponseEntity<?> getPaginationList(@RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "5") int size,
                                               HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.getPagination(page, size));
    }

    @GetMapping("/adm/{profileId}")
    public ResponseEntity<?> getById(@PathVariable("profileId") Integer profileId,
                                      HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.getById(profileId));
    }

    @DeleteMapping("/adm/{profileId}")
    public ResponseEntity<?> delete(@PathVariable("profileId") Integer profileId,
                                    HttpServletRequest request){
        JwtUtil.getIdFromHeader(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.delete(profileId));
    }

    @PostMapping("/image")
    public ResponseEntity<?> updateImage(@RequestBody AttachDTO image, HttpServletRequest request){

        Integer pId = JwtUtil.getIdFromHeader(request);
        try {
            return ResponseEntity.ok(profileService.updateImage(image.getId(), pId));
        } catch (DataIntegrityViolationException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Attach Not Found");
        }
    }
}
