package com.liujun.auto.config.generate;

import com.liujun.auto.config.generate.entity.GenerateConfigEntity;
import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.config.generate.entity.TypeInfo;
import com.liujun.auto.config.generate.entity.WhereInfo;
import com.liujun.auto.constant.GenerateDefineFlag;
import com.liujun.auto.constant.MethodOperatorEnum;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.constant.CodeComment;
import com.liujun.auto.generator.builder.ddd.constant.JavaVarName;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 生成代码的相关配制信息
 *
 * @author liujun
 * @version 0.0.1
 * @date 2019/09/10
 */
public class GenerateConfigProcess {

  public static final GenerateConfigProcess INSTANCE = new GenerateConfigProcess();

  /** 数据加载操作 */
  private final GenerateConfigEntity cfgEntity = GenerateConfigLoader.INSTANCE.getCfgEntity();

  private GenerateConfigProcess() {
    // 执行数据处理
    process(cfgEntity.getGenerate().getMethodList());
  }

  /**
   * 进行数据解析操作
   *
   * @param methodList 方法列表
   */
  private void process(List<MethodInfo> methodList) {
    // 1,排队当前已经禁用的方法
    cleanDisableMethod(methodList);

    // 1,对代码方法进行排序
    Collections.sort(cfgEntity.getGenerate().getMethodList());

    // 进行方法的参数设置
    for (MethodInfo method : methodList) {
      // 设置参数信息
      method.setParamType(this.getParamType(method.getParams()));
      // 设置返回值
      method.setReturnType(this.getReturnType(method, method.getReturns()));
      // 设置where条件
      method.setWhereInfo(this.getWhereInfo(method.getWhere()));
      // 设置类型
      method.setOperatorType(MethodOperatorEnum.getType(method.getOperator()));
    }
  }

  /**
   * 排队掉已经禁用掉的方法
   *
   * @param methodList 方法信息
   */
  private void cleanDisableMethod(List<MethodInfo> methodList) {

    Iterator<MethodInfo> iterator = methodList.iterator();

    while (iterator.hasNext()) {
      MethodInfo methodInfo = iterator.next();
      // 如果当前禁用开关打开，则表示当前方法被禁用
      if (methodInfo.getDisable() != null && methodInfo.getDisable()) {
        iterator.remove();
      }
    }
  }

  /**
   * 获取返回的类型对象信息
   *
   * @param dataItem 数所类型信息
   * @return 类型信息
   */
  private List<TypeInfo> getParamType(String dataItem) {

    if (StringUtils.isEmpty(dataItem)) {
      return Arrays.asList(
          this.getTypeEntity(
              GenerateDefineFlag.TABLE_NAME.getDefineFlag(),
              JavaVarName.DEF_METHOD_PARAM_NAME,
              CodeComment.METHOD_PARAM_DOC));
    }

    return this.getParamTypeInfo(dataItem);
  }

  /**
   * 获取返回的类型对象信息
   *
   * @param method 方法
   * @param dataItem 数所类型信息
   * @return 类型信息
   */
  private TypeInfo getReturnType(MethodInfo method, String dataItem) {

    return this.getTypeEntity(dataItem, null, CodeComment.METHOD_RESULT_UPDATE_DOC);
  }

  /**
   * 其存在以下这几种情况
   *
   * <p>java.util.List<$table_name> 集合直接与数据库对应
   *
   * <p>$table_name 单对象 与数据库对象对应
   *
   * <p>String,String 参数 通过where条件中的字段与数据库对应
   *
   * @param paramTypeStr
   * @return
   */
  private List<TypeInfo> getParamTypeInfo(String paramTypeStr) {
    if (StringUtils.isEmpty(paramTypeStr)) {
      return new ArrayList<>(0);
    }

    String[] dataInfo = paramTypeStr.split(Symbol.COMMA);
    List<TypeInfo> dataList = new ArrayList<>(dataInfo.length);
    for (String dataItem : dataInfo) {

      TypeInfo entity =
          getTypeEntity(dataItem, JavaVarName.DEF_METHOD_PARAM_NAME, CodeComment.METHOD_PARAM_DOC);
      if (null != entity) {
        dataList.add(entity);
      }
    }

    return dataList;
  }

  /**
   * 获取类型的实体信息
   *
   * @param dataItem 字符配制
   * @return
   */
  private TypeInfo getTypeEntity(String dataItem, String varName, String comment) {
    if (StringUtils.isEmpty(dataItem)) {
      // TypeInfo typeInfo = new TypeInfo(classPath, className);
      return TypeInfo.builder()
          // 注释
          .comment(comment)
          .build();
    }

    // 如果存在尖括号说明为集合带泛型的情况
    int leftIndex = dataItem.indexOf(Symbol.ANGLE_BRACKETS_LEFT);
    if (leftIndex != -1) {
      String classPath = dataItem.substring(0, leftIndex);
      int subIndex = classPath.lastIndexOf(Symbol.POINT);
      subIndex = subIndex == -1 ? 0 : subIndex + 1;
      String className = dataItem.substring(subIndex);

      // TypeInfo typeInfo = new TypeInfo(classPath, className);
      return TypeInfo.builder()
          // 类路径
          .importPath(classPath)
          // 类名
          .importClassName(className)
          // 引用变量名
          .varName(varName)
          // 注释
          .comment(comment)
          .build();
    }

    // 检查当前是否存在.的情况，带路径，需要导入
    int indexPoint = dataItem.indexOf(Symbol.COMMA);
    if (indexPoint != -1) {
      String className = dataItem.substring(indexPoint);
      // return new TypeInfo(dataItem, className);
      return TypeInfo.builder()
          // 类路径
          .importPath(dataItem)
          // 类名
          .importClassName(className)
          // 引用变量名
          .varName(varName)
          // 注释
          .comment(comment)
          .build();
    }

    // 当其他情况都不匹配，说当前不需要导入包
    // return new TypeInfo(dataItem);
    return TypeInfo.builder()
        // 类名
        .importClassName(dataItem)
        // 引用变量名
        .varName(varName)
        // 注释
        .comment(comment)
        .build();
  }

  private List<WhereInfo> getWhereInfo(String whereStr) {
    if (StringUtils.isEmpty(whereStr)) {
      return new ArrayList<>(0);
    }

    String[] dataInfo = whereStr.split(Symbol.COMMA);
    List<WhereInfo> dataList = new ArrayList<>(dataInfo.length);

    for (String whereColumn : dataInfo) {
      if (StringUtils.isEmpty(whereColumn)) {
        continue;
      }

      whereColumn = whereColumn.toLowerCase();

      int spaceIndex = whereColumn.indexOf(Symbol.SPACE);
      if (spaceIndex == -1) {
        dataList.add(new WhereInfo(whereColumn));
      } else {
        String[] info = whereColumn.split(Symbol.SPACE);
        dataList.add(new WhereInfo(info[0], info[1]));
      }
    }
    return dataList;
  }

  /**
   * 配制 文件的实体信息
   *
   * @return
   */
  public GenerateConfigEntity getCfgEntity() {
    return cfgEntity;
  }
}
