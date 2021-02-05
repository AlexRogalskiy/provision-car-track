INSERT INTO ENGINE_TYPEs (id, engine_type_name) VALUES(1, 'GASOLINE');
INSERT INTO ENGINE_TYPEs (id, engine_type_name) VALUES(2, 'ELETRIC');

INSERT INTO MAKES(id,make_NAME) VALUES(1,'LEXUS');
INSERT INTO MAKES(id,make_NAME) VALUES(2,'TESLA');

INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) values(1, 'LFA500', 2010, (select max(id) from users), now(), 1);
INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) values(1, 'LS400', 1989, (select max(id) from users), now(), 1);

INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) values(2, 'MODEL 3', 2020, (select max(id) from users), now(), 2);
INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) values(2, 'MODEL 3', 2019, (select max(id) from users), now(), 2);

INSERT INTO vehicles(model_id, serial_number, user_created_by_id, created_at) VALUES ((select max(id) from models where model_name='LFA500'), 'ALB1234', (select max(id) from users), NOW());
INSERT INTO vehicles(model_id, serial_number, user_created_by_id, created_at) VALUES ((select max(id) from models where model_name='MODEL 3'), 'RED1234', (select max(id) from users), NOW());

INSERT INTO task_types(allowed_engine_types, task_name)	VALUES ('{1}', 'OIL CHANGE');
INSERT INTO task_types(allowed_engine_types, task_name)	VALUES ('{1,2}', 'TIRE ROTATION');
INSERT INTO task_types(allowed_engine_types, task_name)	VALUES ('{2}', 'BATTERY REPLACEMENT');
	
	