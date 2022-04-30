package com.liujun.auto.generator.builder.ddd.config;

import com.liujun.auto.config.generate.GenerateConfigProcess;
import com.liujun.auto.config.generate.entity.GenerateConfigEntity;
import com.liujun.auto.config.menutree.DirTree;
import com.liujun.auto.config.menutree.DirNode;
import com.liujun.auto.constant.JavaDomainTreeKey;

/**
 * 用于目录树构建代码中java的包路径
 *
 * @author liujun
 * @version 0.0.1
 */
public class MenuTreeCodePackage {

  /** 实例信息 */
  public static final MenuTreeCodePackage INSTANCE = new MenuTreeCodePackage(DirTree.INSTANCE);

  private final DirTree menuTree;

  public MenuTreeCodePackage(DirTree menuTree) {
    this.menuTree = menuTree;
  }

  public DirNode getPkgRoot() {
    return menuTree.getRoot();
  }

  /**
   * 获取数据库存储的实体对象节点
   *
   * @return 模块名称
   */
  public DirNode getRepositoryObjectNode() {
    return menuTree
        .getRoot()
        .getChildren(JavaDomainTreeKey.BASE)
        .getChildren(JavaDomainTreeKey.INFRASTRUCTURE)
        .getChildren(JavaDomainTreeKey.PREFIX)
        .getChildren(JavaDomainTreeKey.MODEL_NAME)
        .getChildren(JavaDomainTreeKey.REPOSITORY_PO);
  }

  /**
   * 获取数据库存储的接口的节点信息
   *
   * @return 模块名称
   */
  public DirNode getRepositoryDaoNode() {
    return menuTree
        .getRoot()
        .getChildren(JavaDomainTreeKey.INFRASTRUCTURE)
        .getChildren(JavaDomainTreeKey.DOMAIN_REPOSITORY)
        .getChildren(menuTree.getModelName())
        .getChildren(JavaDomainTreeKey.REPOSITORY_MAPPER);
  }

  /**
   * 获取数据库存储的接口的节点信息
   *
   * @return 模块名称
   */
  public DirNode getRepositoryDaoNodeConfig() {
    return menuTree
        .getRoot()
        .getChildren(JavaDomainTreeKey.INFRASTRUCTURE)
        .getChildren(JavaDomainTreeKey.DOMAIN_REPOSITORY)
        .getChildren(menuTree.getModelName())
        .getChildren(JavaDomainTreeKey.REPOSITORY_MAPPER)
        .getChildren(JavaDomainTreeKey.MAPPER_CONFIG);
  }

  /**
   * 获取领域层与存储接口的节点信息
   *
   * @return 模块名称
   */
  public DirNode getRepositoryFacadeNode() {
    return menuTree
        .getRoot()
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
  public DirNode getRepositoryAssemblerNode() {
    return menuTree
        .getRoot()
        .getChildren(JavaDomainTreeKey.INFRASTRUCTURE)
        .getChildren(JavaDomainTreeKey.DOMAIN_REPOSITORY)
        .getChildren(menuTree.getModelName())
        .getChildren(JavaDomainTreeKey.REPOSITORY_ASSEMBLER);
  }

  /**
   * 获取领域存储的对象实现
   *
   * @return 模块信息
   */
  public DirNode getRepositoryPersistenceNode() {
    return menuTree
        .getRoot()
        .getChildren(JavaDomainTreeKey.INFRASTRUCTURE)
        .getChildren(JavaDomainTreeKey.DOMAIN_REPOSITORY)
        .getChildren(menuTree.getModelName())
        .getChildren(JavaDomainTreeKey.REPOSITORY_PERSISTENCE);
  }

  /**
   * 获取领域实体对象节点
   *
   * @return 模块名称
   */
  public DirNode getDomainObjectNode() {

    return menuTree
        .getRoot()
        .getChildren(JavaDomainTreeKey.DOMAIN)
        .getChildren(menuTree.getModelName())
        .getChildren(JavaDomainTreeKey.DOMAIN_ENTITY);
  }

  /**
   * 获取基础资源的实体
   *
   * @return 菜单实体
   */
  public DirNode getInfrastructureEntity() {

    return menuTree
        .getRoot()
        .getChildren(JavaDomainTreeKey.INFRASTRUCTURE)
        .getChildren(JavaDomainTreeKey.INFRASTRUCTURE_ENTITY);
  }

  /**
   * 获取领域服务的节点信息
   *
   * @return 模块名称
   */
  public DirNode getDomainServiceNode() {
    return menuTree
        .getRoot()
        .getChildren(JavaDomainTreeKey.DOMAIN)
        .getChildren(menuTree.getModelName())
        .getChildren(JavaDomainTreeKey.DOMAIN_SERVICE);
  }

  /**
   * 获取应用服务的信息
   *
   * @return 模块名称
   */
  public DirNode getApplicationServiceNode() {
    return menuTree.getRoot().getChildren(JavaDomainTreeKey.APPLICATION);
  }

  /**
   * 获取对外的API
   *
   * @return 模块名称
   */
  public DirNode getInterfaceFacadeNode() {
    return menuTree
        .getRoot()
        .getChildren(JavaDomainTreeKey.INTERFACE)
        .getChildren(JavaDomainTreeKey.INTERFACE_FACADE);
  }

  /**
   * 获取对外的API
   *
   * @return 模块名称
   */
  public DirNode getInterfaceValidNode() {
    return menuTree
        .getRoot()
        .getChildren(JavaDomainTreeKey.INTERFACE)
        .getChildren(JavaDomainTreeKey.INTERFACE_VALID);
  }

  /**
   * 获取API的对象
   *
   * @return 模块名称
   */
  public DirNode getInterfaceObjectNode() {
    return menuTree
        .getRoot()
        .getChildren(JavaDomainTreeKey.INTERFACE)
        .getChildren(JavaDomainTreeKey.INTERFACE_DTO);
  }

  /**
   * 获取转换的对象
   *
   * @return 模块名称
   */
  public DirNode getInterfaceAssemblerNode() {
    return menuTree
        .getRoot()
        .getChildren(JavaDomainTreeKey.INTERFACE)
        .getChildren(JavaDomainTreeKey.INTERFACE_ASSEMBLER);
  }

  /**
   * 错误码包
   *
   * @return 模块名称
   */
  public DirNode getInterfaceErrorCodeNode() {
    return menuTree
        .getRoot()
        .getChildren(JavaDomainTreeKey.INTERFACE)
        .getChildren(JavaDomainTreeKey.INTERFACE_ERROR_CODE);
  }

  /**
   * 配制目录
   *
   * @return 模块名称
   */
  public DirNode getInterfaceConfigNode() {
    return menuTree
        .getRoot()
        .getChildren(JavaDomainTreeKey.INTERFACE)
        .getChildren(JavaDomainTreeKey.INTERFACE_CONFIG);
  }

  /**
   * 静态常量包
   *
   * @return 模块名称
   */
  public DirNode getInterfaceConstant() {
    return menuTree
        .getRoot()
        .getChildren(JavaDomainTreeKey.INTERFACE)
        .getChildren(JavaDomainTreeKey.INTERFACE_CONSTANT);
  }

  /**
   * 前端参数的验证
   *
   * @return 模块名称
   */
  public DirNode getApiCheckParamNode() {
    return menuTree
        .getRoot()
        .getChildren(JavaDomainTreeKey.INTERFACE)
        .getChildren(JavaDomainTreeKey.INTERFACE_CHECK);
  }
}
