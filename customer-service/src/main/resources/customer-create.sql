create table customer (
	id serial primary key,
	name varchar(512) not null,
	email varchar(512) not null unique,
	internal_data varchar(512)
);

create index idx_customer_name on customer(name);