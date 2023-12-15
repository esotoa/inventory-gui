PRAGMA foreign_keys=ON;
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
p_code  INTEGER PRIMARY KEY AUTOINCREMENT,
v_code  INTEGER ,
p_price REAL    not null,
p_name  char(20)    not null,
d_code  integer ,
foreign key(d_code) references department(dep_code) on delete cascade,
foreign key (v_code) references Vendor(v_code) on delete cascade);
INSERT INTO Product (p_price, p_name) VALUES (14.990000000000001101,'Beef');
INSERT INTO Product (p_price, p_name) VALUES (20.989999999999997548,'Ribs');
INSERT INTO Product (p_price, p_name) VALUES (8.9900000000000002131,'Ham');
INSERT INTO Product (p_price, p_name) VALUES (8.9900000000000002131,'Turkey');
INSERT INTO Product (p_price, p_name) VALUES (2.9900000000000002131,'Cereal');
INSERT INTO Product (p_price, p_name) VALUES (3.9900000000000002131,'Chips');
INSERT INTO Product (p_price, p_name) VALUES (15.990000000000000213,'Sushi');
INSERT INTO Product (p_price, p_name) VALUES (14.990000000000001101,'Salmon');

CREATE TABLE Stock(
s_code  INTEGER PRIMARY KEY AUTOINCREMENT,
p_code  integer ,
quantity   integer not NULL,
foreign key (p_code) references Product(p_code) on delete CASCADE);
INSERT INTO Stock (quantity) VALUES (5);
INSERT INTO Stock (quantity) VALUES (30);
INSERT INTO Stock (quantity) VALUES (15);
INSERT INTO Stock (quantity) VALUES (20);
INSERT INTO Stock (quantity) VALUES (34);
INSERT INTO Stock (quantity) VALUES (21);
INSERT INTO Stock (quantity) VALUES (10);
INSERT INTO Stock (quantity) VALUES (4);
COMMIT;