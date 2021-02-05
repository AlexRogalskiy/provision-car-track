INSERT INTO ENGINE_TYPEs (engine_type_name) VALUES('GASOLINE');

INSERT INTO MAKES(make_NAME) VALUES('LEXUS');
INSERT INTO MAKES(make_NAME) VALUES('AUDI');
INSERT INTO MAKES(make_NAME) VALUES('LINCOLN');

INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) 
	values((select id from makes WHERE MAKE_NAME='LEXUS'), 'LFA500', 2010, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));
INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) 
	values((select id from makes WHERE MAKE_NAME='LEXUS'), 'LS400', 1989, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));

INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) 
	values((select id from makes WHERE MAKE_NAME='AUDI'), 'Q7', 2015, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));
INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) 
	values((select id from makes WHERE MAKE_NAME='AUDI'), 'A8', 2011, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));
INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) 
	values((select id from makes WHERE MAKE_NAME='AUDI'), 'A3', 2012, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));

INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) 
	values((select id from makes WHERE MAKE_NAME='LINCOLN'), 'NAVIGATOR', 2019, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));
INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) 
	values((select id from makes WHERE MAKE_NAME='LINCOLN'), 'AVIATOR', 2014, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));
INSERT INTO models(make_id, model_name, model_year, user_created_by_id, created_at, engine_type_id) 
	values((select id from makes WHERE MAKE_NAME='LINCOLN'), 'CONTINENTAL', 2017, (select max(id) from users), now(), (select max(id) from ENGINE_TYPEs));