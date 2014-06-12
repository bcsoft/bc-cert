-- 插入司机证件测试数据
INSERT INTO bc_form(id, uid_, status_, tpl_, type_, code, pid, subject
	, file_date, author_id, modified_date, modifier_id)
	select NEXTVAL('hibernate_sequence'), 'cert.' || NEXTVAL('hibernate_sequence'), 0, 'TEST', '司机证件', '身份证'
	, (select id from bs_temp_driver where name = '余岳辉'), '余岳辉身份证'
	, now(), (select id from bc_identity_actor_history where actor_code = 'admin' and current = true)
	, now(), (select id from bc_identity_actor_history where actor_code = 'admin' and current = true)
	from bc_dual where not exists (
		select 0 from bc_form where type_ = '司机证件' and code = '身份证' and subject = '余岳辉身份证'
	);

-- 插入车辆证件测试数据
INSERT INTO bc_form(id, uid_, status_, tpl_, type_, code, pid, subject
	, file_date, author_id, modified_date, modifier_id)
	select NEXTVAL('hibernate_sequence'), 'cert.' || NEXTVAL('hibernate_sequence'), 0, 'TEST', '车辆证件', '机动车行驶证'
	, (select id from bs_car where plate_no = '3DD47'), '粤A.3DD47机动车行驶证'
	, now(), (select id from bc_identity_actor_history where actor_code = 'admin' and current = true)
	, now(), (select id from bc_identity_actor_history where actor_code = 'admin' and current = true)
	from bc_dual where not exists (
		select 0 from bc_form where type_ = '车辆证件' and code = '机动车行驶证' and subject = '粤A.3DD47机动车行驶证'
	);