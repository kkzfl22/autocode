package com.liujun.micro.autocode.config.menutree;

import com.liujun.micro.autocode.constant.JavaDomainTreeKey;
import com.liujun.micro.autocode.constant.Symbol;
import lombok.Getter;

import java.util.*;

/**
 * 目录树节点
 *
 * @author liujun
 * @version 0.0.1
 */
@Getter
public class MenuNode {

  /** 编号 */
  private final String code;

  /** 路径 */
  private final String path;

  /** 子级目录树 */
  private final Map<String, MenuNode> children = new HashMap<>();

  /** 记录下父节点，用于路径的输出操作 */
  private MenuNode parent;

  /**
   * 目录树cod与路相同的情况
   *
   * @param code
   */
  public MenuNode(String code) {
    this.code = code;
    this.path = code;
  }

  /**
   * 构建树形结构
   *
   * @param code
   */
  public MenuNode(JavaDomainTreeKey code) {
    this(code.getKey());
  }

  /**
   * 添加子节点的方法，通过枚举对象进行构建l
   *
   * @param treeNode 树形结构信息
   * @return 节点信息
   */
  public MenuNode addChildren(JavaDomainTreeKey treeNode) {
    MenuNode childrenNode = new MenuNode(treeNode);
    childrenNode.parent = this;
    this.getChildren().put(treeNode.getKey(), childrenNode);
    return childrenNode;
  }

  /**
   * 添加子节点的方法，仅添加子节点，不返回对象
   *
   * @param treeNode 树形结构信息
   * @return 节点信息
   */
  public void addChildrenOnly(JavaDomainTreeKey treeNode) {
    MenuNode childrenNode = new MenuNode(treeNode);
    childrenNode.parent = this;
    this.getChildren().put(treeNode.getKey(), childrenNode);
  }

  /**
   * 添加子节点的方法，通过编码进行构建
   *
   * @param code 树形结构信息
   * @return 节点信息
   */
  public MenuNode addChildren(String code) {
    MenuNode childrenNode = new MenuNode(code);
    childrenNode.parent = this;
    this.getChildren().put(code, childrenNode);
    return childrenNode;
  }

  /**
   * 添加子节点的方法，通过编码进行构建,仅添加
   *
   * @param code 树形结构信息
   * @return 节点信息
   */
  public void addChildrenOnly(String code) {
    MenuNode childrenNode = new MenuNode(code);
    childrenNode.parent = this;
    this.getChildren().put(code, childrenNode);
  }

  /**
   * 获取子节点的服务，以枚举为key
   *
   * @param key
   * @return
   */
  public MenuNode getChildren(JavaDomainTreeKey key) {
    return this.getChildren().get(key.getKey());
  }

  /**
   * 以字符串为key，获取节点信息
   *
   * @param key
   * @return
   */
  public MenuNode getChildren(String key) {
    return this.getChildren().get(key);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("MenuNode{");
    sb.append("code='").append(code).append('\'');
    sb.append(", path='").append(path).append('\'');
    sb.append('}');
    return sb.toString();
  }

  /**
   * 按java的包路径进行输出
   *
   * @return
   */
  public String outJavaPackage() {
    return outPath(this, Symbol.POINT);
  }

  /**
   * 按路径进行输出操作
   *
   * @return
   */
  public String outPath() {
    return outPath(this, Symbol.PATH);
  }

  /**
   * 输出路径
   *
   * @param outNode 当前节点信息
   * @param symbolInfo 分隔符
   * @return 输出路径
   */
  private String outPath(MenuNode outNode, String symbolInfo) {
    List<String> javaPackage = new ArrayList<>();

    do {
      String outPath = outNode.getPath() + symbolInfo;
      javaPackage.add(outPath);
      outNode = outNode.getParent();
    } while (outNode != null && outNode.getParent() != null);

    StringBuilder outJavaPackageBuilder = new StringBuilder();
    for (int i = javaPackage.size() - 1; i >= 0; i--) {
      outJavaPackageBuilder.append(javaPackage.get(i));
    }
    outJavaPackageBuilder.deleteCharAt(outJavaPackageBuilder.length() - 1);

    return outJavaPackageBuilder.toString();
  }
}
