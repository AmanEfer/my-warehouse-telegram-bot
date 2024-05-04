create table if not exists my_db_bot.role
(
    id bigserial constraint role_pkey primary key,
    name varchar(225) not null
);

create table if not exists my_db_bot.my_user
(
    id bigserial constraint my_user_pkey primary key,
    username varchar(20) not null,
    constraint my_user_check_username_length check (length(username) >= 2 and length(username) <= 20)
);

create table if not exists my_db_bot.my_user_role
(
    user_id bigint references my_db_bot.my_user (id),
    role_id bigint references my_db_bot.role (id),
    constraint user_role_pkey primary key (user_id, role_id)
);

create table if not exists my_db_bot.product
(
    article varchar(6)  not null constraint product_id_pkey primary key,
    title varchar(128) not null constraint product_title unique,
    purchase_last_price numeric,
    sale_last_price numeric,
    created_at timestamp,
    updated_at timestamp,
    deleted_at timestamp
);

create table if not exists my_db_bot.stock
(
    id bigserial constraint stock_id_pkey primary key,
    stock_name varchar(25) not null constraint stock_name_uq unique,
    created_at timestamp,
    updated_at timestamp,
    deleted_at timestamp
);

create table if not exists my_db_bot.product_quantity
(
    id bigserial constraint product_quantity_id_pkey primary key,
    product_id varchar(6) not null references my_db_bot.product (article) on delete cascade,
    stock_id bigint not null references my_db_bot.stock (id),
    quantity int default 0,
    constraint product_quantity_check_quantity_value check (quantity >= 0),
    constraint product_quantity_uk unique (product_id, stock_id)
);
