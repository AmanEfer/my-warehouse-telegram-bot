package com.amanefer.crud.mappers;

public interface MyMapper<M, D> {
    D toDto(M m);

    M toUser(D d);

}
