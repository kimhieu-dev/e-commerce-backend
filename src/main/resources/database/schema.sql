create database if not exists ecommerce;

use ecommerce;

create table if not exists users 
( 
    id              varchar(36)                                           not null 
        primary key, 
    username        varchar(32)                                           not null, 
    password        varchar(255)                                          not null, 
    email           varchar(255)                                          not null, 
    phone_number    varchar(20)                                           not null, 
    full_name       varchar(100)                                          not null, 
    profile_picture varchar(512)                                          null, 
    gender          enum ('MALE', 'FEMALE')                               not null, 
    date_birth      date                                                  not null, 
    status          enum ('ACTIVE', 'INACTIVE') default 'ACTIVE'          not null, 
    is_verified     tinyint(1)                  default 0                 not null, 
    created_at      datetime                    default CURRENT_TIMESTAMP null, 
    updated_at      datetime                    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP, 
    created_by      varchar(32)                                           null, 
    updated_by      varchar(32)                                           null, 
    deleted         tinyint(1)                                            null, 
    constraint username 
        unique (username) 
); 

INSERT INTO `users`
VALUES ('6066f8df-3f9e-4779-9ea7-3e9c68d2d392', 'sieunhan11',
        '$2a$10$UnvSR0UQQMOAYUC27VBjyuFiaw9v8Bu68FoBUyDEasSOTjpQanW6K', 'sieunhan11@example.com', '0867845343',
        'Sieu nhan', NULL, 'MALE', '2003-10-01', 'ACTIVE', 0, '2026-05-02 12:48:05', '2026-05-02 12:48:05', 'SYSTEM',
        'SYSTEM', 0),
       ('fd1ad804-4bfd-4ed7-98d0-fd65b2403ccd', 'admin1234',
        '$2a$10$UhaSRzi5QT3gnXR1uD.OzOBMnOpKdNRJzxiVUW2r6Ty0KFVJYoh6G', 'admin@example.com', '1234567890', 'Admin Hieu',
        NULL, 'MALE', '2003-07-10', 'ACTIVE', 0, '2026-05-01 20:36:06', '2026-05-01 20:36:06', 'SYSTEM', 'SYSTEM', 0);

create table if not exists roles 
( 
    id         varchar(36)                                                 not null 
        primary key, 
    name       enum ('USER', 'SHIPPER', 'ADMIN') default 'USER'            null, 
    created_at datetime                          default CURRENT_TIMESTAMP null, 
    updated_at datetime                          default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP, 
    created_by varchar(32)                                                 null, 
    updated_by varchar(32)                                                 null, 
    deleted    tinyint(1)                                                  null 
); 

INSERT INTO `roles`
VALUES ('77ca1640-4276-468f-9b67-be81360e306a', 'SHIPPER', '2026-05-01 00:45:40', '2026-05-01 00:45:40', NULL, NULL,
        NULL),
       ('ac39b7cc-00c3-4bd2-8306-7b318dae1dfe', 'USER', '2026-05-01 00:45:40', '2026-05-01 00:45:40', NULL, NULL, NULL),
       ('d39f130a-7240-4546-a4a0-49c372873e13', 'ADMIN', '2026-05-01 00:45:40', '2026-05-01 00:45:40', NULL, NULL,
        NULL);

create table if not exists users_roles 
( 
    id         varchar(36)                        not null 
        primary key, 
    user_id    varchar(36)                        not null, 
    role_id    varchar(36)                        not null, 
    created_at datetime default CURRENT_TIMESTAMP null, 
    updated_at datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP, 
    created_by varchar(32)                        null, 
    updated_by varchar(32)                        null, 
    deleted    tinyint(1)                         null, 
    constraint users_roles_ibfk_1 
        foreign key (user_id) references users (id), 
    constraint users_roles_ibfk_2 
        foreign key (role_id) references roles (id) 
); 

INSERT INTO `users_roles`
VALUES ('6ae0ab45-5d61-448b-bd34-144c1c6740f5', '6066f8df-3f9e-4779-9ea7-3e9c68d2d392',
        'ac39b7cc-00c3-4bd2-8306-7b318dae1dfe', '2026-05-02 12:48:05', '2026-05-02 12:48:05', 'SYSTEM', 'SYSTEM', 0),
       ('6cbb4117-f12f-4b64-8636-2b7dfadf3b0e', 'fd1ad804-4bfd-4ed7-98d0-fd65b2403ccd',
        'd39f130a-7240-4546-a4a0-49c372873e13', '2026-05-01 20:36:06', '2026-05-01 20:36:06', 'SYSTEM', 'SYSTEM', 0);

create table if not exists categories 
( 
    id          varchar(36)                          not null 
        primary key, 
    parent_id   varchar(36)                          null, 
    name        varchar(100)                         not null, 
    slug        varchar(100)                         not null, 
    description varchar(255)                         not null, 
    created_at  datetime   default CURRENT_TIMESTAMP null, 
    updated_at  datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP, 
    created_by  varchar(32)                          null, 
    updated_by  varchar(32)                          null, 
    deleted     tinyint(1) default 0                 null, 
    constraint slug 
        unique (slug), 
    constraint categories_ibfk_1 
        foreign key (parent_id) references categories (id) 
); 

INSERT INTO `categories` (id, parent_id, name, slug, description, created_by, updated_by)
VALUES ('cat-001', NULL, 'Điện thoại', 'dien-thoai', 'Các loại điện thoại thông minh', 'SYSTEM', 'SYSTEM'),
       ('cat-002', 'cat-001', 'iPhone', 'iphone', 'Điện thoại Apple iPhone', 'SYSTEM', 'SYSTEM'),
       ('cat-003', 'cat-001', 'Samsung', 'samsung', 'Điện thoại Samsung Galaxy', 'SYSTEM', 'SYSTEM'),
       ('cat-004', NULL, 'Máy tính', 'may-tinh', 'Các loại máy tính xách tay và để bàn', 'SYSTEM', 'SYSTEM'),
       ('cat-005', 'cat-004', 'Laptop', 'laptop', 'Máy tính xách tay', 'SYSTEM', 'SYSTEM');

