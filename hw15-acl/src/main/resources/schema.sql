CREATE SCHEMA IF NOT EXISTS auth
    AUTHORIZATION admin;

 CREATE SCHEMA IF NOT EXISTS library
     AUTHORIZATION admin;

CREATE TABLE IF NOT EXISTS auth.users
(
    id bigint NOT NULL,
    user_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    password character varying(255) COLLATE pg_catalog."default" NOT NULL,
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT uniq_username UNIQUE (user_name)
)

CREATE TABLE IF NOT EXISTS auth.authorities
(
    id bigint NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT authorities_pkey PRIMARY KEY (id),
    CONSTRAINT uniq_name UNIQUE (name)
)

CREATE TABLE IF NOT EXISTS auth.users_authorities
(
    user_id bigint NOT NULL,
    authority_id bigint NOT NULL,
    CONSTRAINT users_authorities_pkey PRIMARY KEY (user_id, authority_id),
    CONSTRAINT fk_authority_id FOREIGN KEY (authority_id)
        REFERENCES auth.authorities (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
        NOT VALID,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id)
        REFERENCES auth.users (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
        NOT VALID
)

CREATE TABLE IF NOT EXISTS library.authors
(
    id bigint NOT NULL,
    full_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT authors_pkey PRIMARY KEY (id),
    CONSTRAINT uniq_full_name UNIQUE (full_name)
)

CREATE TABLE IF NOT EXISTS library.books
(
    id bigint NOT NULL,
    title character varying COLLATE pg_catalog."default" NOT NULL,
    author_id bigint NOT NULL,
    CONSTRAINT books_pkey PRIMARY KEY (id),
    CONSTRAINT uniq_title UNIQUE (title),
    CONSTRAINT fk_author_id FOREIGN KEY (author_id)
        REFERENCES library.authors (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
        NOT VALID
)

CREATE TABLE IF NOT EXISTS library.comments
(
    id bigint NOT NULL,
    comment character varying(255) COLLATE pg_catalog."default",
    book_id bigint,
    CONSTRAINT comments_pkey PRIMARY KEY (id),
    CONSTRAINT fk_book_id FOREIGN KEY (book_id)
        REFERENCES library.books (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

CREATE TABLE IF NOT EXISTS library.genres
(
    id bigint NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT genres_pkey PRIMARY KEY (id),
    CONSTRAINT uniq_name UNIQUE (name)
)

CREATE TABLE IF NOT EXISTS library.books_genres
(
    book_id bigint NOT NULL,
    genre_id bigint NOT NULL,
    CONSTRAINT books_genres_pkey PRIMARY KEY (book_id, genre_id),
    CONSTRAINT fk_book_id FOREIGN KEY (book_id)
        REFERENCES library.books (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_genre_id FOREIGN KEY (genre_id)
        REFERENCES library.genres (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
        NOT VALID
)

CREATE TABLE IF NOT EXISTS library.collections
(
    id bigint NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    description character varying(500) COLLATE pg_catalog."default",
    CONSTRAINT collections_pkey PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS library.collections_books
(
    collection_id bigint NOT NULL,
    book_id bigint,
    CONSTRAINT collections_books_pkey PRIMARY KEY (collection_id),
    CONSTRAINT fk_book_id FOREIGN KEY (book_id)
        REFERENCES library.books (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_collection_id FOREIGN KEY (collection_id)
        REFERENCES library.collections (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

create table acl_sid(
	id bigserial not null primary key,
	principal boolean not null,
	sid varchar(100) not null,
	constraint unique_uk_1 unique(sid,principal)
);

create table acl_class(
	id bigserial not null primary key,
	class varchar(100) not null,
	constraint unique_uk_2 unique(class)
);

create table acl_object_identity(
	id bigserial primary key,
	object_id_class bigint not null,
	object_id_identity varchar(36) not null,
	parent_object bigint,
	owner_sid bigint,
	entries_inheriting boolean not null,
	constraint unique_uk_3 unique(object_id_class,object_id_identity),
	constraint foreign_fk_1 foreign key(parent_object)references acl_object_identity(id),
	constraint foreign_fk_2 foreign key(object_id_class)references acl_class(id),
	constraint foreign_fk_3 foreign key(owner_sid)references acl_sid(id)
);

create table acl_entry(
	id bigserial primary key,
	acl_object_identity bigint not null,
	ace_order int not null,
	sid bigint not null,
	mask integer not null,
	granting boolean not null,
	audit_success boolean not null,
	audit_failure boolean not null,
	constraint unique_uk_4 unique(acl_object_identity,ace_order),
	constraint foreign_fk_4 foreign key(acl_object_identity) references acl_object_identity(id),
	constraint foreign_fk_5 foreign key(sid) references acl_sid(id)
);