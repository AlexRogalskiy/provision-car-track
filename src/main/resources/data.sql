insert into ROLES(name) values ('ROLE_ADMIN');
insert into ROLES(name) values ('ROLE_USER');

INSERT INTO USERS (username, password, email, name) VALUES ('victor', '$2a$10$ZKKwseFDjOu8Xus.GFSwUOvC9Yhzb7tXkvK1DR6yAOOTbQdTYB6aK', 'victor.rumanski@gmail.com', 'Victor Rumanski');

INSERT INTO ENGINE_TYPEs (ID, engine_type_name) VALUES(1,'GASOLINE');


INSERT INTO MAKES(make_NAME) VALUES('LEXUS');
INSERT INTO MAKES(make_NAME) VALUES('AUDI');
INSERT INTO MAKES(make_NAME) VALUES('LINCOLN');

INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) values((select id from makes WHERE MAKE_NAME='LEXUS'), 'LFA500', 2010, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));
INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) values((select id from makes WHERE MAKE_NAME='LEXUS'), 'LS400', 1989, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));

INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) values((select id from makes WHERE MAKE_NAME='AUDI'), 'Q7', 2015, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));
INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) values((select id from makes WHERE MAKE_NAME='AUDI'), 'A8', 2011, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));
INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) values((select id from makes WHERE MAKE_NAME='AUDI'), 'A3', 2012, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));

INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) values((select id from makes WHERE MAKE_NAME='LINCOLN'), 'NAVIGATOR', 2019, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));
INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) values((select id from makes WHERE MAKE_NAME='LINCOLN'), 'AVIATOR', 2014, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));
INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) values((select id from makes WHERE MAKE_NAME='LINCOLN'), 'CONTINENTAL', 2017, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));

INSERT INTO ENGINE_TYPEs (ID, engine_type_name) VALUES(2, 'ELETRIC');

INSERT INTO MAKES(make_NAME) VALUES('TESLA');

INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) values((select id from makes WHERE MAKE_NAME='TESLA'), 'MODEL 3', 2019, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));
INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) values((select id from makes WHERE MAKE_NAME='TESLA'), 'MODEL X', 2020, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));

INSERT INTO vehicles(created_at, serial_number, user_created_by_id, model_id) VALUES (NOW(), 'ALB1234', (select max(id) from users), (select max(id) from MODELS WHERE MODEL_NAME='MODEL 3'));
INSERT INTO vehicles(created_at, serial_number, user_created_by_id, model_id) VALUES (NOW(), 'CKG2222', (select max(id) from users), (select max(id) from MODELS WHERE MODEL_NAME='NAVIGATOR'));

INSERT INTO reading_types( reading_name)	VALUES ('ODOMETER');
INSERT INTO reading_types( reading_name)	VALUES ('BATTERY DISCHARGE SPEED');



INSERT INTO task_types(allowed_engine_types, task_name)	VALUES ('{1}', 'OIL CHANGE');
INSERT INTO task_types(allowed_engine_types, task_name)	VALUES ('{1,2}', 'TIRE ROTATION');
INSERT INTO task_types(allowed_engine_types, task_name)	VALUES ('{2}', 'BATTERY REPLACEMENT');
	
	
-- now use the API to create Vehicles, Reading Types, Task Types then Readings and Tasks