create table if not exists products 
( 
    id            varchar(36)                                           not null 
        primary key, 
    category_id   varchar(36)                                           null, 
    sku           varchar(255)                                          not null, 
    name          varchar(255)                                          not null, 
    base_price    decimal(19, 2)                                        not null, 
    thumbnail_url varchar(255)                                          not null, 
    status        enum ('ACTIVE', 'INACTIVE') default 'ACTIVE'          not null, 
    created_at    datetime                    default CURRENT_TIMESTAMP null, 
    updated_at    datetime                    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP, 
    created_by    varchar(32)                                           null, 
    updated_by    varchar(32)                                           null, 
    deleted       tinyint(1)                  default 0                 null, 
    constraint products_ibfk_1 
        foreign key (category_id) references categories (id) 
); 

INSERT INTO `products`
VALUES ('01a8fbde-c054-4e45-89c6-111034e322bc', 'cat-004', 'ABCSSS111', 'Vua ko ngai', 2223.88, '', 'ACTIVE',
        '2026-05-04 17:31:45', '2026-05-04 17:31:45', 'sieunhan11', 'sieunhan11', 0),
       ('39268869-8331-477a-bab6-acb51578dc35', 'cat-001', 'DKdsD1', 'Mu coi', 9223.88, '', 'ACTIVE', '2026-05-04 17:31:17',
        '2026-05-04 17:31:17', 'sieunhan11', 'sieunhan11', 0),
       ('525195c9-49b9-415b-9f88-ef24cc67e496', 'cat-001', 'SKU-010', 'King von', 99.99, '', 'ACTIVE', '2026-05-03 00:14:36',
        '2026-05-03 00:14:36', 'sieunhan11', 'sieunhan11', 0),
       ('55ed6816-5b0d-49aa-a6ec-7ce100fd46b2', 'cat-001', 'DHKK174', 'Dep tong', 94.88, '', 'ACTIVE', '2026-05-04 17:30:28',
        '2026-05-04 17:30:28', 'sieunhan11', 'sieunhan11', 0),
       ('6829f4e1-c031-4322-aac4-42a2d9519c5a', 'cat-001', 'SKU-0111', 'Test Product', 99.99, '', 'ACTIVE', '2026-05-03 01:12:57',
        '2026-05-03 01:12:57', 'sieunhan11', 'sieunhan11', 0),
       ('6e3de641-b8a1-4fc0-8a8d-4114fe995ab1', 'cat-004', 'DHKK1244', 'Tu lanh khi', 84.88, '', 'ACTIVE', '2026-05-04 17:29:26',
        '2026-05-04 17:29:26', 'sieunhan11', 'sieunhan11', 0),
       ('6ed68775-c740-44ca-86d4-2efdee61e57c', 'cat-001', 'SKU-0101', 'Test Product', 99.99, '', 'ACTIVE', '2026-05-03 01:12:44',
        '2026-05-03 01:12:44', 'sieunhan11', 'sieunhan11', 0),
       ('76ca6748-1e41-4887-b1f9-726e480cb321', 'cat-001', 'ABC123', 'ABC', 130000000.23, 'url/example', 'ACTIVE',
        '2026-05-02 14:13:25', '2026-05-02 14:13:25', 'SYSTEM', 'SYSTEM', 0),
       ('8be8b38a-149e-49e5-b6e5-0a628973c04c', 'cat-004', 'DHKK1234', 'Dieu hoa khong khi', 88.88, '', 'ACTIVE',
        '2026-05-04 16:04:02', '2026-05-04 16:04:02', 'sieunhan11', 'sieunhan11', 0),
       ('a01b5a18-d63c-485c-8a5e-73dbdd960d12', 'cat-001', 'Prod123', 'Test Product', 99.99, '', 'ACTIVE', '2026-05-03 01:29:19',
        '2026-05-03 01:29:19', 'sieunhan11', 'sieunhan11', 0),
       ('ac66eacd-d486-408e-a293-55e84e546f99', 'cat-001', 'SKU-0211', 'Test Product', 99.99, '', 'ACTIVE', '2026-05-03 01:13:12',
        '2026-05-03 01:13:12', 'sieunhan11', 'sieunhan11', 0),
       ('b36ef438-0c96-4ecb-8bb6-a1f9395101bd', 'cat-001', 'SKU-001', 'Test Product', 99.99, '', 'ACTIVE', '2026-05-03 00:03:45',
        '2026-05-03 00:11:46', 'sieunhan11', 'sieunhan11', 0),
       ('bbb7353b-a9d7-48a5-8c5e-1488c812e2e7', 'cat-001', 'DKDSA11', 'Mu coi', 922.88, '', 'ACTIVE', '2026-05-04 17:30:54',
        '2026-05-04 17:30:54', 'sieunhan11', 'sieunhan11', 0),
       ('fcac3677-a0f7-4ed0-80f7-d39030042904', 'cat-001', 'DHKK1224', 'Giay dep', 94.88, '', 'ACTIVE', '2026-05-04 17:30:14',
        '2026-05-04 17:30:14', 'sieunhan11', 'sieunhan11', 0);

create table if not exists product_details 
 ( 
     id          varchar(36)                          not null 
         primary key, 
     product_id  varchar(36)                          not null, 
     description text                                 not null, 
     weight      decimal(10, 2)                       not null, 
     length      decimal(10, 2)                       not null, 
     width       decimal(10, 2)                       not null, 
     height      decimal(10, 2)                       not null, 
     created_at  datetime   default CURRENT_TIMESTAMP null, 
     updated_at  datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP, 
     created_by  varchar(32)                          null, 
     updated_by  varchar(32)                          null, 
     deleted     tinyint(1) default 0                 null 
 ); 

