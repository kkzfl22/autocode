package com.liujun.micro.autocode.generator.builder.operator.code.junit;

import com.liujun.micro.autocode.config.generate.entity.MethodInfo;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.CodeComment;
import com.liujun.micro.autocode.generator.builder.constant.JavaMethodName;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarValue;
import com.liujun.micro.autocode.generator.builder.constant.JunitKey;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodEntity;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.MethodUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.WhereUtils;
import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeEnum;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.javalanguage.serivce.JavaFormat;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.List;
import java.util.Map;

/**
 * 数据修改的相关操作
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJunitUpdate {

  public static final GenerateJunitUpdate INSTANCE = new GenerateJunitUpdate();

  /**
   * 批量添加方法
   *
   * @param sb 集合
   * @param method 方法
   * @param methodList 方法集合
   * @param insertReturnType 插入方法返回的类型
   * @param insertSuccessValue 数据成功后的返回值
   */
  public void batchInsertMethod(
      StringBuilder sb,
      MethodInfo method,
      List<MethodInfo> methodList,
      String insertReturnType,
      String insertSuccessValue) {
    String methodName = NameProcess.INSTANCE.toJavaNameFirstUpper(method.getName());
    int tabIndex = 1;

    // 方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 注解符
            .annotation(JunitKey.ANNO_TEST)
            // 公共的访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 方法注释
            .comment(method.getComment())
            // 返回值
            .type(JavaKeyWord.VOID)
            // 方法名
            .name(GenerateJunitDefine.JUNIT_METHOD_BEFORE + methodName)
            .build();

    // 方法定义生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);

    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 调用批量插入方法
    this.invokeBatchInsert(sb, tabIndex, methodList, insertReturnType, insertSuccessValue);

    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 调用批量的数据插入
   *
   * @param sb 代码
   * @param tabIndex 当前的tab数
   * @param methodList 所有的方法
   * @param insertReturnType 插入的返回类型
   * @param insertSuccessValue 插入成功的值
   */
  private void invokeBatchInsert(
      StringBuilder sb,
      int tabIndex,
      List<MethodInfo> methodList,
      String insertReturnType,
      String insertSuccessValue) {

    // 调用批量添加方法
    this.invokeBatch(sb, tabIndex, methodList, insertReturnType, insertSuccessValue);

    // 进行标识的设置操作
    GenerateJunitDefine.INSTANCE.setBatchInsertFlag(
        sb, tabIndex - 1, JavaVarValue.INSERT_TYPE_BATCH_KEY);
  }

  /**
   * 单个添加方法
   *
   * @param sb 添加的对象
   * @param method 方法
   * @param insertReturnType 插入的类型
   * @param insertSuccessValue 插入的成功值
   */
  public void oneInsertMethod(
      StringBuilder sb, MethodInfo method, String insertReturnType, String insertSuccessValue) {
    String methodName = NameProcess.INSTANCE.toJavaNameFirstUpper(method.getName());

    int tabIndex = 0;

    // 方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 注解符
            .annotation(JunitKey.ANNO_TEST)
            // 公共的访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 方法注释
            .comment(method.getComment())
            // 返回值
            .type(JavaKeyWord.VOID)
            // 方法名
            .name(GenerateJunitDefine.JUNIT_METHOD_BEFORE + methodName)
            .build();

    // 方法定义生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);

    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 调用添加方法
    insertInvokeMethod(sb, tabIndex, method, insertReturnType, insertSuccessValue);

    // 进行标识的设置操作
    GenerateJunitDefine.INSTANCE.setBatchInsertFlag(sb, tabIndex, JavaVarValue.INSERT_TYPE_ONE_KEY);

    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE).append(Symbol.ENTER_LINE);
  }

  /**
   * 批量插入操作
   *
   * @param sb 字符
   * @param tabIndex 索引号
   * @param methodList 批次
   * @param insertReturnType 插入数据的返回类型
   * @param insertSuccessValue 插入数据的成功标识
   */
  public void invokeBatch(
      StringBuilder sb,
      int tabIndex,
      List<MethodInfo> methodList,
      String insertReturnType,
      String insertSuccessValue) {
    // 获取批量添加的数据
    MethodInfo batchInsert = MethodUtils.getBatchInsertMethod(methodList);

    // 方法调用
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(insertReturnType);
    sb.append(Symbol.SPACE).append(JavaVarName.INVOKE_METHOD_OPERATOR_RSP);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(JavaVarName.INSTANCE_NAME).append(Symbol.POINT).append(batchInsert.getName());
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.BATCH_LIST_NAME);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 进行插入结果的判断
    sb.append(JavaFormat.appendTab(tabIndex + 1)).append(JunitKey.ASSERT);
    sb.append(insertSuccessValue).append(Symbol.COMMA);
    sb.append(JavaVarName.INVOKE_METHOD_OPERATOR_RSP);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
  }

  /**
   * 调用插入方法
   *
   * @param sb 字符信息
   * @param tabIndex 当前插入的tab字符的索引
   * @param method 方法信息
   * @param insertReturnType 插入的返回类型
   * @param insertSuccessValue 插入数据的对比值
   */
  public void insertInvokeMethod(
      StringBuilder sb,
      int tabIndex,
      MethodInfo method,
      String insertReturnType,
      String insertSuccessValue) {
    // 方法调用
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(insertReturnType).append(Symbol.SPACE);
    sb.append(JavaVarName.INVOKE_METHOD_OPERATOR_RSP).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE).append(JavaVarName.INSTANCE_NAME);
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.INSTANCE_NAME_ENTITY)
        .append(Symbol.BRACKET_RIGHT)
        .append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 进行插入结果的判断
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JunitKey.ASSERT);
    sb.append(insertSuccessValue).append(Symbol.COMMA);
    sb.append(JavaVarName.INVOKE_METHOD_OPERATOR_RSP);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
  }

  /**
   * 数据修改方法
   *
   * @param sb 添加的对象
   * @param updateMethod 方法
   * @param insertMethod 插入方法
   * @param insertReturnType 返回的值
   * @param insertSuccessValue 插入的成功值
   */
  public void oneUpdateMethod(
      StringBuilder sb,
      MethodInfo updateMethod,
      MethodInfo insertMethod,
      String insertReturnType,
      String insertSuccessValue) {
    String methodName = NameProcess.INSTANCE.toJavaNameFirstUpper(updateMethod.getName());

    int tabIndex = 0;

    // 方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 注解符
            .annotation(JunitKey.ANNO_TEST)
            // 公共的访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 方法注释
            .comment(updateMethod.getComment())
            // 返回值
            .type(JavaKeyWord.VOID)
            // 方法名
            .name(GenerateJunitDefine.JUNIT_METHOD_BEFORE + methodName)
            .build();

    // 方法定义生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);

    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 调用添加方法
    insertInvokeMethod(sb, tabIndex, insertMethod, insertReturnType, insertSuccessValue);

    // 进行标识的设置操作
    GenerateJunitDefine.INSTANCE.setBatchInsertFlag(sb, tabIndex, JavaVarValue.INSERT_TYPE_ONE_KEY);

    sb.append(Symbol.ENTER_LINE);

    // 对非主键属性数据进行修改
    sb.append(JavaFormat.appendTab(tabIndex + 2));
    sb.append(JavaKeyWord.THIS).append(Symbol.POINT).append(JavaMethodName.SET_DATA_FIELD);
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.INSTANCE_NAME_ENTITY);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);

    sb.append(Symbol.ENTER_LINE);
    // 方法调用
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(insertReturnType).append(Symbol.SPACE);
    sb.append(JavaVarName.INVOKE_METHOD_UPDATE_RSP).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE).append(JavaVarName.INSTANCE_NAME);
    sb.append(Symbol.POINT).append(updateMethod.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.INSTANCE_NAME_ENTITY)
        .append(Symbol.BRACKET_RIGHT)
        .append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 进行插入结果的判断
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JunitKey.ASSERT);
    sb.append(insertSuccessValue).append(Symbol.COMMA);
    sb.append(JavaVarName.INVOKE_METHOD_UPDATE_RSP);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 清理数据方法
   *
   * @param sb
   * @param deleteMethod
   */
  public void deleteMethod(
      StringBuilder sb,
      MethodInfo deleteMethod,
      ImportPackageInfo poPackage,
      String insertReturnType,
      String insertSuccessValue) {
    int tabIndex = 0;

    // 方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 注解符
            .annotation(JunitKey.ANNO_AFTER)
            // 公共的访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 方法注释
            .comment(CodeComment.JUNIT_AFTER_CLEAN)
            // 返回值
            .type(JavaKeyWord.VOID)
            // 方法名
            .name(JavaMethodName.AFTER_CLEAN)
            .build();

    // 方法定义生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);

    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 检查并执行单个清理操作
    junitDeleteOne(sb, tabIndex, deleteMethod, insertReturnType, insertSuccessValue);

    // 检查并执行批量删除操作
    junitBatchDelete(sb, tabIndex, deleteMethod, poPackage, insertReturnType, insertSuccessValue);

    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 执行单个清理操作
   *
   * @param sb
   * @param tabIndex
   * @param deleteMethod
   */
  private void junitDeleteOne(
      StringBuilder sb,
      int tabIndex,
      MethodInfo deleteMethod,
      String insertReturnType,
      String insertSuccessValue) {
    // 1,检查当前是否为单个操作
    sb.append(JavaFormat.appendTab(tabIndex + 2));
    sb.append(JavaKeyWord.IF).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.JUNIT_VAR_BATCH_INSERT).append(Symbol.SPACE);
    sb.append(Symbol.EQUALS).append(Symbol.SPACE).append(JavaVarValue.INSERT_TYPE_ONE_KEY);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);

    // 执行删除方法
    sb.append(JavaFormat.appendTab(tabIndex + 3));
    sb.append(insertReturnType).append(Symbol.SPACE).append(JavaVarName.DELETE_METHOD_RSP);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(JavaVarName.INSTANCE_NAME).append(Symbol.POINT).append(deleteMethod.getName());
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.INSTANCE_NAME_ENTITY);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 进行删除后的结果断言
    sb.append(JavaFormat.appendTab(tabIndex + 3));
    sb.append(JunitKey.ASSERT).append(insertSuccessValue);
    sb.append(Symbol.COMMA).append(Symbol.SPACE).append(JavaVarName.DELETE_METHOD_RSP);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 判断结束
    sb.append(JavaFormat.appendTab(tabIndex + 2));
    sb.append(Symbol.BRACE_RIGHT).append(Symbol.ENTER_LINE);
  }

  /**
   * 执行批量删除操作
   *
   * @param sb
   * @param tabIndex
   * @param deleteMethod
   */
  private void junitBatchDelete(
      StringBuilder sb,
      int tabIndex,
      MethodInfo deleteMethod,
      ImportPackageInfo poPackage,
      String insertReturnType,
      String insertSuccessValue) {
    // 2,检查当前是否为批量操作
    sb.append(JavaFormat.appendTab(tabIndex + 2));
    sb.append(JavaKeyWord.ELSE_IF).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.JUNIT_VAR_BATCH_INSERT).append(Symbol.SPACE);
    sb.append(Symbol.EQUALS).append(Symbol.SPACE).append(JavaVarValue.INSERT_TYPE_BATCH_KEY);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);

    // 执行循环遍历的开始
    sb.append(JavaFormat.appendTab(tabIndex + 3)).append(JavaKeyWord.FOR_KEY);
    sb.append(Symbol.SPACE);
    sb.append(Symbol.BRACKET_LEFT).append(JavaKeyWord.INT_TYPE).append(Symbol.SPACE);
    sb.append(JavaVarName.FOR_TEMP_INDEX).append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(JavaVarValue.FOR_INDEX_START).append(Symbol.SEMICOLON);
    sb.append(Symbol.SPACE).append(JavaVarName.FOR_TEMP_INDEX).append(Symbol.SPACE);
    sb.append(Symbol.ANGLE_BRACKETS_LEFT).append(Symbol.SPACE);
    sb.append(JavaVarName.FINAL_BATCH_INSERT_NUM);
    sb.append(Symbol.SEMICOLON).append(Symbol.SPACE).append(JavaVarName.FOR_TEMP_INDEX);
    sb.append(JavaVarValue.ADD_ADD).append(Symbol.BRACKET_RIGHT).append(Symbol.SPACE);
    sb.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);

    // 数据设置操作
    sb.append(JavaFormat.appendTab(tabIndex + 4)).append(poPackage.getClassName());
    sb.append(Symbol.SPACE).append(JavaVarName.LIST_GET_NAME_TEMP_NAME);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(JavaVarName.BATCH_LIST_NAME);
    sb.append(Symbol.POINT).append(JavaMethodName.GET);
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.FOR_TEMP_INDEX);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 调用删除方法
    sb.append(JavaFormat.appendTab(tabIndex + 4));
    sb.append(insertReturnType).append(Symbol.SPACE).append(JavaVarName.DELETE_METHOD_RSP);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(JavaVarName.INSTANCE_NAME).append(Symbol.POINT).append(deleteMethod.getName());
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.LIST_GET_NAME_TEMP_NAME);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 进行删除后的结果断言
    sb.append(JavaFormat.appendTab(tabIndex + 4));
    sb.append(JunitKey.ASSERT).append(insertSuccessValue);
    sb.append(Symbol.COMMA).append(Symbol.SPACE).append(JavaVarName.DELETE_METHOD_RSP);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 循环遍历结束
    sb.append(JavaFormat.appendTab(tabIndex + 3)).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE);

    // 判断结束
    sb.append(JavaFormat.appendTab(tabIndex + 2));
    sb.append(Symbol.BRACE_RIGHT).append(Symbol.ENTER_LINE);
  }

  /**
   * 数据查询方法
   *
   * @param sb 添加的对象
   * @param queryMethod 方法
   * @param poPackageInfo 导入的实体
   * @param columnMap 列集合
   * @param methodList 方法
   * @param dbType 类型信息
   * @param primaryList 主键列
   * @param insertReturnType 插入的返回值
   * @param insertSuccessValue 插入的成功的值
   */
  public void batchDelete(
      StringBuilder sb,
      MethodInfo queryMethod,
      ImportPackageInfo poPackageInfo,
      Map<String, TableColumnDTO> columnMap,
      List<MethodInfo> methodList,
      DatabaseTypeEnum dbType,
      List<TableColumnDTO> primaryList,
      String insertReturnType,
      String insertSuccessValue) {
    String methodName = NameProcess.INSTANCE.toJavaNameFirstUpper(queryMethod.getName());

    int tabIndex = 0;

    // 查询方法的定义
    GenerateJunitQuery.INSTANCE.queryMethodDefine(sb, queryMethod, methodName);

    // 进行in的处理
    GenerateJunitQuery.INSTANCE.conditionIn(sb, queryMethod, tabIndex, poPackageInfo, columnMap);

    // 调用批量添加方法
    GenerateJunitUpdate.INSTANCE.invokeBatch(
        sb, tabIndex + 1, methodList, insertReturnType, insertSuccessValue);

    // 进行标识的设置操作,当前为批量删除方法，
    GenerateJunitDefine.INSTANCE.setBatchInsertFlag(
        sb, tabIndex, JavaVarValue.INSERT_TYPE_NONE_KEY);

    sb.append(Symbol.ENTER_LINE);

    // 进行查询条件的封装
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(poPackageInfo.getClassName());
    sb.append(Symbol.SPACE).append(JavaVarName.METHOD_PARAM_TEMP_NAME).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE).append(JavaKeyWord.NEW);
    sb.append(Symbol.SPACE).append(poPackageInfo.getClassName()).append(Symbol.BRACKET_LEFT);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 检查是否存在in关键字
    boolean inCondition = WhereUtils.checkInCondition(queryMethod.getWhereInfo());

    // 如果存在in关键字，还需要进行集合条件的封装
    if (inCondition) {
      // 进行关联参数的定义
      GenerateJunitQuery.INSTANCE.conditionInDefine(
          sb, queryMethod, columnMap, poPackageInfo, dbType, tabIndex, insertReturnType);
    } else {
      // 如果当前存在where条件，则使用where条件
      if (queryMethod.getWhereInfo() != null && !queryMethod.getWhereInfo().isEmpty()) {
        GenerateJunitQuery.INSTANCE.setQueryField(sb, queryMethod, columnMap, tabIndex);
      } else {
        // 当不存在where条件时，则使用主键作为条件
        GenerateJunitQuery.INSTANCE.setQueryFieldPrimary(sb, tabIndex, primaryList);
      }
      // 单个查询的调用
      GenerateJunitQuery.INSTANCE.queryOneRsp(sb, tabIndex, insertReturnType, queryMethod);
    }

    this.batchDeleteRspAssert(sb, tabIndex, insertSuccessValue);

    // 执行方法结果断言
    sb.append(JavaFormat.appendTab(tabIndex + 1));
    sb.append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 批量删除后的结果验证
   *
   * @param sb
   * @param tabIndex
   */
  private void batchDeleteRspAssert(StringBuilder sb, int tabIndex, String insertSuccessValue) {
    // 执行方法结果断言
    sb.append(JavaFormat.appendTab(tabIndex + 2));
    sb.append(JunitKey.ASSERT).append(insertSuccessValue);
    sb.append(Symbol.COMMA).append(Symbol.SPACE).append(JavaVarName.INVOKE_METHOD_QUERY_RSP);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);
  }
}
