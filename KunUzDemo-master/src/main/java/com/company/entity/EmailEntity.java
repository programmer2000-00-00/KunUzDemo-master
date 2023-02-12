package com.company.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "email")
public class EmailEntity extends BaseEntity{
    @Column
    private String toEmail;
    @Column
    private String title;
    @Column(columnDefinition = "text")
    private String content;
}
