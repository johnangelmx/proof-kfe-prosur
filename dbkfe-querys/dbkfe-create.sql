create database dbkfe;
use dbkfe;
create table if not exists productos
(
    id             bigint auto_increment
        primary key,
    cantidad_stock int          not null,
    descripcion    varchar(255) null,
    nombre         varchar(255) null,
    precio         double       not null
);

create table if not exists usuarios
(
    id         bigint auto_increment
        primary key,
    contrasena varchar(255) null,
    email      varchar(255) null,
    nombre     varchar(255) null,
    rol        varchar(255) null
);

create table if not exists ventas
(
    id          bigint auto_increment
        primary key,
    cantidad    int            not null,
    fecha       date           null,
    total       decimal(19, 2) null,
    id_producto bigint         null,
    id_usuario  bigint         null,
    constraint fk_ventas_producto
        foreign key (id_producto) references productos (id),
    constraint fk_ventas_usuario
        foreign key (id_usuario) references usuarios (id)
);

