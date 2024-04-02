package com.amanefer.crud.mappers;

public interface BaseMapper<E, D> {
    D toDto(E e);

    E toEntity(D d);
}
