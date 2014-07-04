--更新宽度
update bc_form_field set name_='attach_width_1' where name_='attach_width_a';
update bc_form_field set name_='attach_width_2' where name_='attach_width_b';
update bc_form_field set name_='attach_width_3' where name_='attach_width_c';
update bc_form_field set name_='attach_width_4' where name_='attach_width_d';

--
update bc_form_field set name_='attach_id_1' where name_='attach_id_a';
update bc_form_field set name_='attach_id_2' where name_='attach_id_b';
update bc_form_field set name_='attach_id_3' where name_='attach_id_c';
update bc_form_field set name_='attach_id_4' where name_='attach_id_d';

--户口簿
UPDATE bc_form
   SET tpl_='DEFAULT_CERT_FORM'
 WHERE tpl_='BC-CARMAN-FORM-CERT-HKB';

--居住证 
UPDATE bc_form
   SET tpl_='DEFAULT_CERT_FORM'
 WHERE tpl_='BC-CARMAN-FORM-CERT-JZZ';
 
--身份证
UPDATE bc_form
   SET tpl_='DEFAULT_CERT_FORM'
 WHERE tpl_='BC-CARMAN-FORM-CERT-IDENTITY';

 --驾驶证
UPDATE bc_form
   SET tpl_='DEFAULT_CERT_FORM'
 WHERE tpl_='BC-CARMAN-FORM-CERT-JSZ';

 --LPG证
 UPDATE bc_form
   SET tpl_='DEFAULT_CERT_FORM'
 WHERE tpl_='BC-CARMAN-FORM-CERT-LPG';

 --服务资格证
  UPDATE bc_form
   SET tpl_='DEFAULT_CERT_FORM'
 WHERE tpl_='BC-CARMAN-FORM-CERT-FWZG';
--从业资格证
  UPDATE bc_form
   SET tpl_='DEFAULT_CERT_FORM'
 WHERE tpl_='BC-CARMAN-FORM-CERT-CYZG';

--计生证
 UPDATE bc_form
   SET tpl_='DEFAULT_CERT_FORM'
 WHERE tpl_='BC-CARMAN-FORM-CERT-JISZ';