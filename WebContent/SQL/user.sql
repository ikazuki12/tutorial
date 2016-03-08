create table user (
	num int auto_increment primary key,
	id char(20) not null,
	password char(255) not null,
	name char(10) not null,
	branch_code int not null,
	Department int not null
);