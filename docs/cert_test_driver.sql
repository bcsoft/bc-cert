-- 司机证件：43ms
-- bc_form 表字段值设计：
-- 		pid = bs_temp_driver.id
-- 		type_ = '[bc_cert_type.name]'
-- 		code = '[bc_cert_cfg.name]'

/* 高性能查询说明：
 * 将存储函数的查询放到分页外，从而保证只查询25次来提高性能
 * 注：排序及查询条件必须放在内层查询中，否则分页结果就不正确了
 */
with man_cert(id, order_no, tname, cname) as (
	select c.id, c.order_no, t.name, c.name 
		from bc_cert_cfg c
		inner join bc_cert_type t on t.id = c.type_id
		where t.name = '司机证件'
) 
select mc.*
	-- 查询函数
	,get_formfieldvalue_byfromparem_fieldkey(mc.mid, mc.tname, mc.cname,'attach_width') attach_width
	,get_formfieldvalue_byfromparem_fieldkey(mc.mid, mc.tname, mc.cname,'attach_width') attach_width
	,get_formfieldvalue_byfromparem_fieldkey(mc.mid, mc.tname, mc.cname,'attach_id_a') attach_id_a
	,get_formfieldvalue_byfromparem_fieldkey(mc.mid, mc.tname, mc.cname,'attach_width_a') attach_width_a
	,get_formfieldvalue_byfromparem_fieldkey(mc.mid, mc.tname, mc.cname,'attach_id_b') attach_id_b
	,get_formfieldvalue_byfromparem_fieldkey(mc.mid, mc.tname, mc.cname,'attach_width_b') attach_width_b
	,get_formfieldvalue_byfromparem_fieldkey(mc.mid, mc.tname, mc.cname,'attach_id_c') attach_id_c
	,get_formfieldvalue_byfromparem_fieldkey(mc.mid, mc.tname, mc.cname,'attach_width_c') attach_width_c 
  from (
	select m.id mid, m.name mname, c.tname tname, c.cname cname, c.order_no order_no, c.id cid, m.file_date file_date
		from man_cert c, bs_carman m
		-- 查询条件
		where m.status_ = 0

		-- 排序
		order by m.file_date desc, c.order_no

		-- 分页
		limit 25 offset 25
) mc;

/*
-- 低性能的查询：3445ms
with man_cert(id, order_no, tname, cname) as (
	select c.id, c.order_no, t.name, c.name 
		from bc_cert_cfg c
		inner join bc_cert_type t on t.id = c.type_id
		where t.name = '司机证件'
) 
select m.id mid, m.name mname, c.tname tname, c.cname cname, c.order_no order_no, c.id cid, m.file_date file_date
	,get_formfieldvalue_byfromparem_fieldkey(m.id, c.tname, c.cname,'attach_width') attach_width
	,get_formfieldvalue_byfromparem_fieldkey(m.id, c.tname, c.cname,'attach_width') attach_width
	,get_formfieldvalue_byfromparem_fieldkey(m.id, c.tname, c.cname,'attach_id_a') attach_id_a
	,get_formfieldvalue_byfromparem_fieldkey(m.id, c.tname, c.cname,'attach_width_a') attach_width_a
	,get_formfieldvalue_byfromparem_fieldkey(m.id, c.tname, c.cname,'attach_id_b') attach_id_b
	,get_formfieldvalue_byfromparem_fieldkey(m.id, c.tname, c.cname,'attach_width_b') attach_width_b
	,get_formfieldvalue_byfromparem_fieldkey(m.id, c.tname, c.cname,'attach_id_c') attach_id_c
	,get_formfieldvalue_byfromparem_fieldkey(m.id, c.tname, c.cname,'attach_width_c') attach_width_c 
	from man_cert c, bs_carman m
	where m.status_ = 0
	order by m.file_date desc, c.order_no
	limit 25 offset 25;

-- 原司机证件模块的查询：6404ms
select m.id,m.status_,m.sex,m.name,m.cert_fwzg,m.cert_identity,m.phone,m.uid_,i.value_ ivalue,i.key_ ikey
	,g.value_ gvalue,g.key_ gkey ,f.id fid,md.actor_name md_name,f.modified_date,d.status_ as bstatus
	,get_formfieldvalue_byfromparem_fieldkey(m.id,'CarManCert',i.key_,'attach_id') attach_id
	,get_formfieldvalue_byfromparem_fieldkey(m.id,'CarManCert',i.key_,'attach_width') attach_width
	,get_formfieldvalue_byfromparem_fieldkey(m.id,'CarManCert',i.key_,'attach_id_a') attach_id_a
	,get_formfieldvalue_byfromparem_fieldkey(m.id,'CarManCert',i.key_,'attach_width_a') attach_width_a
	,get_formfieldvalue_byfromparem_fieldkey(m.id,'CarManCert',i.key_,'attach_id_b') attach_id_b
	,get_formfieldvalue_byfromparem_fieldkey(m.id,'CarManCert',i.key_,'attach_width_b') attach_width_b
	,get_formfieldvalue_byfromparem_fieldkey(m.id,'CarManCert',i.key_,'attach_id_c') attach_id_c
	,get_formfieldvalue_byfromparem_fieldkey(m.id,'CarManCert',i.key_,'attach_width_c') attach_width_c 
	from bs_temp_driver m 
	inner join bc_option_group g on g.key_='carman.cert' 
	inner join bc_option_item i on i.pid=g.id 
	left join bc_form f on f.pid=m.id and f.code=i.key_ and f.type_='CarManCert' and f.status_ in(0,-1) 
	left join bs_carman d on d.cert_identity = m.cert_identity 
	left join bc_identity_actor_history md on md.id=f.modifier_id 
	order by m.file_date desc,i.order_ asc limit 25 offset 25
*/
