create table comment (
	id int auto_increment primary key,
	text varchar(500) not null,
	insert_date timestamp not null,
	user_id int not null
);