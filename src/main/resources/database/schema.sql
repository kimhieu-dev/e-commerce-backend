create database ecommerce;

use ecommerce;

create table user
(
    id              varchar(36) primary key,
    username        varchar(32) unique         not null,
    password        varchar(255)               not null,
    email           varchar(255)               not null,
    phone_number    varchar(20)                not null,
    full_name       varchar(100)               not null,
    profile_picture varchar(512),
    gender          enum ('MALE','FEMALE')     not null,
    date_birth      date                       not null,
    status          enum ('ACTIVE','INACTIVE') not null default 'ACTIVE',
    is_verified     boolean                    not null default false,
    created_at      datetime                            default current_timestamp,
    updated_at      datetime                            default current_timestamp on update current_timestamp,
    created_by      varchar(32),
    updated_by      varchar(32),
    deleted         boolean
);

create table role
(
    id         varchar(36) primary key,
    name       enum ('USER','SHIPPER','ADMIN') default 'USER',
    created_at datetime default current_timestamp,
    updated_at datetime default current_timestamp on update current_timestamp,
    created_by varchar(32),
    updated_by varchar(32),
    deleted    boolean
);

create table user_role
(
    id         varchar(36) primary key,
    user_id    varchar(36) not null,
    role_id    varchar(36) not null,
    created_at datetime default current_timestamp,
    updated_at datetime default current_timestamp on update current_timestamp,
    created_by varchar(32),
    updated_by varchar(32),
    deleted    boolean,
    foreign key (user_id) references user (id),
    foreign key (role_id) references role (id)
);

create table cart
(
    id         varchar(36) primary key,
    user_id    varchar(36) not null unique,
    created_at datetime default current_timestamp,
    updated_at datetime default current_timestamp on update current_timestamp,
    created_by varchar(32),
    updated_by varchar(32),
    deleted    boolean,
    foreign key (user_id) references user (id)
);

create table cart_item
(
    id         varchar(36) primary key,
    cart_id    varchar(36) not null,
    product_id varchar(36) not null,
    quantity   integer     not null,
    created_at datetime default current_timestamp,
    updated_at datetime default current_timestamp on update current_timestamp,
    created_by varchar(32),
    updated_by varchar(32),
    deleted    boolean
);

create table product
(
    id            varchar(36) primary key,
    sku           varchar(255)               not null,
    name          varchar(255)               not null,
    base_price    decimal(19, 2)             not null,
    thumbnail_url varchar(255)               not null,
    status        enum ('ACTIVE','INACTIVE') not null default 'ACTIVE',
    created_at    datetime                            default current_timestamp,
    updated_at    datetime                            default current_timestamp on update current_timestamp,
    created_by    varchar(32),
    updated_by    varchar(32),
    deleted       boolean
);

create table inventory
(
    id                varchar(36) primary key,
    product_id        varchar(36) not null,
    quantity_in_stock long        not null,
    reserved_quantity long        not null,
    status            enum ('IN_STOCK','LIMITED_STOCK','OUT_OF_STOCK'),
    created_at        datetime default current_timestamp,
    updated_at        datetime default current_timestamp on update current_timestamp,
    created_by        varchar(32),
    updated_by        varchar(32),
    deleted           boolean
);
/* d*/
create table `order`
(
    `id`                 varchar(36)                                                                        not null,
    `tracking_number`    varchar(255)                                                                       not null,
    `user_id`            varchar(36)                                                                        not null,
    `payment_id`         varchar(36)                                                                        not null,
    `status`             ENUM ('PENDING','CONFIRMED','PICKING','SHIPPING','DELIVERED','FAILED','RETURNING') not null,
    `total_price`        decimal(19, 2)                                                                     not null,
    `shipping_fee`       decimal(19, 2)                                                                     not null,
    `discount_amount`    decimal(19, 2)                                                                     not null,
    `grand_total`        decimal(19, 2)                                                                     not null,
    `discount_id`        varchar(36)                                                                        not null,
    `estimated_delivery` date                                                                               not null,
    `carrier_id`         varchar(36)                                                                        not null,
    `address_id`         varchar(36)                                                                        not null,
    created_at           datetime default current_timestamp,
    updated_at           datetime default current_timestamp on update current_timestamp,
    created_by           varchar(32),
    updated_by           varchar(32),
    deleted              boolean
);

create table `order_item`
(
    `id`         varchar(36) primary key,
    `order_id`   varchar(36)    not null,
    `product_id` varchar(36)    not null,
    `quantity`   integer        not null,
    `price`      decimal(19, 2) not null,
    created_at   datetime default current_timestamp,
    updated_at   datetime default current_timestamp on update current_timestamp,
    created_by   varchar(32),
    updated_by   varchar(32),
    deleted      boolean
);

