package com.liujun.micro.autocode.config.menutree;

import com.liujun.micro.autocode.constant.ProjectMenuTreeKey;

/**
 * 用于目录树构建代码中项目的路径
 *
 * @author liujun
 * @version 0.0.1
 */
public class MenuTreeProjectPath {

  /** 实例信息 */
  public static final MenuTreeProjectPath INSTANCE =
      new MenuTreeProjectPath(ProjectMenuTree.INSTANCE);

  private final ProjectMenuTree projectMenuTree;

  public MenuTreeProjectPath(ProjectMenuTree projectMenuTree) {
    this.projectMenuTree = projectMenuTree;
  }

  /**
   * 获取mybatis的目录节点
   *
   * @return 模块名称
   */
  public MenuNode getRepositoryMybatisMapperNode() {
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
   * @return 模块名称
   */
  public MenuNode getSrcJavaNode() {
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
   * @return 模块名称
   */
  public MenuNode getTestJavaNode() {
    MenuNode javaNode =
        projectMenuTree
            .getRoot()
            .getChildren(ProjectMenuTreeKey.SRC.getKey())
            .getChildren(ProjectMenuTreeKey.SRC_TEST.getKey())
            .getChildren(ProjectMenuTreeKey.MAIN_JAVA.getKey());
    return javaNode;
  }
}
