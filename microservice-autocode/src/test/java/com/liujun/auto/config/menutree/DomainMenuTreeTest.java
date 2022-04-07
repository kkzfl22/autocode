package com.liujun.auto.config.menutree;

import com.liujun.auto.constant.JavaDomainTreeKey;
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
    // DomainMenuTree menuTree = DomainMenuTree.INSTANCE;
    // 构建目录树
    MenuNode rootNode = menuTree.getDefineRoot();
    treeNodePrint(rootNode, 0);
    MenuNode node =
        rootNode
            .getChildren(JavaDomainTreeKey.INFRASTRUCTURE)
            .getChildren(JavaDomainTreeKey.INFRASTRUCTURE_ALGORITHM);
    System.out.println(node);
    Assert.assertNotNull(node);

    Assert.assertEquals("com.liujun.test.infrastructure.algorithm", node.outJavaPackage());
    // 使用java包转换为路径
    Assert.assertEquals("com/liujun/test/infrastructure/algorithm", node.outPath());
    // 使用节点树处理转换为路径
    Assert.assertEquals("com/liujun/test/infrastructure/algorithm", node.outPath());
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

  @Test
  public void testBooleanNull() {
    Boolean value = null;
    Assert.assertNotEquals(Boolean.TRUE, value);
  }
}