INSERT INTO `product_details`
VALUES ('07d459a5-5df8-4644-825d-032d00dc4660', '8be8b38a-149e-49e5-b6e5-0a628973c04c', 'mat lanh', 1.50, 10.00, 5.00,
        3.00, '2026-05-04 16:04:02', '2026-05-04 16:04:02', 'sieunhan11', 'sieunhan11', 0),
       ('197bddad-cc33-4878-ae47-a67a59865947', 'a01b5a18-d63c-485c-8a5e-73dbdd960d12', '', 1.50, 10.00, 5.00, 3.00,
        '2026-05-03 01:29:19', '2026-05-03 01:29:19', 'sieunhan11', 'sieunhan11', 0),
       ('2afc5edc-0f9d-4ef3-81c7-91085e96da34', 'b36ef438-0c96-4ecb-8bb6-a1f9395101bd', '', 1.50, 10.00, 5.00, 3.00,
        '2026-05-03 00:03:45', '2026-05-03 00:03:45', 'sieunhan11', 'sieunhan11', 0),
       ('61931ecf-957b-4c22-b8e3-4e1d001be3a8', '01a8fbde-c054-4e45-89c6-111034e322bc', 'kooo', 1.50, 10.00, 5.00, 3.00,
        '2026-05-04 17:31:45', '2026-05-04 17:31:45', 'sieunhan11', 'sieunhan11', 0),
       ('81d6d332-2fc4-4693-8dae-473d2eb12b77', '55ed6816-5b0d-49aa-a6ec-7ce100fd46b2', 'Ma ylanh', 1.50, 10.00, 5.00,
        3.00, '2026-05-04 17:30:28', '2026-05-04 17:30:28', 'sieunhan11', 'sieunhan11', 0),
       ('a7590f78-8ccf-4d76-8fd7-9d5bfc32334c', '6e3de641-b8a1-4fc0-8a8d-4114fe995ab1', 'Tu lanh', 1.50, 10.00, 5.00,
        3.00, '2026-05-04 17:29:26', '2026-05-04 17:29:26', 'sieunhan11', 'sieunhan11', 0),
       ('c37d9815-f05a-435f-ada3-c0a11c6bab5d', '39268869-8331-477a-bab6-acb51578dc35', 'kooo', 1.50, 10.00, 5.00, 3.00,
        '2026-05-04 17:31:17', '2026-05-04 17:31:17', 'sieunhan11', 'sieunhan11', 0),
       ('c91b00bb-bedf-4cf8-b0f1-c7d4e30b003c', 'fcac3677-a0f7-4ed0-80f7-d39030042904', 'Ma ylanh', 1.50, 10.00, 5.00,
        3.00, '2026-05-04 17:30:14', '2026-05-04 17:30:14', 'sieunhan11', 'sieunhan11', 0),
       ('d7b33be7-0449-4c2e-b746-634b42142bb0', '525195c9-49b9-415b-9f88-ef24cc67e496', '', 1.50, 10.00, 5.00, 3.00,
        '2026-05-03 00:14:36', '2026-05-03 00:14:36', 'sieunhan11', 'sieunhan11', 0),
       ('e57f7b10-b661-47d5-b166-d867e3cd699f', '6829f4e1-c031-4322-aac4-42a2d9519c5a', '', 1.50, 10.00, 5.00, 3.00,
        '2026-05-03 01:12:57', '2026-05-03 01:12:57', 'sieunhan11', 'sieunhan11', 0),
       ('e8d17d08-960f-4522-abcc-8c1e3f054e18', 'ac66eacd-d486-408e-a293-55e84e546f99', '', 1.50, 10.00, 5.00, 3.00,
        '2026-05-03 01:13:12', '2026-05-03 01:13:12', 'sieunhan11', 'sieunhan11', 0),
       ('ed4f8e05-45b9-4f18-84c6-c4ae12d5ffa9', '6ed68775-c740-44ca-86d4-2efdee61e57c', '', 1.50, 10.00, 5.00, 3.00,
        '2026-05-03 01:12:44', '2026-05-03 01:12:44', 'sieunhan11', 'sieunhan11', 0),
       ('fb93d497-beee-457c-8856-4db6f5d69b3f', 'bbb7353b-a9d7-48a5-8c5e-1488c812e2e7', 'Vuia', 1.50, 10.00, 5.00, 3.00,
        '2026-05-04 17:30:54', '2026-05-04 17:30:54', 'sieunhan11', 'sieunhan11', 0);

create table if not exists inventories 
 ( 
     id                varchar(36)                                        not null 
         primary key, 
     product_id        varchar(36)                                        not null, 
     quantity_in_stock int                                                not null, 
     reserved_quantity int                                                not null, 
     status            enum ('IN_STOCK', 'LIMITED_STOCK', 'OUT_OF_STOCK') null, 
     version           bigint     default 0                               not null, 
     created_at        datetime   default CURRENT_TIMESTAMP               null, 
     updated_at        datetime   default CURRENT_TIMESTAMP               null on update CURRENT_TIMESTAMP, 
     created_by        varchar(32)                                        null, 
     updated_by        varchar(32)                                        null, 
     deleted           tinyint(1) default 0                               null, 
     constraint inventories_ibfk_1 
         foreign key (product_id) references products (id) 
 ); 

