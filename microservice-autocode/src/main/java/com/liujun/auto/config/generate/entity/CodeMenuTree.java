package com.liujun.auto.config.generate.entity;

/**
 * 代码内的目录树相关的配制
 *
 * @author liujun
 * @version 0.0.1
 */
public class CodeMenuTree {

  private String baseMenu;

  /** 最基础的目录 */
  private String baseDir;

  /** 代码内的领域名称 */
  private String domainName;

  /** 基础层中的前缀信息 */
  private String infrastructurePrefix;

  public String getBaseDir() {
    return baseDir;
  }

  public void setBaseDir(String baseDir) {
    this.baseDir = baseDir;
  }

  public String getDomainName() {
    return domainName;
  }

  public void setDomainName(String domainName) {
    this.domainName = domainName;
  }

  public String getInfrastructurePrefix() {
    return infrastructurePrefix;
  }

  public void setInfrastructurePrefix(String infrastructurePrefix) {
    this.infrastructurePrefix = infrastructurePrefix;
  }

  public String getBaseMenu() {
    return baseMenu;
  }

  public void setBaseMenu(String baseMenu) {
    this.baseMenu = baseMenu;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("CodeMenuTree{");
    sb.append("baseDir='").append(baseDir).append('\'');
    sb.append(", domainName='").append(domainName).append('\'');
    sb.append(", infrastructurePrefix='").append(infrastructurePrefix).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
