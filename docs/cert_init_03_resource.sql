-- 资源配置：证件管理800600
insert into BC_IDENTITY_RESOURCE (ID,STATUS_,INNER_,TYPE_,BELONG,ORDER_,NAME,URL,ICONCLASS,PNAME) 
	select NEXTVAL('CORE_SEQUENCE'), 0, false, 1, m.id, '800600','证件管理', '', 'i0509','系统维护' 
	from BC_IDENTITY_RESOURCE m 
	where m.ORDER_='800000'
	and not exists (select 1 from BC_IDENTITY_RESOURCE where order_='800600');


-- 资源配置：证件类别800601
insert into BC_IDENTITY_RESOURCE (ID,STATUS_,INNER_,TYPE_,BELONG,ORDER_,NAME,URL,ICONCLASS,PNAME) 
	select NEXTVAL('CORE_SEQUENCE'), 0, false, 2, m.id, '800601','证件类别', '/modules/bc/cert/certTypes/paging', 'i0001','证件管理'
	from BC_IDENTITY_RESOURCE m 
	where m.order_='800600'
	and not EXISTS(select 1 from bc_identity_resource where order_='800601');
	
-- 资源配置：证件配置800602
insert into BC_IDENTITY_RESOURCE (ID,STATUS_,INNER_,TYPE_,BELONG,ORDER_,NAME,URL,ICONCLASS,PNAME) 
	select NEXTVAL('CORE_SEQUENCE'), 0, false, 2, m.id, '800602','证件配置', '/modules/bc/cert/certCfgs/paging', 'i0003','证件管理'
	from BC_IDENTITY_RESOURCE m 
	where m.order_='800600'
	and not EXISTS(select 1 from bc_identity_resource where order_='800602');
	
	-- 资源配置：证件信息800603
insert into BC_IDENTITY_RESOURCE (ID,STATUS_,INNER_,TYPE_,BELONG,ORDER_,NAME,URL,ICONCLASS,PNAME) 
	select NEXTVAL('CORE_SEQUENCE'), 0, false, 2, m.id, '800603','证件信息', '/modules/bc/cert/certInfos/paging', 'i0003','证件管理'
	from BC_IDENTITY_RESOURCE m 
	where m.order_='800600'
	and not EXISTS(select 1 from bc_identity_resource where order_='800603');

-- 角色配置：证件管理员 BC_CERT_MANAGE
insert into  BC_IDENTITY_ROLE (ID, STATUS_,INNER_,TYPE_,ORDER_,CODE,NAME) 
	select NEXTVAL('CORE_SEQUENCE'), 0, false,  0,'0000', 'BC_CERT_MANAGE','证件管理'
	from bc_dual
	where not exists(select 1 from bc_identity_role where code='BC_CERT_MANAGE');
	
--	增加证件管理岗位CertManageGroup
insert into BC_IDENTITY_ACTOR (ID,UID_,STATUS_,INNER_,TYPE_,CODE, NAME, ORDER_,PCODE,PNAME) 
	select NEXTVAL('CORE_SEQUENCE'),'group.cert.manage'||NEXTVAL('CORE_SEQUENCE'), 0, false, 3
	, 'CertManageGroup','证件管理岗', '11001','[1]baochengzongbu','宝城'
	from BC_DUAL
	where not exists (select 0 from BC_IDENTITY_ACTOR where CODE='CertManageGroup');
	
insert into BC_IDENTITY_ACTOR_RELATION (TYPE_,MASTER_ID,FOLLOWER_ID) 
    select 0,am.id,af.id
    from BC_IDENTITY_ACTOR am,BC_IDENTITY_ACTOR af
    where am.CODE='baochengzongbu' 
	and af.CODE = 'CertManageGroup' 
	and not exists (select 0 from BC_IDENTITY_ACTOR_RELATION r where r.TYPE_=0 and r.MASTER_ID=am.id and r.FOLLOWER_ID=af.id);
	
--配置证件岗位跟上级岗位宝城的关系	
insert into BC_IDENTITY_ACTOR_RELATION (TYPE_,MASTER_ID,FOLLOWER_ID) 
    select 0,am.id,af.id
    from BC_IDENTITY_ACTOR am,BC_IDENTITY_ACTOR af
    where am.CODE='baochengzongbu' 
	and af.CODE = 'CertManageGroup' 
	and not exists (select 0 from BC_IDENTITY_ACTOR_RELATION r where r.TYPE_=0 and r.MASTER_ID=am.id and r.FOLLOWER_ID=af.id);
	