INSERT INTO `inventories`
VALUES ('245ed9e5-849a-44b8-b05c-479fa0b1c8e2', '6e3de641-b8a1-4fc0-8a8d-4114fe995ab1', 100, 1, 'IN_STOCK', 0,
        '2026-05-04 17:29:26', '2026-05-04 17:29:26', 'sieunhan11', 'sieunhan11', 0),
       ('2fbaa6d6-3307-4369-b1de-00a7c1908d62', '8be8b38a-149e-49e5-b6e5-0a628973c04c', 30, 1, 'IN_STOCK', 0,
        '2026-05-04 16:04:02', '2026-05-04 16:04:02', 'sieunhan11', 'sieunhan11', 0),
       ('3058fb20-8ad9-4bab-a491-4011d769548b', 'fcac3677-a0f7-4ed0-80f7-d39030042904', 990, 1, 'IN_STOCK', 11,
        '2026-05-04 17:30:14', '2026-05-09 11:35:06', 'sieunhan11', 'sieunhan11', 0),
       ('42b7bb40-7270-46bf-978a-9a8d21677f4e', '39268869-8331-477a-bab6-acb51578dc35', 11222, 1, 'IN_STOCK', 0,
        '2026-05-04 17:31:17', '2026-05-04 17:31:17', 'sieunhan11', 'sieunhan11', 0),
       ('5312f8e7-4b3a-4c07-9df9-a49a806add39', '6ed68775-c740-44ca-86d4-2efdee61e57c', 10, 1, 'LIMITED_STOCK', 0,
        '2026-05-03 01:12:43', '2026-05-03 01:12:43', NULL, NULL, 0),
       ('5dc1e18c-e025-43ed-a738-629cee9d3729', 'ac66eacd-d486-408e-a293-55e84e546f99', 0, 1, 'OUT_OF_STOCK', 0,
        '2026-05-03 01:13:11', '2026-05-03 01:13:11', NULL, NULL, 0),
       ('91927180-40d4-459a-9717-8d79b38454b8', '6829f4e1-c031-4322-aac4-42a2d9519c5a', 140, 1, 'IN_STOCK', 0,
        '2026-05-03 01:12:57', '2026-05-03 01:12:57', NULL, NULL, 0),
       ('9345230b-d314-4bb7-ae38-e495159e988c', 'bbb7353b-a9d7-48a5-8c5e-1488c812e2e7', 11020, 1, 'IN_STOCK', 0,
        '2026-05-04 17:30:55', '2026-05-04 17:30:55', 'sieunhan11', 'sieunhan11', 0),
       ('a0ce87a6-3a0b-46c2-be52-fb1f209f8c70', 'a01b5a18-d63c-485c-8a5e-73dbdd960d12', 0, 1, 'IN_STOCK', 5,
        '2026-05-03 01:29:19', '2026-05-09 11:24:54', NULL, 'sieunhan11', 0),
       ('ace77a61-5c11-4165-8689-cc116e883eb7', '55ed6816-5b0d-49aa-a6ec-7ce100fd46b2', 1100, 1, 'IN_STOCK', 0,
        '2026-05-04 17:30:28', '2026-05-04 17:30:28', 'sieunhan11', 'sieunhan11', 0),
       ('b2714c60-77f5-42d9-8c00-c66c0102cd54', '525195c9-49b9-415b-9f88-ef24cc67e496', 23, 1, 'IN_STOCK', 0,
        '2026-05-03 00:14:35', '2026-05-04 17:37:04', NULL, 'sieunhan11', 0),
       ('c8a782e5-a969-4223-8904-94f2f83e9697', 'b36ef438-0c96-4ecb-8bb6-a1f9395101bd', 10, 1, 'LIMITED_STOCK', 0,
        '2026-05-03 00:03:45', '2026-05-04 17:35:36', NULL, 'sieunhan11', 0),
       ('e3ec998b-b3c5-4245-802d-3a4c2aef51c4', '01a8fbde-c054-4e45-89c6-111034e322bc', 11232, 1, 'IN_STOCK', 0,
        '2026-05-04 17:31:45', '2026-05-04 17:31:45', 'sieunhan11', 'sieunhan11', 0);

create table if not exists discounts 
 ( 
     id             varchar(36)                                                   not null 
         primary key, 
     code           varchar(255)                                                  not null, 
     type           enum ('FIXED_AMOUNT', 'PERCENTAGE') default 'FIXED_AMOUNT'    not null, 
     value          decimal(19, 4)                                                not null, 
     start_date     date                                                          not null, 
     end_date       date                                                          not null, 
     usage_limit    int                                                           not null, 
     used_count     int                                 default 0                 not null, 
     reserved_count int                                                           not null, 
     version        bigint                                                        not null, 
     created_at     datetime                            default CURRENT_TIMESTAMP null, 
     updated_at     datetime                            default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP, 
     created_by     varchar(32)                                                   null, 
     updated_by     varchar(32)                                                   null, 
     deleted        tinyint(1)                          default 0                 null, 
     constraint code 
         unique (code) 
 ); 

INSERT INTO `discounts`
VALUES ('019e05e3-6dee-7041-8c43-891a0b633afe', 'HELLO123', 'FIXED_AMOUNT', 100.0000, '2026-08-05', '2026-09-05', 100,
        12, 12, 1, '2026-05-08 11:46:01', '2026-05-12 20:17:56', 'SYSTEM', 'SYSTEM', 0);

create table if not exists carriers 
 ( 
     id             varchar(36)                          not null 
         primary key, 
     name           varchar(50)                          not null, 
     base_price     decimal(19, 2)                       not null, 
     estimated_days int                                  not null, 
     created_at     datetime   default CURRENT_TIMESTAMP null, 
     updated_at     datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP, 
     created_by     varchar(32)                          null, 
     updated_by     varchar(32)                          null, 
     deleted        tinyint(1) default 0                 null 
 ); 

INSERT INTO `carriers`
VALUES ('760e4dae-c885-41ba-88b9-ef930dd941a4', 'SHOPEE', 50.00, 4, '2026-05-08 11:47:58', '2026-05-08 11:47:58',
        'SYSTEM', 'SYSTEM', 0);

create table if not exists addresses 
 ( 
     id             varchar(36)                          not null 
         primary key, 
     user_id        varchar(36)                          not null, 
     recipient_name varchar(50)                          not null, 
     phone_number   varchar(20)                          not null, 
     province       varchar(50)                          not null, 
     district       varchar(50)                          not null, 
     ward           varchar(50)                          not null, 
     detail_address varchar(100)                         not null, 
     created_at     datetime   default CURRENT_TIMESTAMP null, 
     updated_at     datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP, 
     created_by     varchar(32)                          null, 
     updated_by     varchar(32)                          null, 
     deleted        tinyint(1) default 0                 null 
 ); 

INSERT INTO `addresses`
VALUES ('645b8534-640c-4d39-8abe-71f65c270bf4', '6066f8df-3f9e-4779-9ea7-3e9c68d2d392', 'Sieu nhan', '0923484212',
        'Bac Giang', 'Viet Yen', 'Tu Lan', 'Tdp Dong Ich', '2026-05-08 11:52:51', '2026-05-08 11:52:51', 'SYSTEM',
        'SYSTEM', 0);

