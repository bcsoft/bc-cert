-- 司机证件：43ms
-- bc_form 表字段值设计：
-- 		pid = bs_temp_driver.id
-- 		type_ = '[bc_cert_type.name]'
-- 		code = '[bc_cert_cfg.name]'

/* 高性能查询说明：
 * 将存储函数的查询放到分页外，从而保证只查询25次来提高性能
 * 注：排序及查询条件必须放在内层查询中，否则分页结果就不正确了
 */
-- 证件
with c(id, t_name, c_name, order_no) as (
	select i.id, 'CarManCert'::text, i.key_, i.order_
		from bc_option_group g
		inner join bc_option_item i on i.pid=g.id 
		where g.key_='carman.cert' 
),
-- 司机+证件
mc(m_id, m_name, t_name, c_name, c_order, file_date) as (
	select m.id, m.name, c.t_name, c.c_name, c.order_no, m.file_date
		from bs_temp_driver m, c
		--where m.status_ = 0	-- 司机状态条件
		--and (m.name like '%' or m.cert_fwzg like '%')	-- 司机模糊查询条件
),
-- 表单
form(id, code, pid, subject) as (
	select f.id, f.code, f.pid, f.subject
		from bc_form f
		inner join mc on (f.pid = mc.m_id and f.type_ = mc.t_name and f.code = mc.c_name)
		--where f.subject like '%'	-- 表单的模糊查询条件
),
-- 表单字段
field(id, f_code, f_id, m_id, f_name, f_value) as (
	select d.id, f.code, d.pid, f.pid, d.name_, d.value_
		FROM bc_form_field d
		inner join form f on f.id = d.pid
		--where d.value_ like '%'		-- 表单字段的模糊查询条件
)
select t.*
	-- 查询函数
	, (select d.f_value from field d where d.m_id = t.m_id and d.f_code = t.c_name and d.f_name = 'attach_id') attach_id0
	,get_formfieldvalue_byfromparem_fieldkey(t.m_id, t.t_name, t.c_name,'attach_id') attach_id
	,get_formfieldvalue_byfromparem_fieldkey(t.m_id, t.t_name, t.c_name,'attach_width') attach_width
	,get_formfieldvalue_byfromparem_fieldkey(t.m_id, t.t_name, t.c_name,'attach_id_a') attach_id_a
	,get_formfieldvalue_byfromparem_fieldkey(t.m_id, t.t_name, t.c_name,'attach_width_a') attach_width_a
	,get_formfieldvalue_byfromparem_fieldkey(t.m_id, t.t_name, t.c_name,'attach_id_b') attach_id_b
	,get_formfieldvalue_byfromparem_fieldkey(t.m_id, t.t_name, t.c_name,'attach_width_b') attach_width_b
	,get_formfieldvalue_byfromparem_fieldkey(t.m_id, t.t_name, t.c_name,'attach_id_c') attach_id_c
	,get_formfieldvalue_byfromparem_fieldkey(t.m_id, t.t_name, t.c_name,'attach_width_c') attach_width_c 
  from (
	select mc.m_id m_id, mc.m_name m_name, mc.t_name t_name, mc.c_name c_name, mc.c_order c_order, mc.file_date file_date
		from mc	mc									-- 司机+证件
		-- 已上传
		where exists (
			select 0 from form f where f.pid = mc.m_id and f.code = mc.c_name
			-- 表单的模糊查询条件
			-- and f.subject like '%'
		)
		-- 字段的模糊搜索
		/*and exists (
			select 0 from field d where d.m_id = mc.m_id
			-- 表单字段的模糊查询条件
			and d.f_value like '%'
		)*/


		-- 模糊查询条件
		--and (c.cname like '%' or mc.name like '%' or f.subject like '%')

		-- 排序
		order by mc.file_date desc, mc.c_order

		-- 分页
		limit 25 --offset 25
) t;

/*
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
	where f.id is not null
	order by m.file_date desc,i.order_ asc limit 25 --offset 25
*/
