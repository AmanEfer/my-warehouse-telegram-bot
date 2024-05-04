package com.amanefer.crud.mappers;

public interface BaseMapper<D, M, E> {

    M fromDtoToModel(D d);

    E fromModelToEntity(M m);

    M fromEntityToModel(E e);

    D fromModelToDto(M m);

    D fromEntityToDto(E e);

    E fromDtoToEntity(D d);

}
