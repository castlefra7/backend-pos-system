/*
create user finalcompta login password '123456';
create database finalcomptadb;
alter database finalcomptadb owner to finalcompta;

psql comptabilitedb comptabilite
123456

DROP SCHEMA accounting CASCADE;
CREATE SCHEMA accounting;
select * from information_schema.tables where table_schema = 'accounting';


create user test login password '123456';
create database testdb;
alter database testdb owner to test;

*/

DROP SCHEMA accounting CASCADE;
CREATE SCHEMA accounting;
create sequence IF NOT EXISTS accounting.categoryIDSeq;
create sequence IF NOT EXISTS accounting.productsIDSeq;
create sequence IF NOT EXISTS accounting.invoiceIDSeq;
create sequence IF NOT EXISTS accounting.ticketNumberSeq;
create sequence IF NOT EXISTS accounting.ordersIDSeq;
create sequence IF NOT EXISTS accounting.stockIDSeq;

/* SAL SUC */

create table accounting.users (
	userID smallint primary key,
	userName varchar(100)
);

create table accounting.companies (
	companyID integer primary key,
	companyName varchar(200)
);

create table accounting.companyAffiliates (
	companyAffiliateID integer primary key,
	companyID integer references accounting.companies(companyID),
	affiliateName varchar(200)
);

create table accounting.placeType (
	placeTypeID smallint primary key,
	placeTypeDesc varchar(25)
);
create table accounting.places (
	placeID smallint primary key,
	placeName varchar(5),
	placeTypeID smallint references accounting.placeType(placeTypeID),
	companyAffiliateID integer references accounting.companyAffiliates(companyAffiliateID)
);

create table accounting.pointOfSales (
	pointOfSaleID integer primary key,
	companyAffiliateID integer references accounting.companyAffiliates(companyAffiliateID)
);

create table accounting.pointOfSaleMoves (
	pointOfSaleID integer references accounting.pointOfSales(pointOfSaleID),
	dateUsage date NOT NULL,
	cashFund numeric(14, 2) NOT NULL,
	inventory numeric(14, 2)
);

CREATE TYPE accounting.tastes AS ENUM ('SAL', 'SUC');


create table accounting.productCategories (
	categoryID char(2) primary key,
	categoryName varchar(100) NOT NULL,
	categoryTaste accounting.tastes,
	categoryImage text,
	companyAffiliateID integer references accounting.companyAffiliates(companyAffiliateID),
	isActive char(1) NOT NULL
);

create table accounting.products (
	productID char(5) primary key,
	categoryID char(2) references accounting.productCategories(categoryID),
	productName varchar(200) NOT NULL,
	price numeric(14, 2) NOT NULL,
	productImage varchar(100),
	isActive char(1) NOT NULL
);

/* NEW TABLES */

create table accounting.stocks (
    stockID char(5) primary key,
    stockDate timestamp NOT NULL,
    productID char(5) references  accounting.products(productID),
    initialQuantity real,
    quantityLeft real,
    realQuantityLeft real,
    isUpdated char(1) NOT NULL,
    editDate timestamp NOT NULL
);

CREATE UNIQUE INDEX stock_index on accounting.stocks(date(stockDate), productID);

/* END NEW TABLES */

create table accounting.customers (
	customerID integer primary key,
	customerName varchar(200) NOT NULL,
	customerContact varchar(26),
	companyAffiliateID integer references accounting.companyAffiliates(companyAffiliateID)
);

create table accounting.orders (
	orderID integer primary key,
	orderDate timestamp NOT NULL,
	userID integer references accounting.users(userID),
	customerID integer references accounting.customers(customerID),
	placeID smallint references accounting.places(placeID)
);

create table accounting.payments (
    pointOfSaleID integer references accounting.pointOfSales(pointOfSaleID),
    orderID integer references accounting.orders(orderID),
    amountReceived numeric (14,2) NOT NULL,
    totalAmount numeric (14,2) NOT NULL,
    amountDue numeric (14,2)
);

create table accounting.invoices (
	invoiceID integer primary key,
	invoiceNumber varchar(10),
	ticketNumber smallint,
	orderID integer references accounting.orders(orderID)
);

