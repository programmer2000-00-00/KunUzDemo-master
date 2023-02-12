package com.company.validation;

import com.company.dto.ProfileDTO;
import com.company.dto.RegistrationDTO;
import com.company.exp.AppBadRequestException;

public class RegistrationValidation {
    public static void isValid (RegistrationDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty() || dto.getName().trim().length() < 3) {
            throw new AppBadRequestException("Name Not Valid");
        }
        if (dto.getSurname() == null || dto.getSurname().trim().isEmpty() || dto.getSurname().trim().length() < 3) {
            throw new AppBadRequestException("Surname Not Valid");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty() || dto.getEmail().trim().length() < 3 || !dto.getEmail().contains("@")) {
            throw new AppBadRequestException("Email Not Valid");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty() || dto.getPassword().trim().length() < 8) {
            throw new AppBadRequestException("Password Not Valid");
        }
    }
}
