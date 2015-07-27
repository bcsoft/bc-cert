bc.certCfgView = {
	/** 访问配置 **/
	aclConfig : function(){
		var $page = $(this);
		// 获取用户选中的条目
		var rows = bc.grid.getSelectedRowHiddenData($page.find(".bc-grid"));
		//console.debug(rows);
		if(rows.length == 0){
			bc.msg.slide("请先选择需要进行访问配置的证件！");
			return;
		}
		if(rows.length > 1){
			bc.msg.slide("每次只能针对一个证件执行此配置！");
			return;
		}
		
		var row = rows[0];
		bc.acl.config({
			docId: row.docId,
			docType: row.docType,
			docName: row.docName,
			role: 'BC_CERT_ACL_MANAGE',
			onOk: function(){
				bc.grid.reloadData($page);
			}
		});
	}
};