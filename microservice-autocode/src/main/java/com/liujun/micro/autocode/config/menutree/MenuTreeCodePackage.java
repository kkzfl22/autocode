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
    MenuNode rootNode = menuTree.getDefineRoot();

    return rootNode;
  }

  /**
   * 获取数据库存储的实体对象节点
   *
   * @return 模块名称
   */
  public MenuNode getRepositoryObjectNode() {
    MenuNode poNode =
        menuTree
            .getDefineRoot()
            .getChildren(JavaDomainTreeKey.DOMAIN)
            .getChildren(menuTree.getModelName())
            .getChildren(JavaDomainTreeKey.DOMAIN_REPOSITORY)
            .getChildren(JavaDomainTreeKey.REPOSITORY_PO);
    return poNode;
  }

  /**
   * 获取数据库存储的接口的节点信息
   *
   * @return 模块名称
   */
  public MenuNode getRepositoryDaoNode() {
    MenuNode poNode =
        menuTree
            .getDefineRoot()
            .getChildren(JavaDomainTreeKey.DOMAIN)
            .getChildren(menuTree.getModelName())
            .getChildren(JavaDomainTreeKey.DOMAIN_REPOSITORY)
            .getChildren(JavaDomainTreeKey.REPOSITORY_MAPPER);
    return poNode;
  }

  /**
   * 获取领域层与存储接口的节点信息
   *
   * @return 模块名称
   */
  public MenuNode getRepositoryFacadeNode() {
    MenuNode poNode =
        menuTree
            .getDefineRoot()
            .getChildren(JavaDomainTreeKey.DOMAIN)
            .getChildren(menuTree.getModelName())
            .getChildren(JavaDomainTreeKey.DOMAIN_REPOSITORY)
            .getChildren(JavaDomainTreeKey.REPOSITORY_FACADE);
    return poNode;
  }

  /**
   * 获取存储层转换的方法节点
   *
   * @return 模块信息
   */
  public MenuNode getRepositoryAssemblerNode() {
    MenuNode poNode =
        menuTree
            .getDefineRoot()
            .getChildren(JavaDomainTreeKey.DOMAIN)
            .getChildren(menuTree.getModelName())
            .getChildren(JavaDomainTreeKey.DOMAIN_REPOSITORY)
            .getChildren(JavaDomainTreeKey.REPOSITORY_ASSEMBLER);
    return poNode;
  }

  /**
   * 获取领域存储的对象实现
   *
   * @return 模块信息
   */
  public MenuNode getRepositoryPersistenceNode() {
    MenuNode poNode =
        menuTree
            .getDefineRoot()
            .getChildren(JavaDomainTreeKey.DOMAIN)
            .getChildren(menuTree.getModelName())
            .getChildren(JavaDomainTreeKey.DOMAIN_REPOSITORY)
            .getChildren(JavaDomainTreeKey.REPOSITORY_PERSISTENCE);
    return poNode;
  }

  /**
   * 获取领域实体对象节点
   *
   * @return 模块名称
   */
  public MenuNode getDomainObjectNode() {
    MenuNode domainNode =
        menuTree
            .getDefineRoot()
            .getChildren(JavaDomainTreeKey.DOMAIN)
            .getChildren(menuTree.getModelName())
            .getChildren(JavaDomainTreeKey.DOMAIN_ENTITY);

    return domainNode;
  }

  /**
   * 获取基础资源的实体
   *
   * @return 菜单实体
   */
  public MenuNode getInfrastructureEntity() {
    MenuNode getNode =
        menuTree
            .getDefineRoot()
            .getChildren(JavaDomainTreeKey.INFRASTRUCTURE)
            .getChildren(JavaDomainTreeKey.INFRASTRUCTURE_ENTITY);

    return getNode;
  }

  /**
   * 获取领域服务的节点信息
   *
   * @param menuTree 树形结构
   * @return 模块名称
   */
  public MenuNode getDomainServiceNode(DomainMenuTree menuTree) {
    MenuNode poNode =
        menuTree
            .getDefineRoot()
            .getChildren(JavaDomainTreeKey.DOMAIN)
            .getChildren(menuTree.getModelName())
            .getChildren(JavaDomainTreeKey.DOMAIN_SERVICE);
    return poNode;
  }
}
