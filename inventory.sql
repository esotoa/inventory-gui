PRAGMA foreign_keys = ON;
BEGIN TRANSACTION;
CREATE TABLE Department(
dep_code    INTEGER PRIMARY KEY AUTOINCREMENT,
dep_name    char(20)    not null,
dep_manager char(20)    not null,
dep_contact char (15)   not null);
INSERT INTO Department (dep_name, dep_manager, dep_contact) VALUES ('Deli','John Smith','132-456-5990');
INSERT INTO Department (dep_name, dep_manager, dep_contact) VALUES ('Meat','Luke Schmidt','387-437-9381');
INSERT INTO Department (dep_name, dep_manager, dep_contact) VALUES ('Grocery','Eric Soto','132-354-5768');
INSERT INTO Department (dep_name, dep_manager, dep_contact) VALUES ('Seafood','Jack Ryan','102-394-5867');

CREATE TABLE Vendor(
v_code  INTEGER PRIMARY KEY AUTOINCREMENT,
v_address   char(20)    not null,
v_contact   char(15)    not null,
v_name      char(15)    not null);
INSERT INTO Vendor (v_address, v_contact, v_name) VALUES ('5634 W. Main Street','678-456-1245','Deli Ven');
INSERT INTO Vendor (v_address, v_contact, v_name) VALUES ('1234 N. Jeff Street','123-456-789','Meat Ven');
INSERT INTO Vendor (v_address, v_contact, v_name) VALUES ('3246 E. West Street','234-573-0956','Grocery Ven');
INSERT INTO Vendor (v_address, v_contact, v_name) VALUES ('5325 N. Columbia Street','235-654-3222','Seafood Ven');


CREATE TABLE Product (
p_code INTEGER PRIMARY KEY AUTOINCREMENT,
v_code  INTEGER not null,
p_price REAL    not null,
p_name  char(20)    not null,
d_code  integer not null,
foreign key(d_code) references Department(dep_code) on delete cascade,
foreign key (v_code) references Vendor(v_code) on delete cascade);
INSERT INTO Product (v_code, p_price, p_name, d_code) VALUES (2,14.99,'Beef',2);
INSERT INTO Product (v_code, p_price, p_name, d_code) VALUES (2,20.99,'Ribs',2);
INSERT INTO Product (v_code, p_price, p_name, d_code) VALUES (1,8.99,'Ham',1);
INSERT INTO Product (v_code, p_price, p_name, d_code) VALUES (1,8.99,'Turkey',1);
INSERT INTO Product (v_code, p_price, p_name, d_code) VALUES (3,2.99,'Cereal',3);
INSERT INTO Product (v_code, p_price, p_name, d_code) VALUES (3,3.99,'Chips',3);
INSERT INTO Product (v_code, p_price, p_name, d_code) VALUES (4,15.99,'Sushi',4);
INSERT INTO Product (v_code, p_price, p_name, d_code) VALUES (4,14.99,'Salmon',4);

CREATE TABLE Stock(
s_code  INTEGER PRIMARY KEY AUTOINCREMENT,
p_code integer not null,
quantity   integer not NULL,
FOREIGN key (p_code) references product(p_code) on delete CASCADE);

INSERT INTO Stock (p_code, quantity) VALUES (1, 5);
INSERT INTO Stock (p_code, quantity) VALUES (2, 30);
INSERT INTO Stock (p_code, quantity) VALUES (3, 15);
INSERT INTO Stock (p_code, quantity) VALUES (4, 20);
INSERT INTO Stock (p_code, quantity) VALUES (5, 34);
INSERT INTO Stock (p_code, quantity) VALUES (6, 21);
INSERT INTO Stock (p_code, quantity) VALUES (7, 10);
INSERT INTO Stock (p_code, quantity) VALUES (8, 4);
COMMIT;
