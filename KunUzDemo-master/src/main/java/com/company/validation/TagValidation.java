package com.company.validation;

import com.company.dto.TagDTO;
import com.company.exp.AppBadRequestException;

public class TagValidation {
    public static void isValid(TagDTO dto){
        if (dto.getNameEn() == null || dto.getNameEn().trim().length() < 2) {
            throw new AppBadRequestException("NameEn not valid");
        }
        if (dto.getNameRu() == null || dto.getNameRu().trim().length() < 2) {
            throw new AppBadRequestException("NameRu not valid");
        }
        if (dto.getNameUz() == null || dto.getNameUz().trim().length() < 2) {
            throw new AppBadRequestException("NameUz not valid");
        }
        if (dto.getKey() == null || dto.getKey().trim().length() < 2) {
            throw new AppBadRequestException("Key not valid");
        }
    }
}
