/** 车辆证件视图 SQL
 * > bc_form 表字段值设计：
 * 		pid 			= [bs_car.id]
 * 		type_ 		= '[bc_cert_type.name]'
 * 		code 			= '[bc_cert_cfg.name]'
 * > bc_acl_doc 表字段值设计：
 * 		doc_id 		= '[bc_cert_cfg.id]'
 * 		doc_type 	= 'CertCfg'
 * 		doc_name 	= '[bc_cert_cfg.name]'
 * >高性能查询说明：
 * 		将存储函数的查询放到分页外，从而保证只查询25次来提高性能
 * 		注：排序及查询条件必须放在内层查询中，否则分页结果就不正确了
 */
-- 证件
with cert(id, t_name, c_name, order_no) as (
	select c.id, t.name, c.name, c.order_no
		from bc_cert_cfg c
		inner join bc_cert_type t on t.id = c.type_id 
		where t.name = '车辆证件' 
),
-- 车辆+证件
car_cert(car_id, plate_type, plate_no, t_name, c_name, c_order, file_date) as (
	select car.id, car.plate_type, car.plate_no, cert.t_name, cert.c_name, cert.order_no, car.file_date
		from bs_car car, cert
		-- 车辆状态条件
		where car.status_ = 0
),
-- 表单
form(id, code, pid, subject) as (
	select f.id, f.code, f.pid, f.subject
		from bc_form f
		inner join car_cert cc on (f.pid = cc.car_id and f.type_ = cc.t_name and f.code = cc.c_name)
),
-- 表单字段
field(id, f_code, f_id, car_id, f_name, f_value) as (
	select d.id, f.code, d.pid, f.pid, d.name_, d.value_
		FROM bc_form_field d
		inner join form f on f.id = d.pid
)
select t.*
	-- 查询函数
	, (select d.f_value from field d where d.car_id = t.car_id and d.f_code = t.c_name and d.f_name = 'attach_id') attach_id0
	,get_formfieldvalue_byfromparem_fieldkey(t.car_id, t.t_name, t.c_name,'attach_id') attach_id
	,get_formfieldvalue_byfromparem_fieldkey(t.car_id, t.t_name, t.c_name,'attach_width') attach_width
	,get_formfieldvalue_byfromparem_fieldkey(t.car_id, t.t_name, t.c_name,'attach_id_a') attach_id_a
	,get_formfieldvalue_byfromparem_fieldkey(t.car_id, t.t_name, t.c_name,'attach_width_a') attach_width_a
	,get_formfieldvalue_byfromparem_fieldkey(t.car_id, t.t_name, t.c_name,'attach_id_b') attach_id_b
	,get_formfieldvalue_byfromparem_fieldkey(t.car_id, t.t_name, t.c_name,'attach_width_b') attach_width_b
	,get_formfieldvalue_byfromparem_fieldkey(t.car_id, t.t_name, t.c_name,'attach_id_c') attach_id_c
	,get_formfieldvalue_byfromparem_fieldkey(t.car_id, t.t_name, t.c_name,'attach_width_c') attach_width_c 
  from (
	select cc.car_id car_id, cc.plate_type plate_type, cc.plate_no plate_no
		, cc.t_name t_name, cc.c_name c_name, cc.c_order c_order, cc.file_date file_date
		from car_cert	cc -- 车辆+证件
		where 1 = 1
		/* 模糊查询条件
		and (
			-- 车辆的车牌
			cc.plate_no like ?
			-- 证件名称
			or cc.c_name like ?
			-- 表单标题
			or exists (select 0 from form f where f.pid = cc.car_id and f.code = cc.c_name and f.subject like ?)
			-- 表单字段值：需指定字段的名称
			or exists (select 0 from field d where d.car_id = cc.car_id and d.f_name = 'name' and d.f_value like ?)
		)
		*/

		-- 已上传条件
		--and exists (select 0 from form f where f.pid = cc.car_id and f.code = cc.c_name)
		
		-- 未上传条件
		-- and not exists (select 0 from form f where f.pid = mc.car_id and f.code = mc.c_name)

		-- 排序
		order by cc.file_date desc, cc.c_order

		-- 分页
		limit 25 offset 0
) t;