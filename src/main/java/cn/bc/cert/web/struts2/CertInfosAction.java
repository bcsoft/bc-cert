package cn.bc.cert.web.struts2;

import cn.bc.core.query.condition.Direction;
import cn.bc.core.query.condition.impl.OrderCondition;
import cn.bc.core.util.DateUtils;
import cn.bc.db.jdbc.RowMapper;
import cn.bc.db.jdbc.SqlObject;
import cn.bc.identity.web.SystemContext;
import cn.bc.web.formater.AbstractFormater;
import cn.bc.web.struts2.ViewAction;
import cn.bc.web.ui.html.grid.Column;
import cn.bc.web.ui.html.grid.HiddenColumn4MapKey;
import cn.bc.web.ui.html.grid.IdColumn4MapKey;
import cn.bc.web.ui.html.grid.TextColumn4MapKey;
import cn.bc.web.ui.html.page.PageOption;
import cn.bc.web.ui.html.toolbar.Toolbar;
import cn.bc.web.ui.html.toolbar.ToolbarButton;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * 证件信息视图action
 * 
 * @author LeeDane
 * 
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Controller
public class CertInfosAction extends ViewAction<Map<String, Object>> {
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isReadonly() {
		// 证件管理
		SystemContext context = (SystemContext) this.getContext();
		return !context.hasAnyRole(getText("key.role.bc.cert.manage"));
	}

	@Override
	protected String getModuleContextPath() {
		return this.getContextPath() + "/modules/bc/cert";
	}

	@Override
	protected String getHtmlPageNamespace() {
		return getModuleContextPath() + "/certInfos";
	}

	@Override
	protected String getFormActionName() {
		return "certInfo";
	}

	@Override
	protected String getHtmlPageJs() {
		return this.getModuleContextPath() + "/certInfo/view.js";
	}

	/** 页面加载后调用的js初始化方法 */
	@Override
	protected String getHtmlPageInitMethod() {
		//return "bc.partOutView.init";
		return null;
	}

	@Override
	protected SqlObject<Map<String, Object>> getSqlObject() {
		SqlObject<Map<String, Object>> sqlObject = new SqlObject<Map<String, Object>>();

		// 构建查询语句,where和order by不要包含在sql中(要统一放到condition中)
		StringBuffer sql = new StringBuffer();
		sql.append("select f.id as id,f.file_date as file_date,t.name as type,c.name as name,");
		sql.append("f.subject as subject,f.version_ as version,f.desc_ as desc,");
		sql.append("iah.actor_name as actor_name,f.modified_date as modify_date,");
		sql.append("getaccessactors4docidtype4docidinteger(f.id,'CertInfo')");
		sql.append(" from bc_form f");
		sql.append(" inner join bc_cert_cfg c on c.name = f.code");
		sql.append(" inner join bc_cert_type t on t.name = f.type_");
		sql.append(" left join bc_identity_actor_history iah on iah.id = f.modifier_id");

		sqlObject.setSql(sql.toString());
		// 注入参数
		sqlObject.setArgs(null);

		// 数据映射器
		sqlObject.setRowMapper(new RowMapper<Map<String, Object>>() {
			public Map<String, Object> mapRow(Object[] rs, int rowNum) {
				Map<String, Object> map = new HashMap<String, Object>();
				int i = 0;
				map.put("id", rs[i++]);
				map.put("file_date", rs[i++]);
				map.put("type", rs[i++]);
				map.put("name", rs[i++]);
				map.put("subject", rs[i++]);
				map.put("version", rs[i++]);
				map.put("desc", rs[i++]);
				map.put("actor_name", rs[i++]);
				map.put("modify_date", rs[i++]);
				map.put("accessactors", rs[i++]);
				map.put("accessControlDocType","CertInfo");
				return map;
			}
		});
		return sqlObject;
	}

	@Override
	protected List<Column> getGridColumns() {
		
		List<Column> columns = new ArrayList<Column>();
		columns.add(new IdColumn4MapKey("f.id", "id"));
		
		// 创建时间
		columns.add(new TextColumn4MapKey("f.file_date", "file_date",
				getText("certInfo.file_date"), 150).setSortable(true));

		// 证件类别
		columns.add(new TextColumn4MapKey("t.name", "type",
				getText("certInfo.type"), 100).setSortable(true));
		
		// 证件名称
		columns.add(new TextColumn4MapKey("c.name", "name",
				getText("certInfo.name"),100).setSortable(true));
				
		// 标题
		columns.add(new TextColumn4MapKey("f.subject", "subject",
				getText("certInfo.subject"),150).setSortable(true));
				
		//访问配置
		columns.add(new TextColumn4MapKey("", "accessactors",
				getText("certCfg.access"),150).setSortable(true)
				.setUseTitleFromLabel(true));
	
		// 版本
		columns.add(new TextColumn4MapKey("f.version_", "version",
				getText("certInfo.version"),100).setSortable(true));
		
		// 备注
		columns.add(new TextColumn4MapKey("f.desc_", "desc",
				getText("certInfo.desc")).setSortable(true));
		
		// 最后修改
		columns.add(new TextColumn4MapKey("iah.actor_name", "actor_name",
				getText("lastModify"),200).setSortable(true)
				.setValueFormater(new AbstractFormater<String>() {
					@Override
					public String format(Object context, Object value) {
						@SuppressWarnings("unchecked")
						Map<String, Object> map = (Map<String, Object>) context;
						if (value == null) {
							return "";
						} else {
							Object order = map.get("actor_name");
							if(order==null){
								return "";
							}else{
								return value
										+ " ("
										+ DateUtils
												.formatDateTime2Minute((Date) map
														.get("modify_date")) + ")";
							}
						}

					}
				}).setUseTitleFromLabel(true));
		
		columns.add(new HiddenColumn4MapKey("accessControlDocType","accessControlDocType"));
		columns.add(new HiddenColumn4MapKey("accessControlDocName", "name"));
		return columns;
	}

	@Override
	protected String getHtmlPageTitle() {
		return this.getText("certInfo.title");
	}

	@Override
	protected PageOption getHtmlPageOption() {
		return super.getHtmlPageOption().setWidth(870).setMinWidth(450)
				.setHeight(400).setMinHeight(200);
	}

	@Override
	protected Toolbar getHtmlPageToolbar() {
		/*SystemContext context = (SystemContext) this.getContext();*/
	
		Toolbar tb = new Toolbar();
		
		if(!this.isReadonly()){
			// 新建按钮
			tb.addButton(Toolbar
					.getDefaultCreateToolbarButton(getText("label.create")));

			// 编辑按钮
			tb.addButton(Toolbar
					.getDefaultEditToolbarButton(getText("label.edit")));

			// 删除按钮
			tb.addButton(Toolbar
					.getDefaultDeleteToolbarButton(getText("label.delete")));
			
			// 访问配置按钮
			tb.addButton(new ToolbarButton().setIcon("ui-icon-lightbulb")
					.setText(getText("certCfg.access"))
					.setClick("bc.certInfoFormView.access"));
			
			// 搜索按钮
			tb.addButton(this.getDefaultSearchToolbarButton());
			
		}
		
		return tb;
	}
	

	@Override
	protected OrderCondition getGridDefaultOrderCondition() {
		return new OrderCondition("c.name", Direction.Asc).add(
				"t.name", Direction.Desc);
	}

	@Override
	protected String getGridRowLabelExpression() {
		return "['name']";
	}

	@Override
	protected String[] getGridSearchFields() {
		return new String[] { "t.name", "c.name"};
	}


}
