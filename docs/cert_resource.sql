-- 资源配置：证件类别030701
insert into BC_IDENTITY_RESOURCE (ID,STATUS_,INNER_,TYPE_,BELONG,ORDER_,NAME,URL,ICONCLASS,PNAME) 
	select NEXTVAL('CORE_SEQUENCE'), 0, false, 2, m.id, '030701','证件类别', '/modules/bc/cert/certTypes/paging', 'i0001','营运系统'
	from BC_IDENTITY_RESOURCE m 
	where m.order_='030000'
	and not EXISTS(select 1 from bc_identity_resource where order_='030701');
	
-- 资源配置：证件配置030702
insert into BC_IDENTITY_RESOURCE (ID,STATUS_,INNER_,TYPE_,BELONG,ORDER_,NAME,URL,ICONCLASS,PNAME) 
	select NEXTVAL('CORE_SEQUENCE'), 0, false, 2, m.id, '030702','证件配置', '/modules/bc/cert/certCfgs/paging', 'i0003','营运系统'
	from BC_IDENTITY_RESOURCE m 
	where m.order_='030000'
	and not EXISTS(select 1 from bc_identity_resource where order_='030702');

-- 角色配置：证件管理员 BC_CERT_MANAGE
insert into  BC_IDENTITY_ROLE (ID, STATUS_,INNER_,TYPE_,ORDER_,CODE,NAME) 
	select NEXTVAL('CORE_SEQUENCE'), 0, false,  0,'0000', 'BC_CERT_MANAGE','证件管理员'
	from bc_dual
	where not exists(select 1 from bc_identity_role where code='BC_CERT_MANAGE');
	
-- 权限配置：证件管理员访问证件类别
insert into BC_IDENTITY_ROLE_RESOURCE (RID,SID) 
	select r.id,m.id from BC_IDENTITY_ROLE r,BC_IDENTITY_RESOURCE m where r.code='BC_CERT_MANAGE' 
	and m.type_ > 1 and m.order_ in ('030701')
	and not exists(select 1 from BC_IDENTITY_ROLE_RESOURCE 
			where rid=(select r2.id from BC_IDENTITY_ROLE r2 where r2.code='BC_CERT_MANAGE')
			and sid=(select m2.id from BC_IDENTITY_RESOURCE m2 where m2.type_ > 1 and m2.order_ in ('030701')))
	order by m.order_;

-- 权限配置：证件管理员访问证件配置
insert into BC_IDENTITY_ROLE_RESOURCE (RID,SID) 
	select r.id,m.id from BC_IDENTITY_ROLE r,BC_IDENTITY_RESOURCE m where r.code='BC_CERT_MANAGE' 
	and m.type_ > 1 and m.order_ in ('030702')
	and not exists(select 1 from BC_IDENTITY_ROLE_RESOURCE 
			where rid=(select r2.id from BC_IDENTITY_ROLE r2 where r2.code='BC_CERT_MANAGE')
			and sid=(select m2.id from BC_IDENTITY_RESOURCE m2 where m2.type_ > 1 and m2.order_ in ('030702')))
	order by m.order_;