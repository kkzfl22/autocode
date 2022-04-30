package com.liujun.auto.config.menutree;

import com.liujun.auto.config.generate.GenerateConfigProcess;
import com.liujun.auto.constant.JavaDomainTreeKey;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.constant.DirNameEnum;
import com.liujun.auto.generator.builder.utils.StringDataUtils;

import java.util.*;

/**
 * 目录树节点
 *
 * @author liujun
 * @version 0.0.1
 */
public class DirNode {

  /** 编号 */
  private final String code;

  /** 路径 */
  private final String path;

  /** 子级目录树 */
  private final Map<String, DirNode> children = new HashMap<>();

  /** 记录下父节点，用于路径的输出操作 */
  private DirNode parent;

  /**
   * 目录树cod与路相同的情况
   *
   * @param code
   */
  public DirNode(String code) {
    this.code = code;
    this.path = code;
  }

  /**
   * 构建树形结构
   *
   * @param code
   */
  public DirNode(JavaDomainTreeKey code) {
    this(code.getKey());
  }

  /**
   * 添加子节点的方法，通过枚举对象进行构建l
   *
   * @param treeNode 树形结构信息
   * @return 节点信息
   */
  public DirNode addChildren(JavaDomainTreeKey treeNode) {
    DirNode childrenNode = new DirNode(treeNode);
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
    DirNode childrenNode = new DirNode(treeNode);
    childrenNode.parent = this;
    this.getChildren().put(treeNode.getKey(), childrenNode);
  }

  /**
   * 添加子节点的方法，通过编码进行构建
   *
   * @param code 树形结构信息
   * @return 节点信息
   */
  public DirNode addChildren(String code) {
    DirNode childrenNode = new DirNode(code);
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
    DirNode childrenNode = new DirNode(code);
    childrenNode.parent = this;
    this.getChildren().put(code, childrenNode);
  }

  /**
   * 获取子节点的服务，以枚举为key
   *
   * @param key
   * @return
   */
  public DirNode getChildren(JavaDomainTreeKey key) {
    return this.getChildren().get(key.getKey());
  }

  /**
   * 以字符串为key，获取节点信息
   *
   * @param key
   * @return
   */
  public DirNode getChildren(String key) {
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
  private String outPath(DirNode outNode, String symbolInfo) {
    List<String> javaPackage = new ArrayList<>();

    do {
      String outNodePath = pathProcess(outNode.getPath());
      String outPath = outNodePath + symbolInfo;
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

  /**
   * 路径处理
   *
   * @param pathName
   * @return
   */
  private String pathProcess(String pathName) {
    // 当检查到基础路径，则返回配制的路径
    if (DirNameEnum.BASE.getName().equals(pathName)) {
      return StringDataUtils.empty(
          GenerateConfigProcess.INSTANCE
              .getCfgEntity()
              .getGenerate()
              .getCodeMenuTree()
              .getBaseDir());
    }
    // 模块的名称
    if (DirNameEnum.MODEL_NAME.getName().equals(pathName)) {
      return StringDataUtils.empty(
          GenerateConfigProcess.INSTANCE
              .getCfgEntity()
              .getGenerate()
              .getCodeMenuTree()
              .getDomainName());
    }
    // 基础层中的名称前缀信息
    if (DirNameEnum.PREFIX.getName().equals(pathName)) {
      return StringDataUtils.empty(
          GenerateConfigProcess.INSTANCE
              .getCfgEntity()
              .getGenerate()
              .getCodeMenuTree()
              .getInfrastructurePrefix());
    }
    return pathName;
  }

  public String getCode() {
    return code;
  }

  public String getPath() {
    return path;
  }

  public Map<String, DirNode> getChildren() {
    return children;
  }

  public DirNode getParent() {
    return parent;
  }
}
