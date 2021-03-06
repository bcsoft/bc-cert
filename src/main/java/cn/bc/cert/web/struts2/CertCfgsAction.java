package cn.bc.cert.web.struts2;

import cn.bc.BCConstants;
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
import cn.bc.web.ui.html.grid.HiddenColumn4MapKey;
import cn.bc.web.ui.html.grid.IdColumn4MapKey;
import cn.bc.web.ui.html.grid.TextColumn4MapKey;
import cn.bc.web.ui.html.page.PageOption;
import cn.bc.web.ui.html.toolbar.Toolbar;
import cn.bc.web.ui.html.toolbar.ToolbarButton;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * 证件配置视图action
 *
 * @author LeeDane
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Controller
public class CertCfgsAction extends ViewAction<Map<String, Object>> {
  private static final long serialVersionUID = 1L;
  public String status = "0";

  @Override
  public boolean isReadonly() {
    // 证件管理||超级管理员
    SystemContext context = (SystemContext) this.getContext();
    return !context.hasAnyRole(getText("key.role.bc.cert.manage"), getText("key.role.bc.admin"));
  }

  @Override
  protected JSONObject getGridExtrasData() {
    JSONObject json = new JSONObject();
    if (!isAcl()) {
      try {
        json.put("showRole", "11");
      } catch (JSONException e) {
        e.printStackTrace();
      }
    } else {
      try {
        json.put("showRole", "00");
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    return json;
  }

  //是否有证件访问控制权限
  public boolean isAcl() {
    SystemContext context = (SystemContext) this.getContext();
    return !context.hasAnyRole(getText("key.role.bc.cert.acl.manage"));
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
    return this.getModuleContextPath() + "/certCfg/view.js," + this.getContextPath() + "/bc/acl/api.js";
  }

  /**
   * 页面加载后调用的js初始化方法
   */
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
    sql.append("select cc.id,cc.status_ as status,cc.order_no as order_no,ct.name as type_name");
    sql.append(",cc.name as name,cc.code as code,cc.page_count as page_count");
    sql.append(",cc.width as width,cc.combine as combine,cc.tpl as tpl,ct.order_no as ct_order");
    sql.append(",iah.actor_name as actor_name,cc.modified_date as modify_date");
    sql.append(",cc.print_direction");
    sql.append(",cc.one_page_one_typography");
    sql.append(",getaccessactors4docidtype4docidinteger(cc.id,'CertCfg')");
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
        map.put("code", rs[i++]);
        map.put("page_count", rs[i++]);
        map.put("width", rs[i++]);
        map.put("combine", rs[i++]);
        map.put("tpl", rs[i++]);
        map.put("ct_order", rs[i++]);
        map.put("actor_name", rs[i++]);
        map.put("modify_date", rs[i++]);
        map.put("print_direction", rs[i++]);
        map.put("one_page_one_typography", rs[i++]);
        map.put("accessactors", rs[i++]);
        map.put("docType", "CertCfg");
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
        getText("certCfg.status"), 40).setSortable(true).setValueFormater(new AbstractFormater<String>() {

      @Override
      public String format(Object context, Object value) {
        if (String.valueOf(value).equals("0")) {
          return "正常";
        } else {
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
        getText("certCfg.name"), 120).setSortable(true).setUseTitleFromLabel(true));

    // 编码
    columns.add(new TextColumn4MapKey("cc.code", "code",
        getText("certCfg.code"), 100).setSortable(true).setUseTitleFromLabel(true));

    //访问配置
    columns.add(new TextColumn4MapKey("", "accessactors",
        getText("certCfg.access")).setSortable(true)
        .setUseTitleFromLabel(true));
    // 分拆页数
    columns.add(new TextColumn4MapKey("cc.page_count", "page_count",
        getText("certCfg.pageCount"), 60).setSortable(true));

    // 打印宽度
    columns.add(new TextColumn4MapKey("cc.width", "width",
        getText("certCfg.width"), 60).setSortable(true));
    // 合并配置
    columns.add(new TextColumn4MapKey("cc.combine", "combine",
        getText("certCfg.combine"), 80).setSortable(true));
    // 所用模板
    columns.add(new TextColumn4MapKey("cc.tpl", "tpl",
        getText("certCfg.tpl"), 150).setSortable(true).setUseTitleFromLabel(true));
    // 打印方向
    columns.add(new TextColumn4MapKey("cc.print_direction", "print_direction",
        getText("certCfg.print_direction"), 60).setSortable(true).setValueFormater(new AbstractFormater<Object>() {

      @Override
      public Object format(Object context, Object value) {
        return Integer.parseInt(String.valueOf(value)) == 1 ? "横向" : "纵向";
      }
    }));
    // 一页一版
    columns.add(new TextColumn4MapKey("cc.one_page_one_typography", "one_page_one_typography",
        getText("certCfg.one_page_one_typography"), 60).setSortable(true).setValueFormater(new AbstractFormater<Object>() {
      @Override
      public Object format(Object context, Object value) {
        return Integer.parseInt(String.valueOf(value)) == 1 ? "否" : "是";
      }
    }));

    // 最后修改
    columns.add(new TextColumn4MapKey("iah.actor_name", "actor_name",
        getText("lastModify"), 200).setSortable(true)
        .setValueFormater(new AbstractFormater<String>() {
          @Override
          public String format(Object context, Object value) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) context;
            if (value == null) {
              return "";
            } else {
              Object order = map.get("actor_name");
              if (order == null) {
                return "";
              } else {
                return value
                    + " ("
                    + DateUtils
                    .formatDateTime2Minute((Date) map
                        .get("modify_date")) + ")";
              }
            }

          }
        }).setUseTitleFromLabel(true));

    columns.add(new HiddenColumn4MapKey("docId", "id"));
    columns.add(new HiddenColumn4MapKey("docType", "docType"));
    columns.add(new HiddenColumn4MapKey("docName", "name"));
    return columns;
  }

