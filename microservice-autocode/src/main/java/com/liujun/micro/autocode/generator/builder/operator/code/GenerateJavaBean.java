package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.config.generate.entity.MethodInfo;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.ImportCodePackageKey;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.entity.JavaClassFieldEntity;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.MethodUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.TableColumnUtils;
import com.liujun.micro.autocode.generator.convergence.TypeConvergence;
import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeEnum;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaDataType;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaBean {

  public static final GenerateJavaBean INSTANCE = new GenerateJavaBean();

  /** 注解导入包 */
  private static final List<String> ANNOTATION_IMPORT_LIST =
      Arrays.asList(
          // get的注解包
          ImportCodePackageKey.ANNOTATION_GETTER.getPackageInfo().packageOut(),
          // set的注解包
          ImportCodePackageKey.ANNOTATION_SETTER.getPackageInfo().packageOut(),
          // toString的注解
          ImportCodePackageKey.ANNOTATION_TOSTRING.getPackageInfo().packageOut());

  /** 注解 */
  private static final List<String> ANNOTATION_LIST =
      Arrays.asList(
          // get的注解包
          ImportCodePackageKey.ANNOTATION_GETTER.getPackageInfo().getAnnotation(),
          // set的注解包
          ImportCodePackageKey.ANNOTATION_SETTER.getPackageInfo().getAnnotation(),
          // toString的注解
          ImportCodePackageKey.ANNOTATION_TOSTRING.getPackageInfo().getAnnotation());

  /**
   * 进行javaBean文件的生成操作
   *
   * @param columnList 列信息
   * @return 生成的javabean对象
   */
  public StringBuilder generateJavaBean(
      ImportPackageInfo entityInfo, List<TableColumnDTO> columnList, GenerateCodeContext param) {

    List<MethodInfo> codeMethod = param.getGenerateConfig().getGenerate().getCode();
    String author = param.getGenerateConfig().getGenerate().getAuthor();
    DatabaseTypeEnum typeEnum = param.getTypeEnum();

    // 类的定义
    StringBuilder sb =
        JavaClassCodeUtils.classDefine(
            entityInfo, getImportList(codeMethod, columnList, typeEnum), ANNOTATION_LIST, author);

    // 作属性输出
    this.outProperties(columnList, sb, typeEnum);
    // in关键输的输出
    this.inCondition(codeMethod, columnList, sb, typeEnum);
    // 类结束
    JavaClassCodeUtils.classEnd(sb);

    return sb;
  }

  /**
   * 获取导入的表信息
   *
   * @param codeMethod
   * @return
   */
  public List<String> getImportList(
      List<MethodInfo> codeMethod, List<TableColumnDTO> columns, DatabaseTypeEnum typeEnum) {
    List<String> importList = new ArrayList<>();
    importList.addAll(ANNOTATION_IMPORT_LIST);
    // 检查是否需要导入list包
    Set<String> inCondition = MethodUtils.getInCondition(codeMethod);
    if (!inCondition.isEmpty()) {
      importList.add(JavaKeyWord.IMPORT_LIST);
    }

    // 检查当前是否需要导入类型的信息

    // 添加属性的信息
    for (int i = 0; i < columns.size(); i++) {
      TableColumnDTO tableBean = columns.get(i);
      // 得到java的数据类型
      String javaDataType = TypeConvergence.getJavaType(typeEnum, tableBean.getDataType());

      // 获取导入的类型
      String importType = JavaDataType.getImportPkg(javaDataType);
      if (null != importType && !importList.contains(importType)) {
        importList.add(importType);
      }
    }

    // 检查当前属性，除开基础类型，其他都需要导入
    return importList;
  }

  /**
   * 进行in条件的condition的输出操作
   *
   * @param codeMethod 需要输出的方法
   * @param columnList 列信息
   * @param sb 输出的对象
   */
  private void inCondition(
      List<MethodInfo> codeMethod,
      List<TableColumnDTO> columnList,
      StringBuilder sb,
      DatabaseTypeEnum typeEnum) {
    // 进行条件的输出
    Set<String> conditionList = MethodUtils.getInCondition(codeMethod);
    // 作为属性输出
    this.outInCondition(conditionList, columnList, sb, typeEnum);
  }

  /**
   * 进行属性的输出操作
   *
   * @param inCondition 条件
   * @param columnList 列
   * @param sb 输出
   */
  private void outInCondition(
      Set<String> inCondition,
      List<TableColumnDTO> columnList,
      StringBuilder sb,
      DatabaseTypeEnum typeEnum) {
    for (String inConditionItem : inCondition) {
      TableColumnDTO tableInfo = TableColumnUtils.getColumn(columnList, inConditionItem);
      if (null == tableInfo) {
        continue;
      }

      // 得到java的数据类型
      String javaDataType = TypeConvergence.getJavaType(typeEnum, tableInfo.getDataType());
      // 得到java输出的名称
      String javaName = NameProcess.INSTANCE.toFieldName(tableInfo.getColumnName());
      // 输出的类型
      javaDataType = JavaKeyWord.LIST_TYPE + javaDataType + JavaKeyWord.LIST_TYPE_END;
      // 输出的名称
      javaName = getInConditionName(javaName);

      JavaClassFieldEntity field =
          JavaClassFieldEntity.builder()
              // 访问修饰符
              .visit(JavaKeyWord.PRIVATE)
              // 数据类型
              .type(javaDataType)
              // 名称
              .name(javaName)
              // 注释
              .comment(tableInfo.getColumnMsg())
              .build();

      sb.append(JavaClassCodeUtils.getClassField(field));
    }
  }

  /**
   * 获取in关键字作为属性的名称
   *
   * @param name 名称
   * @return 获取名称
   */
  public static String getInConditionName(String name) {
    return name + JavaKeyWord.FIELD_SUFFIX_NAME;
  }

  /**
   * 属性输出
   *
   * @param columnList 列集合
   * @param sb 输出的对象 信息
   */
  private void outProperties(
      List<TableColumnDTO> columnList, StringBuilder sb, DatabaseTypeEnum typeEnum) {
    List<JavaClassFieldEntity> javaEntityList = this.propertiesToJavaEntity(columnList, typeEnum);
    // 执行属性对象的输出操作
    for (JavaClassFieldEntity entity : javaEntityList) {
      sb.append(JavaClassCodeUtils.getClassField(entity));
    }
  }

  /**
   * 属性输出为java的实体对象
   *
   * @param columnList 列集合
   * @param typeEnum 输出的对象 信息
   * @return 生成的对象集合
   */
  public List<JavaClassFieldEntity> propertiesToJavaEntity(
      List<TableColumnDTO> columnList, DatabaseTypeEnum typeEnum) {

    List<JavaClassFieldEntity> dataList = new ArrayList<>(columnList.size());

    // 添加属性的信息
    for (int i = 0; i < columnList.size(); i++) {
      TableColumnDTO tableBean = columnList.get(i);

      // 得到java的数据类型
      String javaDataType = TypeConvergence.getJavaType(typeEnum, tableBean.getDataType());
      // 得到java输出的名称
      String javaName = NameProcess.INSTANCE.toFieldName(tableBean.getColumnName());

      JavaClassFieldEntity field =
          JavaClassFieldEntity.builder()
              // 访问修饰符
              .visit(JavaKeyWord.PRIVATE)
              // 数据类型
              .type(javaDataType)
              // 名称
              .name(javaName)
              // 注释
              .comment(tableBean.getColumnMsg())
              .build();

      dataList.add(field);
    }

    return dataList;
  }
}
