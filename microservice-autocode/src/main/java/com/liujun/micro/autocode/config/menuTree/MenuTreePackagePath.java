package com.liujun.micro.autocode.config.menuTree;

import com.liujun.micro.autocode.constant.JavaDomainTreeKey;

/**
 * 用于目录树构建代码中java的包路径
 *
 * @author liujun
 * @version 0.0.1
 */
public class MenuTreePackagePath {

  /**
   * 获取数据库存储的实体对象节点
   *
   * @param menuTree 树形结构
   * @return 模块名称
   */
  public static MenuNode getRepositoryObjectNode(DomainMenuTree menuTree) {
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
   * @param menuTree 树形结构
   * @return 模块名称
   */
  public static MenuNode getRepositoryDaoNode(DomainMenuTree menuTree) {
    MenuNode poNode =
        menuTree
            .getDefineRoot()
            .getChildren(JavaDomainTreeKey.DOMAIN)
            .getChildren(menuTree.getModelName())
            .getChildren(JavaDomainTreeKey.DOMAIN_REPOSITORY)
            .getChildren(JavaDomainTreeKey.REPOSITORY_MAPPER);
    return poNode;
  }


}
