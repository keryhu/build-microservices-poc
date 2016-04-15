create table orders (
	id serial primary key,
	customer_id int not null,
	line_item varchar(1024) not null,
	unit_price numeric(10,2) not null,
	quantity int not null
);

create index idx_orders_customer_id on orders (customer_id);