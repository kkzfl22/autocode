package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.config.generate.entity.MethodInfo;
import com.liujun.micro.autocode.generator.builder.constant.JavaMethodName;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarValue;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodArguments;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodEntity;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.MethodUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.TableColumnUtils;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.javalanguage.serivce.JavaFormat;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 生成java实体转换的接口
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaAssembler {

  public static final GenerateJavaAssembler INSTANCE = new GenerateJavaAssembler();

  /** 需要导入的包 */
  private static final String[] IMPORT_PKG =
      new String[] {
        JavaKeyWord.IMPORT_LIST, JavaKeyWord.IMPORT_COLLECTIONS, JavaKeyWord.IMPORT_ARRAYLIST
      };

  /**
   * 集合的转换方法
   *
   * @param sb 字符的信息
   * @param methodName 方法名
   * @param methodComment 注释
   * @param entityPackageSrc 实体类型1
   * @param entityPackageTarget 实体类型2
   * @param parseMethod 转换的方法名称
   */
  public void assemblerListMethod(
      StringBuilder sb,
      String methodName,
      String methodComment,
      ImportPackageInfo entityPackageSrc,
      ImportPackageInfo entityPackageTarget,
      String parseMethod) {
    int tabIndex = 1;

    // 构建方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            .comment(methodComment)
            .visit(JavaKeyWord.PUBLIC)
            .staticFlag(JavaKeyWord.STATIC)
            .type(JavaClassCodeUtils.listType(entityPackageTarget.getClassName()))
            .returnComment(entityPackageTarget.getClassComment())
            .name(methodName)
            .arguments(
                Arrays.asList(
                    JavaMethodArguments.parsePackageList(
                        entityPackageSrc, JavaVarName.ASSERT_PARAM_SRC_LIST)))
            .build();

    // 方法头生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);

    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 进行判空检查
    listEmptyCheck(sb, tabIndex);

    // 空行
    sb.append(JavaFormat.appendTab(tabIndex + 1));
    sb.append(Symbol.ENTER_LINE);

    // 转换操作
    listLoopParse(sb, tabIndex, entityPackageSrc, entityPackageTarget, parseMethod);

    // 返回语句
    sb.append(JavaFormat.appendTab(tabIndex + 1));
    sb.append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(JavaVarName.ASSERT_PARAM_TARGET_LIST).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 方法的结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 集合判空的检查
   *
   * @param sb 字符信息
   * @param tabIndex tab数量
   */
  private void listEmptyCheck(StringBuilder sb, int tabIndex) {
    // 对声明的对象进行检查
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.IF).append(Symbol.SPACE);
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.ASSERT_PARAM_SRC_LIST);
    sb.append(Symbol.SPACE).append(Symbol.EQUALS);
    sb.append(Symbol.SPACE).append(JavaVarValue.VALUE_NULL).append(Symbol.SPACE);
    sb.append(JavaKeyWord.LOGIC_OR).append(Symbol.SPACE).append(JavaVarName.ASSERT_PARAM_SRC_LIST);
    sb.append(Symbol.POINT).append(JavaMethodName.LIST_EMPTY).append(Symbol.BRACKET_LEFT);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);

    // 返回空对象
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JavaKeyWord.RETURN);
    sb.append(Symbol.SPACE).append(JavaVarValue.LIST_EMPTY_DEFAULT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);



    // 方法声明检查结束
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 集合转换方法
   *
   * @param sb
   * @param tabIndex
   * @param entityPackageSrc 转换的原始信息
   * @param entityPackageTarget 转换到的目标信息
   * @param parseMethod 转换的方法
   */
  private void listLoopParse(
      StringBuilder sb,
      int tabIndex,
      ImportPackageInfo entityPackageSrc,
      ImportPackageInfo entityPackageTarget,
      String parseMethod) {
    // 返回对象的声明
    sb.append(JavaFormat.appendTab(tabIndex + 1));
    sb.append(JavaKeyWord.LIST_TYPE).append(entityPackageTarget.getClassName());
    sb.append(JavaKeyWord.LIST_TYPE_END).append(Symbol.SPACE);
    sb.append(JavaVarName.ASSERT_PARAM_TARGET_LIST).append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(JavaKeyWord.NEW).append(Symbol.SPACE);
    sb.append(JavaKeyWord.LIST_TYPE_ARRAYLIST).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.ASSERT_PARAM_SRC_LIST).append(Symbol.POINT);
    sb.append(JavaMethodName.SIZE).append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 循环遍历
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.FOR_KEY);
    sb.append(Symbol.SPACE).append(Symbol.BRACKET_LEFT);
    sb.append(entityPackageSrc.getClassName()).append(Symbol.SPACE);
    sb.append(JavaVarName.LIST_GET_NAME_TEMP_NAME).append(Symbol.SPACE).append(Symbol.COLON);
    sb.append(Symbol.SPACE).append(JavaVarName.ASSERT_PARAM_SRC_LIST).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);

    // 将转换转换插入到集合中
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JavaVarName.ASSERT_PARAM_TARGET_LIST);
    sb.append(Symbol.POINT).append(JavaMethodName.LIST_ADD).append(Symbol.BRACKET_LEFT);
    sb.append(parseMethod).append(Symbol.BRACKET_LEFT).append(JavaVarName.LIST_GET_NAME_TEMP_NAME);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 循环结束
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 转换的方法
   *
   * @param methodName 转换的方法名
   * @param methodComment 方法注释
   * @param entityPackageSrc 实体1
   * @param entityPackageTarget 实体2
   * @param columnList 列集合
   * @param sb 字符信息
   * @param codeMethod 方法列表
   * @param setParamFlag 设置参数标识, true 当前进行设置，false则不设置
   */
  public void assemblerMethod(
      String methodName,
      String methodComment,
      ImportPackageInfo entityPackageSrc,
      ImportPackageInfo entityPackageTarget,
      List<TableColumnDTO> columnList,
      StringBuilder sb,
      List<MethodInfo> codeMethod,
      boolean setParamFlag) {

    // 方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 方法注释
            .comment(methodComment)
            // 访问修饰
            .visit(JavaKeyWord.PUBLIC)
            // 静态标识
            .staticFlag(JavaKeyWord.STATIC)
            // 返回值
            .type(entityPackageTarget.getClassName())
            // 返回值注释
            .returnComment(entityPackageTarget.getClassComment())
            // 方法名
            .name(methodName)
            // 参数
            .arguments(
                Arrays.asList(
                    // 源方法参数
                    JavaMethodArguments.parsePackage(
                        entityPackageSrc, JavaVarName.ASSERT_DATA_SRC)))
            .build();

    // 方法定义生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);

    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    int tabIndex = 1;

    // 数据项的检查
    ifDataCheck(sb, tabIndex);

    // 返回对象的声明
    sb.append(JavaFormat.appendTab(tabIndex + 1));
    sb.append(entityPackageTarget.getClassName()).append(Symbol.SPACE);
    sb.append(JavaVarName.ASSERT_DATA_TARGET).append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(JavaKeyWord.NEW).append(Symbol.SPACE);
    sb.append(entityPackageTarget.getClassName()).append(Symbol.BRACKET_LEFT);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 内容设置操作
    setProperties(
        columnList, JavaVarName.ASSERT_DATA_SRC, JavaVarName.ASSERT_DATA_TARGET, sb, tabIndex);

    // 当前如果为参数设置，则进行条件的设置操作
    if (setParamFlag) {
      // 进行条件的输出
      Set<String> conditionList = MethodUtils.getInCondition(codeMethod);
      // 作为属性输出
      this.outInCondition(
          conditionList,
          columnList,
          sb,
          JavaVarName.ASSERT_DATA_SRC,
          JavaVarName.ASSERT_DATA_TARGET);
    }

    // 返回语句
    sb.append(JavaFormat.appendTab(tabIndex + 1));
    sb.append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(JavaVarName.ASSERT_DATA_TARGET).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 方法的结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 数据项的检查操作
   *
   * @param sb
   * @param tabIndex
   */
  private void ifDataCheck(StringBuilder sb, int tabIndex) {
    // 对查询的对象进行空的检查，当为空时，不能进行转换
    sb.append(JavaFormat.appendTab(tabIndex + 1));
    sb.append(JavaKeyWord.IF).append(Symbol.SPACE);
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarValue.VALUE_NULL).append(Symbol.SPACE);
    sb.append(Symbol.EQUALS).append(Symbol.SPACE).append(JavaVarName.ASSERT_DATA_SRC);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);

    // 返回语句
    sb.append(JavaFormat.appendTab(tabIndex + 2));
    sb.append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(JavaVarValue.VALUE_NULL).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 判断结束
    sb.append(JavaFormat.appendTab(tabIndex + 1));
    sb.append(Symbol.BRACE_RIGHT).append(Symbol.ENTER_LINE);
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
      String srcName,
      String targetName) {
    for (String inConditionItem : inCondition) {
      TableColumnDTO tableInfo = TableColumnUtils.getColumn(columnList, inConditionItem);
      if (null == tableInfo) {
        continue;
      }

      // 得到java输出的名称
      String javaName = NameProcess.INSTANCE.toFieldName(tableInfo.getColumnName());
      javaName = NameProcess.INSTANCE.toJavaNameFirstUpper(javaName);

      // 输出的名称
      javaName = javaName + JavaKeyWord.FIELD_SUFFIX_NAME;

      // 进行其他属性的设置操作
      setField(sb, javaName, srcName, targetName, tableInfo.getColumnMsg(), 1);
    }
  }

  /**
   * 属性设置
   *
   * @param columnList 列集合
   * @param sb 输出的对象 信息
   */
  private void setProperties(
      List<TableColumnDTO> columnList,
      String srcName,
      String targetName,
      StringBuilder sb,
      int tabIndex) {
    // 添加属性的信息
    for (int i = 0; i < columnList.size(); i++) {
      TableColumnDTO tableBean = columnList.get(i);

      // 得到java输出的名称
      String fieldName = NameProcess.INSTANCE.toFieldName(tableBean.getColumnName());
      String fileNameUpper = NameProcess.INSTANCE.toJavaNameFirstUpper(fieldName);

      // 属性输出
      this.setField(sb, fileNameUpper, srcName, targetName, tableBean.getColumnMsg(), tabIndex);
    }
  }

  /**
   * 属性设置
   *
   * @param sb 字符信息
   * @param javaName 名称
   * @param srcName 源名称
   * @param targetName 目标名称
   * @param comment 注释
   * @param tabIndex tab数
   */
  private void setField(
      StringBuilder sb,
      String javaName,
      String srcName,
      String targetName,
      String comment,
      int tabIndex) {

    String getName = JavaMethodName.GET + javaName;
    String setName = JavaMethodName.SET + javaName;

    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JavaKeyWord.LINE_ANNOTATION);
    sb.append(comment).append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(targetName).append(Symbol.POINT);
    sb.append(setName).append(Symbol.BRACKET_LEFT);
    sb.append(srcName).append(Symbol.POINT).append(getName);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
  }

  /**
   * 定义类相关的信息，包括包，导入包及类的定义
   *
   * @param entityPackageInfo1 转换的类1
   * @param entityPackageInfo2 转换的类2
   * @param assemblerPackage 转换的类信息
   * @return 包的信息
   */
  public StringBuilder defineClass(
      ImportPackageInfo entityPackageInfo1,
      ImportPackageInfo entityPackageInfo2,
      ImportPackageInfo assemblerPackage,
      String author) {
    // 导包的信息
    List<String> importList = this.importClass(entityPackageInfo1, entityPackageInfo2);
    // 类的定义
    return JavaClassCodeUtils.classDefine(assemblerPackage, importList, author);
  }

  /**
   * 导入实体包信息
   *
   * @param entityPackageInfo1 实体信息1
   * @param entityPackageInfo2 实体信息2
   */
  private List<String> importClass(
      ImportPackageInfo entityPackageInfo1, ImportPackageInfo entityPackageInfo2) {
    List<String> dataList = new ArrayList<>(IMPORT_PKG.length + 2);

    for (String entityItem : IMPORT_PKG) {
      dataList.add(entityItem);
    }
    // 导入实体包
    dataList.add(entityPackageInfo1.packageOut());
    dataList.add(entityPackageInfo2.packageOut());

    return dataList;
  }
}