create table if not exists orders 
 ( 
     id                 varchar(36)                                                                                                    not null 
         primary key, 
     tracking_number    varchar(255)                                                                                                   not null, 
     user_id            varchar(36)                                                                                                    not null, 
     payment_method     enum ('COD', 'VNPAY', 'MOMO', 'STRIPE')                                                                        not null, 
     status             enum ('PENDING', 'CONFIRMED', 'REJECTED', 'PICKING', 'SHIPPING', 'DELIVERED', 'FAILED', 'REFUND', 'RETURNING') not null, 
     payment_status     enum ('AWAITING_PAYMENT', 'PAID', 'UNPAID', 'REFUNDED', 'FAILED') default 'AWAITING_PAYMENT'                   null, 
     total_price        decimal(19, 2)                                                                                                 not null, 
     shipping_fee       decimal(19, 2)                                                                                                 not null, 
     discount_amount    decimal(19, 2)                                                                                                 not null, 
     grand_total        decimal(19, 2)                                                                                                 not null, 
     discount_id        varchar(36)                                                                                                    not null, 
     estimated_delivery date                                                                                                           not null, 
     carrier_id         varchar(36)                                                                                                    not null, 
     carrier_name       varchar(255)                                                                                                   null, 
     address_id         varchar(36)                                                                                                    not null, 
     user_address       varchar(255)                                                                                                   null, 
     version            bigint                                                                                                         not null, 
     created_at         datetime                                                          default CURRENT_TIMESTAMP                    null, 
     updated_at         datetime                                                          default CURRENT_TIMESTAMP                    null on update CURRENT_TIMESTAMP, 
     created_by         varchar(32)                                                                                                    null, 
     updated_by         varchar(32)                                                                                                    null, 
     deleted            tinyint(1)                                                        default 0                                    null, 
     constraint order_pk_2 
         unique (id), 
     constraint order_pk_3 
         unique (tracking_number), 
     constraint orders_ibfk_1 
         foreign key (user_id) references users (id), 
     constraint orders_ibfk_2 
         foreign key (discount_id) references discounts (id), 
     constraint orders_ibfk_3 
         foreign key (carrier_id) references carriers (id), 
     constraint orders_ibfk_4 
         foreign key (address_id) references addresses (id) 
 ); 

INSERT INTO `orders` (id, tracking_number, user_id, payment_method, status, payment_status, total_price, shipping_fee, discount_amount, grand_total, discount_id, estimated_delivery, carrier_id, address_id, version, created_at, updated_at, created_by, updated_by, deleted)
VALUES ('0392cd94-4285-4f1d-a10d-3b846ee7d83b', 'SHOPEE4AC31378', '6066f8df-3f9e-4779-9ea7-3e9c68d2d392', 'VNPAY',
        'PENDING', 'AWAITING_PAYMENT', 948.80, 30.00, 100.00, 878.80, '019e05e3-6dee-7041-8c43-891a0b633afe',
        '2026-05-12', '760e4dae-c885-41ba-88b9-ef930dd941a4', '645b8534-640c-4d39-8abe-71f65c270bf4', 0,
        '2026-05-08 12:19:20', '2026-05-08 12:19:20', 'sieunhan11', 'sieunhan11', 0),
       ('0540fff6-6bc4-44b4-9983-9caf0e0c9773', 'SHOPEED4F7D409', '6066f8df-3f9e-4779-9ea7-3e9c68d2d392', 'MOMO',
        'PENDING', 'AWAITING_PAYMENT', 1348.76, 30.00, 100.00, 1278.76, '019e05e3-6dee-7041-8c43-891a0b633afe',
        '2026-05-12', '760e4dae-c885-41ba-88b9-ef930dd941a4', '645b8534-640c-4d39-8abe-71f65c270bf4', 1,
        '2026-05-08 13:20:08', '2026-05-08 13:20:08', 'sieunhan11', 'sieunhan11', 0),
       ('073c635b-020b-4675-8759-5eeda59332bf', 'SHOPEE93F1D2A9', '6066f8df-3f9e-4779-9ea7-3e9c68d2d392', 'MOMO',
        'PENDING', 'AWAITING_PAYMENT', 1348.76, 30.00, 100.00, 1278.76, '019e05e3-6dee-7041-8c43-891a0b633afe',
        '2026-05-13', '760e4dae-c885-41ba-88b9-ef930dd941a4', '645b8534-640c-4d39-8abe-71f65c270bf4', 1,
        '2026-05-09 11:24:54', '2026-05-09 11:24:54', 'sieunhan11', 'sieunhan11', 0),
       ('30731736-13f2-4154-b26b-45243b2a031d', 'SHOPEEAD04D29F', '6066f8df-3f9e-4779-9ea7-3e9c68d2d392', 'MOMO',
        'PENDING', 'AWAITING_PAYMENT', 948.80, 30.00, 100.00, 878.80, '019e05e3-6dee-7041-8c43-891a0b633afe',
        '2026-05-12', '760e4dae-c885-41ba-88b9-ef930dd941a4', '645b8534-640c-4d39-8abe-71f65c270bf4', 1,
        '2026-05-08 12:37:07', '2026-05-08 12:37:07', 'sieunhan11', 'sieunhan11', 0),
       ('62c2ee12-96a8-4ced-b99b-e452a6336185', 'SHOPEE41197508', '6066f8df-3f9e-4779-9ea7-3e9c68d2d392', 'COD',
        'PENDING', 'UNPAID', 948.80, 30.00, 100.00, 878.80, '019e05e3-6dee-7041-8c43-891a0b633afe', '2026-05-12',
        '760e4dae-c885-41ba-88b9-ef930dd941a4', '645b8534-640c-4d39-8abe-71f65c270bf4', 0, '2026-05-08 12:08:05',
        '2026-05-08 12:08:05', 'sieunhan11', 'sieunhan11', 0),
       ('7b5b4f0a-b87a-46ec-a920-d6e49ab24b58', 'SHOPEEFC871B5C', '6066f8df-3f9e-4779-9ea7-3e9c68d2d392', 'MOMO',
        'PENDING', 'AWAITING_PAYMENT', 948.80, 30.00, 100.00, 878.80, '019e05e3-6dee-7041-8c43-891a0b633afe',
        '2026-05-13', '760e4dae-c885-41ba-88b9-ef930dd941a4', '645b8534-640c-4d39-8abe-71f65c270bf4', 1,
        '2026-05-09 11:33:41', '2026-05-09 11:33:41', 'sieunhan11', 'sieunhan11', 0),
       ('9f7dbef9-0c7b-4bd6-8918-c2813c870232', 'SHOPEEB4DD8A1D', '6066f8df-3f9e-4779-9ea7-3e9c68d2d392', 'MOMO',
        'PENDING', 'AWAITING_PAYMENT', 948.80, 30.00, 100.00, 878.80, '019e05e3-6dee-7041-8c43-891a0b633afe',
        '2026-05-12', '760e4dae-c885-41ba-88b9-ef930dd941a4', '645b8534-640c-4d39-8abe-71f65c270bf4', 0,
        '2026-05-08 12:24:30', '2026-05-08 12:24:30', 'sieunhan11', 'sieunhan11', 0),
       ('a24cb8dd-658d-4ae6-a19b-b00330bacd46', 'SHOPEE3D69BD5F', '6066f8df-3f9e-4779-9ea7-3e9c68d2d392', 'MOMO',
        'PENDING', 'AWAITING_PAYMENT', 1348.76, 30.00, 100.00, 1278.76, '019e05e3-6dee-7041-8c43-891a0b633afe',
        '2026-05-12', '760e4dae-c885-41ba-88b9-ef930dd941a4', '645b8534-640c-4d39-8abe-71f65c270bf4', 1,
        '2026-05-08 12:55:23', '2026-05-08 12:55:23', 'sieunhan11', 'sieunhan11', 0),
       ('b0e2faa3-d82c-4857-aead-420caa3beb42', 'SHOPEE8DACA6E5', '6066f8df-3f9e-4779-9ea7-3e9c68d2d392', 'MOMO',
        'PENDING', 'AWAITING_PAYMENT', 1348.76, 30.00, 100.00, 1278.76, '019e05e3-6dee-7041-8c43-891a0b633afe',
        '2026-05-12', '760e4dae-c885-41ba-88b9-ef930dd941a4', '645b8534-640c-4d39-8abe-71f65c270bf4', 1,
        '2026-05-08 12:54:55', '2026-05-08 12:54:55', 'sieunhan11', 'sieunhan11', 0),
       ('f1d45d8d-8a50-45c9-9b86-c01c6623e6d6', 'SHOPEEB4C5C9E3', '6066f8df-3f9e-4779-9ea7-3e9c68d2d392', 'MOMO',
        'PENDING', 'AWAITING_PAYMENT', 1348.76, 30.00, 100.00, 1278.76, '019e05e3-6dee-7041-8c43-891a0b633afe',
        '2026-05-12', '760e4dae-c885-41ba-88b9-ef930dd941a4', '645b8534-640c-4d39-8abe-71f65c270bf4', 1,
        '2026-05-08 12:46:58', '2026-05-08 12:46:58', 'sieunhan11', 'sieunhan11', 0),
       ('f329da46-49bf-429e-9590-19c0f6b94a10', 'SHOPEEF786ECCC', '6066f8df-3f9e-4779-9ea7-3e9c68d2d392', 'COD',
        'PENDING', 'UNPAID', 948.80, 30.00, 100.00, 878.80, '019e05e3-6dee-7041-8c43-891a0b633afe', '2026-05-13',
        '760e4dae-c885-41ba-88b9-ef930dd941a4', '645b8534-640c-4d39-8abe-71f65c270bf4', 1, '2026-05-09 11:35:06',
        '2026-05-09 11:35:06', 'sieunhan11', 'sieunhan11', 0);

