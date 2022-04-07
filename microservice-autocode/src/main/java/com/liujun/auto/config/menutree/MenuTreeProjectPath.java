package com.liujun.auto.config.menutree;

import com.liujun.auto.constant.ProjectMenuTreeKey;

/**
 * 用于目录树构建代码中项目的路径
 *
 * @author liujun
 * @version 0.0.1
 */
public class MenuTreeProjectPath {

    /**
     * 实例信息
     */
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
        return projectMenuTree
                .getRoot()
                .getChildren(ProjectMenuTreeKey.SRC.getKey())
                .getChildren(ProjectMenuTreeKey.SRC_MAIN.getKey())
                .getChildren(ProjectMenuTreeKey.MAIN_RESOURCES.getKey())
                .getChildren(ProjectMenuTreeKey.RESOURCES_MAPPER.getKey())
                .getChildren(projectMenuTree.getModelName())
                .getChildren(ProjectMenuTreeKey.RESOURCES_REPOSITORY.getKey());
    }

    /**
     * 获取java的源代码目录
     *
     * @return 模块名称
     */
    public MenuNode getSrcJavaNode() {
        return projectMenuTree
                .getRoot()
                .getChildren(ProjectMenuTreeKey.SRC.getKey())
                .getChildren(ProjectMenuTreeKey.SRC_MAIN.getKey())
                .getChildren(ProjectMenuTreeKey.MAIN_JAVA.getKey());
    }

    /**
     * 获取java的源代码目录
     *
     * @return 模块名称
     */
    public MenuNode getTestJavaNode() {
        return projectMenuTree
                .getRoot()
                .getChildren(ProjectMenuTreeKey.SRC.getKey())
                .getChildren(ProjectMenuTreeKey.SRC_TEST.getKey())
                .getChildren(ProjectMenuTreeKey.MAIN_JAVA.getKey());
    }


  /**
   * 获取java的源代码目录
   *
   * @return 模块名称
   */
  public MenuNode getTestResourceNode() {
    return projectMenuTree
            .getRoot()
            .getChildren(ProjectMenuTreeKey.SRC.getKey())
            .getChildren(ProjectMenuTreeKey.SRC_TEST.getKey())
            .getChildren(ProjectMenuTreeKey.MAIN_RESOURCES.getKey());
  }


    /**
     * 获取java的资源文件目录
     *
     * @return 模块名称
     */
    public MenuNode getResourceNode() {
        return projectMenuTree
                .getRoot()
                .getChildren(ProjectMenuTreeKey.SRC.getKey())
                .getChildren(ProjectMenuTreeKey.SRC_MAIN.getKey())
                .getChildren(ProjectMenuTreeKey.MAIN_RESOURCES.getKey());
    }


    /**
     * 获取java的资源文件目录
     *
     * @return 模块名称
     */
    public MenuNode getI18nNode() {
        return projectMenuTree
                .getRoot()
                .getChildren(ProjectMenuTreeKey.SRC.getKey())
                .getChildren(ProjectMenuTreeKey.SRC_MAIN.getKey())
                .getChildren(ProjectMenuTreeKey.MAIN_RESOURCES.getKey())
                .getChildren(ProjectMenuTreeKey.RESOURCE_I18N.getKey())
                .getChildren(ProjectMenuTreeKey.I18N_RESOURCE.getKey())
                .getChildren(projectMenuTree.getModelName());
    }
}
