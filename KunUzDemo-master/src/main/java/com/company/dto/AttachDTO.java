package com.company.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class AttachDTO {
    private String id;
    private String url;
    private String origenName;
    private String path;
    private Long size;
    private LocalDateTime createdDate;

    public AttachDTO() {
    }

    public AttachDTO(String url) {
        this.url = url;
    }
}
