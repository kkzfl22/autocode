package com.liujun.auto.config.generate.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 代码内的目录树相关的配制
 *
 * @author liujun
 * @version 0.0.1
 */
public class CodeMenuTree {

  /** 最基础的目录 */
  private String baseMenu;

  /** 代码内的领域名称 */
  private String domainName;

  public String getBaseMenu() {
    return baseMenu;
  }

  public void setBaseMenu(String baseMenu) {
    this.baseMenu = baseMenu;
  }

  public String getDomainName() {
    return domainName;
  }

  public void setDomainName(String domainName) {
    this.domainName = domainName;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("CodeMenuTree{");
    sb.append("baseMenu='").append(baseMenu).append('\'');
    sb.append(", domainName='").append(domainName).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
