package com.company.validation;

import com.company.dto.ArticleDTO;
import com.company.exp.AppBadRequestException;

public class ArticleValidation {
    public static void isValid(ArticleDTO dto) {
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty() || dto.getTitle().length() < 3){
            throw new AppBadRequestException("Title Not Valid");
        }
        if (dto.getContent() == null || dto.getContent().trim().isEmpty() || dto.getContent().length() < 3){
            throw new AppBadRequestException("Content Not Valid");
        }
        if (dto.getDescription() == null || dto.getDescription().trim().isEmpty() || dto.getDescription().length() < 3){
            throw new AppBadRequestException("Description Not Valid");
        }
        if (dto.getRegionId() == null) {
            throw new AppBadRequestException("Region Not Valid");
        }
        if (dto.getTypeId() == null) {
            throw new AppBadRequestException("Region Not Valid");
        }
        if (dto.getCategoryId() == null) {
            throw new AppBadRequestException("Region Not Valid");
        }
    }

    public static void isValid2(ArticleDTO dto){
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty() || dto.getTitle().length() < 3){
            throw new AppBadRequestException("Title Not Valid");
        }
        if (dto.getContent() == null || dto.getContent().trim().isEmpty() || dto.getContent().length() < 3){
            throw new AppBadRequestException("Content Not Valid");
        }
        if (dto.getDescription() == null || dto.getDescription().trim().isEmpty() || dto.getDescription().length() < 3){
            throw new AppBadRequestException("Description Not Valid");
        }
    }
}
