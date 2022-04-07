package com.liujun.auto.generator.builder.operator.code;

import com.liujun.auto.config.generate.GenerateConfigProcess;
import com.liujun.auto.config.generate.GenerateErrorCodeProcess;
import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.config.generate.entity.WhereInfo;
import com.liujun.auto.constant.MyBatisOperatorFlag;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.constant.CodeComment;
import com.liujun.auto.generator.builder.constant.ImportCodePackageKey;
import com.liujun.auto.generator.builder.constant.JavaMethodName;
import com.liujun.auto.generator.builder.constant.JavaVarName;
import com.liujun.auto.generator.builder.entity.ErrorCodeGenerate;
import com.liujun.auto.generator.builder.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.entity.JavaClassEntity;
import com.liujun.auto.generator.builder.entity.JavaClassFieldEntity;
import com.liujun.auto.generator.builder.entity.JavaEnumFieldEntity;
import com.liujun.auto.generator.builder.entity.JavaMethodEntity;
import com.liujun.auto.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.auto.generator.builder.operator.utils.MethodUtils;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.serivce.JavaFormat;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 错误码信息
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaErrorCode {

  public static final GenerateJavaErrorCode INSTANCE = new GenerateJavaErrorCode();

  /** 导入包信息 */
  private static final List<String> IMPORT_LIST =
      Arrays.asList(
          // 错误对象
          ImportCodePackageKey.ERROR_DATA.getPackageInfo().packageOut(),
          // 错误容器对象
          ImportCodePackageKey.ERROR_COLLECT.getPackageInfo().packageOut(),
          // list
          JavaKeyWord.IMPORT_LIST,
          // 集合
          JavaKeyWord.IMPORT_ARRAYLIST,
          // 集合加载容器
          ImportCodePackageKey.ERROR_LOADER_COLLECT.getPackageInfo().packageOut());

  /** 以错误类型为前缀，当前表示为空 */
  private static final String NULL_PREFIX = "NULL_";

  /** 空错误的前缀 */
  private static final String NULL_KEY_PREFIX = "null";

  /** 注释 */
  private static final String NULL_COMMENT = "为空的错误码";

  /** 超过最大长度的错误的前缀 */
  private static final String MAX_PREFIX = "MAX_LENGTH_";

  /** 超过最大值的前缀 */
  private static final String MAX_KEY_PREFIX = "max.length";

  /** 注释 */
  private static final String MAX_COMMENT = "超过最大长度的错误码";

  /** 获取的方法 */
  private static final String GET_COMMENT = "获取错误码对象";

  /** 数据初始化方法 */
  private static final String DATA_INIT_COMMENT = "数据初始化方法";

  /** 请求集合参数 */
  private static final String REQUEST_LIST_SUFFIX = "request.list";

  /** 当前的请求集合为空 */
  private static final String REQUEST_LIST_COMMENT = "请求集合为空";

  /**
   * 生成错误码
   *
   * @param errorCodePackage 错误码包
   * @param tableColumnList 表列的信息
   * @param methodCode 参数信息
   * @param author 作者
   * @return 生成的代码
   */
  public StringBuilder generateErrorCode(
      ImportPackageInfo errorCodePackage,
      List<TableColumnDTO> tableColumnList,
      List<MethodInfo> methodCode,
      String author) {

    // 类的定义
    StringBuilder sb = new StringBuilder();
    // 枚举文件的定义
    sb.append(this.enumDefine(errorCodePackage, author));

    // 按列进行定义错误码,包括判断空以及最大长度
    sb.append(errorCodeDefine(tableColumnList));

    // 按方法检查集合操作
    sb.append(errorCodeMethodWhereDefine(methodCode, tableColumnList.get(0).getTableName()));

    // 属性结束
    sb.append(fieldFinish());

    // 属性的定义
    sb.append(enumFieldDefine());

    // 定义模块名称的定义
    sb.append(moduleName());

    // 枚举的构建函数
    sb.append(this.enumFieldConstructor(errorCodePackage));

    // 获取值的方法
    sb.append(this.getValueMethod());

    // 错误码加载方法
    sb.append(this.errorLoaderMethod(errorCodePackage));

    // 错误码init方法
    sb.append(this.dataInit(errorCodePackage));

    // 类的结束
    JavaClassCodeUtils.classEnd(sb);

    return sb;
  }

  /**
   * 生成模块的名称
   *
   * @return 模块的定义
   */
  private String moduleName() {
    String moduleName =
        GenerateConfigProcess.INSTANCE
            .getCfgEntity()
            .getGenerate()
            .getCodeMenuTree()
            .getDomainName();
    String moduleNameOutValue = Symbol.QUOTE + moduleName + Symbol.QUOTE;

    JavaClassFieldEntity fieldEntity =
        JavaClassFieldEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 静态
            .staticFlag(JavaKeyWord.STATIC)
            // final标识
            .finalFlag(JavaKeyWord.FINAL)
            // 类型
            .type(JavaKeyWord.TYPE_STRING)
            // 名称
            .name(JavaVarName.MODULE_NAME_VAR)
            // 模块名
            .value(moduleNameOutValue)
            // 模块名称的注释
            .comment(CodeComment.MODULE_NAME_COMMENT)
            .build();

    return JavaClassCodeUtils.getClassField(fieldEntity);
  }

  /**
   * 获取值的方法
   *
   * @return 获取值
   */
  private String getValueMethod() {

    String methodName =
        NameProcess.INSTANCE.toJavaNameFirstUpper(
            ImportCodePackageKey.ERROR_DATA.getPackageInfo().getVarName());
    methodName = JavaMethodName.GET + methodName;

    JavaMethodEntity methodEntity =
        JavaMethodEntity.builder()
            // 获取值的方法
            .visit(JavaKeyWord.PUBLIC)
            // 返回值
            .type(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getClassName())
            // 方法名
            .name(methodName)
            // 注释
            .comment(GET_COMMENT)
            .build();

    StringBuilder dataInfo = new StringBuilder();

    // 方法开始
    JavaClassCodeUtils.methodDefine(dataInfo, methodEntity);
    JavaClassCodeUtils.methodStart(dataInfo);

    // 返回
    dataInfo.append(JavaFormat.appendTab(2));
    dataInfo.append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    dataInfo.append(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getVarName());
    dataInfo.append(Symbol.SEMICOLON);
    dataInfo.append(Symbol.ENTER_LINE);

    // 方法结束
    JavaClassCodeUtils.methodEnd(dataInfo);

    return dataInfo.toString();
  }

  /**
   * 枚举的构建函数
   *
   * @return
   */
  private String enumFieldConstructor(ImportPackageInfo checkPackage) {
    StringBuilder out = new StringBuilder();

    // 枚举的构建函数开始
    out.append(JavaFormat.appendTab(1));
    out.append(checkPackage.getClassName());
    out.append(Symbol.BRACKET_LEFT);
    out.append(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getClassName());
    out.append(Symbol.SPACE);
    out.append(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getVarName());
    out.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACE_LEFT);
    out.append(Symbol.ENTER_LINE);

    // 构建函数赋值
    out.append(JavaFormat.appendTab(2));
    out.append(JavaKeyWord.THIS).append(Symbol.POINT);
    out.append(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getVarName());
    out.append(Symbol.SPACE).append(Symbol.EQUAL).append(Symbol.SPACE);
    out.append(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getVarName());
    out.append(Symbol.SEMICOLON);
    out.append(Symbol.ENTER_LINE);

    // 枚举的构建函数结束
    out.append(JavaFormat.appendTab(1));
    out.append(Symbol.BRACE_RIGHT);
    out.append(Symbol.ENTER_LINE);
    out.append(Symbol.ENTER_LINE);

    return out.toString();
  }

  /**
   * 错误码的加载方法
   *
   * @return
   */
  private String errorLoaderMethod(ImportPackageInfo errorCodePackage) {

    // 类的定义
    JavaMethodEntity loaderMethod =
        JavaMethodEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // static标识
            .staticFlag(JavaKeyWord.STATIC)
            // 返回类型void
            .type(JavaKeyWord.VOID)
            // 名称加载方法
            .name(JavaMethodName.ERROR_CODE_LOADER)
            // 注释
            .comment(CodeComment.ERROR_CODE_LOADER_COMMENT)
            .build();

    StringBuilder loaderMethodStr = new StringBuilder();

    // 方法的定义
    JavaClassCodeUtils.methodDefine(loaderMethodStr, loaderMethod);

    // 方法的开始
    JavaClassCodeUtils.methodStart(loaderMethodStr);

    // 循环添加到错误容器中
    loaderMethodStr.append(forAddCode(errorCodePackage));

    // 方法的结束
    JavaClassCodeUtils.methodEnd(loaderMethodStr);

    return loaderMethodStr.toString();
  }

  /**
   * 初始化方法的生成
   *
   * @return 生成的代码信息
   */
  private String dataInit(ImportPackageInfo errorCodePackage) {
    StringBuilder sb = new StringBuilder();

    JavaMethodEntity methodEntity =
        JavaMethodEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 静态标识
            .staticFlag(JavaKeyWord.STATIC)
            // 返回类型
            .type(JavaKeyWord.VOID)
            // 方法名
            .name(JavaMethodName.CODE_METHOD_INIT)
            // 初始化方法
            .comment(DATA_INIT_COMMENT)
            .build();

    JavaClassCodeUtils.methodDefine(sb, methodEntity);

    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 添加至容器中
    sb.append(JavaFormat.appendTab(2));
    sb.append(ImportCodePackageKey.ERROR_LOADER_COLLECT.getPackageInfo().getClassName());
    sb.append(Symbol.POINT);
    sb.append(JavaMethodName.ADD_ERROR_CODE).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.MODULE_NAME_VAR).append(Symbol.COMMA);
    sb.append(errorCodePackage.getClassName()).append(Symbol.COLON).append(Symbol.COLON);
    sb.append(JavaMethodName.ERROR_CODE_LOADER).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);

    return sb.toString();
  }

  /**
   * 循环的添加到错误容器中的代码生成
   *
   * @return 生成的代码信息
   */
  private String forAddCode(ImportPackageInfo errorCodePackage) {
    StringBuilder sb = new StringBuilder();

    // 方法名
    String methodName =
        NameProcess.INSTANCE.toJavaNameFirstUpper(
            ImportCodePackageKey.ERROR_DATA.getPackageInfo().getVarName());
    methodName = JavaMethodName.GET + methodName;

    // 将错误码加入到容器中
    sb.append(addErrorCodeCollect(errorCodePackage, methodName));

    return sb.toString();
  }

  /**
   * 将错误码加载到容器中
   *
   * @param errorCodePackage 错误码包的信息
   * @param methodName 方法名
   * @return 返回的代码
   */
  private String addErrorCodeCollect(ImportPackageInfo errorCodePackage, String methodName) {
    StringBuilder sb = new StringBuilder();

    // 定义变量
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.LIST_TYPE);
    sb.append(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getClassName());
    sb.append(JavaKeyWord.LIST_TYPE_END).append(Symbol.SPACE);
    sb.append(JavaVarName.LIST_NAME).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(JavaKeyWord.NEW).append(Symbol.SPACE);
    sb.append(JavaKeyWord.LIST_TYPE_ARRAYLIST).append(Symbol.BRACKET_LEFT);
    sb.append(JavaMethodName.ENUM_METHOD_VALUES).append(Symbol.BRACKET_LEFT);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.POINT);
    sb.append(JavaMethodName.ARRAY_LENGTH);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 循环将数据加入到错误容器集合中
    sb.append(this.forEachOperator(errorCodePackage, methodName));

    // 将数据加入到错误码容器中
    sb.append(JavaFormat.appendTab(2));
    sb.append(ImportCodePackageKey.ERROR_COLLECT.getPackageInfo().getClassName());
    sb.append(Symbol.POINT).append(JavaVarName.INSTANCE_NAME);
    sb.append(Symbol.POINT).append(JavaMethodName.ERROR_ADD_CODE);
    sb.append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.MODULE_NAME_VAR).append(Symbol.COMMA);
    sb.append(JavaVarName.LIST_NAME);
    sb.append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);
    return sb.toString();
  }

  /**
   * 循环将数据加入集合
   *
   * @param errorCodePackage
   * @param methodName
   * @return
   */
  private String forEachOperator(ImportPackageInfo errorCodePackage, String methodName) {
    StringBuilder sb = new StringBuilder();
    // 遍历开始
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.FOR_KEY).append(Symbol.BRACKET_LEFT);
    sb.append(errorCodePackage.getClassName());
    sb.append(Symbol.SPACE).append(JavaVarName.LOOP_TEMP);
    sb.append(Symbol.SPACE).append(Symbol.COLON).append(Symbol.SPACE);
    sb.append(JavaMethodName.ENUM_METHOD_VALUES).append(Symbol.BRACKET_LEFT);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACE_LEFT).append(Symbol.ENTER_LINE);

    // 调用方法添加到错误码容器中
    sb.append(JavaFormat.appendTab(3));
    sb.append(JavaVarName.LIST_NAME);
    sb.append(Symbol.POINT).append(JavaMethodName.LIST_ADD);
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.LOOP_TEMP);
    sb.append(Symbol.POINT).append(methodName);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 遍历结束
    sb.append(JavaFormat.appendTab(2));
    sb.append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE);

    return sb.toString();
  }

  /**
   * 获取枚举属性的定义
   *
   * @return 属性的定义
   */
  private String enumFieldDefine() {

    JavaClassFieldEntity fieldInfo =
        JavaClassFieldEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 不可变标识符
            .finalFlag(JavaKeyWord.FINAL)
            // 类型
            .type(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getClassName())
            // 名称
            .name(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getVarName())
            // 注释
            .comment(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getClassComment())
            .build();

    return JavaClassCodeUtils.getClassField(fieldInfo);
  }

  /**
   * 枚举文件的定义
   *
   * @param classInfo
   * @param author
   * @return
   */
  private String enumDefine(ImportPackageInfo classInfo, String author) {
    JavaClassEntity classEntityInfo =
        JavaClassEntity.builder()
            // 类的关键字
            .classKey(JavaKeyWord.ENUM_KEY)
            // 类名
            .className(classInfo.getClassName())
            // 类注释
            .classComment(classInfo.getClassComment())
            // 包类路径信息
            .packagePath(classInfo.getPackagePath())
            // 导入包信息
            .importList(IMPORT_LIST)
            // 作者
            .author(author)
            .build();

    // 文件类定义
    return JavaClassCodeUtils.javaClassDefine(classEntityInfo).toString();
  }

  /**
   * 错误码定义
   *
   * @param tableColumnList
   * @return
   */
  private String errorCodeDefine(List<TableColumnDTO> tableColumnList) {
    StringBuilder outInsert = new StringBuilder();

    // 获取开始的编码
    GenerateErrorCodeProcess.INSTANCE.getStartCode();

    // 错误码的类处理操作
    for (TableColumnDTO columnInfo : tableColumnList) {

      // 1为空的检查的错误码,当前列不能为空，则需要进行设置操作错误码
      if (!columnInfo.getNullFlag()) {
        String enumValue = this.outEnumFieldValue(columnInfo);
        outInsert.append(enumValue);
      }

      // 2，超过长度的检查
      String enumMoreMax = outEnumFieldMoreMax(columnInfo);
      outInsert.append(enumMoreMax);
    }

    return outInsert.toString();
  }

  /**
   * 按方法参数进行错误码定义
   *
   * <p>目前
   *
   * @param methodCode 方法信息
   * @return 错误码信息
   */
  private String errorCodeMethodWhereDefine(List<MethodInfo> methodCode, String tableName) {
    StringBuilder outInsert = new StringBuilder();

    // 获取开始的编码
    GenerateErrorCodeProcess.INSTANCE.getStartCode();

    // 错误码的类处理操作
    for (MethodInfo columnInfo : methodCode) {
      // 检查参数是否为集合
      if (MethodUtils.checkBatch(columnInfo.getParamType())) {
        // 集合参数检查
        String outValue = this.outEnumFieldList(columnInfo, tableName);
        outInsert.append(outValue);
      }

      // 还有条件的检查
      if (columnInfo.getWhereInfo() != null && !columnInfo.getWhereInfo().isEmpty()) {
        for (WhereInfo whereItem : columnInfo.getWhereInfo()) {
          if (MyBatisOperatorFlag.IN.equals(whereItem.getOperatorFlag())) {
            outInsert.append(errorCodeWhereIn(columnInfo, tableName, whereItem, outInsert));
          }
        }
      }
    }

    return outInsert.toString();
  }

  /**
   * where条件中的错误码定义
   *
   * @param method
   * @param tableName
   * @param whereItem
   * @param outInsert
   * @return
   */
  private String errorCodeWhereIn(
      MethodInfo method, String tableName, WhereInfo whereItem, StringBuilder outInsert) {
    StringBuilder outDataStr = new StringBuilder();

    // 进行数据枚举的集合的查询
    String enumValue = outEnumWhereFieldList(method, tableName, whereItem, outInsert);
    outDataStr.append(enumValue);

    // 2，超过长度的检查
    String enumMoreMax =
        outEnumFieldMoreMaxList(
            tableName,
            whereItem.getSqlColumn() + Symbol.UNDER_LINE + JavaVarName.NAME_LIST_SUFFIX,
            method.getComment(),
            outInsert);
    outDataStr.append(enumMoreMax);

    return outDataStr.toString();
  }

  /**
   * 生成的国际资源的key
   *
   * @param tableName
   * @param columnName
   * @return
   */
  public static String propertiesErrorKeyNullWhereIn(String tableName, String columnName) {

    String fieldName = NameProcess.INSTANCE.toProJavaName(columnName);
    return propertiesErrorKey(NULL_KEY_PREFIX, tableName, fieldName, Symbol.EMPTY);
  }

  /**
   * 生成的国际资源的key
   *
   * @param tableName
   * @param columnName
   * @return
   */
  public static String propertiesErrorKeyNull(String tableName, String columnName) {

    String fieldName = NameProcess.INSTANCE.toProJavaName(columnName);
    return propertiesErrorKey(NULL_KEY_PREFIX, tableName, fieldName, null);
  }

  /**
   * 生成的国际资源的key
   *
   * @param tableName
   * @param columnName
   * @return
   */
  public static String propertiesErrorKeyMax(String tableName, String columnName) {

    // 转换为属性的名称
    String fieldName = NameProcess.INSTANCE.toProJavaName(columnName);
    return propertiesErrorKey(MAX_KEY_PREFIX, tableName, fieldName, null);
  }

  /**
   * 请求参数中集合不能为空
   *
   * @param tableName 表名
   * @param methodName 方法名
   * @return
   */
  public static String propertiesErrorKeyParamListNull(String tableName, String methodName) {
    return propertiesErrorKey(NULL_KEY_PREFIX, tableName, methodName, REQUEST_LIST_SUFFIX);
  }

  /**
   * 错误码输出
   *
   * @param prefix
   * @param tableName
   * @param methodName
   * @param suffix
   * @return
   */
  public static String propertiesErrorKey(
      String prefix, String tableName, String methodName, String suffix) {

    StringBuilder outPropertiesKey = new StringBuilder();

    // 获取模块名称
    String moduleName =
        GenerateConfigProcess.INSTANCE
            .getCfgEntity()
            .getGenerate()
            .getCodeMenuTree()
            .getDomainName();

    // 实体的名称
    String entityName = NameProcess.INSTANCE.toJavaClassName(tableName);

    outPropertiesKey.append(prefix);
    outPropertiesKey.append(Symbol.POINT);
    outPropertiesKey.append(moduleName).append(Symbol.POINT);
    outPropertiesKey.append(entityName).append(Symbol.POINT);
    outPropertiesKey.append(methodName);

    if (!StringUtils.isEmpty(suffix)) {
      outPropertiesKey.append(Symbol.POINT);
      outPropertiesKey.append(suffix);
    }

    return outPropertiesKey.toString();
  }

  /**
   * 获取空的错误码
   *
   * @param tableColumn
   * @return
   */
  public static String enumNullCode(String tableColumn) {
    StringBuilder code = new StringBuilder();

    // 错误类型为前缀
    code.append(NULL_PREFIX);
    // 列名
    String fieldName = NameProcess.INSTANCE.toNameUpperCase(tableColumn);
    code.append(fieldName);

    return code.toString();
  }

  /**
   * 超过最大值的错误码
   *
   * @param tableColumn
   * @return
   */
  public static String enumMaxCode(String tableColumn) {
    String upperCaseWord = NameProcess.INSTANCE.toNameUpperCase(tableColumn);
    return MAX_PREFIX + upperCaseWord;
  }

  /**
   * 属性结束
   *
   * @return
   */
  private String fieldFinish() {
    StringBuilder outFinish = new StringBuilder();

    outFinish.append(JavaFormat.appendTab(1));
    outFinish.append(Symbol.SEMICOLON);
    outFinish.append(Symbol.ENTER_LINE);
    outFinish.append(Symbol.ENTER_LINE);

    return outFinish.toString();
  }

  /**
   * 枚举的属性输出
   *
   * @param columnInfo
   * @return
   */
  private String outEnumFieldValue(TableColumnDTO columnInfo) {
    int incrementId = GenerateErrorCodeProcess.INSTANCE.increment();

    // 生成的属性的key信息
    String outPropertyKeyNull =
        propertiesErrorKeyNull(columnInfo.getTableName(), columnInfo.getColumnName());

    JavaEnumFieldEntity enumFieldEntity =
        JavaEnumFieldEntity.builder()
            // 名称
            .name(enumNullCode(columnInfo.getColumnName()))
            // 值错误提示信息
            .value(ErrorCodeGenerate.outErrorCode(incrementId, outPropertyKeyNull))
            // 注释
            .comment(columnInfo.getColumnMsg() + NULL_COMMENT)
            .build();

    // 枚举值进行输出操作
    String outEnumField = JavaClassCodeUtils.getEnumField(enumFieldEntity);

    return outEnumField;
  }

  /**
   * 枚举的属性,超过最大值
   *
   * @param columnInfo 列信息
   * @return
   */
  private String outEnumFieldMoreMax(TableColumnDTO columnInfo) {

    int incrementId = GenerateErrorCodeProcess.INSTANCE.increment();

    // 生成超过最大值的key信息
    String outPropertyKeyMax =
        propertiesErrorKeyMax(columnInfo.getTableName(), columnInfo.getColumnName());

    JavaEnumFieldEntity enumFieldEntity =
        JavaEnumFieldEntity.builder()
            // 名称
            .name(enumMaxCode(columnInfo.getColumnName()))
            // 值错误提示信息
            .value(ErrorCodeGenerate.outErrorCode(incrementId, outPropertyKeyMax))
            // 超过最大长度的注释
            .comment(columnInfo.getColumnMsg() + MAX_COMMENT)
            .build();

    // 枚举值进行输出操作
    String outEnumField = JavaClassCodeUtils.getEnumField(enumFieldEntity);

    return outEnumField;
  }

  /**
   * 枚举的属性集合,超过最大值
   *
   * @param name 名称信息
   * @param comment 注释
   * @return
   */
  private String outEnumFieldMoreMaxList(
      String tableName, String name, String comment, StringBuilder outDataStr) {

    // 生成超过最大值的key信息
    String outPropertyKeyMax = propertiesErrorKeyMax(tableName, name);

    // 重复数据的检查
    if (outDataStr.indexOf(enumMaxCode(name)) != -1) {
      return Symbol.EMPTY;
    }

    int incrementId = GenerateErrorCodeProcess.INSTANCE.increment();

    JavaEnumFieldEntity enumFieldEntity =
        JavaEnumFieldEntity.builder()
            // 名称
            .name(enumMaxCode(name))
            // 值错误提示信息
            .value(ErrorCodeGenerate.outErrorCode(incrementId, outPropertyKeyMax))
            // 超过最大长度的注释
            .comment(comment + MAX_COMMENT)
            .build();

    // 枚举值进行输出操作
    String outEnumField = JavaClassCodeUtils.getEnumField(enumFieldEntity);

    return outEnumField;
  }

  /**
   * 枚举的属性,集合为空
   *
   * @param columnInfo 列信息
   * @return
   */
  private String outEnumFieldList(MethodInfo columnInfo, String tableName) {

    int incrementId = GenerateErrorCodeProcess.INSTANCE.increment();

    // 生成的属性的key信息
    String outPropertyKeyNull = propertiesErrorKeyParamListNull(tableName, columnInfo.getName());

    JavaEnumFieldEntity enumFieldEntity =
        JavaEnumFieldEntity.builder()
            // 名称
            .name(enumNullCode(columnInfo.getName()))
            // 集合为空的错误提示
            .value(ErrorCodeGenerate.outErrorCode(incrementId, outPropertyKeyNull))
            // 注释
            .comment(columnInfo.getComment() + REQUEST_LIST_COMMENT)
            .build();

    // 枚举值进行输出操作
    String outEnumField = JavaClassCodeUtils.getEnumField(enumFieldEntity);

    return outEnumField;
  }

  /**
   * 请求的查询请求中的集合
   *
   * @param columnInfo 列信息
   * @return
   */
  private String outEnumWhereFieldList(
      MethodInfo columnInfo, String tableName, WhereInfo whereInfo, StringBuilder outDataInfo) {

    String sqlName = whereInfo.getSqlColumn() + Symbol.UNDER_LINE + JavaVarName.NAME_LIST_SUFFIX;

    String nameInfo = enumNullCode(sqlName);

    if (outDataInfo.indexOf(nameInfo) != -1) {
      return Symbol.EMPTY;
    }

    int incrementId = GenerateErrorCodeProcess.INSTANCE.increment();

    // 生成的属性的key信息
    String outPropertyKeyNull = propertiesErrorKeyNullWhereIn(tableName, sqlName);

    JavaEnumFieldEntity enumFieldEntity =
        JavaEnumFieldEntity.builder()
            // 名称
            .name(nameInfo)
            // 集合为空的错误提示
            .value(ErrorCodeGenerate.outErrorCode(incrementId, outPropertyKeyNull))
            // 注释
            .comment(
                columnInfo.getComment()
                    + Symbol.SPACE
                    + whereInfo.getSqlColumn()
                    + Symbol.SPACE
                    + REQUEST_LIST_COMMENT)
            .build();

    // 枚举值进行输出操作
    String outEnumField = JavaClassCodeUtils.getEnumField(enumFieldEntity);

    return outEnumField;
  }
}
