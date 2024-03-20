package com.amanefer.telegram.services;

import com.amanefer.telegram.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RestToCrud {

    public static final String URI_GET_USERS = "http://localhost:8080/crud";
    public static final String URI_GET_USER = "http://localhost:8080/crud/%s";
    public static final String URI_REGISTER_NEW_USER = "http://localhost:8080/crud/new?selectedRole=%s";
    public static final String URI_FILES_EXPORT = "http://localhost:8082/filesExport";

    private final RestTemplate restTemplate;


    public UserDto registerNewUser(UserDto userDto, String selectedRole) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers);

        String uri = String.format(URI_REGISTER_NEW_USER, selectedRole);

        return restTemplate.postForEntity(uri, requestEntity, UserDto.class).getBody();
    }

    public List<UserDto> getUsers() {

        ResponseEntity<UserDto[]> response = restTemplate.getForEntity(URI_GET_USERS, UserDto[].class);

        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

    public UserDto getUser(long id) {

        return restTemplate.getForEntity(String.format(URI_GET_USER, id), UserDto.class).getBody();
    }

    public byte[] exportFile() {

        return restTemplate.getForEntity(URI_FILES_EXPORT, byte[].class).getBody();
    }

}
