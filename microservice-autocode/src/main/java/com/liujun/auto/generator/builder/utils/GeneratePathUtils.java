package com.liujun.auto.generator.builder.utils;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.config.MenuTreeProjectPath;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;

/**
 * 生成的路径处理
 *
 * @author liujun
 * @version 0.0.1
 */
public class GeneratePathUtils {

  /**
   * 获取服务层的基础路径
   *
   * @param param 上下文信息
   * @return
   */
  public static String outServicePath(GenerateCodeContext param) {
    return ouProjectPath(param) + Symbol.PATH + param.getModuleName();
  }

  /**
   * 获取服务层的基础路径
   *
   * @param param 上下文信息
   * @return
   */
  public static String outApiPath(GenerateCodeContext param) {
    return ouProjectPath(param) + Symbol.PATH + param.getModuleName();
  }

  /**
   * 获取项目的的基础路径
   *
   * @param param 上下文信息
   * @return
   */
  public static String ouProjectPath(GenerateCodeContext param) {
    return param.getFileBasePath()
        + Symbol.PATH
        + param.getGenerateConfig().getGenerate().getProjectName();
  }

  /**
   * 输出项目内的相对路径
   *
   * @param projectPath 项目路径信息
   * @param pkg 包信息
   * @return 相对路径信息
   */
  public static String outRelativePath(MenuTreeProjectPath projectPath, ImportPackageInfo pkg) {
    return projectPath.getSrcJavaNode().outPath() + Symbol.PATH + pkg.getPackagePath();
  }
}
