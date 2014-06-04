-- 证件类别
INSERT INTO bc_cert_type(id, name, order_no, file_date, author_id, modified_date, modifier_id)
  select generate_series(1,2), UNNEST(ARRAY['司机证件','车辆证件']), generate_series(1,2)
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
  from bc_dual where not exists (select 0 from bc_cert_type where id in (select * from generate_series(1,2)));

-- 司机证件/身份证
INSERT INTO bc_cert_cfg(id, uid_, order_no, status_, type_id, name, page_count, width, combine 
	, tpl, file_date, author_id, modified_date, modifier_id)
  select 1, 'cert-cfg.1', '1001', 0, (select id from bc_cert_type where name = '司机证件')
	, '身份证', 2, 172, '1,1', null
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
  from bc_dual where not exists (select 0 from bc_cert_cfg where id = 1);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 11, 1, 1, '正面', 86
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 11);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 12, 1, 2, '背面', 86
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 12);

-- 司机证件/驾驶证
INSERT INTO bc_cert_cfg(id, uid_, order_no, status_, type_id, name, page_count, width, combine 
	, tpl, file_date, author_id, modified_date, modifier_id)
  select 2, 'cert-cfg.2', '1002', 0, (select id from bc_cert_type where name = '司机证件')
	, '驾驶证', 2, 182, '1,1', null
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
  from bc_dual where not exists (select 0 from bc_cert_cfg where id = 2);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 21, 2, 1, '正面', 91
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 21);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 22, 2, 2, '背面', 91
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 22);

-- 司机证件/从业资格证
INSERT INTO bc_cert_cfg(id, uid_, order_no, status_, type_id, name, page_count, width, combine 
	, tpl, file_date, author_id, modified_date, modifier_id)
  select 3, 'cert-cfg.3', '1003', 0, (select id from bc_cert_type where name = '司机证件')
	, '从业资格证', 1, 160, null, null
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
  from bc_dual where not exists (select 0 from bc_cert_cfg where id = 3);

-- 司机证件/户口簿
INSERT INTO bc_cert_cfg(id, uid_, order_no, status_, type_id, name, page_count, width, combine 
	, tpl, file_date, author_id, modified_date, modifier_id)
  select 4, 'cert-cfg.4', '1004', 0, (select id from bc_cert_type where name = '司机证件')
	, '户口簿', 3, 140, '1;1;1', null
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
  from bc_dual where not exists (select 0 from bc_cert_cfg where id = 4);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 41, 4, 1, '户主页', 140
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 41);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 42, 4, 2, '本人页', 140
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 42);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 43, 4, 3, '其他', 140
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 43);

-- 司机证件/服务资格证
INSERT INTO bc_cert_cfg(id, uid_, order_no, status_, type_id, name, page_count, width, combine 
	, tpl, file_date, author_id, modified_date, modifier_id)
  select 5, 'cert-cfg.5', '1005', 0, (select id from bc_cert_type where name = '司机证件')
	, '服务资格证', 1, 125, null, null
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
  from bc_dual where not exists (select 0 from bc_cert_cfg where id = 5);

-- 司机证件/居住证
INSERT INTO bc_cert_cfg(id, uid_, order_no, status_, type_id, name, page_count, width, combine 
	, tpl, file_date, author_id, modified_date, modifier_id)
  select 6, 'cert-cfg.6', '1006', 0, (select id from bc_cert_type where name = '司机证件')
	, '居住证', 2, 172, '1,1', null
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
  from bc_dual where not exists (select 0 from bc_cert_cfg where id = 6);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 61, 6, 1, '正面', 86
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 61);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 62, 6, 2, '背面', 86
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 62);

-- 司机证件/LPG证
INSERT INTO bc_cert_cfg(id, uid_, order_no, status_, type_id, name, page_count, width, combine 
	, tpl, file_date, author_id, modified_date, modifier_id)
  select 7, 'cert-cfg.7', '1007', 0, (select id from bc_cert_type where name = '司机证件')
	, 'LPG证', 1, 187, null, null
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
  from bc_dual where not exists (select 0 from bc_cert_cfg where id = 7);

-- 司机证件/计生证
INSERT INTO bc_cert_cfg(id, uid_, order_no, status_, type_id, name, page_count, width, combine 
	, tpl, file_date, author_id, modified_date, modifier_id)
  select 8, 'cert-cfg.8', '1008', 0, (select id from bc_cert_type where name = '司机证件')
	, '计生证', 3, 144, '1;1;1', null
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
  from bc_dual where not exists (select 0 from bc_cert_cfg where id = 8);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 81, 8, 1, '图片1', 144
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 81);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 82, 8, 2, '图片2', 144
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 82);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 83, 8, 3, '图片3', 144
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 83);

