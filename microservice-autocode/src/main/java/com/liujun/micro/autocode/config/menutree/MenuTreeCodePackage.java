package com.liujun.micro.autocode.config.menutree;

import com.liujun.micro.autocode.constant.JavaDomainTreeKey;

/**
 * 用于目录树构建代码中java的包路径
 *
 * @author liujun
 * @version 0.0.1
 */
public class MenuTreeCodePackage {

  /** 实例信息 */
  public static final MenuTreeCodePackage INSTANCE =
      new MenuTreeCodePackage(DomainMenuTree.INSTANCE);

  private final DomainMenuTree menuTree;

  public MenuTreeCodePackage(DomainMenuTree menuTree) {
    this.menuTree = menuTree;
  }

  public MenuNode getPkgRoot() {

    return menuTree.getDefineRoot();
  }

  /**
   * 获取数据库存储的实体对象节点
   *
   * @return 模块名称
   */
  public MenuNode getRepositoryObjectNode() {
    return menuTree
        .getDefineRoot()
        .getChildren(JavaDomainTreeKey.DOMAIN)
        .getChildren(menuTree.getModelName())
        .getChildren(JavaDomainTreeKey.DOMAIN_REPOSITORY)
        .getChildren(JavaDomainTreeKey.REPOSITORY_PO);
  }

  /**
   * 获取数据库存储的接口的节点信息
   *
   * @return 模块名称
   */
  public MenuNode getRepositoryDaoNode() {
    return menuTree
        .getDefineRoot()
        .getChildren(JavaDomainTreeKey.DOMAIN)
        .getChildren(menuTree.getModelName())
        .getChildren(JavaDomainTreeKey.DOMAIN_REPOSITORY)
        .getChildren(JavaDomainTreeKey.REPOSITORY_MAPPER);
  }

  /**
   * 获取领域层与存储接口的节点信息
   *
   * @return 模块名称
   */
  public MenuNode getRepositoryFacadeNode() {
    return menuTree
        .getDefineRoot()
        .getChildren(JavaDomainTreeKey.DOMAIN)
        .getChildren(menuTree.getModelName())
        .getChildren(JavaDomainTreeKey.DOMAIN_REPOSITORY)
        .getChildren(JavaDomainTreeKey.REPOSITORY_FACADE);
  }

  /**
   * 获取存储层转换的方法节点
   *
   * @return 模块信息
   */
  public MenuNode getRepositoryAssemblerNode() {
    return menuTree
        .getDefineRoot()
        .getChildren(JavaDomainTreeKey.DOMAIN)
        .getChildren(menuTree.getModelName())
        .getChildren(JavaDomainTreeKey.DOMAIN_REPOSITORY)
        .getChildren(JavaDomainTreeKey.REPOSITORY_ASSEMBLER);
  }

  /**
   * 获取领域存储的对象实现
   *
   * @return 模块信息
   */
  public MenuNode getRepositoryPersistenceNode() {
    return menuTree
        .getDefineRoot()
        .getChildren(JavaDomainTreeKey.DOMAIN)
        .getChildren(menuTree.getModelName())
        .getChildren(JavaDomainTreeKey.DOMAIN_REPOSITORY)
        .getChildren(JavaDomainTreeKey.REPOSITORY_PERSISTENCE);
  }

  /**
   * 获取领域实体对象节点
   *
   * @return 模块名称
   */
  public MenuNode getDomainObjectNode() {

    return menuTree
        .getDefineRoot()
        .getChildren(JavaDomainTreeKey.DOMAIN)
        .getChildren(menuTree.getModelName())
        .getChildren(JavaDomainTreeKey.DOMAIN_ENTITY);
  }

  /**
   * 获取基础资源的实体
   *
   * @return 菜单实体
   */
  public MenuNode getInfrastructureEntity() {

    return menuTree
        .getDefineRoot()
        .getChildren(JavaDomainTreeKey.INFRASTRUCTURE)
        .getChildren(JavaDomainTreeKey.INFRASTRUCTURE_ENTITY);
  }

  /**
   * 获取领域服务的节点信息
   *
   * @return 模块名称
   */
  public MenuNode getDomainServiceNode() {
    return menuTree
        .getDefineRoot()
        .getChildren(JavaDomainTreeKey.DOMAIN)
        .getChildren(menuTree.getModelName())
        .getChildren(JavaDomainTreeKey.DOMAIN_SERVICE);
  }

  /**
   * 获取应用服务的信息
   *
   * @return 模块名称
   */
  public MenuNode getApplicationServiceNode() {
    return menuTree.getDefineRoot().getChildren(JavaDomainTreeKey.APPLICATION);
  }

  /**
   * 获取对外的API
   *
   * @return 模块名称
   */
  public MenuNode getInterfaceFacadeNode() {
    return menuTree
        .getDefineRoot()
        .getChildren(JavaDomainTreeKey.INTERFACE)
        .getChildren(JavaDomainTreeKey.INTERFACE_FACADE);
  }

  /**
   * 获取API的对象
   *
   * @return 模块名称
   */
  public MenuNode getInterfaceObjectNode() {
    return menuTree
        .getDefineRoot()
        .getChildren(JavaDomainTreeKey.INTERFACE)
        .getChildren(JavaDomainTreeKey.INTERFACE_DTO);
  }

  /**
   * 获取转换的对象
   *
   * @return 模块名称
   */
  public MenuNode getInterfaceAssemblerNode() {
    return menuTree
        .getDefineRoot()
        .getChildren(JavaDomainTreeKey.INTERFACE)
        .getChildren(JavaDomainTreeKey.INTERFACE_ASSEMBLER);
  }
}
