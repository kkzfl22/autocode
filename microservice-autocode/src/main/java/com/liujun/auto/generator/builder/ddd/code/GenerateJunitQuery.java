package com.liujun.auto.generator.builder.ddd.code;

import com.liujun.auto.generator.builder.utils.JavaClassCodeUtils;
import com.liujun.auto.generator.builder.utils.ReturnUtils;
import com.liujun.auto.generator.builder.utils.WhereUtils;
import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.config.generate.entity.TypeInfo;
import com.liujun.auto.config.generate.entity.WhereInfo;
import com.liujun.auto.constant.GenerateDefineFlag;
import com.liujun.auto.constant.MyBatisOperatorFlag;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.constant.ImportCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.JavaMethodName;
import com.liujun.auto.generator.builder.ddd.constant.JavaVarName;
import com.liujun.auto.generator.builder.ddd.constant.JavaVarValue;
import com.liujun.auto.generator.builder.ddd.constant.JunitKey;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.ddd.entity.JavaMethodEntity;
import com.liujun.auto.generator.builder.utils.JavaCommentUtil;
import com.liujun.auto.generator.convergence.TypeConvergence;
import com.liujun.auto.generator.database.constant.DatabaseTypeEnum;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.serivce.JavaFormat;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.List;
import java.util.Map;

