package com.liujun.micro.autocode.config.menuTree;

import com.liujun.micro.autocode.constant.JavaDomainTreeKey;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 目录树节点
 *
 * @author liujun
 * @version 0.0.1
 */
@Getter
public class MenuNode {

  /** 编号 */
  private String code;

  /** 路径 */
  private String path;

  /** 子级目录树 */
  private Map<String, MenuNode> children = new HashMap<>();

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


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MenuNode menuNode = (MenuNode) o;
    return Objects.equals(code, menuNode.code) &&
            Objects.equals(path, menuNode.path) &&
            Objects.equals(parent, menuNode.parent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, path, parent);
  }
}
