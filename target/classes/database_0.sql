/*create user finalcompta login password '123456';
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
create sequence IF NOT EXISTS accounting.documentIDSeq;
create sequence IF NOT EXISTS accounting.transactionIDSeq;
create sequence IF NOT EXISTS accounting.ledgerIDSeq;
create sequence IF NOT EXISTS accounting.invoiceIDSeq;

/* SAL SUC */

/* NEW TABLES */
create table accounting.companies (
	companyID integer,

);

create table accounting.companyAffiliates (
	companyAffiliateID integer,
	companyID integer references accounting.companies(companyID),
);

create table accounting.pointOfSales (
	pointOfSaleID integer,
	companyAffiliateID integer references accounting.companyAffiliates(companyAffiliateID)
);

create table accounting.pointOfSaleMoves (
	pointOfSaleID integer references accounting.pointOfSales(pointOfSaleID),
	dateUsage date NOT NULL,
	cashFund numeric(14, 2) NOT NULL,
	-- WHAT ABOUT CASH INVENTORY WHERE SHOULD YOU PUT IT? IN TRANSACTION OR IN A FIELD OF THIS TABLE
);

/* */

CREATE TYPE accounting.tastes AS ENUM ('SAL', 'SUC');

create table accounting.productCategories (
	categoryID char(2) primary key,
	categoryName varchar(100) NOT NULL,
	categoryTaste accounting.tastes,
	categoryImage varchar(100),
	companyAffiliateID integer references accounting.companyAffiliates(companyAffiliateID)
);

create table accounting.products (
	productID char(5) primary key,
	categoryID char(2) references accounting.productCategories(categoryID),
	productName varchar(200) NOT NULL,
	price numeric(14, 2) NOT NULL,
	productImage varchar(100),
	companyAffiliateID integer references accounting.companyAffiliates(companyAffiliateID)
);

create table accounting.officialAccounts (
	accountType smallint primary key,
	accountName varchar(20) NOT NULL,
	companyAffiliateID integer references accounting.companyAffiliates(companyAffiliateID)
);
	
create table accounting.accounts (
	accountCode varchar(10) primary key,
	accountType smallint references accounting.officialAccounts(accountType),
	companyAffiliateID integer references accounting.companyAffiliates(companyAffiliateID)
);

create table accounting.customers (
	accountCode varchar(10) references accounting.accounts(accountCode),
	customerName varchar(200) NOT NULL,
	customerContact varchar(26),
	companyAffiliateID integer references accounting.companyAffiliates(companyAffiliateID)
);

create table accounting.documentTypes (
	documentTypeID smallint  primary key,
	documentTypeName varchar(25) NOT NULL
);

create table accounting.documents (
	documentID integer primary key,
	documentDate date NOT NULL,
	documentDescription varchar(50),
	documentNo varchar(10) NOT NULL,
	documentTypeID smallint references accounting.documentTypes(documentTypeID)
);

create table accounting.orders (
	orderID integer primary key,
	documentID  integer references accounting.documents(documentID),
	accountCode varchar(10) references accounting.accounts(accountCode)
);

create table accounting.transactions (
	transactionID integer primary key,
	documentID integer references accounting.documents(documentID),
	transactionDate timestamp NOT NULL
);

CREATE TYPE accounting.entryTypes AS ENUM ('D', 'C');
create table accounting.ledger (
	ledgerID integer primary key,
	transactionID integer references accounting.transactions(transactionID),
	accountCode varchar(10) references accounting.accounts(accountCode),
	entryType accounting.entryTypes NOT NULL,
	amount numeric(14, 2) NOT NULL
);

create table accounting.invoices (
	invoiceID integer primary key,
	documentID integer references accounting.documents(documentID),
	accountCode varchar(10) references accounting.accounts(accountCode)
);

create table accounting.invoiceDetails (
	invoiceID integer references accounting.invoices(invoiceID),
	productID char(5) references accounting.products(productID),
	quantity smallint NOT NULL,
	amount numeric(14, 2) NOT NULL
);


/* List products */
-- insert into accounting.productCategories values (1, 'crêpe', 'SAL', 'crepe.jpg');
-- insert into accounting.productCategories values (2, 'crêpe', 'SUC', 'crepe.jpg');
-- insert into accounting.productCategories values (3, 'hamburger', 'SAL', 'hamburger.jpg');

-- /* Chart of accounts and customers */
-- insert into accounting.products values (1, 1, 'crêpe au fromage', 10000, 'crepe.jpg');
-- insert into accounting.products values (2, 2, 'crêpe au chocolat', 15000, 'crepe.jpg');
-- insert into accounting.products values (3, 3, 'double cheese burger', 20000, 'crepe.jpg');

-- insert into accounting.officialAccounts values (4, 'tiers');
-- insert into accounting.officialAccounts values (5, 'trésorerie');
-- insert into accounting.officialAccounts values (7, 'produits');

-- insert into accounting.accounts values ('411', 4);
-- insert into accounting.accounts values ('411001', 4);
-- insert into accounting.accounts values ('53', 5);
-- insert into accounting.accounts values ('707', 7);

-- insert into accounting.customers values ('411001', 'baba', NULL);

-- /* MAKE A TRANSATION */
-- insert into accounting.documentTypes values (1, 'facture');
-- insert into accounting.documents values (1, '2020-02-20', 'Vente crêpe', 'JAN01/01', 1);
-- insert into accounting.transactions values (1, 1, '2020-02-20 13:09');
-- insert into accounting.ledger values (1, 1, '53', 'D', 30000);
-- insert into accounting.ledger values (1, 1, '707', 'C', 30000);
-- insert into accounting.invoices values (1, 1, '411');
-- insert into accounting.invoiceDetails values (1, 2, 1, 15000);

-- create sequence accounting.ticketNumber;
/*
select nextVal('accounting.productsIDSeq');
select nextVal('accounting.productsIDSeq');
select nextVal('accounting.productsIDSeq');
select nextVal('accounting.categoryIDSeq');
select nextVal('accounting.categoryIDSeq');
select nextVal('accounting.categoryIDSeq');*/


 */
