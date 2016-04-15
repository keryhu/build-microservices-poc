-- connect to default redshift db (i.e. dev) as master
create database warehouse;

-- connect to warehouse as master
create user dev password 'fTe11CwUdo4tSTeckziid5Judq0tyX';
create user dwuser password '94m!640Cqum8';

-- connect to warehouse as dev
create table customer (id integer not null primary key, email varchar(512) not null, name varchar(512) not null, deleted boolean not null default false);
create table customer_order (order_id integer not null primary key, customer_id integer not null references customer(id), email varchar(512) not null, name varchar(512) not null, line_item varchar(1024) not null, unit_price numeric(5,2) not null, quantity integer not null, deleted boolean not null default false);

grant select on customer to dwuser;
grant select on customer_order to dwuser;