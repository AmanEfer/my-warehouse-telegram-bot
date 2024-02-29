package com.amanefer.crud.mappers;

public interface MyMapper<U, D> {
    D toDto(U u);

    U toUser(D d);
}
