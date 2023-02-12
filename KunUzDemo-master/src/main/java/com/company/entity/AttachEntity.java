package com.company.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "attach")
public class AttachEntity{
    @Id
    private String id;
    @Column
    private String path;
    @Column
    private String extension;
    @Column(name = "origen_name")
    private String origenName;
    @Column
    private Long size;
    @Column
    private LocalDateTime createdDate = LocalDateTime.now();

}