/**
 * 数据库单元测试的查询方法
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJunitQuery {

  public static final GenerateJunitQuery INSTANCE = new GenerateJunitQuery();

  /**
   * 单元测试中的查询部分
   *
   * @param sb
   * @param queryMethod
   * @param poPackageInfo
   * @param columnMap
   * @param tabIndex
   * @param dbType
   * @param primaryList
   */
  public void junitQueryMethod(
      StringBuilder sb,
      MethodInfo queryMethod,
      ImportPackageInfo poPackageInfo,
      Map<String, TableColumnDTO> columnMap,
      int tabIndex,
      DatabaseTypeEnum dbType,
      List<TableColumnDTO> primaryList,
      ImportPackageInfo targetPackage) {

    sb.append(Symbol.ENTER_LINE);

    TypeInfo resultType = queryMethod.getReturnType();
    String className = resultType.getImportClassName();
    className =
        className.replaceAll(
            GenerateDefineFlag.TABLE_NAME.getDefineFlag(), poPackageInfo.getClassName());

    // 进行条件的设置
    this.methodQueryConditionSet(sb, queryMethod, poPackageInfo, columnMap, dbType, primaryList);

    // 调用查询方法
    this.invokeQueryMethodData(sb, tabIndex, queryMethod, className, targetPackage);

    // 执行方法结果断言
    methodResponseAssert(sb, tabIndex, queryMethod);
  }

  /**
   * 单元测试中的分页查询部分
   *
   * @param sb
   * @param queryMethod
   * @param poPackageInfo
   * @param columnMap
   * @param tabIndex
   * @param dbType
   * @param primaryList
   */
  public void junitPageQueryMethod(
      StringBuilder sb,
      MethodInfo queryMethod,
      ImportPackageInfo poPackageInfo,
      Map<String, TableColumnDTO> columnMap,
      int tabIndex,
      DatabaseTypeEnum dbType,
      List<TableColumnDTO> primaryList,
      ImportPackageInfo targetPackage) {

    sb.append(Symbol.ENTER_LINE);

    TypeInfo resultType = queryMethod.getReturnType();
    String className = resultType.getImportClassName();
    className =
        className.replaceAll(
            GenerateDefineFlag.TABLE_NAME.getDefineFlag(), poPackageInfo.getClassName());

    // 进行条件的设置
    this.methodQueryConditionSet(sb, queryMethod, poPackageInfo, columnMap, dbType, primaryList);

    // 调用分页查询方法
    this.invokePageQueryMethod(sb, queryMethod, poPackageInfo, targetPackage);

    // 执行方法结果断言
    methodPageResponseAssert(sb, poPackageInfo);
  }

  /**
   * 查询的条件的设置
   *
   * @param sb
   * @param queryMethod 查询方法
   * @param poPackageInfo po实体信息
   * @param columnMap 列信息
   * @param dbType 类型
   * @param primaryList 主键
   */
  public void methodQueryConditionSet(
      StringBuilder sb,
      MethodInfo queryMethod,
      ImportPackageInfo poPackageInfo,
      Map<String, TableColumnDTO> columnMap,
      DatabaseTypeEnum dbType,
      List<TableColumnDTO> primaryList) {
    int tabIndex = 0;
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
      conditionInDefine(sb, queryMethod, columnMap, poPackageInfo, dbType, tabIndex);
    } else {
      // 如果当前存在where条件，则使用where条件
      if (queryMethod.getWhereInfo() != null && !queryMethod.getWhereInfo().isEmpty()) {
        setQueryField(sb, queryMethod, columnMap, tabIndex);
      } else {
        // 当不存在where条件时，则使用主键作为条件
        setQueryFieldPrimary(sb, tabIndex, primaryList);
      }
    }
  }

  /**
   * 查询方法结果断言操作
   *
   * @param sb
   * @param poPackage 实体信息
   */
  private void methodPageResponseAssert(StringBuilder sb, ImportPackageInfo poPackage) {
    int tabIndex = 0;
    // 如果当前返回结果为结果集，则使用使用集合断言
    sb.append(JavaFormat.appendTab(tabIndex + 2));
    sb.append(JavaKeyWord.THIS).append(Symbol.POINT);
    sb.append(JavaMethodName.ASSERT_DATA_LIST);
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.BATCH_LIST_NAME);
    sb.append(Symbol.COMMA).append(Symbol.SPACE);
    sb.append(ImportCodePackageKey.PAGE_RESULT.getPackageInfo().getVarName());
    sb.append(Symbol.POINT).append(JavaMethodName.GET);
    sb.append(NameProcess.INSTANCE.toJavaNameFirstUpper(JavaMethodName.PAGE_DATA));
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 查询方法结果断言操作
   *
   * @param sb
   * @param tabIndex
   * @param methodInfo
   */
  private void methodResponseAssert(StringBuilder sb, int tabIndex, MethodInfo methodInfo) {
    // 如果当前返回结果为结果集，则使用使用集合断言
    boolean checkList = ReturnUtils.checkReturnList(methodInfo.getReturns());
    if (checkList) {
      sb.append(JavaFormat.appendTab(tabIndex + 2));
      sb.append(JavaKeyWord.THIS).append(Symbol.POINT);
      sb.append(JavaMethodName.ASSERT_DATA_LIST);
      sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.BATCH_LIST_NAME);
      sb.append(Symbol.COMMA).append(Symbol.SPACE).append(JavaVarName.INVOKE_METHOD_QUERY_RSP);
      sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
      sb.append(Symbol.ENTER_LINE);
    }
    // 当前为对象，则执行对象断言
    else if (ReturnUtils.checkReturnObject(methodInfo.getReturns())) {
      sb.append(JavaFormat.appendTab(tabIndex + 2));
      sb.append(JavaKeyWord.THIS).append(Symbol.POINT);
      sb.append(JavaMethodName.ASSERT_DATA);
      sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.INSTANCE_NAME_ENTITY);
      sb.append(Symbol.COMMA).append(Symbol.SPACE).append(JavaVarName.INVOKE_METHOD_QUERY_RSP);
      sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
      sb.append(Symbol.ENTER_LINE);
    }
  }

  /**
   * 查询方法的定义
   *
   * @param sb 字符
   * @param queryMethod 查询方法
   * @param methodName 方法名
   */
  public void queryMethodDefine(StringBuilder sb, MethodInfo queryMethod, String methodName) {

    // 方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 注解符
            .annotation(JunitKey.ANNO_TEST)
            // 公共的访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 方法注释
            .comment(queryMethod.getComment())
            // 返回值
            .type(JavaKeyWord.VOID)
            // 方法名
            .name(GenerateJunitDefine.JUNIT_METHOD_BEFORE + methodName)
            .build();

    // 方法定义生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);

    // 方法开始
    JavaClassCodeUtils.methodStart(sb);
  }

  /**
   * 针查询条件条件中的in进行封装
   *
   * @param sb
   * @param queryMethod 查询方法
   * @param tabIndex tab个数
   * @param columnMap 列
   */
  private void setQueryFieldIn(
      StringBuilder sb,
      MethodInfo queryMethod,
      int tabIndex,
      Map<String, TableColumnDTO> columnMap) {
    // 对in字段进行查询参数值的设置
    for (WhereInfo whereInfo : queryMethod.getWhereInfo()) {
      TableColumnDTO tableInfo = columnMap.get(whereInfo.getSqlColumn());
      // java属性的命名
      String fieldName = NameProcess.INSTANCE.toFieldName(tableInfo.getColumnName());
      String setName = NameProcess.INSTANCE.toJavaNameFirstUpper(fieldName);

      // 不为in则进行设置
      if (MyBatisOperatorFlag.IN.equals(whereInfo.getOperatorFlag())) {
        sb.append(JavaFormat.appendTab(tabIndex + 2));
        sb.append(JavaVarName.METHOD_PARAM_TEMP_NAME);
        sb.append(Symbol.POINT).append(JavaMethodName.SET).append(setName);
        sb.append(JavaVarName.NAME_LIST_SUFFIX);
        sb.append(Symbol.BRACKET_LEFT).append(fieldName).append(JavaVarName.NAME_LIST_SUFFIX);
        sb.append(Symbol.BRACKET_RIGHT);
        sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
      }
    }
  }

  /**
   * 以主键作为作查询条件
   *
   * @param sb 字符信息
   * @param tabIndex tab个数
   * @param primaryList 主键列
   */
  public void setQueryFieldPrimary(
      StringBuilder sb, int tabIndex, List<TableColumnDTO> primaryList) {
    for (TableColumnDTO tableInfo : primaryList) {
      // java属性的命名
      String fieldName = NameProcess.INSTANCE.toFieldName(tableInfo.getColumnName());
      String setName = NameProcess.INSTANCE.toJavaNameFirstUpper(fieldName);

      sb.append(JavaFormat.appendTab(tabIndex + 2));
      sb.append(JavaVarName.METHOD_PARAM_TEMP_NAME);
      sb.append(Symbol.POINT).append(JavaMethodName.SET).append(setName);
      sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.INSTANCE_NAME_ENTITY);
      sb.append(Symbol.POINT).append(JavaMethodName.GET).append(setName);
      sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
      sb.append(Symbol.BRACKET_RIGHT);
      sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    }
  }

  /**
   * 对where条件进行设置
   *
   * @param sb 字符信息
   * @param queryMethod 查询方法
   * @param columnMap 列信息
   * @param tabIndex tab个数
   */
  public void setQueryField(
      StringBuilder sb,
      MethodInfo queryMethod,
      Map<String, TableColumnDTO> columnMap,
      int tabIndex) {

    // 循环进行对值的设置
    for (WhereInfo whereInfo : queryMethod.getWhereInfo()) {
      TableColumnDTO tableInfo = columnMap.get(whereInfo.getSqlColumn());
      // java属性的命名
      String fieldName = NameProcess.INSTANCE.toFieldName(tableInfo.getColumnName());
      String setName = NameProcess.INSTANCE.toJavaNameFirstUpper(fieldName);

      sb.append(JavaFormat.appendTab(tabIndex + 2));
      sb.append(JavaVarName.METHOD_PARAM_TEMP_NAME);
      sb.append(Symbol.POINT).append(JavaMethodName.SET).append(setName);
      sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.INSTANCE_NAME_ENTITY);
      sb.append(Symbol.POINT).append(JavaMethodName.GET).append(setName);
      sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
      sb.append(Symbol.BRACKET_RIGHT);
      sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    }
  }

  /**
   * 对in字段进行设置操作
   *
   * @param sb 字符信息
   * @param queryMethod 查询方法
   * @param columnMap 列信息
   * @param tabIndex tab个数
   */
  private void setFieldIn(
      StringBuilder sb,
      MethodInfo queryMethod,
      Map<String, TableColumnDTO> columnMap,
      int tabIndex) {

    // 循环进行对值的设置
    for (WhereInfo whereInfo : queryMethod.getWhereInfo()) {
      TableColumnDTO tableInfo = columnMap.get(whereInfo.getSqlColumn());
      // java属性的命名
      String fieldName = NameProcess.INSTANCE.toFieldName(tableInfo.getColumnName());
      String setName = NameProcess.INSTANCE.toJavaNameFirstUpper(fieldName);

      // 为in则进行设置
      if (MyBatisOperatorFlag.IN.equals(whereInfo.getOperatorFlag())) {
        sb.append(JavaFormat.appendTab(tabIndex + 3));
        sb.append(fieldName).append(JavaVarName.NAME_LIST_SUFFIX).append(Symbol.POINT);
        sb.append(JavaMethodName.LIST_ADD).append(Symbol.BRACKET_LEFT);
        sb.append(JavaVarName.LIST_GET_NAME_TEMP_NAME);
        sb.append(Symbol.POINT).append(JavaMethodName.GET).append(setName);
        sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
        sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
        sb.append(Symbol.ENTER_LINE);
      }
    }
  }

  /**
   * 进行条件的声明操作
   *
   * @param sb
   * @param queryMethod 条件的声明
   * @param columnMap 列信息
   * @param poPackageInfo 包信息
   * @param dbType 类型
   * @param tabIndex tab数量
   */
  public void conditionInDefine(
      StringBuilder sb,
      MethodInfo queryMethod,
      Map<String, TableColumnDTO> columnMap,
      ImportPackageInfo poPackageInfo,
      DatabaseTypeEnum dbType,
      int tabIndex) {

    // 属性的相关定义
    defineField(sb, queryMethod, columnMap, dbType, tabIndex);

    // 对数据进行循环操作
    foreachStart(sb, tabIndex, poPackageInfo, JavaVarValue.FOR_INDEX_START);

    // 对in的字段进行值设置操作
    setFieldIn(sb, queryMethod, columnMap, tabIndex);

    // 循环结束
    foreachFinish(sb, tabIndex);

    // 将in字段设置到请求的查询条件中
    setQueryFieldIn(sb, queryMethod, tabIndex, columnMap);
  }

  /**
   * 属性的相关定义信息
   *
   * @param sb
   * @param queryMethod 查询方法
   * @param columnMap 列信息
   * @param dbType 类型
   * @param tabIndex tab数量
   */
  private void defineField(
      StringBuilder sb,
      MethodInfo queryMethod,
      Map<String, TableColumnDTO> columnMap,
      DatabaseTypeEnum dbType,
      int tabIndex) {
    // 当前为in的集合作为为条件的变量定义
    for (WhereInfo whereInfo : queryMethod.getWhereInfo()) {

      TableColumnDTO tableInfo = columnMap.get(whereInfo.getSqlColumn());
      // java属性的命名
      String fieldName = NameProcess.INSTANCE.toFieldName(tableInfo.getColumnName());

      String javaDataType = TypeConvergence.getJavaType(dbType, tableInfo.getDataType());
      String setName = NameProcess.INSTANCE.toJavaNameFirstUpper(fieldName);
      String columnName = NameProcess.INSTANCE.toFieldName(tableInfo.getColumnName());

      // 只要当前不为in，则进行生成
      if (MyBatisOperatorFlag.IN.equals(whereInfo.getOperatorFlag())) {
        sb.append(JavaFormat.appendTab(tabIndex + 2));
        sb.append(JavaKeyWord.LIST_TYPE).append(javaDataType).append(JavaKeyWord.LIST_TYPE_END);
        sb.append(Symbol.SPACE).append(columnName).append(JavaVarName.NAME_LIST_SUFFIX);
        sb.append(Symbol.SPACE).append(Symbol.EQUAL);
        sb.append(Symbol.SPACE).append(JavaKeyWord.NEW).append(Symbol.SPACE);
        sb.append(JavaKeyWord.LIST_TYPE_ARRAYLIST);
        sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
        sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
      }

      // 不为in则进行设置
      else {
        sb.append(JavaFormat.appendTab(tabIndex + 2));
        sb.append(JavaVarName.METHOD_PARAM_TEMP_NAME);
        sb.append(Symbol.POINT).append(JavaMethodName.SET).append(setName);
        sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.INSTANCE_NAME_ENTITY);
        sb.append(Symbol.POINT).append(JavaMethodName.GET).append(setName);
        sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
        sb.append(Symbol.BRACKET_RIGHT);
        sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
      }
    }
  }

  /**
   * 检查当前是否为批量操作的检查
   *
   * @param method
   * @return
   */
  public boolean batchFlag(MethodInfo method) {

    // 1,如果当前存在in关键字，则为批量操作
    if (WhereUtils.checkInCondition(method.getWhereInfo())) {
      return true;
    }

    // 如果当前存在使用主键标识，则说明为当前为单个添加
    if (null != method.getPrimaryFlag() && method.getPrimaryFlag()) {
      return false;
    }

    // 其他情况，则检查返回类型，为集合说明为批量操作
    return method.getReturns() != null
        && method.getReturns().indexOf(JavaKeyWord.IMPORT_LIST) != -1;
  }

  /**
   * in条件的处理
   *
   * @param sb sb
   * @param method 方法
   * @param tabIndex tab的索引值
   * @param poPackage 实体
   * @param columnMap 列信息
   */
  public void conditionIn(
      StringBuilder sb,
      MethodInfo method,
      int tabIndex,
      ImportPackageInfo poPackage,
      Map<String, TableColumnDTO> columnMap) {
    // 存在in的关联，并且还存在其他条件，则需要对非in的字段设置为统一的值
    boolean updFlag = WhereUtils.batchUpdateData(method.getWhereInfo());

    // 当前仅处理in的字段操作,非in字段不操作
    if (!updFlag) {
      return;
    }

    // 循环输出开始
    foreachStart(sb, tabIndex, poPackage, JavaVarValue.FOR_INDEX_START_1);

    // 针对非in的字段进行处理
    this.conditionSetNotIn(sb, tabIndex, method, columnMap);

    // 循环的结束
    this.foreachFinish(sb, tabIndex);
  }

  /**
   * 非in的字段进行设置操作
   *
   * @param sb 字符
   * @param tabIndex 字符
   * @param method 方法
   * @param columnMap 列map
   */
  private void conditionSetNotIn(
      StringBuilder sb, int tabIndex, MethodInfo method, Map<String, TableColumnDTO> columnMap) {
    for (WhereInfo whereInfo : method.getWhereInfo()) {
      TableColumnDTO tableInfo = columnMap.get(whereInfo.getSqlColumn());
      // java属性的命名
      String fieldName = NameProcess.INSTANCE.toFieldName(tableInfo.getColumnName());
      String setName = NameProcess.INSTANCE.toJavaNameFirstUpper(fieldName);

      // 只要当前不为in，则进行生成
      if (!MyBatisOperatorFlag.IN.equals(whereInfo.getOperatorFlag())) {

        sb.append(JavaFormat.appendTab(tabIndex + 3));
        sb.append(JavaVarName.LIST_GET_NAME_TEMP_NAME);
        sb.append(Symbol.POINT).append(JavaMethodName.SET).append(setName);
        sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.INSTANCE_NAME_ENTITY);
        sb.append(Symbol.POINT).append(JavaMethodName.GET).append(setName);
        sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
        sb.append(Symbol.BRACKET_RIGHT);
        sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
      }
    }
  }

  /**
   * 循环输出开始
   *
   * @param sb 字符信息
   * @param tabIndex tab的位置
   * @param poPackage po包的信息
   * @param forStart 开始索引
   */
  private void foreachStart(
      StringBuilder sb, int tabIndex, ImportPackageInfo poPackage, int forStart) {

    // 循环进行数据的设置
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(JavaKeyWord.FOR_KEY);
    sb.append(Symbol.SPACE);
    sb.append(Symbol.BRACKET_LEFT).append(JavaKeyWord.INT_TYPE).append(Symbol.SPACE);
    sb.append(JavaVarName.FOR_TEMP_INDEX).append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(forStart).append(Symbol.SEMICOLON);
    sb.append(Symbol.SPACE).append(JavaVarName.FOR_TEMP_INDEX).append(Symbol.SPACE);
    sb.append(Symbol.ANGLE_BRACKETS_LEFT).append(Symbol.SPACE);
    sb.append(JavaVarName.FINAL_BATCH_INSERT_NUM);
    sb.append(Symbol.SEMICOLON).append(Symbol.SPACE).append(JavaVarName.FOR_TEMP_INDEX);
    sb.append(JavaVarValue.ADD_ADD).append(Symbol.BRACKET_RIGHT).append(Symbol.SPACE);
    sb.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);

    // 数据设置操作
    sb.append(JavaFormat.appendTab(tabIndex + 3)).append(poPackage.getClassName());
    sb.append(Symbol.SPACE).append(JavaVarName.LIST_GET_NAME_TEMP_NAME);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(JavaVarName.BATCH_LIST_NAME);
    sb.append(Symbol.POINT).append(JavaMethodName.GET);
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.FOR_TEMP_INDEX);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
  }

  /**
   * 调用查询方法
   *
   * @param sb
   * @param tabIndex tab符号
   */
  public void invokeQueryMethodData(
      StringBuilder sb,
      int tabIndex,
      MethodInfo queryMethod,
      String classInfo,
      ImportPackageInfo targetPackage) {

    // 方法调用
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(classInfo).append(Symbol.SPACE);
    sb.append(JavaVarName.INVOKE_METHOD_QUERY_RSP).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE).append(targetPackage.getVarName());
    sb.append(Symbol.POINT).append(queryMethod.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.METHOD_PARAM_TEMP_NAME).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
  }

  /**
   * 调用分页的方法
   *
   * @param sb
   */
  public void invokePageQueryMethod(
      StringBuilder sb,
      MethodInfo queryMethod,
      ImportPackageInfo poPackageInfo,
      ImportPackageInfo targetPackage) {

    int tabIndex = 0;
    // 声明分页的对象信息
    sb.append(JavaFormat.appendTab(tabIndex + 2));
    sb.append(ImportCodePackageKey.PAGE_RESULT.getPackageInfo().getClassName());
    sb.append(Symbol.ANGLE_BRACKETS_LEFT);
    sb.append(poPackageInfo.getClassName());
    sb.append(Symbol.ANGLE_BRACKETS_RIGHT);
    sb.append(Symbol.SPACE).append(JavaVarName.QUERY_PAGE_PARAM_VAR).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(ImportCodePackageKey.PAGE_PARAM.getPackageInfo().getClassName());
    sb.append(Symbol.POINT);
    sb.append(Symbol.ANGLE_BRACKETS_LEFT);
    sb.append(poPackageInfo.getClassName());
    sb.append(Symbol.ANGLE_BRACKETS_RIGHT);
    sb.append(JavaMethodName.PAGE_BUILDER);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.POINT).append(JavaMethodName.PAGE_SIZE);
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.FINAL_BATCH_INSERT_NUM);
    sb.append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.POINT).append(JavaMethodName.PAGE_NUM);
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarValue.ZERO);
    sb.append(Symbol.BRACKET_RIGHT);
    // 数据
    sb.append(Symbol.POINT);
    sb.append(JavaMethodName.PAGE_DATA).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.METHOD_PARAM_TEMP_NAME);
    sb.append(Symbol.BRACKET_RIGHT);

    sb.append(Symbol.POINT).append(JavaMethodName.PAGE_BUILD);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 方法调用
    sb.append(JavaFormat.appendTab(tabIndex + 2));
    sb.append(JavaCommentUtil.pageQueryReturn(poPackageInfo.getClassName()));
    sb.append(Symbol.SPACE).append(ImportCodePackageKey.PAGE_RESULT.getPackageInfo().getVarName());
    sb.append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE).append(targetPackage.getVarName());
    sb.append(Symbol.POINT).append(queryMethod.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.QUERY_PAGE_PARAM_VAR);
    sb.append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
  }

  /**
   * 循环输出的结束
   *
   * @param sb 字符往上
   * @param tabIndex tab的个数
   */
  private void foreachFinish(StringBuilder sb, int tabIndex) {
    // 循环结束
    sb.append(JavaFormat.appendTab(tabIndex + 2)).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE);
  }
}