create table if not exists order_items 
 ( 
     id         varchar(36)                          not null 
         primary key, 
     order_id   varchar(36)                          not null, 
     product_id varchar(36)                          not null, 
     quantity   int                                  not null, 
     price      decimal(19, 2)                       not null, 
     created_at datetime   default CURRENT_TIMESTAMP null, 
     updated_at datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP, 
     created_by varchar(32)                          null, 
     updated_by varchar(32)                          null, 
     deleted    tinyint(1) default 0                 null, 
     constraint order_items_ibfk_1 
         foreign key (order_id) references orders (id), 
     constraint order_items_ibfk_2 
         foreign key (product_id) references products (id) 
 ); 

INSERT INTO `order_items`
VALUES ('0eeee2c3-4344-420e-b719-688ea893a253', '9f7dbef9-0c7b-4bd6-8918-c2813c870232',
        'fcac3677-a0f7-4ed0-80f7-d39030042904', 10, 94.88, '2026-05-08 12:24:30', '2026-05-08 12:24:30', 'sieunhan11',
        'sieunhan11', 0),
       ('1aeda9ef-82ec-4193-bd0c-19f8ed2f2377', 'f1d45d8d-8a50-45c9-9b86-c01c6623e6d6',
        'fcac3677-a0f7-4ed0-80f7-d39030042904', 10, 94.88, '2026-05-08 12:46:58', '2026-05-08 12:46:58', 'sieunhan11',
        'sieunhan11', 0),
       ('2f5f6bad-deb7-4c1c-830c-b8d36569f078', '073c635b-020b-4675-8759-5eeda59332bf',
        'a01b5a18-d63c-485c-8a5e-73dbdd960d12', 4, 99.99, '2026-05-09 11:24:54', '2026-05-09 11:24:54', 'sieunhan11',
        'sieunhan11', 0),
       ('3298725a-509a-4d53-8850-afb0e0449389', '073c635b-020b-4675-8759-5eeda59332bf',
        'fcac3677-a0f7-4ed0-80f7-d39030042904', 10, 94.88, '2026-05-09 11:24:54', '2026-05-09 11:24:54', 'sieunhan11',
        'sieunhan11', 0),
       ('3635bb48-57cb-425d-ae14-9c3d897b6341', '62c2ee12-96a8-4ced-b99b-e452a6336185',
        'fcac3677-a0f7-4ed0-80f7-d39030042904', 10, 94.88, '2026-05-08 12:08:05', '2026-05-08 12:08:05', 'sieunhan11',
        'sieunhan11', 0),
       ('3d038bf3-b41b-46ce-83b2-284355332fa9', 'b0e2faa3-d82c-4857-aead-420caa3beb42',
        'fcac3677-a0f7-4ed0-80f7-d39030042904', 10, 94.88, '2026-05-08 12:54:55', '2026-05-08 12:54:55', 'sieunhan11',
        'sieunhan11', 0),
       ('407a31b7-88e4-4b34-a4e7-015538ed1c6a', 'f1d45d8d-8a50-45c9-9b86-c01c6623e6d6',
        'a01b5a18-d63c-485c-8a5e-73dbdd960d12', 4, 99.99, '2026-05-08 12:46:58', '2026-05-08 12:46:58', 'sieunhan11',
        'sieunhan11', 0),
       ('421bedf8-cbc7-4e3c-9127-dfd431397513', '0392cd94-4285-4f1d-a10d-3b846ee7d83b',
        'fcac3677-a0f7-4ed0-80f7-d39030042904', 10, 94.88, '2026-05-08 12:19:20', '2026-05-08 12:19:20', 'sieunhan11',
        'sieunhan11', 0),
       ('7186fbce-a30c-423e-b3c0-bd85be09c887', 'a24cb8dd-658d-4ae6-a19b-b00330bacd46',
        'a01b5a18-d63c-485c-8a5e-73dbdd960d12', 4, 99.99, '2026-05-08 12:55:23', '2026-05-08 12:55:23', 'sieunhan11',
        'sieunhan11', 0),
       ('83886f21-1341-4dcb-a22b-b8d4822023d3', '30731736-13f2-4154-b26b-45243b2a031d',
        'fcac3677-a0f7-4ed0-80f7-d39030042904', 10, 94.88, '2026-05-08 12:37:07', '2026-05-08 12:37:07', 'sieunhan11',
        'sieunhan11', 0),
       ('a028a9c9-5f5a-4635-91be-31ff4fcc8167', 'f329da46-49bf-429e-9590-19c0f6b94a10',
        'fcac3677-a0f7-4ed0-80f7-d39030042904', 10, 94.88, '2026-05-09 11:35:06', '2026-05-09 11:35:06', 'sieunhan11',
        'sieunhan11', 0),
       ('a897f67f-1c82-4180-b048-3195dacdf14d', '7b5b4f0a-b87a-46ec-a920-d6e49ab24b58',
        'fcac3677-a0f7-4ed0-80f7-d39030042904', 10, 94.88, '2026-05-09 11:33:41', '2026-05-09 11:33:41', 'sieunhan11',
        'sieunhan11', 0),
       ('b7884464-fd15-4784-8122-0bc3adcbf8dc', 'b0e2faa3-d82c-4857-aead-420caa3beb42',
        'a01b5a18-d63c-485c-8a5e-73dbdd960d12', 4, 99.99, '2026-05-08 12:54:55', '2026-05-08 12:54:55', 'sieunhan11',
        'sieunhan11', 0),
       ('e380cb99-c0e3-4f68-a0e8-4f28c7b41c83', 'a24cb8dd-658d-4ae6-a19b-b00330bacd46',
        'fcac3677-a0f7-4ed0-80f7-d39030042904', 10, 94.88, '2026-05-08 12:55:23', '2026-05-08 12:55:23', 'sieunhan11',
        'sieunhan11', 0),
       ('f4a2cc0a-03d3-4c58-b7ff-05d977e157d8', '0540fff6-6bc4-44b4-9983-9caf0e0c9773',
        'fcac3677-a0f7-4ed0-80f7-d39030042904', 10, 94.88, '2026-05-08 13:20:08', '2026-05-08 13:20:08', 'sieunhan11',
        'sieunhan11', 0),
       ('f9e29f25-44b0-425e-9162-76ef5160d33a', '0540fff6-6bc4-44b4-9983-9caf0e0c9773',
        'a01b5a18-d63c-485c-8a5e-73dbdd960d12', 4, 99.99, '2026-05-08 13:20:08', '2026-05-08 13:20:08', 'sieunhan11',
        'sieunhan11', 0);