  @Override
  protected String getHtmlPageTitle() {
    return this.getText("certCfg.title");
  }

  @Override
  protected PageOption getHtmlPageOption() {
    return super.getHtmlPageOption().setWidth(870).setMinWidth(680)
        .setHeight(400).setMinHeight(200);
  }

  @Override
  protected Toolbar getHtmlPageToolbar() {
    Toolbar tb = new Toolbar();
    if (!this.isReadonly()) { //证件管理||超级管理员
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

      // 切换状态
      tb.addButton(Toolbar.getDefaultToolbarRadioGroup(
          this.getStatuses(), "status", BCConstants.STATUS_ENABLED,
          getText("title.click2changeSearchStatus")));
    } else {
      // 查看按钮
      tb.addButton(this.getDefaultOpenToolbarButton());
    }

    if (!isAcl()) {
      // 访问配置按钮
      tb.addButton(new ToolbarButton().setIcon("ui-icon-lightbulb")
          .setText(getText("certCfg.access"))
          .setClick("bc.certCfgView.aclConfig"));
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
    return new OrderCondition("cc.status_", Direction.Asc).add(
        "ct.order_no", Direction.Asc).add("cc.order_no", Direction.Asc);
  }

  @Override
  protected String getGridRowLabelExpression() {
    return "['name']";
  }

  @Override
  protected String[] getGridSearchFields() {
    return new String[]{"cc.name", "ct.name", "cc.code", "cc.tpl"};
  }

  private Map<String, String> getStatuses() {
    Map<String, String> statuses = new LinkedHashMap<String, String>();
    statuses.put(String.valueOf(BCConstants.STATUS_ENABLED),
        getText("certCfg.status.enabled"));
    statuses.put(String.valueOf(BCConstants.STATUS_DISABLED),
        getText("certCfg.status.disabled"));
    statuses.put(String.valueOf(BCConstants.STATUS_ENABLED) + ","
            + String.valueOf(BCConstants.STATUS_DISABLED),
        getText("certCfg.status.all"));
    return statuses;
  }
}