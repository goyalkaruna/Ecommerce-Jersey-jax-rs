show tables;
select * from userlogin;
show tables;
select * from organization;
select * from orders;
select * from orderstatus;
select * from inventory;
drop table inventory;
create database cmpe273;
use cmpe273;
drop table userlogin;
create table userlogin(username varchar(40), pwd varchar(40), primary key(username));
create table inventory(id varchar(40), quantity int);
insert into inventory values ("Trendent", 6);
insert into inventory values ("Belkin", 1);
insert into inventory values ("Mercedes", 5);
insert into inventory values ("Honda", 10);
insert into inventory values ("Netgear", 5);
insert into inventory values ("Cisco", 10);
insert into inventory values ("Lambhorgini", 5);
insert into inventory values ("Ferrari", 10);

insert into inventory values ('learningsingle quotesinsertion',20);

create table orders(id int, username varchar(40), productid varchar(40), productname varchar(40), cost varchar(10),
 date timestamp , apartment varchar(30), street varchar(20),city varchar(20), state varchar(20), creditcard varchar(30) , 
primary key(id));
create table orderstatus (orderid int, date timestamp, orderstatus varchar(20), primary key(orderid));
insert into orders values(2510 ,"karu","Mercedes", "Mercedes","89,999$", now(),"123", "3rd Street", "SF","CA", "3242354235524");
drop table orders;
drop table orderstatus;
insert into inventory values("Mercedes", 8);
insert into orderstatus values(2510, "2014-11-21 03:42:42", "In process");


insert into userlogin values ("root", SHA1("root"));
delete from userlogin where username="karu";
insert into userlogin values ("karu", "karu");
insert into userlogin values ("karu1", SHA1("karu"));
insert into userlogin values ("admin", "admin");
select * from userlogin;
select * from orders order by id desc;
select * from orderstatus;
select * from inventory;
desc orders;
select * from userlogin;
desc orderstatus;

update orderstatus set orderstatus = 'cancelled' where orderid= '1215';
select * from orders where username = 'karu1'

SELECT @@global.time_zone, @@session.time_zone;