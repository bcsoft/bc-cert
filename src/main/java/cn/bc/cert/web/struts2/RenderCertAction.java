package cn.bc.cert.web.struts2;

import cn.bc.cert.domain.CertCfg;
import cn.bc.cert.service.CertCfgService;
import cn.bc.form.domain.Form;
import cn.bc.form.struts2.RenderFormAction;
import cn.bc.web.ui.html.page.ButtonOption;
import cn.bc.web.ui.html.page.PageOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;

/**
 * 渲染证件表单 Action
 *
 * @author dragon
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Controller
public class RenderCertAction extends RenderFormAction {
  private static final long serialVersionUID = 1L;
  protected CertCfg certCfg;

  @Autowired
  private CertCfgService certCfgService;

  public RenderCertAction() {
    // 强制证件配置信息优先
    this.replace = false;
  }

  @Override
  public String render() throws Exception {
    // 获取证件配置对象
    this.certCfg = this.certCfgService.loadByCode(this.type, this.code);
    Assert.notNull(certCfg, "couldn't find CertCfg: type=" + this.type + ", code=" + this.code);

    return super.render();
  }

  @Override
  protected String getPageNamespace() {
    return "bc.defaultCertForm";
  }

  @Override
  protected PageOption getPageOption(boolean readonly) {
    return super.getPageOption(readonly).setWidth(600).setHeight(400);
  }

  @Override
  protected void addPageButton(PageOption pageOption, boolean readonly) {
    if(!readonly){
      pageOption.setButtons(
        new ButtonOption(getText("form.save2NewVersion"), null, "bc.cert.save2NewVersion"),
        new ButtonOption(getText("form.save"), null, "bc.defaultCertForm.save"),
        new ButtonOption(getText("form.saveAndClose"), null, "bc.defaultCertForm.saveAndClose")
      );
    }
  }

  @Override
  protected void addHtmlPageJsCss(Collection<String> jscss, String contextPath, boolean readonly) {
    super.addHtmlPageJsCss(jscss, contextPath, readonly);
    jscss.add(contextPath + "/bc/docs/image/api.js");
    jscss.add(contextPath + "/modules/bc/cert/default/form.js");
  }

  @Override
  protected void replaceFormProperty(Form form, boolean readonly) {
    super.replaceFormProperty(form, readonly);

    // 从证件配置中获取模板编码
    form.setTpl(this.certCfg.getTpl());
  }

  @Override
  protected Form createForm() {
    Form form = super.createForm();

    // 从证件配置中获取模板编码
    form.setTpl(this.certCfg.getTpl());

    return form;
  }

  @Override
  protected Map<String, Object> addContextParams(Map<String, Object> args, boolean readonly) {
    Map<String, Object> ctx = super.addContextParams(args, readonly);

    // 添加证件配置信息
    ctx.put("cfg", this.certCfg);

    return ctx;
  }
}