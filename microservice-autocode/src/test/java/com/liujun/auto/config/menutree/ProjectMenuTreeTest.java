package com.liujun.auto.config.menutree;

import com.liujun.auto.constant.ProjectMenuTreeKey;
import com.liujun.auto.generator.builder.ddd.config.ProjectMenuTree;
import org.junit.Assert;
import org.junit.Test;

/**
 * 项目的代码目录树
 *
 * @author liujun
 * @version 0.0.1
 */
public class ProjectMenuTreeTest {

  @Test
  public void testTreeProject() {
    String modelName = "pap";
    ProjectMenuTree menuTree = new ProjectMenuTree(modelName);
    // 构建项目目录树
    DirNode rootNode = menuTree.getRoot();
    DirTreeTest.treeNodePrint(rootNode, 0);
    DirNode node =
        rootNode
            .getChildren(ProjectMenuTreeKey.SRC.getKey())
            .getChildren(ProjectMenuTreeKey.SRC_MAIN.getKey())
            .getChildren(ProjectMenuTreeKey.MAIN_RESOURCES.getKey())
            .getChildren(ProjectMenuTreeKey.RESOURCES_MAPPER.getKey())
            .getChildren(modelName)
            .getChildren(ProjectMenuTreeKey.RESOURCES_REPOSITORY.getKey());
    System.out.println(node);
    Assert.assertNotNull(node);

    // 使用节点树处理转换为路径
    Assert.assertEquals("src/main/resources/mapper/pap/repository", node.outPath());
  }
}