create table accounting.invoiceDetails (
	invoiceID integer references accounting.invoices(invoiceID),
	productID char(5) references accounting.products(productID),
	quantity smallint NOT NULL,
	amount numeric(14, 2) NOT NULL,
	unique (invoiceID, productID)
);

/* VERY IMPORTANT: amount in invoiceDetails is equals to productPRICE * QUANTITY */

insert into accounting.users values (1, 'Aina');
insert into accounting.companies values (1, 'Lo Seqüent');
insert into accounting.companyAffiliates values (1, 1, 'Lo Seqüent');
insert into accounting.pointOfSales values (1, 1);
insert into accounting.customers values (1, 'client divers', '', 1);

insert into accounting.productCategories values (nextVal('accounting.categoryIDSeq'),'Boissons chaudes','SUC','hamburger.jpg',1, 'y');
insert into accounting.productCategories values (nextVal('accounting.categoryIDSeq'),'Hamburgers','SAL','hamburger.jpg',1, 'y');

insert into accounting.products values (nextVal('accounting.productsIDSeq'),1, 'Café au lait', 14000, 'cafe.png', 'y');
insert into accounting.products values (nextVal('accounting.productsIDSeq'),1, 'Thé vert', 14000, 'the-vert.jpg', 'y');

insert into accounting.products values (nextVal('accounting.productsIDSeq'),2, 'Double cheese burger', 20000, 'hamburger.jpg', 'y');
insert into accounting.products values (nextVal('accounting.productsIDSeq'),2, 'Burger poulet', 22000, 'hamburger.jpg', 'y');