create table if not exists tracking_logs 
 ( 
     id          varchar(36)                                                                                          not null 
         primary key, 
     order_id    varchar(36)                                                                                          not null, 
     from_status enum ('PENDING', 'CONFIRMED', 'REJECTED', 'PICKING', 'SHIPPING', 'DELIVERED', 'FAILED', 'RETURNING') not null, 
     to_status   enum ('PENDING', 'CONFIRMED', 'REJECTED', 'PICKING', 'SHIPPING', 'DELIVERED', 'FAILED', 'RETURNING') not null, 
     note        mediumtext                                                                                           null, 
     location    varchar(255)                                                                                         not null, 
     created_at  datetime   default CURRENT_TIMESTAMP                                                                 null, 
     updated_at  datetime   default CURRENT_TIMESTAMP                                                                 null on update CURRENT_TIMESTAMP, 
     created_by  varchar(32)                                                                                          null, 
     updated_by  varchar(32)                                                                                          null, 
     deleted     tinyint(1) default 0                                                                                 null, 
     constraint tracking_logs_ibfk_1 
         foreign key (order_id) references orders (id) 
 ); 

create table if not exists carts 
 ( 
     id         varchar(36)                                            not null 
         primary key, 
     user_id    varchar(36)                                            not null, 
     status     enum ('ACTIVE', 'CONVERTED') default 'ACTIVE'          null, 
     version    bigint                                                 not null, 
     created_at datetime                     default CURRENT_TIMESTAMP null, 
     updated_at datetime                     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP, 
     created_by varchar(32)                                            null, 
     updated_by varchar(32)                                            null, 
     deleted    tinyint(1)                                             null, 
     constraint user_id 
         unique (user_id), 
     constraint carts_ibfk_1 
         foreign key (user_id) references users (id) 
 ); 

INSERT INTO `carts`
VALUES ('4d065b69-d5b1-421f-b53d-d291ae284367', '6066f8df-3f9e-4779-9ea7-3e9c68d2d392', NULL, 0, '2026-05-02 12:48:05',
        '2026-05-02 12:48:05', 'SYSTEM', 'SYSTEM', 0);

