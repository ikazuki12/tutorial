create table posting (
	id int auto_increment primary key,
	subject char(50) not null,
	text varchar(1000) not null,
	category char(10) not null,
	insert_date timestamp not null,
	user_id int not null
);