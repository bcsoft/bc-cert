-- 获取证件打印相关参数
with cfg(type_, code, pid, ver_) as (
	select 'CarManCert'::text, 'certIdentity'::text, 15035416, '1.0'::text
) 
select ct.name typeName, ct.code typeCode, cc.name certName, cc.code certCode, c.pid pid, c.ver_ as version
	, f.subject subject, f.desc_ as description
	, (select value_ from bc_form_field ff where ff.pid = f.id and ff.name_ = 'attach_width') attachWidth
	, (select value_ from bc_form_field ff where ff.pid = f.id and ff.name_ = 'attach_id') attachId 
	from bc_form f
	inner join cfg c on (c.type_ = f.type_ and c.code = f.code and c.pid = f.pid and c.ver_ = f.ver_)
	inner join bc_cert_cfg cc on (cc.code = c.code)
	inner join bc_cert_type ct on (ct.code = c.type_)
	order by ct.order_no, cc.order_no;