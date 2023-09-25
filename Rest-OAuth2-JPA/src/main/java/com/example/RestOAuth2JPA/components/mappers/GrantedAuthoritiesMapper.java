package com.example.RestOAuth2JPA.components.mappers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class GrantedAuthoritiesMapper {
    public static Map<String, Object> get_authorities_info(Authentication authentication) throws Exception {

        Map<String,Object> info = new HashMap<>();

        try {
            Collection<String> authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    
            info.put("role_authorities", authorities.stream().filter(auth -> auth.startsWith("ROLE_")).findFirst().get());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        //info.put("name", authentication.getPrincipal());

        return info;
    }
}