create table if not exists cart_items 
 ( 
     id         varchar(36)                          not null 
         primary key, 
     cart_id    varchar(36)                          not null, 
     product_id varchar(36)                          not null, 
     quantity   int                                  not null, 
     created_at datetime   default CURRENT_TIMESTAMP null, 
     updated_at datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP, 
     created_by varchar(32)                          null, 
     updated_by varchar(32)                          null, 
     deleted    tinyint(1) default 0                 null, 
     constraint cart_items_ibfk_1 
         foreign key (cart_id) references carts (id), 
     constraint cart_items_ibfk_2 
         foreign key (product_id) references products (id) 
 ); 

INSERT INTO `cart_items` (id, cart_id, product_id, quantity, created_at, updated_at, created_by, updated_by, deleted)
VALUES ('095c7ddd-66ff-4c9b-ab70-6df616ef3929', '4d065b69-d5b1-421f-b53d-d291ae284367',
        'b36ef438-0c96-4ecb-8bb6-a1f9395101bd', 1, '2026-05-04 17:35:36', '2026-05-04 17:35:36', 'sieunhan11',
        'sieunhan11', 0),
       ('0bb200c6-91b5-4d82-a160-f4db6aea03d5', '4d065b69-d5b1-421f-b53d-d291ae284367',
        '8be8b38a-149e-49e5-b6e5-0a628973c04c', 29, '2026-05-04 17:34:39', '2026-05-04 17:34:39', 'sieunhan11',
        'sieunhan11', 0),
       ('14351701-5d14-4051-96bb-7e92ee97ec01', '4d065b69-d5b1-421f-b53d-d291ae284367',
        'fcac3677-a0f7-4ed0-80f7-d39030042904', 10, '2026-05-04 17:35:57', '2026-05-09 11:35:06', 'sieunhan11',
        'sieunhan11', 1),
       ('1eb1bd9f-285f-4344-87e7-814fab31bbd6', '4d065b69-d5b1-421f-b53d-d291ae284367',
        '76ca6748-1e41-4887-b1f9-726e480cb321', 2, '2026-05-02 14:21:35', '2026-05-09 11:33:37', 'sieunhan11',
        'sieunhan11', 0),
       ('46269688-7cdd-43a3-b125-d9c844522020', '4d065b69-d5b1-421f-b53d-d291ae284367',
        'a01b5a18-d63c-485c-8a5e-73dbdd960d12', 4, '2026-05-03 01:31:41', '2026-05-09 11:33:37', 'sieunhan11',
        'sieunhan11', 1),
       ('8e5103a8-0348-47d9-8904-ab33bbbc2591', '4d065b69-d5b1-421f-b53d-d291ae284367',
        '6ed68775-c740-44ca-86d4-2efdee61e57c', 9, '2026-05-04 17:36:16', '2026-05-04 17:36:16', 'sieunhan11',
        'sieunhan11', 0),
       ('ab6ee87e-935c-4b2b-8b51-c7393d5703c3', '4d065b69-d5b1-421f-b53d-d291ae284367',
        '55ed6816-5b0d-49aa-a6ec-7ce100fd46b2', 95, '2026-05-04 17:36:48', '2026-05-04 17:36:48', 'sieunhan11',
        'sieunhan11', 0),
       ('ae4392f2-10f6-4d7c-9703-817d2d6af693', '4d065b69-d5b1-421f-b53d-d291ae284367',
        '525195c9-49b9-415b-9f88-ef24cc67e496', 10, '2026-05-04 17:37:04', '2026-05-04 17:37:04', 'sieunhan11',
        'sieunhan11', 0),
       ('b99a821a-a345-4389-8437-e397d6e856b0', '4d065b69-d5b1-421f-b53d-d291ae284367',
        '6e3de641-b8a1-4fc0-8a8d-4114fe995ab1', 90, '2026-05-04 17:36:27', '2026-05-04 17:36:27', 'sieunhan11',
        'sieunhan11', 0),
       ('d4440c2d-5767-47e3-b31b-b96d9208a14d', '4d065b69-d5b1-421f-b53d-d291ae284367',
        '6829f4e1-c031-4322-aac4-42a2d9519c5a', 95, '2026-05-04 17:36:40', '2026-05-04 17:36:40', 'sieunhan11',
        'sieunhan11', 0),
       ('da97bda3-a7af-4d40-9991-81621e29bfa8', '4d065b69-d5b1-421f-b53d-d291ae284367',
        '39268869-8331-477a-bab6-acb51578dc35', 23, '2026-05-04 17:37:16', '2026-05-04 17:37:27', 'sieunhan11',
        'sieunhan11', 0),
       ('e0dc9da8-36c2-48d1-a38c-e743e557db65', '4d065b69-d5b1-421f-b53d-d291ae284367',
        'bbb7353b-a9d7-48a5-8c5e-1488c812e2e7', 10, '2026-05-04 17:35:47', '2026-05-04 17:35:47', 'sieunhan11',
        'sieunhan11', 0);

create table if not exists invalidated_token 
 ( 
     id          varchar(36)                          not null 
         primary key, 
     expiry_time date                                 null, 
     created_at  datetime   default CURRENT_TIMESTAMP null, 
     updated_at  datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP, 
     created_by  varchar(32)                          null, 
     updated_by  varchar(32)                          null, 
     deleted     tinyint(1) default 0                 null 
 ); 

create index parent_id 
     on categories (parent_id); 
 
create index product_id 
     on inventories (product_id); 
 
create index category_id 
     on products (category_id); 
 
create index cart_id 
     on cart_items (cart_id); 
 
create index cart_product_id 
     on cart_items (product_id); 
 
create index order_id 
     on order_items (order_id); 
 
create index order_product_id 
     on order_items (product_id); 
 
create index address_id 
     on orders (address_id); 
 
create index carrier_id 
     on orders (carrier_id); 
 
create index discount_id 
     on orders (discount_id); 
 
create index order_user_id 
     on orders (user_id); 
 
create index tracking_order_id 
     on tracking_logs (order_id); 

create index role_id 
     on users_roles (role_id); 
 
create index user_id 
     on users_roles (user_id); 
