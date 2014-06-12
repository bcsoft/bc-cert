/** 证件视图 SQL
 * > bc_form 表字段值设计：
 * 		pid 			= [businessTable.id]
 * 		type_ 		= '[bc_cert_type.name]'
 * 		code 			= '[bc_cert_cfg.name]'
 */
select t.name, c.name, f.subject, f.* 
	from bc_cert_cfg c
	inner join bc_cert_type t on t.id = c.type_id
	inner join bc_form f on f.type_ = t.name and f.code = c.name
	--where
	order by f.file_date desc

/*
SELECT * FROM bc_acl_doc;
SELECT * FROM bc_form;
*/