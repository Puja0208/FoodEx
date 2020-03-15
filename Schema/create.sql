CREATE TABLE student(
	student_id	VARCHAR(20),
	name		VARCHAR(100) not null,
	photo		TEXT,
	PRIMARY KEY (student_id)
);

CREATE TABLE canteen(
	canteen_id		VARCHAR(20),
	name			VARCHAR(100) not null,
	hostel_no		VARCHAR(20),
	opening_hrs		VARCHAR(100),
	phone_no		VARCHAR(20),
	open			VARCHAR(20),
	PRIMARY KEY (canteen_id)
);

CREATE TABLE "order"(
	order_id		VARCHAR(20),
	time_of_order		TIMESTAMP,
	time_of_collecting	TIMESTAMP,
	status_of_order		VARCHAR(20),
	student_id 		VARCHAR(20),
	canteen_id 		VARCHAR(20),
	PRIMARY KEY (order_id),
	FOREIGN KEY (student_id) REFERENCES student(student_id)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	FOREIGN KEY (canteen_id) REFERENCES canteen(canteen_id)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);

CREATE TABLE account(
	account_id		VARCHAR(20),
	balance			NUMERIC(8,2),
	dues			NUMERIC(8,2),
	student_id 		VARCHAR(20),
	PRIMARY KEY (account_id),
	FOREIGN KEY (student_id) REFERENCES student(student_id)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);

CREATE TABLE canteen_account(
	account_id		VARCHAR(20),
	balance			NUMERIC(8,2),
	dues			NUMERIC(8,2),
	canteen_id 		VARCHAR(20),
	PRIMARY KEY (account_id),
	FOREIGN KEY (canteen_id) REFERENCES canteen(canteen_id)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);

CREATE TABLE bills(
	bill_id			VARCHAR(20),
	amount			NUMERIC(8,2),
	order_id		VARCHAR(20),
	payment_status 		VARCHAR(20),
	details 		TEXT,
	account_id 		VARCHAR(20),
	PRIMARY KEY (bill_id),
	FOREIGN KEY (order_id) REFERENCES "order"(order_id)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	FOREIGN KEY (account_id) REFERENCES account(account_id)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);

CREATE TABLE dish(
	dish_id		VARCHAR(20),
	name		VARCHAR(100) not null,
	veg		VARCHAR(20),
	cooking_drn	VARCHAR(20),
	PRIMARY KEY (dish_id)
);

CREATE TABLE pending_dish_list(
	order_id		VARCHAR(20),
	dish_id			VARCHAR(20),
    item_id         VARCHAR(20),
	quantity		NUMERIC(8,2),
	PRIMARY KEY (order_id,dish_id),
	FOREIGN KEY (dish_id) REFERENCES dish(dish_id)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);

CREATE TABLE menu(
	canteen_id		VARCHAR(20),
	dish_id			VARCHAR(20),
	price 			NUMERIC(8,2),
	PRIMARY KEY (canteen_id, dish_id),
	FOREIGN KEY (canteen_id) REFERENCES canteen(canteen_id)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	FOREIGN KEY (dish_id) REFERENCES dish(dish_id)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);

CREATE TABLE reviews(
	review_id		VARCHAR(20),
	canteen_id		VARCHAR(20),
	student_id		VARCHAR(20),
	dish_id			VARCHAR(20),
	rating 			NUMERIC(2,0),
	review_text		TEXT,
	time			TIMESTAMP,
	PRIMARY KEY (review_id),
	FOREIGN KEY (canteen_id) REFERENCES canteen(canteen_id)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	FOREIGN KEY (student_id) REFERENCES student(student_id)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	FOREIGN KEY (dish_id) REFERENCES dish(dish_id)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);


CREATE TABLE stud_password(
	student_id	VARCHAR(20),
	password	VARCHAR(20),
	PRIMARY KEY (student_id),
	FOREIGN KEY (student_id) REFERENCES student(student_id)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);

CREATE TABLE canteen_password(
	canteen_id	VARCHAR(20),
	password	VARCHAR(20),
	PRIMARY KEY (canteen_id),
	FOREIGN KEY (canteen_id) REFERENCES canteen(canteen_id)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);

CREATE TABLE notification(
	notification_id VARCHAR(20),
	to_id		VARCHAR(20),
	from_id		VARCHAR(20),
	notify_details TEXT,
	seen_status VARCHAR(10),
	time		TIMESTAMP,
	ntype		VARCHAR(30),
	PRIMARY KEY (notification_id)
);

create sequence if not exists dishid start 1;
create sequence if not exists accountid start 1;
create sequence if not exists orderid start 1;
create sequence if not exists itemid start 1;
create sequence if not exists billid start 1;
create sequence if not exists notifyid start 1;
create sequence if not exists reviewid start 1;