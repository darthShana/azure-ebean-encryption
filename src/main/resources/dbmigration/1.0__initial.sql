-- apply changes
create table customer (
  id                            bigserial not null,
  name                          varchar(255) not null,
  registered                    date,
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint pk_customer primary key (id)
);

