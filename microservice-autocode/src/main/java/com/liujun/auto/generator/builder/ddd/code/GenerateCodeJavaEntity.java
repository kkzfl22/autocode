/*
 * Copyright (C), 2008-2021, Paraview All Rights Reserved.
 */
package com.liujun.auto.generator.builder.ddd.code;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.constant.ClassCommonCfg;
import com.liujun.auto.generator.builder.ddd.constant.CodeAnnotation;
import com.liujun.auto.generator.builder.ddd.constant.ImportCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.JavaMethodName;
import com.liujun.auto.generator.builder.ddd.constant.JavaVarName;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.convergence.TypeConvergence;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.constant.JavaUseClassEnum;
import com.liujun.auto.generator.javalanguage.constant.PrefixSpaceEnum;
import com.liujun.auto.generator.javalanguage.constant.VisitEnum;
import com.liujun.auto.generator.javalanguage.entity.ContentBase;
import com.liujun.auto.generator.javalanguage.entity.ContentField;
import com.liujun.auto.generator.javalanguage.entity.ContentMethod;
import com.liujun.auto.generator.javalanguage.entity.ContextAnnotation;
import com.liujun.auto.generator.javalanguage.entity.ContextFieldDocument;
import com.liujun.auto.generator.javalanguage.entity.ContextLineCode;
import com.liujun.auto.generator.javalanguage.entity.ContextMethodParam;
import com.liujun.auto.generator.javalanguage.entity.JavaClassImportClass;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author liujun
 * @since 2022/5/12
 */
public class GenerateCodeJavaEntity {
  private GenerateCodeJavaEntity() {}

  /**
   * 导包信息
   *
   * @param context 上下文对象信息
   * @return 导包的信息
   */
  public static List<JavaClassImportClass> packageImpport(GenerateCodeContext context) {
    List<JavaClassImportClass> importPkgList = new ArrayList<>();
    // 检查是否启用loombok插伯来生成相关的get和set及toString方法
    if (null != context.getGenerateCfg().getLombok() && context.getGenerateCfg().getLombok()) {
      importPkgList.add(
          JavaClassImportClass.normalImport(
              ImportCodePackageKey.ANNOTATION_SETTER.getPackageInfo().packageOut()));
      importPkgList.add(
          JavaClassImportClass.normalImport(
              ImportCodePackageKey.ANNOTATION_GETTER.getPackageInfo().packageOut()));
      importPkgList.add(
          JavaClassImportClass.normalImport(
              ImportCodePackageKey.ANNOTATION_TOSTRING.getPackageInfo().packageOut()));
    }

    return importPkgList;
  }

  /**
   * 类注释信息
   *
   * @param context
   * @return
   */
  public static List<ContextAnnotation> classAnnotation(GenerateCodeContext context) {
    List<ContextAnnotation> annotationList = new ArrayList<>();
    // 检查是否启用loombok插伯来生成相关的get和set及toString方法
    if (context.lombokOpen()) {
      annotationList.add(
          ContextAnnotation.builder()
              .annotation(ImportCodePackageKey.ANNOTATION_SETTER.getPackageInfo().getAnnotation())
              .build());
      annotationList.add(
          ContextAnnotation.builder()
              .annotation(ImportCodePackageKey.ANNOTATION_GETTER.getPackageInfo().getAnnotation())
              .build());
      annotationList.add(
          ContextAnnotation.builder()
              .annotation(ImportCodePackageKey.ANNOTATION_TOSTRING.getPackageInfo().getAnnotation())
              .build());
    }

    return annotationList;
  }

  /**
   * 执行所有属性的输出
   *
   * @param tableInfo 表信息
   * @return 属性信息
   */
  public static List<ContentBase> countFieldList(
      TableInfoDTO tableInfo, GenerateCodeContext context) {
    List<ContentBase> result = new ArrayList<>();

    for (TableColumnDTO tableColumn : tableInfo.getColumnList()) {
      result.add(builderField(tableColumn, context));
    }

    return result;
  }

