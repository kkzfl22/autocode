package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.config.generate.entity.MethodInfo;
import com.liujun.micro.autocode.generator.builder.constant.CodeComment;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.entity.JavaAnnotation;
import com.liujun.micro.autocode.generator.builder.entity.JavaClassFieldEntity;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.MethodUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.TableColumnUtils;
import com.liujun.micro.autocode.generator.builder.utils.TypeProcessUtils;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

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

  /** 注解的相关信息 */
  private static final List<String> ANNOTATION_LIST =
      Arrays.asList(JavaKeyWord.BEAN_USE_DATA, JavaKeyWord.BEAN_USE_TOSTRING);

  /** 导包信息 */
  private static final List<String> ANNOTATION_IMPORT =
      Arrays.asList(JavaKeyWord.SWAGGER_IMPORT_MODEL, JavaKeyWord.SWAGGER_IMPORT_PROPERTY);

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
      String author) {
    // 类的定义
    StringBuilder sb =
        JavaClassCodeUtils.classDefine(
            entityInfo, getImportList(codeMethod), getAnnotationList(entityInfo), author);

    // 作属性输出
    this.outProperties(columnList, sb);
    // in关键输的输出
    this.inCondition(codeMethod, columnList, sb);
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
  private List<String> getImportList(List<MethodInfo> codeMethod) {

    // 获取实体需导入的基础包
    List<String> dataList = GenerateJavaBean.INSTANCE.getImportList(codeMethod);

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
            .annotation(JavaKeyWord.SWAGGER_APIMODEL)
            .value(entityInfo.getClassComment())
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
      List<MethodInfo> codeMethod, List<TableColumnDTO> columnList, StringBuilder sb) {
    // 进行条件的输出
    Set<String> conditionList = MethodUtils.getInCondition(codeMethod);
    // 作为属性输出
    this.outInCondition(conditionList, columnList, sb);
  }

  /**
   * 进行属性的输出操作
   *
   * @param inCondition 条件
   * @param columnList 列
   * @param sb 输出
   */
  private void outInCondition(
      Set<String> inCondition, List<TableColumnDTO> columnList, StringBuilder sb) {
    for (String inConditionItem : inCondition) {
      TableColumnDTO tableInfo = TableColumnUtils.getColumn(columnList, inConditionItem);
      if (null == tableInfo) {
        continue;
      }

      // 得到java的数据类型
      String javaDataType = TypeProcessUtils.getJavaType(tableInfo.getDataType());
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
                      .value(tableInfo.getColumnMsg() + CodeComment.JUNIT_PARSE_LIST_COMMENT)
                      .annotation(JavaKeyWord.SWAGGER_APIMODELPROPERTY)
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
  private void outProperties(List<TableColumnDTO> columnList, StringBuilder sb) {
    // 添加属性的信息
    for (int i = 0; i < columnList.size(); i++) {
      TableColumnDTO tableBean = columnList.get(i);

      // 得到java的数据类型
      String javaDataType = TypeProcessUtils.getJavaType(tableBean.getDataType());
      // 得到java输出的名称
      String javaName = NameProcess.INSTANCE.toFieldName(tableBean.getColumnName());

      JavaClassFieldEntity field =
          JavaClassFieldEntity.builder()
              // 注解
              .annotation(
                  JavaAnnotation.builder()
                      .value(tableBean.getColumnMsg())
                      .annotation(JavaKeyWord.SWAGGER_APIMODELPROPERTY)
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
