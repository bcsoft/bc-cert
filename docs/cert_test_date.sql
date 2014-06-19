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
	, (select id from bs_car where plate_no = '031RZ'), '粤A.031RZ机动车行驶证'
	, now(), (select id from bc_identity_actor_history where actor_code = 'admin' and current = true)
	, now(), (select id from bc_identity_actor_history where actor_code = 'admin' and current = true)
	from bc_dual where not exists (
		select 0 from bc_form where type_ = '车辆证件' and code = '机动车行驶证' and subject = '粤A.031RZ机动车行驶证'
	);
INSERT INTO bc_form(id, uid_, status_, tpl_, type_, code, pid, subject
	, file_date, author_id, modified_date, modifier_id)
	select NEXTVAL('hibernate_sequence'), 'cert.' || NEXTVAL('hibernate_sequence'), 0, 'TEST', '车辆证件', '道路运输证'
	, (select id from bs_car where plate_no = '031RZ'), '粤A.031RZ道路运输证'
	, now(), (select id from bc_identity_actor_history where actor_code = 'admin' and current = true)
	, now(), (select id from bc_identity_actor_history where actor_code = 'admin' and current = true)
	from bc_dual where not exists (
		select 0 from bc_form where type_ = '车辆证件' and code = '道路运输证' and subject = '粤A.031RZ道路运输证'
	);
-- ACL
INSERT INTO bc_acl_doc(id, doc_id, doc_type, doc_name, file_date, author_id, modified_date, modifier_id)
	select NEXTVAL('hibernate_sequence'), (select id from bc_cert_cfg where name = '道路运输证'), 'CertCfg', '道路运输证'
	, now(), (select id from bc_identity_actor_history where actor_code = 'admin' and current = true)
	, now(), (select id from bc_identity_actor_history where actor_code = 'admin' and current = true)
	from bc_dual where not exists (
		select 0 from bc_acl_doc where doc_type = 'CertCfg' 
		and doc_id = (select id::text from bc_cert_cfg where name = '道路运输证')
	);
-- ACL: admin 可查阅道路运输证
INSERT INTO bc_acl_actor(pid, aid, role, order_)
	select (
		select id from bc_acl_doc where doc_type = 'CertCfg' 
			and doc_id = (select id::text from bc_cert_cfg where name = '道路运输证')
	)
	, (select id from bc_identity_actor where code = 'admin'), '10', 1
	from bc_dual where not exists (
		select 0 from bc_acl_actor 
		where pid = (
			select id from bc_acl_doc where doc_type = 'CertCfg' 
				and doc_id = (select id::text from bc_cert_cfg where name = '道路运输证')
		)
		and aid = (select id from bc_identity_actor where code = 'admin')
	);
-- ACL: dragon 不可查阅道路运输证
INSERT INTO bc_acl_actor(pid, aid, role, order_)
	select (
		select id from bc_acl_doc where doc_type = 'CertCfg' 
			and doc_id = (select id::text from bc_cert_cfg where name = '道路运输证')
	)
	, (select id from bc_identity_actor where code = 'dragon'), '00', 1
	from bc_dual where not exists (
		select 0 from bc_acl_actor 
		where pid = (
			select id from bc_acl_doc where doc_type = 'CertCfg' 
				and doc_id = (select id::text from bc_cert_cfg where name = '道路运输证')
		)
		and aid = (select id from bc_identity_actor where code = 'dragon')
	);