  /**
   * 构建属性
   *
   * @param tableColumn
   * @param context
   * @return
   */
  private static ContentField builderField(
      TableColumnDTO tableColumn, GenerateCodeContext context) {
    ContentField field = new ContentField();

    // 得到java输出的名称
    String javaName = NameProcess.INSTANCE.toFieldName(tableColumn.getColumnName());

    field.setDocument(ContextFieldDocument.buildFieldDoc(tableColumn.getColumnMsg()));
    // 间隔行数1
    field.setTopLine(ClassCommonCfg.FIELD_TOP_LINE);
    // 左边空格2个
    field.setLeftSpace(PrefixSpaceEnum.ONE);
    // 访问修饰符私有
    field.setVisit(VisitEnum.PRIVATE);
    // 类型
    // 得到java的数据类型
    String javaDataType =
        TypeConvergence.getJavaType(context.getTypeEnum(), tableColumn.getDataType());
    field.setClassType(javaDataType);
    // 名称
    field.setName(javaName);

    return field;
  }

  /**
   * 执行所有属性的输出
   *
   * @param tableInfo 表信息
   * @return 属性信息
   */
  public static List<ContentBase> countFieldMethodToString(
      TableInfoDTO tableInfo, GenerateCodeContext context) {

    // 如果关联，则不再做当前的输出操作
    if (context.lombokOpen()) {
      return Collections.emptyList();
    }

    ContentMethod method = new ContentMethod();

    // 得到java输出的名称
    String methodName = JavaMethodName.TO_STRING;

    // 间隔行数1
    method.setTopLine(ClassCommonCfg.FIELD_TOP_LINE);
    // 左边空格2个
    method.setLeftSpace(PrefixSpaceEnum.ONE);
    // 访问修饰符私有
    method.setVisit(VisitEnum.PUBLIC);
    // 类型信息
    method.setReturnClass(JavaKeyWord.VOID);
    // 名称
    method.setName(methodName);
    // 方法注解
    method.setAnnotation(ContextAnnotation.annotationList(CodeAnnotation.OVERRIDE));
    // 代码首行信息
    method.setCodeLine(builderToStringContent(tableInfo));

    return Arrays.asList(method);
  }

  private static List<ContextLineCode> builderToStringContent(TableInfoDTO tableInfo) {
    List<ContextLineCode> result = new ArrayList<>(tableInfo.getColumnList().size() + 3);

    // 首行信息加入
    result.add(builderToStringFirstCodeLine(tableInfo));
    // 中间行信息加入
    for (int i = 0; i < tableInfo.getColumnList().size(); i++) {
      TableColumnDTO column = tableInfo.getColumnList().get(i);
      boolean last = false;
      if (i == tableInfo.getColumnList().size() - 1) {
        last = true;
      }
      result.add(builderToStringCodeLine(column, last));
    }
    // 末尾行的引号
    result.add(builderToStringLastEndCodeLine());
    result.add(builderToStringLastParseCodeLine());

    return result;
  }

  /**
   * 构建code操作
   *
   * @param tableInfo 表信息
   * @return
   */
  private static ContextLineCode builderToStringFirstCodeLine(TableInfoDTO tableInfo) {
    // 得到java输出的名称
    String className = NameProcess.INSTANCE.toJavaClassName(tableInfo.getTableName());

    StringBuilder outCode = new StringBuilder();
    outCode.append(JavaKeyWord.FINAL);
    outCode.append(Symbol.SPACE);
    outCode.append(JavaUseClassEnum.STRING_BUILDER.getName());
    outCode.append(Symbol.SPACE);
    outCode.append(JavaVarName.TO_STRING_OUT_NAME);
    outCode.append(Symbol.SPACE);
    outCode.append(Symbol.EQUAL);
    outCode.append(Symbol.SPACE);
    outCode.append(JavaKeyWord.NEW);
    outCode.append(Symbol.SPACE);
    outCode.append(JavaUseClassEnum.STRING_BUILDER.getName());
    outCode.append(Symbol.BRACKET_LEFT);
    outCode.append(Symbol.QUOTE);
    outCode.append(className);
    outCode.append(Symbol.BRACE_LEFT);
    outCode.append(Symbol.QUOTE);
    outCode.append(Symbol.BRACKET_RIGHT);

    return ContextLineCode.builderCode(outCode.toString());
  }