-- 车辆证件/机动车行驶证
INSERT INTO bc_cert_cfg(id, uid_, order_no, status_, type_id, name, page_count, width, combine 
	, tpl, file_date, author_id, modified_date, modifier_id)
  select 21, 'cert-cfg.21', '2001', 0, (select id from bc_cert_type where name = '车辆证件')
	, '机动车行驶证', 4, 172, '1,1;1,1', null
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
  from bc_dual where not exists (select 0 from bc_cert_cfg where id = 21);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 211, 21, 1, '有效期页', 86
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 211);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 212, 21, 2, '所有人页', 86
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 212);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 213, 21, 3, '空白填写页', 86
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 213);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 214, 21, 4, '车辆照片页', 86
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 214);

-- 车辆证件/车辆登记证
INSERT INTO bc_cert_cfg(id, uid_, order_no, status_, type_id, name, page_count, width, combine 
	, tpl, file_date, author_id, modified_date, modifier_id)
  select 22, 'cert-cfg.22', '2002', 0, (select id from bc_cert_type where name = '车辆证件')
	, '车辆登记证', 2, 200, '1;1', null
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
  from bc_dual where not exists (select 0 from bc_cert_cfg where id = 22);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 221, 22, 1, '正面', 86
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 221);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 222, 22, 2, '背面', 86
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 222);

-- 车辆证件/车辆购置税完税证明
INSERT INTO bc_cert_cfg(id, uid_, order_no, status_, type_id, name, page_count, width, combine 
	, tpl, file_date, author_id, modified_date, modifier_id)
  select 23, 'cert-cfg.23', '2003', 0, (select id from bc_cert_type where name = '车辆证件')
	, '车辆购置税完税证明', 1, 200, null, null
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
  from bc_dual where not exists (select 0 from bc_cert_cfg where id = 23);

-- 车辆证件/购置税发票
INSERT INTO bc_cert_cfg(id, uid_, order_no, status_, type_id, name, page_count, width, combine 
	, tpl, file_date, author_id, modified_date, modifier_id)
  select 24, 'cert-cfg.24', '2004', 0, (select id from bc_cert_type where name = '车辆证件')
	, '购置税发票', 1, 200, null, null
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
  from bc_dual where not exists (select 0 from bc_cert_cfg where id = 24);

-- 车辆证件/购车发票
INSERT INTO bc_cert_cfg(id, uid_, order_no, status_, type_id, name, page_count, width, combine 
	, tpl, file_date, author_id, modified_date, modifier_id)
  select 25, 'cert-cfg.25', '2005', 0, (select id from bc_cert_type where name = '车辆证件')
	, '购车发票', 1, 200, null, null
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
  from bc_dual where not exists (select 0 from bc_cert_cfg where id = 25);

-- 车辆证件/强检使用证
INSERT INTO bc_cert_cfg(id, uid_, order_no, status_, type_id, name, page_count, width, combine 
	, tpl, file_date, author_id, modified_date, modifier_id)
  select 26, 'cert-cfg.26', '2006', 0, (select id from bc_cert_type where name = '车辆证件')
	, '强检使用证', 1, 200, '1;1', null
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
  from bc_dual where not exists (select 0 from bc_cert_cfg where id = 26);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 261, 26, 1, '正面', 86
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 261);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 262, 26, 2, '背面', 86
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 262);

-- 车辆证件/道路运输证
INSERT INTO bc_cert_cfg(id, uid_, order_no, status_, type_id, name, page_count, width, combine 
	, tpl, file_date, author_id, modified_date, modifier_id)
  select 27, 'cert-cfg.27', '2007', 0, (select id from bc_cert_type where name = '车辆证件')
	, '道路运输证', 1, 200, '1;1', null
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
	, now(), (select id from bc_identity_actor_history where actor_code='admin' and current=true)
  from bc_dual where not exists (select 0 from bc_cert_cfg where id = 27);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 271, 27, 1, '正面', 86
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 271);
INSERT INTO bc_cert_cfg_detail(id, pid, page_no, name, width)
  select 272, 27, 2, '背面', 86
  from bc_dual where not exists (select 0 from bc_cert_cfg_detail where id = 272);


/*
select t.name, c.name, c.uid_, c.width, c.combine, d.name, d.*, c.* 
	from bc_cert_cfg c
	inner join bc_cert_type t on t.id = c.type_id
	left join bc_cert_cfg_detail d on c.id = d.pid
	order by t.order_no, c.order_no, d.page_no;
*/