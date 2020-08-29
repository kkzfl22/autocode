package com.liujun.micro.autocode.generator.builder.utils;

import com.liujun.micro.autocode.config.menuTree.MenuNode;
import com.liujun.micro.autocode.constant.Symbol;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 目录树的处理
 *
 * @author liujun
 * @version 0.0.1
 */
public class MenuTreeProcessUtil {

  /**
   * 按路径/进行输出
   *
   * @return
   */
  public static String outPath(String javaDefinePackage) {
    if (StringUtils.isEmpty(javaDefinePackage)) {
      return javaDefinePackage;
    }

    String outValue = javaDefinePackage.replaceAll(Symbol.SPIT_POINT, Symbol.PATH);
    return outValue;
  }

  /**
   * 按java的包路径进行输出
   *
   * @return
   */
  public static String outJavaPackage(MenuNode outNode) {
    return outPath(outNode, Symbol.POINT);
  }

  /**
   * 按路径进行输出操作
   *
   * @return
   */
  public static String outPath(MenuNode outNode) {
    return outPath(outNode, Symbol.PATH);
  }

  /**
   * 输出路径
   *
   * @param outNode 当前节点信息
   * @param symbolInfo 分隔符
   * @return 输出路径
   */
  private static String outPath(MenuNode outNode, String symbolInfo) {
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
