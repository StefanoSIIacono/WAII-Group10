drop table if exists products;

create table if not exists products(
    id varchar(15) primary key,
    name varchar(255),
    brand varchar(255)
);

insert into products(id, name, brand)
values(9780702305511,'Welsh Fairy Tales, Myths and Legends by Claire Fayers (Paperback, 2021)','Scholastic'),
    (9781292204482,'Accounting and Finance: An Introduction 9th edition by Peter Atrill, Eddie McLaney (Paperback, 2018)','Pearson Education The Limited'),
    (9781788307017,'Vegans Deserve Better than a Fruit Salad by Danielle Maupertuis (Paperback, 2020)','Olympia Publishers'),
    (9781942275732,'The Black Sable by Joe Brusha (Paperback, 2018)','Zenescope Entertainment'),
    (9781941610428,'The Prophetic Ethics and the Courtesies of Living by Al-Ghazali (Paperback, 2019)','Fons Vitae,US'),
    (9781617137082,'My Years with Townes Van Zandt: Music, Genius and Rage by Harold F. Eggers (Hardcover, 2018)', 'Hal Leonard Corporation');