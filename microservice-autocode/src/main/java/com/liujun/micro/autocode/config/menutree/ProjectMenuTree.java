package com.liujun.micro.autocode.config.menutree;

import com.liujun.micro.autocode.config.generate.GenerateConfigProcess;
import com.liujun.micro.autocode.constant.ProjectMenuTreeKey;
import com.liujun.micro.autocode.constant.Symbol;

/**
 * 项目的目录树构建
 *
 * @author liujun
 * @version 0.0.1
 */
public class ProjectMenuTree {

  /** 顶层根节点 */
  private static final String ROOT = Symbol.PATH;

  /** 实例信息 */
  public static final ProjectMenuTree INSTANCE =
      new ProjectMenuTree(
          GenerateConfigProcess.INSTANCE
              .getCfgEntity()
              .getGenerate()
              .getCodeMenuTree()
              .getDomainName());

  /** 目录树结构的最顶层节点 */
  private final MenuNode root = new MenuNode(ROOT);

  /** 模块名称 */
  private final String modelName;

  public ProjectMenuTree(String modelName) {
    this.modelName = modelName;
    // 执行构建代码目录结构
    this.builder();
  }

  /** 构建基础的目录结构 */
  private MenuNode builder() {
    // 添加根目录下的src目录
    MenuNode srcNode = root.addChildren(ProjectMenuTreeKey.SRC.getKey());
    // 添加main
    MenuNode srcMainNode = srcNode.addChildren(ProjectMenuTreeKey.SRC_MAIN.getKey());
    // 添加java和
    srcMainNode.addChildrenOnly(ProjectMenuTreeKey.MAIN_JAVA.getKey());
    // resource目录
    MenuNode resourceNode = srcMainNode.addChildren(ProjectMenuTreeKey.MAIN_RESOURCES.getKey());
    // 添加mapper的目录
    MenuNode mapperNode = resourceNode.addChildren(ProjectMenuTreeKey.RESOURCES_MAPPER.getKey());
    // 国际化资源目标
    MenuNode i18nResourceNode = resourceNode.addChildren(ProjectMenuTreeKey.RESOURCE_I18N.getKey());
    MenuNode i18nModuleNode =
        i18nResourceNode.addChildren(ProjectMenuTreeKey.I18N_RESOURCE.getKey());
    // 模块
    i18nModuleNode.addChildrenOnly(modelName);

    // 在mapper添加模块
    MenuNode modelNode = mapperNode.addChildren(modelName);
    // 模块下添加repository目录
    modelNode.addChildrenOnly(ProjectMenuTreeKey.RESOURCES_REPOSITORY.getKey());

    // 添加test目录
    MenuNode srcTestNode = srcNode.addChildren(ProjectMenuTreeKey.SRC_TEST.getKey());
    // test目录下添加java
    srcTestNode.addChildrenOnly(ProjectMenuTreeKey.MAIN_JAVA.getKey());
    // test目录下添加resource目录
    srcTestNode.addChildrenOnly(ProjectMenuTreeKey.MAIN_RESOURCES.getKey());

    // resource目录
    MenuNode testResourceNode = srcTestNode.addChildren(ProjectMenuTreeKey.MAIN_RESOURCES.getKey());
    // 添加mapper的目录
    MenuNode testMapperNode =
        testResourceNode.addChildren(ProjectMenuTreeKey.RESOURCES_MAPPER.getKey());
    // 在mapper添加模块
    MenuNode testModelNode = testMapperNode.addChildren(modelName);
    // 模块下添加repository目录
    testModelNode.addChildrenOnly(ProjectMenuTreeKey.RESOURCES_REPOSITORY.getKey());

    return root;
  }

  public MenuNode getRoot() {
    return root;
  }

  public String getModelName() {
    return modelName;
  }
}
