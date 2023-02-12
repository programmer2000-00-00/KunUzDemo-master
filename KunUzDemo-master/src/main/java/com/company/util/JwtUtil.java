package com.company.util;

import com.company.dto.ProfileJwtDTO;
import com.company.enums.ProfileRole;
import com.company.exp.AppForbiddenException;
import com.company.exp.TokenNotValidException;
import io.jsonwebtoken.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtil {

    private final static String secretKey = "kalitSo'z";

    public static String encode(Integer id){
        return doEncode(id, null, 60);
    }

    public static String encode(Integer id, ProfileRole role){
        return doEncode(id, role, 120);
    }
    public static String doEncode(Integer id, ProfileRole role, long minute){
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setSubject(String.valueOf(id))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setExpiration(new Date(System.currentTimeMillis() + (minute * 60 + 1000 )))
                .setIssuer("kun.uz");
                if (role != null){
                    jwtBuilder.claim("role", role);
                }

        return jwtBuilder.compact();
    }



    public static Integer decode(String jwt) {
        try {

            JwtParser jwtParser = Jwts.parser();

            jwtParser.setSigningKey(secretKey);
            Jws<Claims> jws = jwtParser.parseClaimsJws(jwt);

            Claims claims = jws.getBody();
            String id = claims.getSubject();

            return Integer.parseInt(id);
        } catch (SignatureException e){
            throw new TokenNotValidException("Token Not Valid");
        }
    }

    public static ProfileJwtDTO decodeJwt(String jwt) {
        try {

            JwtParser jwtParser = Jwts.parser();

            jwtParser.setSigningKey(secretKey);
            Jws<Claims> jws = jwtParser.parseClaimsJws(jwt);

            Claims claims = jws.getBody();
            String id = claims.getSubject();
            String role = String.valueOf(claims.get("role"));
            return new ProfileJwtDTO(Integer.parseInt(id), ProfileRole.valueOf(role));
        } catch (SignatureException e){
            throw new TokenNotValidException("Token Not Valid");
        }
    }


    public static Integer getIdFromHeader(HttpServletRequest request){
        try {
            return (Integer) request.getAttribute("pId");
        } catch (RuntimeException e){
            throw  new TokenNotValidException("Not Authorized");
        }
    }

    public static Integer getIdFromHeader(HttpServletRequest request, ProfileRole... requiredRoles) {
        try {
            ProfileJwtDTO dto = (ProfileJwtDTO) request.getAttribute("profileJwtDto");

            if (requiredRoles == null || requiredRoles.length == 0) {
                return dto.getPId();
            }
            for (ProfileRole role : requiredRoles) {
                if (role.equals(dto.getRole())) {
                    return dto.getPId();
                }
            }
        } catch (RuntimeException e) {

            throw new TokenNotValidException("Not Authorized");
        }
        throw new AppForbiddenException("Not Access");
    }

    public static ProfileJwtDTO getProfileFromHeader(HttpServletRequest request, ProfileRole... requiredRoles) {
        try {
            ProfileJwtDTO dto = (ProfileJwtDTO) request.getAttribute("profileJwtDto");

            if (requiredRoles == null || requiredRoles.length == 0) {
                return dto;
            }
            for (ProfileRole role : requiredRoles) {
                if (role.equals(dto.getRole())) {
                    return dto;
                }
            }
        } catch (RuntimeException e) {

            throw new TokenNotValidException("Not Authorized");
        }
        throw new AppForbiddenException("Not Access");
    }

}
