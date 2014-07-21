-- 插入查看所有证件的模板
insert into BC_TEMPLATE (ID,UID_,STATUS_,ORDER_,CATEGORY,CODE,VERSION_,FORMATTED,INNER_,PATH,SIZE_,SUBJECT,DESC_,TYPE_ID,FILE_DATE,AUTHOR_ID) 
	select NEXTVAL('CORE_SEQUENCE'),'Template.cmc.15',0,'6015','证件模板','SEE_ALL_BY_PARENT','1',true,false,'/bc/seeAllByParent.ftl',25000,'查看所有证件模板','',
	(select id from BC_TEMPLATE_TYPE where code='freemarker'),now(),(select id from BC_IDENTITY_ACTOR_HISTORY where actor_name='系统管理员' and current=true)
	from bc_dual where  not exists(select 1 from bc_template where uid_= 'Template.cmc.15');
	
	