-- 配置岗位跟角色的关系
insert into BC_IDENTITY_ROLE_ACTOR (RID,AID) 
	select r.id,a.id 
	from BC_IDENTITY_ROLE r,BC_IDENTITY_ACTOR a 
	where r.CODE = 'BC_CERT_MANAGE'
	and a.NAME in ('证件管理岗')
	and not exists (select 0 from BC_IDENTITY_ROLE_ACTOR ra where ra.RID=r.id and ra.AID=a.id);
	
	
--	插入证件管理岗位与用户之间的关系
insert into BC_IDENTITY_ACTOR_RELATION (TYPE_,MASTER_ID,FOLLOWER_ID) 
    select 0,am.id,af.id
    from BC_IDENTITY_ACTOR am,BC_IDENTITY_ACTOR af
    where am.CODE='CertManageGroup' 
	and af.CODE in ('ghy', 'hrj', 'ldx') -- 用户帐号
	and not exists (select 0 from BC_IDENTITY_ACTOR_RELATION r where r.TYPE_=0 and r.MASTER_ID=am.id and r.FOLLOWER_ID=af.id);
	
	
-- 权限配置：证件管理员访问证件类别
insert into BC_IDENTITY_ROLE_RESOURCE (RID,SID) 
	select r.id,m.id from BC_IDENTITY_ROLE r,BC_IDENTITY_RESOURCE m where r.code='BC_CERT_MANAGE' 
	and m.type_ > 1 and m.order_ in ('800601')
	and not exists(select 1 from BC_IDENTITY_ROLE_RESOURCE 
			where rid=(select r2.id from BC_IDENTITY_ROLE r2 where r2.code='BC_CERT_MANAGE')
			and sid=(select m2.id from BC_IDENTITY_RESOURCE m2 where m2.type_ > 1 and m2.order_ in ('800601')))
	order by m.order_;
	
-- 权限配置：证件管理访问证件配置
insert into BC_IDENTITY_ROLE_RESOURCE (RID,SID) 
	select r.id,m.id from BC_IDENTITY_ROLE r,BC_IDENTITY_RESOURCE m where r.code='BC_CERT_MANAGE' 
	and m.type_ > 1 and m.order_ in ('800602')
	and not exists(select 1 from BC_IDENTITY_ROLE_RESOURCE 
			where rid=(select r2.id from BC_IDENTITY_ROLE r2 where r2.code='BC_CERT_MANAGE')
			and sid=(select m2.id from BC_IDENTITY_RESOURCE m2 where m2.type_ > 1 and m2.order_ in ('800602')))
	order by m.order_;
	
	-- 权限配置：证件管理员访问证件信息
insert into BC_IDENTITY_ROLE_RESOURCE (RID,SID) 
	select r.id,m.id from BC_IDENTITY_ROLE r,BC_IDENTITY_RESOURCE m where r.code='BC_CERT_MANAGE' 
	and m.type_ > 1 and m.order_ in ('800603')
	and not exists(select 1 from BC_IDENTITY_ROLE_RESOURCE 
			where rid=(select r2.id from BC_IDENTITY_ROLE r2 where r2.code='BC_CERT_MANAGE')
			and sid=(select m2.id from BC_IDENTITY_RESOURCE m2 where m2.type_ > 1 and m2.order_ in ('800603')))
	order by m.order_;
	
	

-- 插入证件表单模板
-- 默认模板
insert into BC_TEMPLATE (ID,UID_,STATUS_,ORDER_,CATEGORY,CODE,VERSION_,FORMATTED,INNER_,PATH,SIZE_,SUBJECT,DESC_,TYPE_ID,FILE_DATE,AUTHOR_ID) 
	select NEXTVAL('CORE_SEQUENCE'),'Template.cmc.0',0,'6000','证件模板','DEFAULT_CERT_FORM','1',true,false,'/bc/default.cert.form.ftl',25000,'默认证件表单模板','',
	(select id from BC_TEMPLATE_TYPE where code='freemarker'),now(),(select id from BC_IDENTITY_ACTOR_HISTORY where actor_name='系统管理员' and current=true)
	from bc_dual where  not exists(select 1 from bc_template where uid_= 'Template.cmc.0');
	
	