/*
insert into accounting.productCategories values (nextVal('accounting.categoryIDSeq'),'Boissons chaudes','SUC','https://scontent.ftnr2-1.fna.fbcdn.net/v/t1.0-9/100791011_107989047602272_2665240044272353280_n.jpg?_nc_cat=107&_nc_sid=730e14&_nc_eui2=AeGSMh1MlT7-nSA_49-fe6BXWKrd1GbuPVNYqt3UZu49U3xCg5A1D8iZ3efmi9db18jwfVlH6aQgQtpZoW4tCZsn&_nc_ohc=_uIcE8dkUfEAX9Gl8uz&_nc_ht=scontent.ftnr2-1.fna&oh=d345c12e00445ff045803d96d6c3ad62&oe=5EF43532',1, 'y');
insert into accounting.productCategories values (nextVal('accounting.categoryIDSeq'),'Crêpes','SAL','https://scontent.ftnr2-1.fna.fbcdn.net/v/t1.0-9/101127919_107982640936246_3098087217730420736_n.jpg?_nc_cat=102&_nc_sid=730e14&_nc_eui2=AeHIwd2di1zjL2p2N1nNJFX7oD6d-gtMa_6gPp36C0xr_kWXIiJsOiSXrutd9SRBKJ_OWEvQCn3EMZGJOvjDEuAi&_nc_ohc=T6hDgZT3_VYAX_Q3zZL&_nc_ht=scontent.ftnr2-1.fna&oh=895b83e6a6cec44245ebc73dfdc1be09&oe=5EF63B80',1, 'y');
insert into accounting.productCategories values (nextVal('accounting.categoryIDSeq'),'Gauffres','SUC','https://scontent.ftnr2-1.fna.fbcdn.net/v/t1.0-9/101183659_107982644269579_4882917901864009728_n.jpg?_nc_cat=105&_nc_sid=730e14&_nc_eui2=AeHi8eicoLD9eZlsnu7mHXW_7byR0RzWn5vtvJHRHNafm1fkMbUn-XyrnyrMbAtr8_gwkwitzZ4Sh8BqKaK498Ah&_nc_ohc=cqbh_sbGDeUAX_f_vew&_nc_ht=scontent.ftnr2-1.fna&oh=7d64db3c80d54e603e01ce3636ae67ff&oe=5EF40301',1, 'y');
insert into accounting.productCategories values (nextVal('accounting.categoryIDSeq'),'Coupes de glaces','SUC','https://scontent.ftnr2-1.fna.fbcdn.net/v/t1.0-9/100851917_107982607602916_8670184887694655488_n.jpg?_nc_cat=106&_nc_sid=730e14&_nc_eui2=AeFfAcSnQhvCKSOVc_hEFSVXjOKg0lb5lYKM4qDSVvmVgsjOL9j1vcE8gNlW-d5oO2cBjpgZKtYSVD4iqtN7ccHX&_nc_ohc=gRwR9eNTcdAAX_AnGJ_&_nc_ht=scontent.ftnr2-1.fna&oh=170a4f9db21671d5a1a3bd7795c85e54&oe=5EF3D7CB',1, 'y');
insert into accounting.productCategories values (nextVal('accounting.categoryIDSeq'),'Boules de glaces','SUC','https://scontent.ftnr2-1.fna.fbcdn.net/v/t1.0-9/100851917_107982607602916_8670184887694655488_n.jpg?_nc_cat=106&_nc_sid=730e14&_nc_eui2=AeFfAcSnQhvCKSOVc_hEFSVXjOKg0lb5lYKM4qDSVvmVgsjOL9j1vcE8gNlW-d5oO2cBjpgZKtYSVD4iqtN7ccHX&_nc_ohc=gRwR9eNTcdAAX_AnGJ_&_nc_ht=scontent.ftnr2-1.fna&oh=170a4f9db21671d5a1a3bd7795c85e54&oe=5EF3D7CB',1, 'y');
insert into accounting.productCategories values (nextVal('accounting.categoryIDSeq'),'Rafraîchissement','SUC','https://scontent.ftnr2-1.fna.fbcdn.net/v/t1.0-9/100918699_107989040935606_2209241373591207936_n.jpg?_nc_cat=104&_nc_sid=730e14&_nc_eui2=AeEixSGhSmWSQRpF_62BmNUj9KxaE35NLZn0rFoTfk0tmc6GTLzOdHRyD2xCdJJRkQFzUupSO8tv2PfO9dhRDBEW&_nc_ohc=NVk2rgzpRgIAX_aCahe&_nc_ht=scontent.ftnr2-1.fna&oh=9d4b0cdabe129c5910b56226cc97f969&oe=5EF55AD4',1, 'y');
insert into accounting.productCategories values (nextVal('accounting.categoryIDSeq'),'crèpes','SUC','https://scontent.ftnr2-1.fna.fbcdn.net/v/t1.0-9/101127919_107982640936246_3098087217730420736_n.jpg?_nc_cat=102&_nc_sid=730e14&_nc_eui2=AeHIwd2di1zjL2p2N1nNJFX7oD6d-gtMa_6gPp36C0xr_kWXIiJsOiSXrutd9SRBKJ_OWEvQCn3EMZGJOvjDEuAi&_nc_ohc=T6hDgZT3_VYAX_Q3zZL&_nc_ht=scontent.ftnr2-1.fna&oh=895b83e6a6cec44245ebc73dfdc1be09&oe=5EF63B80',1, 'y');
insert into accounting.productCategories values (nextVal('accounting.categoryIDSeq'),'Panini','SAL','https://scontent.ftnr2-1.fna.fbcdn.net/v/t1.0-9/101332636_107982650936245_3193311534650490880_n.jpg?_nc_cat=107&_nc_sid=730e14&_nc_eui2=AeEIf3Nwt4wAMvCVmUWu69rgzgKHRppdjwPOAodGml2PA1mXJ4d_yJHAn4VEADJEL8ZaGAhc8Y2njrleWJsn71eN&_nc_ohc=m_cJtYZ-D-IAX_PGYmo&_nc_ht=scontent.ftnr2-1.fna&oh=b27f6f08693f9cf143335825f54cfbbb&oe=5EF484E3',1, 'y');
insert into accounting.productCategories values (nextVal('accounting.categoryIDSeq'),'Hamburger','SAL','https://scontent.ftnr2-1.fna.fbcdn.net/v/t1.0-9/101253449_107982674269576_6588242084149329920_n.jpg?_nc_cat=108&_nc_sid=730e14&_nc_eui2=AeEK4j0w_eFSSOKSP8TEd61UCYk9J1S1JqMJiT0nVLUmo1giDzaZ35xWQnnDiQ9sCnknK-J4noj__m8Zb5wMUO29&_nc_ohc=l0i5SqLdtb4AX-w5SUG&_nc_ht=scontent.ftnr2-1.fna&oh=aeb47f6015e630dbae2c11f8b4894b75&oe=5EF5B826',1, 'y');
insert into accounting.productCategories values (nextVal('accounting.categoryIDSeq'),'Divers','SAL','https://scontent.ftnr2-1.fna.fbcdn.net/v/t1.0-9/100882527_107982664269577_2460976463441035264_n.jpg?_nc_cat=101&_nc_sid=730e14&_nc_eui2=AeGWHfDo5l6e5MVdVK-hZYXyHTx6oGpRReIdPHqgalFF4t0wlFcAGyVofUeS4ul-9tF-Ht9kykmbGP8gECBIFJ3J&_nc_ohc=KWqVzTkVKwMAX-wfsZE&_nc_ht=scontent.ftnr2-1.fna&oh=a6c8104d309cf392655e7c3383180b13&oe=5EF610A6',1, 'y');

*/

