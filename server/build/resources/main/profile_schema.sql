drop table if exists profiles;

create table if not exists profiles(
    email varchar(31) primary key,
    name varchar(255),
    surname varchar(255)
    );

insert into profiles(email, name, surname)
values('mariorossi@gmail.com', 'mario', 'rossi'),
      ('luigiverdi@gmail.com', 'luigi', 'verdi'),
      ('sergiobianchi@gmail.com', 'sergio', 'bianchi');
