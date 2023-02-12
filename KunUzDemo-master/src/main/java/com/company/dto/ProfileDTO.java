package com.company.dto;

import com.company.enums.ProfileRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private Boolean visible;
    private ProfileRole role;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private String imageId;
    private AttachDTO image;
    private String jwt;
}
