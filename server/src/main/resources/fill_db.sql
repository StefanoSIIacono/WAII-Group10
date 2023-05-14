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
values('mariorossi@gmail.com', 'mario', 'rossi'),
      ('luigiverdi@gmail.com', 'luigi', 'verdi'),
      ('sergiobianchi@gmail.com', 'sergio', 'bianchi');

insert into expertises(id,field)
values(1,'WRONG-DELIVERY'),
      (2,'DAMAGED-PRODUCT'),
      (3, 'COMPUTER'),
      (4,'ELECTRONIC'),
      (5,'MECHANICAL'),
      (6,'ELECTRIC');

insert into experts(id,name,surname)
values(1,'Gino','Cuccagno'),
      (2,'Mohamed','Letija'),
      (3,'Pickle','Rick'),
      (4,'Pino','Paolino'),
      (5,'Mastro','Gesualdo');

insert into expert_expertise(expert_id, expertise_id)
values (1,2);

insert into managers(id,name,surname)
values(1,'BIG','BOSS');
