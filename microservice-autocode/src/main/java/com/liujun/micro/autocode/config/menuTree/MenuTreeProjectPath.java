package com.liujun.micro.autocode.config.menuTree;

import com.liujun.micro.autocode.constant.ProjectMenuTreeKey;

/**
 * 用于目录树构建代码中项目的路径
 *
 * @author liujun
 * @version 0.0.1
 */
public class MenuTreeProjectPath {

  /**
   * 获取mybatis的目录节点
   *
   * @param projectMenuTree 树形结构
   * @return 模块名称
   */
  public static MenuNode getRepositoryMybatisMapperNode(ProjectMenuTree projectMenuTree) {
    MenuNode mapperNode =
        projectMenuTree
            .getRoot()
            .getChildren(ProjectMenuTreeKey.SRC.getKey())
            .getChildren(ProjectMenuTreeKey.SRC_MAIN.getKey())
            .getChildren(ProjectMenuTreeKey.MAIN_RESOURCES.getKey())
            .getChildren(ProjectMenuTreeKey.RESOURCES_MAPPER.getKey())
            .getChildren(projectMenuTree.getModelName())
            .getChildren(ProjectMenuTreeKey.RESOURCES_REPOSITORY.getKey());
    return mapperNode;
  }

  /**
   * 获取java的源代码目录
   *
   * @param projectMenuTree 树形结构
   * @return 模块名称
   */
  public static MenuNode getSrcJavaNode(ProjectMenuTree projectMenuTree) {
    MenuNode javaNode =
        projectMenuTree
            .getRoot()
            .getChildren(ProjectMenuTreeKey.SRC.getKey())
            .getChildren(ProjectMenuTreeKey.SRC_MAIN.getKey())
            .getChildren(ProjectMenuTreeKey.MAIN_JAVA.getKey());
    return javaNode;
  }


  /**
   * 获取java的源代码目录
   *
   * @param projectMenuTree 树形结构
   * @return 模块名称
   */
  public static MenuNode getTestJavaNode(ProjectMenuTree projectMenuTree) {
    MenuNode javaNode =
            projectMenuTree
                    .getRoot()
                    .getChildren(ProjectMenuTreeKey.SRC.getKey())
                    .getChildren(ProjectMenuTreeKey.SRC_TEST.getKey())
                    .getChildren(ProjectMenuTreeKey.MAIN_JAVA.getKey());
    return javaNode;
  }

}
