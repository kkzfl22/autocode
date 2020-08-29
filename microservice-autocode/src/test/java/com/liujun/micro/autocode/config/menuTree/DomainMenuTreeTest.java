package com.liujun.micro.autocode.config.menuTree;

import com.liujun.micro.autocode.constant.JavaDomainTreeKey;
import com.liujun.micro.autocode.generator.builder.utils.MenuTreeProcessUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * 进行树目录构建测试
 *
 * @author liujun
 * @version 0.0.1
 */
public class DomainMenuTreeTest {

  @Test
  public void testTreeBuilder() {
    DomainMenuTree menuTree = new DomainMenuTree("com.liujun.test", "pap");
    // 构建目录树
    MenuNode rootNode = menuTree.getDefineRoot();
    treeNodePrint(rootNode, 0);
    MenuNode node =
        rootNode
            .getChildren(JavaDomainTreeKey.INFRASTRUCTURE)
            .getChildren(JavaDomainTreeKey.INFRASTRUCTURE_ALGORITHM);
    System.out.println(node);
    Assert.assertNotNull(node);
    String outJavaPackage = MenuTreeProcessUtil.outJavaPackage(node);
    Assert.assertEquals("com.liujun.test.infrastructure.algorithm", outJavaPackage);
    // 使用java包转换为路径
    Assert.assertEquals(
        "com/liujun/test/infrastructure/algorithm", MenuTreeProcessUtil.outPath(outJavaPackage));
    // 使用节点树处理转换为路径
    Assert.assertEquals(
        "com/liujun/test/infrastructure/algorithm", MenuTreeProcessUtil.outPath(node));
  }

  /**
   * 树形节点打印
   *
   * @param node
   * @param level
   */
  public static void treeNodePrint(MenuNode node, int level) {
    for (int i = 0; i < level; i++) {
      System.out.print("--/");
    }
    System.out.println(node.getPath());

    if (node.getChildren().isEmpty()) {
      return;
    }
    for (Map.Entry<String, MenuNode> nodeItem : node.getChildren().entrySet()) {
      treeNodePrint(nodeItem.getValue(), level + 1);
    }
  }
}
