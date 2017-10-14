drop table LesMaladies ;
drop table LesAnimaux ;
drop table LesResponsables ;
drop table LesGardiens ;
drop table LesSpecialites;
drop table LesEMployes ;
drop table LesCages ;
drop table LesHistoiresAff ;

create table LesCages (
	noCage number(3),
	fonction varchar2(20),
	noAllee number(3) not null,
	constraint LesCages_C1 primary key (noCage),
	constraint LesCages_C2 check (noCage between 1 and 999),
	constraint LesCages_C3 check (noAllee between 1 and 999)
);

create table LesEmployes (
	nomE varchar2(20),
	adresse varchar2(20),
	constraint LesEmployes_C1 primary key (nomE)
);

create table LesSpecialites (
	nomE varchar2(20),
	fonction_cage varchar2(20),
	constraint LesSpecialites_C1 primary key (nomE, fonction_cage),
	constraint LesSpecialites_C2 foreign key (nomE) references LesEmployes (nomE)
);

create table LesGardiens (
	noCage number(3),
	nomE varchar2(20),
	constraint LesGardiens_C1 primary key (noCage, nomE),
	constraint LesGardiens_C2 foreign key (nomE) references LesEmployes (nomE),
	constraint LesGardiens_C3 foreign key (noCage) references LesCages (noCage)
);

create table LesResponsables (
	noAllee number(3),
	nomE varchar2(20),
	constraint LesResponsables_C1 primary key (noAllee),
	constraint LesResponsables_C2 foreign key (nomE) references LesEmployes (nomE)
);
		
create table LesAnimaux (
	nomA varchar2(20),
	sexe varchar2(13),
	type_an varchar2(15),
	fonction_cage varchar2(20),
	pays varchar2(20),
	anNais number(4),
	noCage number(3) not null,
	nb_maladies number(3),
	constraint LesAnimaux_C1 primary key (nomA),
	constraint LesAnimaux_C2 check (sexe in ('femelle','male','hermaphrodite')),
	constraint LesAnimaux_C3 check (anNais >= 1900)
);

create table LesMaladies (
	nomA varchar2(20),
	nomM varchar2(20),
	constraint LesMaladies_C1 primary key (nomA,nomM),
	constraint LesMaladies_C2 foreign key (nomA) references LesAnimaux (nomA) on delete cascade
);

create table LesHistoiresAff (
	dateFin date,
	noCage number(3),
	nomE varchar2(20),
	constraint LesHistoiresAff_C1 primary key (dateFin, noCage, nomE)
);

insert into LesCages values (11 ,  'fauve'           , 10 );
insert into LesCages values (1     , 'fosse'         , 1 );
insert into LesCages values (2     , 'aquarium'      , 1 );
insert into LesCages values (3     , 'petits oiseaux'        , 2 );
insert into LesCages values (4     , 'grand aquarium'        , 1 );
insert into LesCages values (12     , 'fauve'           , 10);



insert into LesEmployes values ('Verdier'  ,       'Noumea' );
insert into LesEmployes values ('Spinnard'  ,       'Sartene' );
insert into LesEmployes values ('Labbe' ,    'Calvi' );
insert into LesEmployes values ('Lachaize' ,       'Pointe a Pitre' );
insert into LesEmployes values ('Desmoulins'  , 'Ushuaia' );
insert into LesEmployes values ('Jouanot'   , 'Papeete' );

insert into LesSpecialites values ('Verdier'  ,       'fauve' );
insert into LesSpecialites values ('Spinnard'  ,       'fauve' );
insert into LesSpecialites values ('Labbe' ,    'fauve' );
insert into LesSpecialites values ('Lachaize' ,       'fauve' );
insert into LesSpecialites values ('Lachaize' ,       'fosse' );
insert into LesSpecialites values ('Lachaize' ,       'petis oiseaux' );
insert into LesSpecialites values ('Desmoulins'  , 'fauve' );
insert into LesSpecialites values ('Desmoulins'  , 'fosse' );
insert into LesSpecialites values ('Desmoulins'  , 'petits oiseaux' );
insert into LesSpecialites values ('Jouanot'   , 'fosse' );
insert into LesSpecialites values ('Jouanot' ,       'aquarium' );
insert into LesSpecialites values ('Jouanot' ,       'gand aquarium' );


insert into LesResponsables values (10      ,       'Verdier' );
insert into LesResponsables values (1       ,       'Jouanot' );
insert into LesResponsables values (2       ,       'Desmoulins');


insert into LesGardiens values (11      ,       'Lachaize' );
insert into LesGardiens values (12      ,       'Spinnard' );
insert into LesGardiens values (12      ,       'Labbe' );
insert into LesGardiens values (11      ,       'Spinnard' );
insert into LesGardiens values (11      ,       'Labbe' );
insert into LesGardiens values (1       ,       'Lachaize' );
insert into LesGardiens values (3       ,       'Lachaize' );
insert into LesGardiens values (12      ,       'Lachaize' );

insert into LesAnimaux values ('Charly', 'male',   'lion', 'fauve',  'Kenya',  2010,   12,2);
insert into LesAnimaux values ('Arthur', 'male',   'ours', 'fosse',  'France', 2000,   1,0 );
insert into LesAnimaux values ('Chloe',  'femelle', 'pie', 'petits oiseaux' ,  'France', 2011,   3,1 );
insert into LesAnimaux values ('Milou',  'male' ,  'leopard', 'fauve', 'France', 2013,  11,1 );
insert into LesAnimaux values ('Tintin', 'male' , 'leopard', 'fauve', 'France', 2013,    11,0 );
insert into LesAnimaux values ('Charlotte', 'femelle', 'lion',  'fauve',  'Kenya',  2012,      12,0 );

insert into LesMaladies values ('Charly'        , 'rage de dents' );
insert into LesMaladies values ('Charly'        , 'grippe' );
insert into LesMaladies values ('Milou'         , 'angine' );
insert into LesMaladies values ('Chloe'         , 'grippe' );
