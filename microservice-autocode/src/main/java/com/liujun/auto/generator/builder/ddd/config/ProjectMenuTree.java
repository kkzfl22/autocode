package com.liujun.auto.generator.builder.ddd.config;

import com.liujun.auto.config.generate.GenerateConfigProcess;
import com.liujun.auto.config.menutree.DirNode;
import com.liujun.auto.constant.ProjectMenuTreeKey;
import com.liujun.auto.constant.Symbol;

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
  private final DirNode root = new DirNode(ROOT);

  /** 模块名称 */
  private final String modelName;

  public ProjectMenuTree(String modelName) {
    this.modelName = modelName;
    // 执行构建代码目录结构
    this.builder();
  }

  /** 构建基础的目录结构 */
  private DirNode builder() {
    // 添加根目录下的src目录
    DirNode srcNode = root.addChildren(ProjectMenuTreeKey.SRC.getKey());
    // 添加main
    DirNode srcMainNode = srcNode.addChildren(ProjectMenuTreeKey.SRC_MAIN.getKey());
    // 添加java和
    srcMainNode.addChildrenOnly(ProjectMenuTreeKey.MAIN_JAVA.getKey());
    // resource目录
    DirNode resourceNode = srcMainNode.addChildren(ProjectMenuTreeKey.MAIN_RESOURCES.getKey());
    // 添加mapper的目录
    DirNode mapperNode = resourceNode.addChildren(ProjectMenuTreeKey.RESOURCES_MAPPER.getKey());
    // 国际化资源目标
    DirNode i18nResourceNode = resourceNode.addChildren(ProjectMenuTreeKey.RESOURCE_I18N.getKey());
    DirNode i18nModuleNode =
        i18nResourceNode.addChildren(ProjectMenuTreeKey.I18N_RESOURCE.getKey());
    // 模块
    i18nModuleNode.addChildrenOnly(modelName);

    // 在mapper添加模块
    DirNode modelNode = mapperNode.addChildren(modelName);
    // 模块下添加repository目录
    modelNode.addChildrenOnly(ProjectMenuTreeKey.RESOURCES_REPOSITORY.getKey());

    // 添加test目录
    DirNode srcTestNode = srcNode.addChildren(ProjectMenuTreeKey.SRC_TEST.getKey());
    // test目录下添加java
    srcTestNode.addChildrenOnly(ProjectMenuTreeKey.MAIN_JAVA.getKey());
    // test目录下添加resource目录
    srcTestNode.addChildrenOnly(ProjectMenuTreeKey.MAIN_RESOURCES.getKey());

    // resource目录
    DirNode testResourceNode = srcTestNode.addChildren(ProjectMenuTreeKey.MAIN_RESOURCES.getKey());
    // 添加mapper的目录
    DirNode testMapperNode =
        testResourceNode.addChildren(ProjectMenuTreeKey.RESOURCES_MAPPER.getKey());
    // 在mapper添加模块
    DirNode testModelNode = testMapperNode.addChildren(modelName);
    // 模块下添加repository目录
    testModelNode.addChildrenOnly(ProjectMenuTreeKey.RESOURCES_REPOSITORY.getKey());

    return root;
  }

  public DirNode getRoot() {
    return root;
  }

  public String getModelName() {
    return modelName;
  }
}
