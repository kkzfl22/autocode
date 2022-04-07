package com.liujun.auto.generator.builder.ddd.code;

import com.liujun.auto.generator.builder.utils.JavaClassCodeUtils;
import com.liujun.auto.generator.builder.utils.MethodUtils;
import com.liujun.auto.generator.builder.utils.TableColumnUtils;
import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.generator.builder.ddd.constant.CodeComment;
import com.liujun.auto.generator.builder.ddd.constant.ImportCodePackageKey;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.ddd.entity.JavaAnnotation;
import com.liujun.auto.generator.builder.ddd.entity.JavaClassFieldEntity;
import com.liujun.auto.generator.convergence.TypeConvergence;
import com.liujun.auto.generator.database.constant.DatabaseTypeEnum;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 生成带有swagger的标识的类信息
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaSwaggerBean {

  public static final GenerateJavaSwaggerBean INSTANCE = new GenerateJavaSwaggerBean();

  /** 注解导入包 */
  private static final List<String> ANNOTATION_IMPORT =
      Arrays.asList(
          // get的注解包
          ImportCodePackageKey.ANNOTATION_GETTER.getPackageInfo().packageOut(),
          // set的注解包
          ImportCodePackageKey.ANNOTATION_SETTER.getPackageInfo().packageOut(),
          // toString的注解
          ImportCodePackageKey.ANNOTATION_TOSTRING.getPackageInfo().packageOut(),
          // apimodel的注解
          ImportCodePackageKey.ANNOTATION_API_MODEL.getPackageInfo().packageOut(),
          // apimodelProperty的注解
          ImportCodePackageKey.ANNOTATION_API_MODEL_PROPERTY.getPackageInfo().packageOut());

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
   * @param codeMethod 需要生成的方法
   * @param author 作者
   * @return 生成的javabean对象
   */
  public StringBuilder generateJavaBean(
      ImportPackageInfo entityInfo,
      List<TableColumnDTO> columnList,
      List<MethodInfo> codeMethod,
      String author,
      DatabaseTypeEnum typeEnum) {
    // 类的定义
    StringBuilder sb =
        JavaClassCodeUtils.classDefine(
            entityInfo,
            getImportList(codeMethod, columnList, typeEnum),
            getAnnotationList(entityInfo),
            author);

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
  private List<String> getImportList(
      List<MethodInfo> codeMethod, List<TableColumnDTO> columns, DatabaseTypeEnum typeEnum) {

    // 获取实体需导入的基础包
    List<String> dataList = GenerateJavaBean.INSTANCE.getImportList(codeMethod, columns, typeEnum);

    // 添加swagger的相关包
    dataList.addAll(ANNOTATION_IMPORT);

    return dataList;
  }

  /**
   * 获取导入的包的注解
   *
   * @return
   */
  private List<String> getAnnotationList(ImportPackageInfo entityInfo) {

    // 获取实体需导入的基础包
    List<String> dataList = new ArrayList<>(ANNOTATION_LIST.size() + 2);

    // 添加数据操作的注解
    dataList.addAll(ANNOTATION_LIST);

    // 添加swagger的注解
    String annotationAdd =
        JavaAnnotation.builder()
            .annotation(ImportCodePackageKey.ANNOTATION_API_MODEL.getPackageInfo().getAnnotation())
            .annotationValue(entityInfo.getClassComment())
            .build()
            .outAnnotation();
    dataList.add(annotationAdd);

    return dataList;
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
      javaName = GenerateJavaBean.getInConditionName(javaName);

      JavaClassFieldEntity field =
          JavaClassFieldEntity.builder()
              // 注解
              .annotation(
                  JavaAnnotation.builder()
                      // 注解中的值信息
                      .annotationValue(
                          tableInfo.getColumnMsg() + CodeComment.JUNIT_PARSE_LIST_COMMENT)
                      // 注解符
                      .annotation(
                          ImportCodePackageKey.ANNOTATION_API_MODEL_PROPERTY
                              .getPackageInfo()
                              .getAnnotation())
                      .build()
                      .outAnnotation())
              // 访问修饰符
              .visit(JavaKeyWord.PRIVATE)
              // 数据类型
              .type(javaDataType)
              // 名称
              .name(javaName)
              // 注释
              .comment(tableInfo.getColumnMsg() + CodeComment.JUNIT_PARSE_LIST_COMMENT)
              .build();

      sb.append(JavaClassCodeUtils.getClassField(field));
    }
  }

  /**
   * 属性输出
   *
   * @param columnList 列集合
   * @param sb 输出的对象 信息
   */
  private void outProperties(
      List<TableColumnDTO> columnList, StringBuilder sb, DatabaseTypeEnum typeEnum) {
    // 添加属性的信息
    for (int i = 0; i < columnList.size(); i++) {
      TableColumnDTO tableBean = columnList.get(i);

      // 得到java的数据类型
      String javaDataType = TypeConvergence.getJavaType(typeEnum, tableBean.getDataType());
      // 得到java输出的名称
      String javaName = NameProcess.INSTANCE.toFieldName(tableBean.getColumnName());

      JavaClassFieldEntity field =
          JavaClassFieldEntity.builder()
              // 注解
              .annotation(
                  JavaAnnotation.builder()
                      .annotationValue(tableBean.getColumnMsg())
                      .annotation(
                          ImportCodePackageKey.ANNOTATION_API_MODEL_PROPERTY
                              .getPackageInfo()
                              .getAnnotation())
                      .build()
                      .outAnnotation())
              // 访问修饰符
              .visit(JavaKeyWord.PRIVATE)
              // 数据类型
              .type(javaDataType)
              // 名称
              .name(javaName)
              // 注释
              .comment(tableBean.getColumnMsg())
              .build();

      sb.append(JavaClassCodeUtils.getClassField(field));
    }
  }
}
