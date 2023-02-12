package com.company.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmailDTO {
    private Integer id;
    private String toEmail;
    private String title;
    private String content;
    private LocalDateTime createdDate;
}
