INSERT INTO ENGINE_TYPEs (engine_type_name) VALUES('GASOLINE');

INSERT INTO MAKES(make_NAME) VALUES('LEXUS');

INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) values((select max(id) from makes), 'LFA500', 2010, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));
INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) values((select max(id) from makes), 'LS400', 1989, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));

INSERT INTO reading_types(reading_name)	VALUES ('ODOMETER');
INSERT INTO reading_types(reading_name)	VALUES ('TIRE THREAD WEAR');
INSERT INTO reading_types(reading_name)	VALUES ('OIL LEVEL');
INSERT INTO reading_types(reading_name)	VALUES ('BATTERY DISCHARGE SPEED');


INSERT INTO vehicles(model_id, serial_number, user_created_by_id, created_at) VALUES ((select min(id) from models), 'ALB1234', (select max(id) from users), NOW());
INSERT INTO vehicles(model_id, serial_number, user_created_by_id, created_at) VALUES ((select max(id) from models), 'RED1234', (select max(id) from users), NOW());