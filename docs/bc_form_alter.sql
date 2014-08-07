--增加字段version_记录版本号信息
alter table bc_form add column ver_ character varying(255);
--增加字段desc_记录备注信息
alter table bc_form add column desc_ character varying(255);


-- 资源配置：证件信息030703
insert into BC_IDENTITY_RESOURCE (ID,STATUS_,INNER_,TYPE_,BELONG,ORDER_,NAME,URL,ICONCLASS,PNAME) 
	select NEXTVAL('CORE_SEQUENCE'), 0, false, 2, m.id, '030703','证件信息', '/modules/bc/cert/certInfos/paging', 'i0004','系统维护'
	from BC_IDENTITY_RESOURCE m 
	where m.order_='800000'
	and not EXISTS(select 1 from bc_identity_resource where order_='030703');
	
-- 权限配置：证件管理员访问证件类别
insert into BC_IDENTITY_ROLE_RESOURCE (RID,SID) 
	select r.id,m.id from BC_IDENTITY_ROLE r,BC_IDENTITY_RESOURCE m where r.code='BC_CERT_MANAGE' 
	and m.type_ > 1 and m.order_ in ('030703')
	and not exists(select 1 from BC_IDENTITY_ROLE_RESOURCE 
			where rid=(select r2.id from BC_IDENTITY_ROLE r2 where r2.code='BC_CERT_MANAGE')
			and sid=(select m2.id from BC_IDENTITY_RESOURCE m2 where m2.type_ > 1 and m2.order_ in ('030703')))
	order by m.order_;
