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
 * 证件类型视图action
 * 
 * @author LeeDane
 * 
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Controller
public class CertTypesAction extends ViewAction<Map<String, Object>> {
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
		return getModuleContextPath() + "/certTypes";
	}

	@Override
	protected String getFormActionName() {
		return "certType";
	}

	@Override
	protected String getHtmlPageJs() {
		//return this.getModuleContextPath() + "/partOut/view.js";
		return null;
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
		sql.append("select ct.id,ct.code as code,ct.name as name,ct.order_no as order,iah.actor_name as actor_name,");
		sql.append("ct.modified_date as modify_date");
		sql.append(" from bc_cert_type ct");
		sql.append(" left join bc_identity_actor_history iah on iah.id = ct.modifier_id");
		sqlObject.setSql(sql.toString());
		// 注入参数
		sqlObject.setArgs(null);

		// 数据映射器
		sqlObject.setRowMapper(new RowMapper<Map<String, Object>>() {
			public Map<String, Object> mapRow(Object[] rs, int rowNum) {
				Map<String, Object> map = new HashMap<String, Object>();
				int i = 0;
				map.put("id", rs[i++]);
				map.put("code",rs[i++]);
				map.put("name", rs[i++]);
				map.put("order", rs[i++]);
				map.put("actor_name", rs[i++]);
				map.put("modify_date", rs[i++]);
				return map;
			}
		});
		return sqlObject;
	}

	@Override
	protected List<Column> getGridColumns() {
		
		List<Column> columns = new ArrayList<Column>();
		columns.add(new IdColumn4MapKey("ct.id", "id"));

		// 排序号
		columns.add(new TextColumn4MapKey("ct.order_no", "order",
				getText("certType.order"), 100).setSortable(true));

		// 名称
		columns.add(new TextColumn4MapKey("ct.code", "code",
				getText("certType.code"),100).setSortable(true));
		
		// 名称
		columns.add(new TextColumn4MapKey("ct.name", "name",
				getText("certType.name"),200).setSortable(true));

		
		// 最后修改
		columns.add(new TextColumn4MapKey("iah.actor_name", "actor_name",
				getText("lastModify"),250).setSortable(true)
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
		return columns;
	}

	@Override
	protected String getHtmlPageTitle() {
		return this.getText("certType.title");
	}

	@Override
	protected PageOption getHtmlPageOption() {
		return super.getHtmlPageOption().setWidth(670).setMinWidth(450)
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
			
			// 搜索按钮
			tb.addButton(this.getDefaultSearchToolbarButton());
		}
		
		return tb;
	}

	@Override
	protected OrderCondition getGridDefaultOrderCondition() {
		return new OrderCondition("ct.order_no", Direction.Desc);
	}

	@Override
	protected String getGridRowLabelExpression() {
		return "['name']";
	}

	@Override
	protected String[] getGridSearchFields() {
		return new String[] { "ct.name", "ct.order_no","ct.code"};
	}

}
