-- apply changes
create table book (
  id                            bigserial not null,
  book_name                     varchar(255),
  author                        varchar(255),
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint pk_book primary key (id)
);

create table data_encryption_key (
  id                            bigserial not null,
  table_name                    varchar(255) not null,
  column_name                   varchar(255) not null,
  data_encryption_key           bytea not null,
  kek_id                        varchar(255) not null,
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint pk_data_encryption_key primary key (id)
);

alter table customer add column mothers_maiden_name bytea;
alter table customer add column date_of_birth bytea;