  /**
   * 构建code操作
   *
   * @param columnName
   * @return
   */
  private static ContextLineCode builderToStringCodeLine(TableColumnDTO columnName, boolean last) {

    // 得到java输出的名称
    String fieldName = NameProcess.INSTANCE.toFieldName(columnName.getColumnName());

    StringBuilder outCode = new StringBuilder();
    outCode.append(JavaVarName.TO_STRING_OUT_NAME);
    outCode.append(Symbol.POINT);
    outCode.append(JavaMethodName.APPEND);
    outCode.append(Symbol.BRACKET_LEFT);
    outCode.append(Symbol.QUOTE);
    outCode.append(fieldName);
    outCode.append(Symbol.EQUAL);
    outCode.append(Symbol.SINGLE_QUOTE);
    outCode.append(Symbol.QUOTE);
    outCode.append(Symbol.BRACKET_RIGHT);

    outCode.append(Symbol.POINT);
    outCode.append(JavaMethodName.APPEND);
    outCode.append(Symbol.BRACKET_LEFT);
    outCode.append(fieldName);
    outCode.append(Symbol.BRACKET_RIGHT);

    outCode.append(Symbol.POINT);
    outCode.append(JavaMethodName.APPEND);
    outCode.append(Symbol.BRACKET_LEFT);
    outCode.append(Symbol.QUOTE);
    outCode.append(Symbol.SINGLE_QUOTE);
    outCode.append(Symbol.QUOTE);
    outCode.append(Symbol.BRACKET_RIGHT);

    // 末尾的引号
    if (!last) {
      outCode.append(Symbol.POINT);
      outCode.append(JavaMethodName.APPEND);
      outCode.append(Symbol.BRACKET_LEFT);
      outCode.append(Symbol.QUOTE);
      outCode.append(Symbol.COMMA);
      outCode.append(Symbol.QUOTE);
      outCode.append(Symbol.BRACKET_RIGHT);
    }

    return ContextLineCode.builderCode(outCode.toString());
  }

  /**
   * 构建最后一行的ToString方法操作
   *
   * @return
   */
  private static ContextLineCode builderToStringLastEndCodeLine() {

    StringBuilder outCode = new StringBuilder();
    outCode.append(JavaVarName.TO_STRING_OUT_NAME);

    outCode.append(Symbol.POINT);
    outCode.append(JavaMethodName.APPEND);
    outCode.append(Symbol.BRACKET_LEFT);
    outCode.append(Symbol.QUOTE);
    outCode.append(Symbol.BRACE_RIGHT);
    outCode.append(Symbol.QUOTE);
    outCode.append(Symbol.BRACKET_RIGHT);

    return ContextLineCode.builderCode(outCode.toString());
  }
  /**
   * 构建最后一行的ToString方法操作
   *
   * @return
   */
  private static ContextLineCode builderToStringLastParseCodeLine() {

    StringBuilder outCode = new StringBuilder();

    outCode.append(JavaKeyWord.RETURN);
    outCode.append(Symbol.SPACE);
    outCode.append(JavaVarName.TO_STRING_OUT_NAME);
    outCode.append(Symbol.POINT);
    outCode.append(JavaMethodName.TO_STRING);
    outCode.append(Symbol.BRACKET_LEFT);
    outCode.append(Symbol.BRACKET_RIGHT);

    return ContextLineCode.builderCode(outCode.toString());
  }

  /**
   * 执行所有属性的输出
   *
   * @param tableInfo 表信息
   * @return 属性信息
   */
  public static List<ContentBase> countFieldMethodGetList(
      TableInfoDTO tableInfo, GenerateCodeContext context) {

    // 如果关联，则不再做当前的输出操作
    if (context.lombokOpen()) {
      return Collections.emptyList();
    }

    List<ContentBase> result = new ArrayList<>();
    for (TableColumnDTO tableColumn : tableInfo.getColumnList()) {
      result.add(builderMethodGetField(tableColumn, context));
    }

    return result;
  }

