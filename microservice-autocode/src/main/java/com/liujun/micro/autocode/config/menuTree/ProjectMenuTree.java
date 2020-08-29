package com.liujun.micro.autocode.config.menuTree;

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

  /** 目录树结构的最顶层节点 */
  private MenuNode root = new MenuNode(ROOT);

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
    srcMainNode.addChildren(ProjectMenuTreeKey.MAIN_JAVA.getKey());
    // resource目录
    MenuNode resourceNode = srcMainNode.addChildren(ProjectMenuTreeKey.MAIN_RESOURCES.getKey());
    // 添加mapper的目录
    MenuNode mapperNode = resourceNode.addChildren(ProjectMenuTreeKey.RESOURCES_MAPPER.getKey());
    // 在mapper添加模块
    MenuNode modelNode = mapperNode.addChildren(modelName);
    // 模块下添加repository目录
    modelNode.addChildren(ProjectMenuTreeKey.RESOURCES_REPOSITORY.getKey());

    // 添加test目录
    MenuNode srcTestNode = srcNode.addChildren(ProjectMenuTreeKey.SRC_TEST.getKey());
    // test目录下添加java
    srcTestNode.addChildren(ProjectMenuTreeKey.MAIN_JAVA.getKey());
    // test目录下添加resource目录
    srcTestNode.addChildren(ProjectMenuTreeKey.MAIN_RESOURCES.getKey());

    return root;
  }

  public MenuNode getRoot() {
    return root;
  }

  public String getModelName() {
    return modelName;
  }
}