insert into accounting.placeType values (1, 'chaise');
insert into accounting.placeType values (2, 'emporter');
insert into accounting.places values (1, 'empor', 1, 1);
insert into accounting.places values (2, '1', 1, 1);
insert into accounting.places values (3, '2', 1, 1);
insert into accounting.places values (4, '3', 1, 1);
insert into accounting.places values (5, '4', 1, 1);
insert into accounting.places values (6, '5', 1, 1);
insert into accounting.places values (7, '6', 2, 1);
insert into accounting.places values (8, '7', 2, 1);
insert into accounting.places values (9, '8', 2, 1);
insert into accounting.places values (10, '9', 2, 1);
insert into accounting.places values (11, '10', 2, 1);
insert into accounting.places values (12, '11', 2, 1);


insert into accounting.places values (13, '2b', 1, 1);
insert into accounting.places values (14, '3b', 1, 1);
insert into accounting.places values (15, '4b', 1, 1);
insert into accounting.places values (16, '5b', 1, 1);
insert into accounting.places values (17, '6b', 2, 1);
insert into accounting.places values (18, '7b', 2, 1);
insert into accounting.places values (19, '8b', 2, 1);
insert into accounting.places values (20, '9b', 2, 1);
insert into accounting.places values (21, '10b', 2, 1);
insert into accounting.places values (22, '11b', 2, 1);
insert into accounting.places values (23, '1b', 2, 1);




/* CA POUR UNE DATE DONNEE
create or replace view accounting.allInvoices as select accounting.invoices.* from accounting.invoices
where orderID in (select orderID from accounting.orders where orderDate::date = date '2020-02-27');
select sum(invoiceDetails.amount) as totalCA from allInvoices join invoiceDetails on invoiceDetails.invoiceID = allInvoices.invoiceID;
select invoiceDetails.productID sum( invoiceDetails.amount) as totalCAPerProduct from allInvoices join
   invoiceDetails on invoiceDetails.invoiceID = allInvoices.invoiceID group by invoiceDetails.productID;
*/

/* ALL ORDERS */

create or replace view accounting.allOrdersWithAmount as
select accounting.orders.orderID, accounting.invoices.invoiceID, accounting.invoices.ticketNumber,
 sum(accounting.invoiceDetails.amount) as totalAmount from accounting.orders join
accounting.invoices on accounting.invoices.orderID = accounting.orders.orderID
 join accounting.invoiceDetails on accounting.invoiceDetails.invoiceID = accounting.invoices.invoiceID
 group by accounting.orders.orderID, accounting.invoices.invoiceID;

create or replace view accounting.allUnpaidOrders as 
select accounting.orders.orderID, accounting.orders.orderDate, accounting.orders.placeID, accounting.allOrdersWithAmount.totalAmount,
accounting.allOrdersWithAmount.invoiceID, accounting.allOrdersWithAmount.ticketNumber
 from accounting.orders
join accounting.allOrdersWithAmount on accounting.allOrdersWithAmount.orderID =
 accounting.orders.orderID where  accounting.orders.orderid not in (select orderid from accounting.payments);

