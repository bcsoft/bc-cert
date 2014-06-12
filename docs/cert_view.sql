/** 证件视图 SQL
 * > bc_form 表字段值设计：
 * 		pid 			= [businessTable.id]
 * 		type_ 		= '[bc_cert_type.name]'
 * 		code 			= '[bc_cert_cfg.name]'
 * > bc_acl_doc 表字段值设计：
 * 		doc_id 		= '[bc_cert_cfg.id]'
 * 		doc_type 	= 'CertCfg'
 * 		doc_name 	= '[bc_cert_cfg.name]'
 */

select t.name, c.name, f.subject, f.* 
	from bc_cert_cfg c
	inner join bc_cert_type t on t.id = c.type_id
	inner join bc_form f on f.type_ = t.name and f.code = c.name
	--where
	order by f.file_date desc

/*
SELECT * FROM bc_acl_doc;
SELECT * FROM bc_form;
-- 插入一些测试数据
INSERT INTO bc_form(id, uid_, status_, tpl_, type_, code, pid, subject
	, file_date, author_id, modified_date, modifier_id)
	select NEXTVAL('hibernate_sequence'), 'cert.' || NEXTVAL('hibernate_sequence'), 0, 'TEST', '司机证件', '身份证'
	, (select id from bs_temp_driver where name = '余岳辉'), '余岳辉身份证'
	, now(), (select id from bc_identity_actor_history where actor_code = 'admin' and current = true)
	, now(), (select id from bc_identity_actor_history where actor_code = 'admin' and current = true)
	from bc_dual where not exists (
		select 0 from bc_form where type_ = '司机证件' and code = '身份证' and subject = '余岳辉身份证'
	);
INSERT INTO bc_form(id, uid_, status_, tpl_, type_, code, pid, subject
	, file_date, author_id, modified_date, modifier_id)
	select NEXTVAL('hibernate_sequence'), 'cert.' || NEXTVAL('hibernate_sequence'), 0, 'TEST', '车辆证件', '机动车行驶证'
	, (select id from bs_car where plate_no = '3DD47'), '粤A.3DD47机动车行驶证'
	, now(), (select id from bc_identity_actor_history where actor_code = 'admin' and current = true)
	, now(), (select id from bc_identity_actor_history where actor_code = 'admin' and current = true)
	from bc_dual where not exists (
		select 0 from bc_form where type_ = '车辆证件' and code = '机动车行驶证' and subject = '粤A.3DD47机动车行驶证'
	);
*/