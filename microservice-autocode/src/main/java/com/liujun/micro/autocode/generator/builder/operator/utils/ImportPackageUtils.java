package com.liujun.micro.autocode.generator.builder.operator.utils;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 导包的公共类处理
 *
 * @author liujun
 * @version 0.0.1
 */
public class ImportPackageUtils {

  /**
   * 获取定义的javabean信息
   *
   * @param packageMap 包的map
   * @param packageFlag 具体包的标识
   * @param tableName 表名
   * @return 包的完整路径
   */
  public static ImportPackageInfo getDefineClass(
      Map<String, Map<String, ImportPackageInfo>> packageMap,
      String packageFlag,
      String tableName) {

    Map<String, ImportPackageInfo> importPackage = packageMap.get(packageFlag);

    if (null == importPackage) {
      return null;
    }

    ImportPackageInfo importBean = importPackage.get(tableName);

    if (null == importBean) {
      return null;
    }

    return importBean;
  }

  /**
   * 进行package信息的输出操作
   *
   * @param packageInfo 包信息
   * @return 路径信息
   */
  public static String packageOut(ImportPackageInfo packageInfo) {

    return packageInfo.getPackagePath() + Symbol.POINT + packageInfo.getClassName();
  }

  /**
   * 将当前文件信息放入到流程的map中
   *
   * @param tableName 表名
   * @param packageMap 用于存储的map
   * @param packageFlagKey 包的定义key
   * @param packageInfo 路径信息
   * @param initSize 初始化大小
   */
  public static void putPackageInfo(
      String tableName,
      Map<String, Map<String, ImportPackageInfo>> packageMap,
      String packageFlagKey,
      ImportPackageInfo packageInfo,
      int initSize) {
    Map<String, ImportPackageInfo> packageMapInfo = packageMap.get(packageFlagKey);

    if (null == packageMapInfo) {
      packageMapInfo = new HashMap<>(initSize);
      packageMap.put(packageFlagKey, packageMapInfo);
    }
    packageMapInfo.put(tableName, packageInfo);
  }
}