create or replace view accounting.allOrderedProducts as
select accounting.products.*, accounting.invoiceDetails.quantity, accounting.invoiceDetails.amount, accounting.invoiceDetails.invoiceID from
accounting.products join accounting.invoiceDetails on
accounting.invoiceDetails.productID = accounting.products.productID;
/*
DELETE FROM accounting.invoiceDetails;
delete from accounting.invoices;
delete from accounting.payments;
delete from accounting.orders;
*/
/* ALTER TABLES AINA */

/*placeID smallint references accounting.places(placeID)*/
/*
ALTER TABLE accounting.orders ADD COLUMN placeID smallint;
UPDATE accounting.orders SET placeID = 1;

ALTER TABLE accounting.orders ADD CONSTRAINT placesOrdersfk FOREIGN KEY (placeID) REFERENCES accounting.places (placeID) 
MATCH FULL;
*/

/* STATISTICS */

create or replace view accounting.totalOrders as
    select accounting.orders.orderID, extract (month from accounting.orders.orderDate) as month, extract (year from accounting.orders.orderDate) as year
    from accounting.orders;

create or replace view accounting.productOrdersWithDate as
    select accounting.invoiceDetails.amount, accounting.invoiceDetails.quantity, extract(month from accounting.orders.orderDate) as month,
           extract (year from accounting.orders.orderDate) as year
    from accounting.invoiceDetails
    join accounting.invoices on accounting.invoices.invoiceID = accounting.invoiceDetails.invoiceID
    join accounting.orders on accounting.orders.orderID = accounting.invoices.orderID;

create or replace view accounting.dailySales as
    select accounting.invoiceDetails.amount, accounting.invoiceDetails.quantity, accounting.orders.orderDate
    from accounting.invoiceDetails
     join accounting.invoices on accounting.invoices.invoiceID = accounting.invoiceDetails.invoiceID
     join accounting.orders on accounting.orders.orderID = accounting.invoices.orderID;

create table accounting.allMonths (
    monthID integer primary key,
    name varchar(10),
    abrev varchar(3)
);

create or replace view accounting.fullYearDays as
    select generate_series( (date '2021-01-01')::timestamp,
                            (date '2021-12-31')::timestamp,
                            interval '1 day'
                            ) as date;

create or replace view accounting.fullMonthDays as
    select extract(day from date) as day from accounting.fullYearDays where extract(month from date) = 11;

insert into accounting.allMonths values (1, 'janvier', 'jan');
insert into accounting.allMonths values (2, 'février', 'fev');
insert into accounting.allMonths values (3, 'mars', 'mar');
insert into accounting.allMonths values (4, 'avril', 'avr');
insert into accounting.allMonths values (5, 'mai', 'mai');
insert into accounting.allMonths values (6, 'juin', 'jui');
insert into accounting.allMonths values (7, 'juillet', 'jui');
insert into accounting.allMonths values (8, 'août', 'aou');
insert into accounting.allMonths values (9, 'septembre', 'sep');
insert into accounting.allMonths values (10, 'octobre', 'oct');
insert into accounting.allMonths values (11, 'novembre', 'nov');
insert into accounting.allMonths values (12, 'décembre', 'déc');



create or replace view accounting.totalOrdersSpecifiDate as
    select * from accounting.totalOrders where month = 5 and year = 2020;

create or replace view accounting.categorySalesDistribution as
    select sum(accounting.invoiceDetails.amount) as data, accounting.products.categoryID as index
    from accounting.totalOrdersSpecifiDate
    join accounting.invoices on accounting.invoices.orderID = accounting.totalOrdersSpecifiDate.orderID
    join accounting.invoiceDetails on accounting.invoiceDetails.invoiceID  = accounting.invoices.invoiceID
    join accounting.products on accounting.products.productID  = accounting.invoiceDetails.productID
    group by accounting.products.categoryID;

create or replace view accounting.top5SoldProducts as
select sum(accounting.invoiceDetails.amount) as data, accounting.products.productID as index
from accounting.totalOrdersSpecifiDate
     join accounting.invoices on accounting.invoices.orderID = accounting.totalOrdersSpecifiDate.orderID
     join accounting.invoiceDetails on accounting.invoiceDetails.invoiceID  = accounting.invoices.invoiceID
     join accounting.products on accounting.products.productID  = accounting.invoiceDetails.productID
    group by accounting.products.productID;