create table `address`
(
    `id`             varchar(36) primary key,
    `recipient_name` varchar(50)  not null,
    `phone_number`   varchar(20)  not null,
    `province`       varchar(50)  not null,
    `district`       varchar(50)  not null,
    `ward`           varchar(50)  not null,
    `detail_address` varchar(100) not null,
    created_at       datetime default current_timestamp,
    updated_at       datetime default current_timestamp on update current_timestamp,
    created_by       varchar(32),
    updated_by       varchar(32),
    deleted          boolean
);

create table `user_address`
(
    `id`         varchar(36) primary key,
    `user_id`    integer not null,
    `address_id` integer not null,
    created_at   datetime default current_timestamp,
    updated_at   datetime default current_timestamp on update current_timestamp,
    created_by   varchar(32),
    updated_by   varchar(32),
    deleted      boolean
);

create table `category`
(
    `id`          varchar(36) primary key,
    `name`        varchar(100) not null,
    `description` varchar(255) not null,
    created_at    datetime default current_timestamp,
    updated_at    datetime default current_timestamp on update current_timestamp,
    created_by    varchar(32),
    updated_by    varchar(32),
    deleted       boolean
);

create table `category_product`
(
    `id`          varchar(36) primary key,
    `product_id`  varchar(36) not null,
    `category_id` varchar(36) not null,
    created_at    datetime default current_timestamp,
    updated_at    datetime default current_timestamp on update current_timestamp,
    created_by    varchar(32),
    updated_by    varchar(32),
    deleted       boolean
);

create table `payment`
(
    `id`             varchar(36) primary key,
    `payment_method` varchar(100) not null,
    `is_active`      BOOLEAN      not null,
    created_at       datetime default current_timestamp,
    updated_at       datetime default current_timestamp on update current_timestamp,
    created_by       varchar(32),
    updated_by       varchar(32),
    deleted          boolean
);

create table `user_payment`
(
    `id`         varchar(36) primary key,
    `user_id`    varchar(36) not null,
    `payment_id` varchar(36) not null,
    created_at   datetime default current_timestamp,
    updated_at   datetime default current_timestamp on update current_timestamp,
    created_by   varchar(32),
    updated_by   varchar(32),
    deleted      boolean
);

create table `tracking_log`
(
    `id`          varchar(36) primary key,
    `order_id`    varchar(36)                                                                        not null,
    `from_status` ENUM ('PENDING','CONFIRMED','PICKING','SHIPPING','DELIVERED','FAILED','RETURNING') not null,
    `to_status`   ENUM ('PENDING','CONFIRMED','PICKING','SHIPPING','DELIVERED','FAILED','RETURNING') not null,
    `note`        TEXT(65535)                                                                        not null,
    `location`    varchar(255)                                                                       not null,
    created_at    datetime default current_timestamp,
    updated_at    datetime default current_timestamp on update current_timestamp,
    created_by    varchar(32),
    updated_by    varchar(32),
    deleted       boolean
);

create table `notification_queue`
(
    `id`              varchar(36) primary key,
    `order_id`        varchar(36)                                                                        not null,
    `recipient_email` varchar(100)                                                                       not null,
    `subject`         varchar(255)                                                                       not null,
    `content`         TEXT(65535)                                                                        not null,
    `status`          ENUM ('PENDING','CONFIRMED','PICKING','SHIPPING','DELIVERED','FAILED','RETURNING') not null,
    `sent_at`         datetime                                                                           not null,
    created_at        datetime default current_timestamp,
    updated_at        datetime default current_timestamp on update current_timestamp,
    created_by        varchar(32),
    updated_by        varchar(32),
    deleted           boolean
);

create table `product_detail`
(
    `id`          varchar(36) primary key,
    `product_id`  varchar(36)    not null,
    `description` TEXT(3000)     not null,
    `weight`      DECIMAL(10, 2) not null,
    `length`      DECIMAL(10, 2) not null,
    `width`       DECIMAL(10, 2) not null,
    `height`      DECIMAL(10, 2) not null,
    created_at    datetime default current_timestamp,
    updated_at    datetime default current_timestamp on update current_timestamp,
    created_by    varchar(32),
    updated_by    varchar(32),
    deleted       boolean
);

create table `discount`
(
    `id`          varchar(36) primary key,
    `code`        varchar(255)                       not null,
    `type`        ENUM ('FIXED_AMOUNT','PERCENTAGE') not null,
    `value`       decimal(19, 4)                     not null,
    `start_date`  date                               not null,
    `end_date`    date                               not null,
    `usage_limit` integer                            not null,
    `used_count`  integer                            not null DEFAULT 0,
    created_at    datetime                                    default current_timestamp,
    updated_at    datetime                                    default current_timestamp on update current_timestamp,
    created_by    varchar(32),
    updated_by    varchar(32),
    deleted       boolean
);

create table `carrier`
(
    `id`             varchar(36) primary key,
    `name`           varchar(50)    not null,
    `base_price`     decimal(19, 2) not null,
    `estimated_days` integer        not null,
    created_at       datetime default current_timestamp,
    updated_at       datetime default current_timestamp on update current_timestamp,
    created_by       varchar(32),
    updated_by       varchar(32),
    deleted          boolean
);
