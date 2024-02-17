package com.amanefer.telegram.services;

import com.amanefer.telegram.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RestToCrud {

    public static final String URI = "http://localhost:8080/crud";
    private final RestTemplate restTemplate;

    @PostMapping()
    public UserDto registerNewUser(@RequestBody UserDto userDto, String selectedRole) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers);

        String uri = String.format("http://localhost:8080/crud/new?selectedRole=%s", selectedRole);

        ResponseEntity<UserDto> responseEntity = restTemplate.postForEntity(uri, requestEntity, UserDto.class);

        return responseEntity.getBody();
    }

    @GetMapping
    public List<UserDto> getUsers() {
        ResponseEntity<UserDto[]> response = restTemplate.getForEntity(URI, UserDto[].class);

        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

    @GetMapping
    public UserDto getUser(long id) {
        ResponseEntity<UserDto> response = restTemplate.getForEntity(String.format(URI + "/%s", id), UserDto.class);

        return response.getBody();
    }
}
