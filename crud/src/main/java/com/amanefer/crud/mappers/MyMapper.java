package com.amanefer.crud.mappers;

public interface MyMapper<M, D> {
    D toDto(M m);

    M toModel(D d);

}
