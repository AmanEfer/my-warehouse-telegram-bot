package com.amanefer.crud.mappers;

public interface BaseMapper<U, D> {
    D toDto(U u);

    U toEntity(D d);
}
