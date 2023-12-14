package ru.kata.spring.boot_security.demo.mappers;

public interface MyMapper<M, D> {
    D toDto(M m);

    M toModel(D d);

}
