package com.liujun.micro.autocode.generator.builder.operator.utils;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.utils.MenuTreeProcessUtil;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.utils.FileUtils;

/**
 * 生成的文件输出公共方法
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateOutFileUtils {

  /**
   * 进行java文件输出操作
   *
   * @param sb 输出字符
   * @param path 最基础的输出路径
   * @param definePackage 工程内的路径信息
   * @param className 输出的文件名
   */
  public static void outJavaFile(
      StringBuilder sb, String path, String definePackage, String className) {

    // 定义输出的文件名
    String fileName = className + JavaKeyWord.FILE_SUFFIX;
    outFile(sb, path, definePackage, fileName);
  }

  /**
   * 文件输出操作
   *
   * @param sb 输出字符
   * @param path 最基础的输出路径
   * @param definePackage 工程内的路径信息
   * @param fileName 输出的文件名
   */
  public static void outFile(StringBuilder sb, String path, String definePackage, String fileName) {

    // 获取以路径/进行输出
    String javaPathStr = MenuTreeProcessUtil.outPath(definePackage);

    StringBuilder outPath = new StringBuilder();
    // 输出的基础路径
    outPath.append(path);
    outPath.append(Symbol.PATH);
    outPath.append(javaPathStr);

    // 文件输出操作
    FileUtils.writeFile(outPath.toString(), fileName, sb);
  }
}
