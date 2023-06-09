insert into products(id, name, brand)
values(9780702305511,'Welsh Fairy Tales, Myths and Legends by Claire Fayers (Paperback, 2021)','Scholastic'),
      (9781292204482,'Accounting and Finance: An Introduction 9th edition by Peter Atrill, Eddie McLaney (Paperback, 2018)','Pearson Education The Limited'),
      (9781788307017,'Vegans Deserve Better than a Fruit Salad by Danielle Maupertuis (Paperback, 2020)','Olympia Publishers'),
      (9781942275732,'The Black Sable by Joe Brusha (Paperback, 2018)','Zenescope Entertainment'),
      (9781941610428,'The Prophetic Ethics and the Courtesies of Living by Al-Ghazali (Paperback, 2019)','Fons Vitae,US'),
      (9781617137082,'My Years with Townes Van Zandt: Music, Genius and Rage by Harold F. Eggers (Hardcover, 2018)', 'Hal Leonard Corporation'),
      (9781617137080,'My Years with Townes Van Zandt: Music, Genius and Rage by Harold F. Eggers (Hardcover, 2018)', 'Hal Leonard Corporation'),
      (9781617137083,'My Years with Townes Van Zandt: Music, Genius and Rage by Harold F. Eggers (Hardcover, 2018)', 'Hal Leonard Corporation'),
      (9781617137084,'My Years with Townes Van Zandt: Music, Genius and Rage by Harold F. Eggers (Hardcover, 2018)', 'Hal Leonard Corporation'),
      (5707286436169,'Nissens 95601 Dryer Air Conditioning OE Replacement Top Quality','Nissens'),
      (9786420945091,'MERCEDES CLS 320 CDI C219 2007 RHD Air Intake Hose Pipe Tube A6420945097','Mercedes-Benz'),
      (5030917124013,'PlayStation 3 Ps3 Destiny Vanguard Edition UK IMPORT a VideoGames','Sony');

insert into profiles(email, name, surname)
values('mario.rossi@gmail.com', 'mario', 'rossi'),
      ('luigi.verdi@gmail.com', 'luigi', 'verdi'),
      ('sergio.bianchi@gmail.com', 'sergio', 'bianchi');

insert into expertises(id,field)
values(1,'WRONG-DELIVERY'),
      (2,'DAMAGED-PRODUCT'),
      (3, 'COMPUTER'),
      (4,'ELECTRONIC'),
      (5,'MECHANICAL'),
      (6,'ELECTRIC');

insert into experts(email,name,surname)
values('gino.cuccagno@tickets.com','Gino','Cuccagno'),
      ('mohamed.letija@tickets.com','Mohamed','Letija'),
      ('pickle.rick@tickets.com','Pickle','Rick'),
      ('pino.paolino@tickets.com','Pino','Paolino'),
      ('mastro.gesualdo@tickets.com','Mastro','Gesualdo');

insert into expert_expertise(expert_email, expertise_id)
values('gino.cuccagno@tickets.com',2),
      ('mohamed.letija@tickets.com',3),
      ('pino.paolino@tickets.com',4),
      ('mastro.gesualdo@tickets.com',1);

insert into managers(email,name,surname)
values('bigboss@tickets.admin.com','BIG','BOSS');

insert into addresses(address_id, city, country, house_number, street, zip_code, profile_email)
values(1, 'turin', 'italy', 12, 'via vigone', 10138, 'mario.rossi@gmail.com'),
      (2, 'narnia', 'italy', 2, 'via narnia', 98172, 'luigi.verdi@gmail.com'),
      (3, 'scampia', 'italy', 18, 'via mun', 12121, 'sergio.bianchi@gmail.com');

INSERT INTO tickets (id, arg_id, obj, priority, expert_email, product_id, profile_email)
values (1, 1, 'obj', 'LOW', 'mastro.gesualdo@tickets.com', 9781292204482, 'mario.rossi@gmail.com'),
       (2, 3, 'i need help', 'HIGH', 'mohamed.letija@tickets.com', 9786420945091, 'mario.rossi@gmail.com'),
       (3, 4, 'you ruined my birthday', 'MEDIUM', 'pino.paolino@tickets.com', 9781617137080, 'luigi.verdi@gmail.com');

INSERT INTO statuses (id, status, status_changer, timestamp, expert_email, ticket_id)
values  (1, 1, 'MANAGER', '1999-01-08 01:05:06', 'mastro.gesualdo@tickets.com', 1),
        (2, 2, 'EXPERT', '2000-02-18 05:25:46', 'mohamed.letija@tickets.com', 2),
        (3, 3, 'PROFILE', '2006-06-13 22:21:46', 'pino.paolino@tickets.com', 3)