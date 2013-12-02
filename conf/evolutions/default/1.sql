# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bid (
  id                        bigint not null,
  user_id                   bigint,
  part_id                   bigint,
  value                     integer,
  constraint pk_bid primary key (id))
;

create table part (
  id                        bigint not null,
  label                     varchar(255),
  brand                     varchar(255),
  quantity                  bigint,
  description               varchar(255),
  constraint pk_part primary key (id))
;

create table user (
  id                        bigint not null,
  password                  varchar(255),
  username                  varchar(255),
  name                      varchar(255),
  company_name              varchar(255),
  constraint pk_user primary key (id))
;

create sequence bid_seq;

create sequence part_seq;

create sequence user_seq;

alter table bid add constraint fk_bid_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_bid_user_1 on bid (user_id);
alter table bid add constraint fk_bid_part_2 foreign key (part_id) references part (id) on delete restrict on update restrict;
create index ix_bid_part_2 on bid (part_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists bid;

drop table if exists part;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists bid_seq;

drop sequence if exists part_seq;

drop sequence if exists user_seq;

