package ru.kata.spring.security.demo.mappers;

public interface MyMapper<M, D> {
    D toDto(M m);

    M toModel(D d);

}