-- 设置默认的模板DEFAULT_CERT_FORM，在司机证件组下面
insert into bc_option_item (id,pid,key_,value_,order_,status_,desc_)
	select NEXTVAL('hibernate_sequence'),(select id from bc_option_group where key_='carman.cert' and value_='司机证件'),
	'cert.Cfg.defaultTplCode','DEFAULT_CERT_FORM','00',0,'默认的证件模板'
	from bc_dual
	where not exists(select 0 from bc_option_item where key_='cert.Cfg.defaultTplCode' and pid=(select id from bc_option_group where key_='carman.cert' and value_='司机证件'));
	

-- 角色配置：证件访问权限管理 BC_CERT_ACL_MANAGE
insert into  BC_IDENTITY_ROLE (ID, STATUS_,INNER_,TYPE_,ORDER_,CODE,NAME) 
	select NEXTVAL('CORE_SEQUENCE'), 0, false,  0,'0000', 'BC_CERT_ACL_MANAGE','证件访问权限管理'
	from bc_dual
	where not exists(select 1 from bc_identity_role where code='BC_CERT_ACL_MANAGE');	
	
-- 权限配置：证件访问权限管理访问证件配置
insert into BC_IDENTITY_ROLE_RESOURCE (RID,SID) 
	select r.id,m.id from BC_IDENTITY_ROLE r,BC_IDENTITY_RESOURCE m where r.code='BC_CERT_ACL_MANAGE' 
	and m.type_ > 1 and m.order_ in ('800602')
	and not exists(select 1 from BC_IDENTITY_ROLE_RESOURCE 
			where rid=(select r2.id from BC_IDENTITY_ROLE r2 where r2.code='BC_CERT_ACL_MANAGE')
			and sid=(select m2.id from BC_IDENTITY_RESOURCE m2 where m2.type_ > 1 and m2.order_ in ('800602')))
	order by m.order_;
	
	
--	增加证件访问权限管理岗CertACLManageGroup
insert into BC_IDENTITY_ACTOR (ID,UID_,STATUS_,INNER_,TYPE_,CODE, NAME, ORDER_,PCODE,PNAME) 
	select NEXTVAL('CORE_SEQUENCE'),'group.cert.acl.manage'||NEXTVAL('CORE_SEQUENCE'), 0, false, 3
	, 'CertACLManageGroup','证件访问权限管理岗', '11001','[1]baochengzongbu','宝城'
	from BC_DUAL
	where not exists (select 0 from BC_IDENTITY_ACTOR where CODE='CertACLManageGroup');
	
insert into BC_IDENTITY_ACTOR_RELATION (TYPE_,MASTER_ID,FOLLOWER_ID) 
    select 0,am.id,af.id
    from BC_IDENTITY_ACTOR am,BC_IDENTITY_ACTOR af
    where am.CODE='baochengzongbu' 
	and af.CODE = 'CertACLManageGroup' 
	and not exists (select 0 from BC_IDENTITY_ACTOR_RELATION r where r.TYPE_=0 and r.MASTER_ID=am.id and r.FOLLOWER_ID=af.id);

-- 配置证件访问权限管理岗位跟证件访问权限角色的关系
insert into BC_IDENTITY_ROLE_ACTOR (RID,AID) 
	select r.id,a.id 
	from BC_IDENTITY_ROLE r,BC_IDENTITY_ACTOR a 
	where r.CODE = 'BC_CERT_ACL_MANAGE'
	and a.NAME in ('证件访问权限管理岗')
	and not exists (select 0 from BC_IDENTITY_ROLE_ACTOR ra where ra.RID=r.id and ra.AID=a.id);
	
-- 配置超级岗岗位跟证件管理角色的关系
insert into BC_IDENTITY_ROLE_ACTOR (RID,AID) 
	select r.id,a.id 
	from BC_IDENTITY_ROLE r,BC_IDENTITY_ACTOR a 
	where r.CODE = 'BC_CERT_MANAGE'
	and a.NAME in ('超级管理岗')
	and not exists (select 0 from BC_IDENTITY_ROLE_ACTOR ra where ra.RID=r.id and ra.AID=a.id);

	
--	插入证件管理岗位与用户之间的关系
insert into BC_IDENTITY_ACTOR_RELATION (TYPE_,MASTER_ID,FOLLOWER_ID) 
    select 0,am.id,af.id
    from BC_IDENTITY_ACTOR am,BC_IDENTITY_ACTOR af
    where am.CODE='CertACLManageGroup' 
	and af.CODE in ('wing','may') -- 用户帐号
	and not exists (select 0 from BC_IDENTITY_ACTOR_RELATION r where r.TYPE_=0 and r.MASTER_ID=am.id and r.FOLLOWER_ID=af.id);	
	