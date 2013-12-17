# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table BID (
  id                        bigint not null,
  value                     integer,
  email                     varchar(255),
  user_id                   bigint,
  part_id                   bigint,
  comment                   varchar(255),
  constraint uq_BID_1 unique (ID),
  constraint pk_BID primary key (id))
;

create table PART (
  id                        bigint not null,
  creator                   varchar(255),
  division                  varchar(255),
  email                     varchar(255),
  phone                     varchar(255),
  label                     varchar(255),
  vendor                    varchar(255),
  quantity                  bigint,
  description               varchar(255),
  attachment_name           varchar(255),
  constraint uq_PART_1 unique (ID),
  constraint pk_PART primary key (id))
;

create table USER (
  id                        bigint not null,
  password                  varchar(255),
  username                  varchar(255),
  name                      varchar(255),
  company_name              varchar(255),
  constraint uq_USER_1 unique (ID,USERNAME),
  constraint pk_USER primary key (id))
;

create sequence BID_seq;

create sequence PART_seq;

create sequence USER_seq;

alter table BID add constraint fk_BID_user_1 foreign key (user_id) references USER (id) on delete restrict on update restrict;
create index ix_BID_user_1 on BID (user_id);
alter table BID add constraint fk_BID_part_2 foreign key (part_id) references PART (id) on delete restrict on update restrict;
create index ix_BID_part_2 on BID (part_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists BID;

drop table if exists PART;

drop table if exists USER;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists BID_seq;

drop sequence if exists PART_seq;

drop sequence if exists USER_seq;