  /**
   * 做方法输出的构建操作
   *
   * @param tableColumn
   * @param context
   * @return
   */
  private static ContentMethod builderMethodGetField(
      TableColumnDTO tableColumn, GenerateCodeContext context) {
    ContentMethod method = new ContentMethod();

    // 得到java输出的名称
    String methodName =
        JavaMethodName.GET + NameProcess.INSTANCE.toJavaClassName(tableColumn.getColumnName());

    // 间隔行数1
    method.setTopLine(ClassCommonCfg.FIELD_TOP_LINE);
    // 左边空格2个
    method.setLeftSpace(PrefixSpaceEnum.ONE);
    // 访问修饰符私有
    method.setVisit(VisitEnum.PUBLIC);
    // 类型
    String javaDataType =
        TypeConvergence.getJavaType(context.getTypeEnum(), tableColumn.getDataType());
    method.setReturnClass(javaDataType);
    // 名称
    method.setName(methodName);
    // 代码行信息
    method.setCodeLine(builderMethodGetCodeLine(tableColumn.getColumnName()));

    return method;
  }

  /**
   * 构建code操作
   *
   * @param columnName
   * @return
   */
  private static List<ContextLineCode> builderMethodGetCodeLine(String columnName) {
    // 得到java输出的名称
    String javaName = NameProcess.INSTANCE.toFieldName(columnName);

    StringBuilder outCode = new StringBuilder();
    outCode.append(JavaKeyWord.RETURN);
    outCode.append(Symbol.SPACE);
    outCode.append(JavaKeyWord.THIS);
    outCode.append(Symbol.POINT);
    outCode.append(javaName);

    ContextLineCode line = new ContextLineCode();

    line.setLeftSpace(PrefixSpaceEnum.TWO);
    line.setLineCode(outCode.toString());

    return Arrays.asList(line);
  }

  /**
   * 执行Set方法的输出操作
   *
   * @param tableInfo 表信息
   * @return 属性信息
   */
  public static List<ContentBase> countFieldMethodSetList(
      TableInfoDTO tableInfo, GenerateCodeContext context) {

    // 如果关联，则不再做当前的输出操作
    if (context.lombokOpen()) {
      return Collections.emptyList();
    }

    List<ContentBase> result = new ArrayList<>();
    for (TableColumnDTO tableColumn : tableInfo.getColumnList()) {
      result.add(builderMethodSetField(tableColumn, context));
    }

    return result;
  }

  /**
   * 做方法输出的构建操作
   *
   * @param tableColumn
   * @param context
   * @return
   */
  private static ContentMethod builderMethodSetField(
      TableColumnDTO tableColumn, GenerateCodeContext context) {
    ContentMethod method = new ContentMethod();

    // 得到java输出的名称
    String methodName =
        JavaMethodName.SET + NameProcess.INSTANCE.toJavaClassName(tableColumn.getColumnName());

    // 类型
    String javaDataType =
        TypeConvergence.getJavaType(context.getTypeEnum(), tableColumn.getDataType());

    // 参数名称
    String referenceName = NameProcess.INSTANCE.toFieldName(tableColumn.getColumnName());

    // 间隔行数1
    method.setTopLine(ClassCommonCfg.FIELD_TOP_LINE);
    // 左边空格2个
    method.setLeftSpace(PrefixSpaceEnum.ONE);
    // 访问修饰符私有
    method.setVisit(VisitEnum.PUBLIC);

    method.setReturnClass(JavaKeyWord.VOID);
    // 名称
    method.setName(methodName);
    // 方法参数
    method.setParam(Arrays.asList(ContextMethodParam.builderParam(javaDataType, referenceName)));
    // 代码信息
    method.setCodeLine(builderMethodSetCodeLine(tableColumn.getColumnName(), referenceName));

    return method;
  }

  /**
   * 构建code操作
   *
   * @param columnName
   * @return
   */
  private static List<ContextLineCode> builderMethodSetCodeLine(
      String columnName, String referenceName) {
    // 得到java输出的名称
    String javaName = NameProcess.INSTANCE.toFieldName(columnName);

    StringBuilder outCode = new StringBuilder();
    outCode.append(JavaKeyWord.THIS);
    outCode.append(Symbol.POINT);
    outCode.append(javaName);
    outCode.append(Symbol.SPACE);
    outCode.append(Symbol.EQUAL);
    outCode.append(Symbol.SPACE);
    outCode.append(referenceName);

    ContextLineCode line = new ContextLineCode();

    line.setLeftSpace(PrefixSpaceEnum.TWO);
    line.setLineCode(outCode.toString());

    return Arrays.asList(line);
  }
}
