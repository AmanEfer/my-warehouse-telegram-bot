package com.amanefer.telegram.services;

import com.amanefer.telegram.dto.ProductDto;
import com.amanefer.telegram.dto.StockDto;
import com.amanefer.telegram.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

    public static final String URI_GET_USERS = "http://localhost:8080/api/crud/users";
    public static final String URI_GET_USER = "http://localhost:8080/api/crud/users/%s";
    public static final String URI_REGISTER_NEW_USER = "http://localhost:8080/api/crud/users?selectedRole=%s";
    public static final String URI_FILES_EXPORT = "http://localhost:8082/api/filesExport";
    public static final String URI_SAVE_STOCK = "http://localhost:8080/api/crud/stocks";
    public static final String URI_GET_ALL_STOCKS = "http://localhost:8080/api/crud/stocks/all";
    public static final String URI_SAVE_PRODUCT = "http://localhost:8080/api/crud/products";
    public static final String URI_GET_ALL_PRODUCTS = "http://localhost:8080/api/crud/products/all";
    public static final String URI_MOVE_PRODUCTS =
            "http://localhost:8080/api/crud/products/move?invoiceNumber=%s&stockNameFrom=%s&stockNameTo=%s";

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

    public StockDto saveNewStock(StockDto stockDto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<StockDto> request = new HttpEntity<>(stockDto, headers);

        return restTemplate.postForEntity(URI_SAVE_STOCK, request, StockDto.class).getBody();
    }

    public List<StockDto> getAllStocks() {

        ResponseEntity<StockDto[]> response = restTemplate.getForEntity(URI_GET_ALL_STOCKS, StockDto[].class);

        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

    public ProductDto saveNewProduct(ProductDto productDto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductDto> request = new HttpEntity<>(productDto, headers);

        return restTemplate.postForEntity(URI_SAVE_PRODUCT, request, ProductDto.class).getBody();
    }

    public List<ProductDto> getAllProducts() {

        ResponseEntity<ProductDto[]> response = restTemplate.getForEntity(URI_GET_ALL_PRODUCTS, ProductDto[].class);

        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

    public List<ProductDto> moveProducts(String invoiceNumber, String stockNameFrom, String stockNameTo,
                                         List<ProductDto> products) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<ProductDto>> request = new HttpEntity<>(products, headers);

        String uri = String.format(URI_MOVE_PRODUCTS, invoiceNumber, stockNameFrom, stockNameTo);

        ResponseEntity<ProductDto[]> response = restTemplate.exchange(uri, HttpMethod.PATCH, request, ProductDto[].class);

        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

}
