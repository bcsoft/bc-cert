package cn.bc.cert.web.struts2;

import cn.bc.cert.domain.CertCfg;
import cn.bc.core.query.condition.Condition;
import cn.bc.core.query.condition.ConditionUtils;
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
 * 证件配置视图action
 * 
 * @author LeeDane
 * 
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Controller
public class CertCfgsAction extends ViewAction<Map<String, Object>> {
	private static final long serialVersionUID = 1L;
	public String status = "0";

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
		return getModuleContextPath() + "/certCfgs";
	}

	@Override
	protected String getFormActionName() {
		return "certCfg";
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
		sql.append("select cc.id,cc.status_ as status,cc.order_no as order_no,ct.name as type_name,cc.name as name,cc.page_count as page_count,");
		sql.append("cc.width as width,cc.combine as combine,cc.tpl as tpl,");
		sql.append("iah.actor_name as actor_name,cc.modified_date as modify_date");
		sql.append(" from bc_cert_cfg cc");
		sql.append(" left join bc_identity_actor_history iah on iah.id = cc.modifier_id");
		sql.append(" join bc_cert_type ct on ct.id = cc.type_id");
		sqlObject.setSql(sql.toString());
		// 注入参数
		sqlObject.setArgs(null);

		// 数据映射器
		sqlObject.setRowMapper(new RowMapper<Map<String, Object>>() {
			public Map<String, Object> mapRow(Object[] rs, int rowNum) {
				Map<String, Object> map = new HashMap<String, Object>();
				int i = 0;
				map.put("id", rs[i++]);
				map.put("status", rs[i++]);
				map.put("order_no", rs[i++]);
				map.put("type_name", rs[i++]);
				map.put("name", rs[i++]);
				map.put("page_count", rs[i++]);
				map.put("width", rs[i++]);
				map.put("combine", rs[i++]);
				map.put("tpl", rs[i++]);
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
		columns.add(new IdColumn4MapKey("cc.id", "id"));

		// 状态
		columns.add(new TextColumn4MapKey("cc.status_", "status",
				getText("certCfg.status"),100).setSortable(true).setValueFormater(new AbstractFormater<String>() {

					@Override
					public String format(Object context, Object value) {
						if(String.valueOf(value).equals("0")){
							return "正常";
						}else{
							return "禁用";
						}
						
					}
					
				}));
		
		// 排序号
		columns.add(new TextColumn4MapKey("cc.order_no", "order_no",
				getText("certCfg.order"), 60).setSortable(true));

		// 证件类别
		columns.add(new TextColumn4MapKey("ct.name", "type_name",
				getText("certCfg.type_name"), 60).setSortable(true));
		
		// 证件名称
		columns.add(new TextColumn4MapKey("cc.name", "name",
				getText("certCfg.name"),100).setSortable(true));
	
		// 分拆页数
		columns.add(new TextColumn4MapKey("cc.page_count", "page_count",
				getText("certCfg.page_count"),100).setSortable(true));
		
		// 打印宽度
		columns.add(new TextColumn4MapKey("cc.width", "width",
				getText("certCfg.width"),100).setSortable(true));
		// 合并配置
		columns.add(new TextColumn4MapKey("cc.combine", "combine",
				getText("certCfg.combine"),100).setSortable(true));
		// 所用模板
		columns.add(new TextColumn4MapKey("cc.tpl", "tpl",
				getText("certCfg.tpl"),100).setSortable(true));
		
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
		return columns;
	}

	@Override
	protected String getHtmlPageTitle() {
		return this.getText("certCfg.title");
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
					.setClick("bc.certCfg.access"));
			
			// 搜索按钮
			tb.addButton(this.getDefaultSearchToolbarButton());
			
			// 切换状态
			tb.addButton(Toolbar.getDefaultToolbarRadioGroup(
					this.getStatuses(), "status", 2,
					getText("title.click2changeSearchStatus")));
		}
		
		return tb;
	}
	
	@Override
	protected Condition getGridSpecalCondition() {
		/** 状态条件,控制不同状态显示不同的结果 */
		Condition statusCondition = ConditionUtils
				.toConditionByComma4IntegerValue(this.status, "cc.status_");

		return statusCondition;
	}

	@Override
	protected OrderCondition getGridDefaultOrderCondition() {
		return new OrderCondition("cc.name", Direction.Asc).add(
				"cc.order_no", Direction.Desc);
	}

	@Override
	protected String getGridRowLabelExpression() {
		return "['name']";
	}

	@Override
	protected String[] getGridSearchFields() {
		return new String[] { "cc.name", "cc.order_no"};
	}
	
	private Map<String, String> getStatuses() {
		Map<String, String> statuses = new LinkedHashMap<String, String>();
		statuses.put(String.valueOf(CertCfg.STATUS_ENABLED),
				getText("certCfg.status.enabled"));
		statuses.put(String.valueOf(CertCfg.STATUS_DISABLED),
				getText("certCfg.status.disabled"));
		statuses.put(String.valueOf(CertCfg.STATUS_ENABLED) + ","
				+ String.valueOf(CertCfg.STATUS_DISABLED),
				getText("certCfg.status.all"));
		return statuses;
	}

}
