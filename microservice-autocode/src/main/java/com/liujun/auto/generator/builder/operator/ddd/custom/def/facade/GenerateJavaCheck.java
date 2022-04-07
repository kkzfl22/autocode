package com.liujun.auto.generator.builder.operator.ddd.custom.def.facade;

import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.config.generate.entity.WhereInfo;
import com.liujun.auto.constant.MethodTypeEnum;
import com.liujun.auto.constant.MyBatisOperatorFlag;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.constant.ImportCodeActionDefaultPackageKey;
import com.liujun.auto.generator.builder.constant.ImportCodePackageKey;
import com.liujun.auto.generator.builder.constant.JavaMethodName;
import com.liujun.auto.generator.builder.constant.JavaVarName;
import com.liujun.auto.generator.builder.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.entity.JavaMethodArguments;
import com.liujun.auto.generator.builder.entity.JavaMethodEntity;
import com.liujun.auto.generator.builder.operator.code.GenerateJavaErrorCode;
import com.liujun.auto.generator.builder.operator.code.GenerateJavaInterfaceConstant;
import com.liujun.auto.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.auto.generator.builder.operator.utils.MethodUtils;
import com.liujun.auto.generator.builder.operator.utils.TableColumnUtils;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.serivce.JavaFormat;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 生成数据参数校验
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaCheck {

  /** 参数校验的方法 */
  private static final String CHECK_PARAM = "参数校验";

  /** 文件前缀 */
  private static final String NAME_PREFIX = "check";

  /** 错误检查的临时变量名 */
  private static final String VAR_CHECK_NAME = "errorData";

  /** 空检查的代码 */
  private static final String METHOD_CHECK_NULL = "checkNull";

  /** 参数判空的检查 */
  private static final String METHOD_CHECK_NULL_COMMENT = "参数判空的检查，";

  /** 空检查的代码 */
  private static final String METHOD_CHECK_MAXLENGTH = "checkMaxLength";

  /** 参数判空的检查 */
  private static final String METHOD_CHECK_MAXLENGTH_COMMENT = "参数最大长度的检查，";

  /** 方法的参数检查 */
  private static final String CHECK_ERROR_METHOD_NAME = "checkErrorToOne";

  /** 单个添加的方法 */
  private static final String ADD_METHOD_NAME = "单个添加方法";

  /** 错误码的成功获取 */
  private static final String ERROR_CODE_SUCCESS = "APICodeEnum.SUCCESS.getErrorData()";

  /** 用于进行获取分页的对象信息 */
  private static final String PAGE_GET_DATA = "getData()";

  public static final GenerateJavaCheck INSTANCE = new GenerateJavaCheck();

  private static final List<String> IMPORT_LIST =
      Arrays.asList(
          // 错误对象
          ImportCodePackageKey.ERROR_DATA.getPackageInfo().packageOut(),
          // 验证对象
          ImportCodePackageKey.PARAM_CHECK.getPackageInfo().packageOut(),
          // 集合对象
          JavaKeyWord.IMPORT_LIST,
          JavaKeyWord.IMPORT_ARRAYLIST,
          // 公共错误导入
          ImportCodePackageKey.ERROR_CODE_COMMON.getPackageInfo().packageOut(),
          // 分页请求的对象
          ImportCodeActionDefaultPackageKey.API_PAGE_REQUEST.getPackageInfo().packageOut());

  /**
   * 生成数据的参数校验方法
   *
   * @param checkPackage 校验包的信息
   * @param methodList 方法集
   * @param tableColumnList 表列的信息
   * @param dtoPackageInfo dto的实体信息
   * @param author 作者
   * @return 生成的代码
   */
  public StringBuilder generateCheck(
      ImportPackageInfo checkPackage,
      List<MethodInfo> methodList,
      List<TableColumnDTO> tableColumnList,
      ImportPackageInfo dtoPackageInfo,
      ImportPackageInfo errorCodeEnum,
      ImportPackageInfo constantPkg,
      String author) {

    // 类的定义
    StringBuilder sb =
        JavaClassCodeUtils.classDefine(
            checkPackage, getImportList(dtoPackageInfo, errorCodeEnum, constantPkg), null, author);

    for (MethodInfo methodInfo : methodList) {
      // 如果当前数据为添加
      if (MethodTypeEnum.INSERT.getType().equals(methodInfo.getOperator())
          && !MethodUtils.checkBatch(methodInfo.getParamType())) {
        sb.append(
            this.checkInsert(
                methodInfo, tableColumnList, errorCodeEnum, dtoPackageInfo, constantPkg));
      }
      // 如果当前的数据为批量添加
      else if (MethodTypeEnum.INSERT.getType().equals(methodInfo.getOperator())
          && MethodUtils.checkBatch(methodInfo.getParamType())) {
        sb.append(
            this.checkInsertBatch(
                methodList,
                methodInfo,
                tableColumnList,
                errorCodeEnum,
                dtoPackageInfo,
                constantPkg));
      }

      // 修改
      else if (MethodTypeEnum.UPDATE.getType().equals(methodInfo.getOperator())) {
        sb.append(
            this.checkUpdate(
                methodInfo, tableColumnList, errorCodeEnum, dtoPackageInfo, constantPkg));
      }
      // 删除
      else if (MethodTypeEnum.DELETE.getType().equals(methodInfo.getOperator())) {
        sb.append(
            this.whereCondition(
                methodInfo, tableColumnList, errorCodeEnum, dtoPackageInfo, constantPkg));
      }
      // 分页查询
      else if (MethodTypeEnum.QUERY_PAGE.getType().equals(methodInfo.getOperator())) {
        sb.append(
            this.pageCondition(
                methodInfo, tableColumnList, errorCodeEnum, dtoPackageInfo, constantPkg));
      }

      // 查询
      else if (MethodTypeEnum.QUERY.getType().equals(methodInfo.getOperator())) {
        sb.append(
            this.whereCondition(
                methodInfo, tableColumnList, errorCodeEnum, dtoPackageInfo, constantPkg));
      }
    }

    // 类的结束
    JavaClassCodeUtils.classEnd(sb);

    return sb;
  }

  /**
   * 添加方法的参数验证
   *
   * @param methodInfo
   * @param tableColumnList
   * @param errorCode
   * @param dtoPackageInfo
   * @return
   */
  private String checkInsertBatch(
      List<MethodInfo> methodList,
      MethodInfo methodInfo,
      List<TableColumnDTO> tableColumnList,
      ImportPackageInfo errorCode,
      ImportPackageInfo dtoPackageInfo,
      ImportPackageInfo constantPkg) {
    StringBuilder outInsert = new StringBuilder();

    // 生成单个数据项检查的方法
    outInsert.append(
        (builderOneInsert(methodList, tableColumnList, dtoPackageInfo, errorCode, constantPkg)));

    // 添加方法的定义
    outInsert.append(defineMethod(methodInfo, dtoPackageInfo));

    // 方法开始
    JavaClassCodeUtils.methodStart(outInsert);

    // 返回集合对象的声明
    outInsert.append(methodResultListDefine());

    // 构建器对象的声明
    outInsert.append(methodBuilderDefine());

    // 进行集合的判空操作
    outInsert.append(checkListNullParam(dtoPackageInfo, errorCode, methodInfo));

    // 当前方法的结束
    outInsert.append(methodBuilderFinish());

    // 将错误结果加入到集合中
    outInsert.append(addListErrorData());

    // 循环调用单个数据项的检查
    outInsert.append(foreachCheckItem(dtoPackageInfo));

    // 数据的结果集判空
    outInsert.append(foreachCheckRsp());

    // 加入返回语句
    outInsert.append(batchReturnCode());

    // 方法结束
    JavaClassCodeUtils.methodEnd(outInsert);

    return outInsert.toString();
  }

  /**
   * 进行参数的结果检查
   *
   * @return
   */
  private String foreachCheckRsp() {
    StringBuilder out = new StringBuilder();

    // 循环开始
    out.append(JavaFormat.appendTab(2));
    out.append(JavaKeyWord.FOR_KEY).append(Symbol.BRACKET_LEFT);
    out.append(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getClassName())
        .append(Symbol.SPACE);
    out.append(JavaVarName.FOREACH_TEMP_NAME).append(Symbol.SPACE);
    out.append(Symbol.COLON).append(Symbol.SPACE);
    out.append(JavaVarName.LIST_NAME);
    out.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACE_LEFT);
    out.append(Symbol.ENTER_LINE);

    // 判断数据检查
    out.append(JavaFormat.appendTab(3));
    out.append(JavaKeyWord.IF).append(Symbol.BRACKET_LEFT);
    out.append(ERROR_CODE_SUCCESS).append(Symbol.SPACE);
    out.append(Symbol.EQUAL_NOT).append(Symbol.SPACE);
    out.append(JavaVarName.FOREACH_TEMP_NAME);
    out.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACE_LEFT);
    out.append(Symbol.ENTER_LINE);

    // 返回语句
    out.append(JavaFormat.appendTab(4));
    out.append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    out.append(JavaVarName.FOREACH_TEMP_NAME).append(Symbol.SEMICOLON);
    out.append(Symbol.ENTER_LINE);

    // 判断检查结果
    out.append(JavaFormat.appendTab(3));
    out.append(Symbol.BRACE_RIGHT);
    out.append(Symbol.ENTER_LINE);

    // 循环结束
    out.append(JavaFormat.appendTab(2));
    out.append(Symbol.BRACE_RIGHT);
    out.append(Symbol.ENTER_LINE);

    return out.toString();
  }

  /**
   * 进行参数的每个检查
   *
   * @param dtoPackageInfo
   * @return
   */
  private String foreachCheckItem(ImportPackageInfo dtoPackageInfo) {
    StringBuilder out = new StringBuilder();

    // 循环开始
    out.append(JavaFormat.appendTab(2));
    out.append(JavaKeyWord.FOR_KEY).append(Symbol.BRACKET_LEFT);
    out.append(dtoPackageInfo.getClassName()).append(Symbol.SPACE);
    out.append(JavaVarName.FOREACH_TEMP_NAME).append(Symbol.SPACE);
    out.append(Symbol.COLON).append(Symbol.SPACE);
    out.append(dtoPackageInfo.getVarName());
    out.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACE_LEFT);
    out.append(Symbol.ENTER_LINE);

    // 数据检查
    out.append(JavaFormat.appendTab(3));
    out.append(JavaVarName.LIST_NAME).append(Symbol.POINT);
    out.append(JavaMethodName.LIST_ADD);

    // 单个添加的方法名
    String addName =
        NAME_PREFIX + NameProcess.INSTANCE.toJavaClassName(MethodTypeEnum.INSERT.name());

    out.append(Symbol.BRACKET_LEFT).append(addName);
    out.append(Symbol.BRACKET_LEFT).append(JavaVarName.FOREACH_TEMP_NAME);
    out.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    out.append(Symbol.ENTER_LINE);

    // 循环结束
    out.append(JavaFormat.appendTab(2));
    out.append(Symbol.BRACE_RIGHT);
    out.append(Symbol.ENTER_LINE);

    return out.toString();
  }

  /**
   * 数据的单项添加
   *
   * @param methodList
   * @param tableColumnList
   * @param dtoPackageInfo
   * @param errorCodeEnum
   * @param constantPkg
   * @return
   */
  private String builderOneInsert(
      List<MethodInfo> methodList,
      List<TableColumnDTO> tableColumnList,
      ImportPackageInfo dtoPackageInfo,
      ImportPackageInfo errorCodeEnum,
      ImportPackageInfo constantPkg) {
    // 数据单项添加的检查
    boolean checkInsert = this.checkOneInsertMethod(methodList);

    // 如果当前不存在
    if (!checkInsert) {
      // 执行生成单个数据的添加的代码
      return this.addInsertMethod(tableColumnList, dtoPackageInfo, errorCodeEnum, constantPkg);
    }

    return Symbol.EMPTY;
  }

  /**
   * 添加单 个数据的添加方法
   *
   * @return
   */
  private String addInsertMethod(
      List<TableColumnDTO> tableColumnList,
      ImportPackageInfo dtoPackageInfo,
      ImportPackageInfo errorCodeEnum,
      ImportPackageInfo constantPkg) {
    return this.checkInsert(
        builderMethod(), tableColumnList, errorCodeEnum, dtoPackageInfo, constantPkg);
  }

  /**
   * 构建方法
   *
   * @return
   */
  private MethodInfo builderMethod() {
    MethodInfo methodInfo = new MethodInfo();
    methodInfo.setName(MethodTypeEnum.INSERT.name());
    methodInfo.setParams(MethodTypeEnum.INSERT.name());
    methodInfo.setComment(ADD_METHOD_NAME);

    return methodInfo;
  }

  /**
   * 检查是否存在单个的添加方法
   *
   * @param methodList 方法合集
   * @return true 存在 false 不存在
   */
  private boolean checkOneInsertMethod(List<MethodInfo> methodList) {

    for (MethodInfo methodInfo : methodList) {
      // 检查是否存在单个添加方法
      if (MethodTypeEnum.INSERT.getType().equals(methodInfo.getOperator())
          && !MethodUtils.checkBatch(methodInfo.getParamType())) {
        return true;
      }
    }

    return false;
  }

  /**
   * 分页查询的验证需要特别的
   *
   * @param methodInfo 方法
   * @param tableColumnList 表列信息
   * @param errorCode 错误码
   * @param dtoPackageInfo 传输实体包
   * @param constantPkg 常量类的包
   * @return 生成的代码
   */
  private String pageCondition(
      MethodInfo methodInfo,
      List<TableColumnDTO> tableColumnList,
      ImportPackageInfo errorCode,
      ImportPackageInfo dtoPackageInfo,
      ImportPackageInfo constantPkg) {
    StringBuilder outInsert = new StringBuilder();

    // 添加方法的定义
    outInsert.append(pageDefineMethod(methodInfo, dtoPackageInfo));
    // 方法开始
    JavaClassCodeUtils.methodStart(outInsert);
    // 构建器对象的声明
    outInsert.append(methodBuilderDefine());

    // 参数的判断
    outInsert.append(
        checkMethodParamPage(
            methodInfo, tableColumnList, errorCode, dtoPackageInfo, constantPkg, outInsert));

    // 当前方法的结束
    outInsert.append(methodBuilderFinish());

    // 加入返回语句
    outInsert.append(returnCode());

    // 方法结束
    JavaClassCodeUtils.methodEnd(outInsert);

    return outInsert.toString();
  }

  /**
   * 以查询条件为准备进行的验证
   *
   * <p>1,删除优先根据条件，如果没有条件，则使用主键的判断
   *
   * @param methodInfo 方法
   * @param tableColumnList 表列信息
   * @param errorCode 错误码
   * @param dtoPackageInfo 传输实体包
   * @param constantPkg 常量类的包
   * @return 生成的代码
   */
  private String whereCondition(
      MethodInfo methodInfo,
      List<TableColumnDTO> tableColumnList,
      ImportPackageInfo errorCode,
      ImportPackageInfo dtoPackageInfo,
      ImportPackageInfo constantPkg) {
    StringBuilder outInsert = new StringBuilder();

    // 添加方法的定义
    outInsert.append(defineMethod(methodInfo, dtoPackageInfo));
    // 方法开始
    JavaClassCodeUtils.methodStart(outInsert);
    // 构建器对象的声明
    outInsert.append(methodBuilderDefine());

    // 参数的判断
    outInsert.append(
        checkMethodParam(
            methodInfo, tableColumnList, errorCode, dtoPackageInfo, constantPkg, outInsert));

    // 当前方法的结束
    outInsert.append(methodBuilderFinish());

    // 加入返回语句
    outInsert.append(returnCode());

    // 方法结束
    JavaClassCodeUtils.methodEnd(outInsert);

    return outInsert.toString();
  }

  /**
   * 方法参数的相关生成
   *
   * @param methodInfo 方法的信息
   * @param tableColumnList 表的表信息
   * @param errorCode 错误码
   * @param dtoPackageInfo dto信息
   * @param constantPkg 静态常量类
   * @return
   */
  private String checkMethodParam(
      MethodInfo methodInfo,
      List<TableColumnDTO> tableColumnList,
      ImportPackageInfo errorCode,
      ImportPackageInfo dtoPackageInfo,
      ImportPackageInfo constantPkg,
      StringBuilder outBuilder) {

    StringBuilder outInsert = new StringBuilder();

    // 检查当前参数是否存在,如果不存在，则使用默认主键
    if (methodInfo.getWhereInfo() != null && !methodInfo.getWhereInfo().isEmpty()) {
      // 获取参数列
      List<TableColumnDTO> dataColumnList =
          getWhereParam(tableColumnList, methodInfo.getWhereInfo());
      // 进行空与最大值参数的检查
      outInsert.append(
          checkNullAndMaxParam(dataColumnList, errorCode, dtoPackageInfo, constantPkg, outBuilder));
    }
    // 存在，则按条件进行设置
    else {
      // 1, 提取出主键
      List<TableColumnDTO> primaryColumnList = TableColumnUtils.getPrimaryKey(tableColumnList);
      // 进行空与最大值参数的检查
      outInsert.append(
          checkNullAndMaxParam(
              primaryColumnList, errorCode, dtoPackageInfo, constantPkg, outBuilder));
    }

    return outInsert.toString();
  }

  /**
   * 分页查询方法参数的相关生成
   *
   * @param methodInfo 方法的信息
   * @param tableColumnList 表的表信息
   * @param errorCode 错误码
   * @param dtoPackageInfo dto信息
   * @param constantPkg 静态常量类
   * @return
   */
  private String checkMethodParamPage(
      MethodInfo methodInfo,
      List<TableColumnDTO> tableColumnList,
      ImportPackageInfo errorCode,
      ImportPackageInfo dtoPackageInfo,
      ImportPackageInfo constantPkg,
      StringBuilder outBuilder) {

    StringBuilder outInsert = new StringBuilder();

    // 检查当前参数是否存在,如果不存在，则使用默认主键
    if (methodInfo.getWhereInfo() != null && !methodInfo.getWhereInfo().isEmpty()) {
      // 获取参数列
      List<TableColumnDTO> dataColumnList =
          getWhereParam(tableColumnList, methodInfo.getWhereInfo());
      // 进行空与最大值参数的检查
      outInsert.append(
          checkNullAndMaxParamPage(
              dataColumnList, errorCode, dtoPackageInfo, constantPkg, outBuilder));
    }
    // 存在，则按条件进行设置
    else {
      // 1, 提取出主键
      List<TableColumnDTO> primaryColumnList = TableColumnUtils.getPrimaryKey(tableColumnList);
      // 进行空与最大值参数的检查
      outInsert.append(
          checkNullAndMaxParamPage(
              primaryColumnList, errorCode, dtoPackageInfo, constantPkg, outBuilder));
    }

    return outInsert.toString();
  }

  /**
   * 进行where条件的检查
   *
   * @param tableColumnList
   * @param whereColumns
   * @return
   */
  private List<TableColumnDTO> getWhereParam(
      List<TableColumnDTO> tableColumnList, List<WhereInfo> whereColumns) {
    // 1, 提取出主键
    Map<String, TableColumnDTO> primaryColumnList = TableColumnUtils.parseToMap(tableColumnList);
    List<TableColumnDTO> tableColumn = new ArrayList<>();

    for (WhereInfo whereInfo : whereColumns) {

      // 如果当前为in字段，则设需要进行列名的设置
      if (MyBatisOperatorFlag.IN.equals(whereInfo.getOperatorFlag())) {
        TableColumnDTO tableColumnInfo = primaryColumnList.get(whereInfo.getSqlColumn());
        if (null != tableColumnInfo) {
          TableColumnDTO columnNewData = tableColumnInfo.newInstanceForCurrValue();
          columnNewData.setColumnName(
              columnNewData.getColumnName() + Symbol.UNDER_LINE + JavaVarName.NAME_LIST_SUFFIX);
          tableColumn.add(columnNewData);
        }
      }
      // 非in字段进行列名的设置操作
      else {
        TableColumnDTO tableColumnInfo = primaryColumnList.get(whereInfo.getSqlColumn());
        if (null != tableColumnInfo) {
          tableColumn.add(tableColumnInfo);
        }
      }
    }

    return tableColumn;
  }

  /**
   * 进行空与最大值的检查
   *
   * @param tableColumnList
   * @param errorCode
   * @param dtoPackageInfo
   * @param constantPkg
   * @return
   */
  private String checkNullAndMaxParam(
      List<TableColumnDTO> tableColumnList,
      ImportPackageInfo errorCode,
      ImportPackageInfo dtoPackageInfo,
      ImportPackageInfo constantPkg,
      StringBuilder outBuilder) {
    StringBuilder outInsert = new StringBuilder();

    // 加入空的参数检查判断
    for (TableColumnDTO columnInfo : tableColumnList) {

      // 当已经被生成过，则不再生成
      String nullCode = GenerateJavaErrorCode.enumNullCode(columnInfo.getColumnName());
      if (outBuilder.indexOf(nullCode) != -1) {
        continue;
      }

      outInsert.append(this.checkNullParam(columnInfo, dtoPackageInfo, errorCode));
    }

    // 最大长度的判断
    for (TableColumnDTO columnInfo : tableColumnList) {

      // 当已经被生成过，则不再生成
      String maxCode = GenerateJavaErrorCode.enumMaxCode(columnInfo.getColumnName());
      if (outBuilder.indexOf(maxCode) != -1) {
        continue;
      }

      outInsert.append(
          this.checkMaxLengthParam(columnInfo, dtoPackageInfo, errorCode, constantPkg));
    }

    return outInsert.toString();
  }

  /**
   * 进行空与最大值的检查
   *
   * @param tableColumnList
   * @param errorCode
   * @param dtoPackageInfo
   * @param constantPkg
   * @return
   */
  private String checkNullAndMaxParamPage(
      List<TableColumnDTO> tableColumnList,
      ImportPackageInfo errorCode,
      ImportPackageInfo dtoPackageInfo,
      ImportPackageInfo constantPkg,
      StringBuilder outBuilder) {
    StringBuilder outInsert = new StringBuilder();

    // 加入空的参数检查判断
    for (TableColumnDTO columnInfo : tableColumnList) {

      // 当已经被生成过，则不再生成
      String nullCode = GenerateJavaErrorCode.enumNullCode(columnInfo.getColumnName());
      if (outBuilder.indexOf(nullCode) != -1) {
        continue;
      }

      outInsert.append(this.checkNullParamPage(columnInfo, dtoPackageInfo, errorCode));
    }

    // 最大长度的判断
    for (TableColumnDTO columnInfo : tableColumnList) {

      // 当已经被生成过，则不再生成
      String maxCode = GenerateJavaErrorCode.enumMaxCode(columnInfo.getColumnName());
      if (outBuilder.indexOf(maxCode) != -1) {
        continue;
      }

      outInsert.append(
          this.checkMaxLengthParamPage(columnInfo, dtoPackageInfo, errorCode, constantPkg));
    }

    return outInsert.toString();
  }

  /**
   * 添加方法的参数验证
   *
   * @param methodInfo 方法
   * @param tableColumnList 表列信息
   * @param errorCode 错误码
   * @param dtoPackageInfo 传输实体包
   * @param constantPkg 常量类的包
   * @return 生成的代码
   */
  private String checkInsert(
      MethodInfo methodInfo,
      List<TableColumnDTO> tableColumnList,
      ImportPackageInfo errorCode,
      ImportPackageInfo dtoPackageInfo,
      ImportPackageInfo constantPkg) {
    StringBuilder outInsert = new StringBuilder();

    // 添加方法的定义
    outInsert.append(defineMethod(methodInfo, dtoPackageInfo));
    // 方法开始
    JavaClassCodeUtils.methodStart(outInsert);
    // 构建器对象的声明
    outInsert.append(methodBuilderDefine());

    // 进行空与最大值的判断
    outInsert.append(
        checkNullAndMaxParam(tableColumnList, errorCode, dtoPackageInfo, constantPkg, outInsert));

    // 当前方法的结束
    outInsert.append(methodBuilderFinish());

    // 加入返回语句
    outInsert.append(returnCode());

    // 方法结束
    JavaClassCodeUtils.methodEnd(outInsert);

    return outInsert.toString();
  }

  /**
   * 添加方法的参数验证
   *
   * @param methodInfo 方法
   * @param tableColumnList 表列信息
   * @param errorCode 错误码
   * @param dtoPackageInfo 传输实体包
   * @param constantPkg 常量类的包
   * @return 生成的代码
   */
  private String checkUpdate(
      MethodInfo methodInfo,
      List<TableColumnDTO> tableColumnList,
      ImportPackageInfo errorCode,
      ImportPackageInfo dtoPackageInfo,
      ImportPackageInfo constantPkg) {
    StringBuilder outInsert = new StringBuilder();

    // 添加方法的定义
    outInsert.append(defineMethod(methodInfo, dtoPackageInfo));
    // 方法开始
    JavaClassCodeUtils.methodStart(outInsert);
    // 构建器对象的声明
    outInsert.append(methodBuilderDefine());

    // 进行空与最大值的判断
    outInsert.append(
        checkNullAndMaxParam(tableColumnList, errorCode, dtoPackageInfo, constantPkg, outInsert));

    // 进行查询参数的检查
    outInsert.append(
        checkMethodParam(
            methodInfo, tableColumnList, errorCode, dtoPackageInfo, constantPkg, outInsert));

    // 当前方法的结束
    outInsert.append(methodBuilderFinish());

    // 加入返回语句
    outInsert.append(returnCode());

    // 方法结束
    JavaClassCodeUtils.methodEnd(outInsert);

    return outInsert.toString();
  }

  /**
   * 批量操作的返回错误码
   *
   * @return 实体对象
   */
  private String batchReturnCode() {
    StringBuilder outInsert = new StringBuilder();
    // 加入return语句
    outInsert.append(JavaFormat.appendTab(2));
    outInsert.append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    outInsert.append(ERROR_CODE_SUCCESS).append(Symbol.SEMICOLON);
    outInsert.append(Symbol.ENTER_LINE);

    return outInsert.toString();
  }

  /**
   * 返回错误对象
   *
   * @return 实体对象
   */
  private String returnCode() {
    StringBuilder outInsert = new StringBuilder();
    // 加入return语句
    outInsert.append(JavaFormat.appendTab(2));
    outInsert.append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    outInsert.append(VAR_CHECK_NAME).append(Symbol.SEMICOLON);
    outInsert.append(Symbol.ENTER_LINE);

    return outInsert.toString();
  }

  /**
   * 集合参数空的检查
   *
   * @param dtoPackageInfo dto的实体信息
   * @param errorCode 错误码
   * @return 构建的关空代码
   */
  private String checkListNullParam(
      ImportPackageInfo dtoPackageInfo, ImportPackageInfo errorCode, MethodInfo methodInfo) {
    StringBuilder outInsert = new StringBuilder();

    String codeDataName =
        NameProcess.INSTANCE.toJavaNameFirstUpper(
            ImportCodePackageKey.ERROR_DATA.getPackageInfo().getVarName());
    String getErrorDataName =
        JavaMethodName.GET + codeDataName + Symbol.BRACKET_LEFT + Symbol.BRACKET_RIGHT;

    // 先添加注释，再添加代码
    outInsert.append(JavaFormat.appendTab(4));
    outInsert.append(Symbol.PATH).append(Symbol.PATH);
    outInsert.append(METHOD_CHECK_NULL_COMMENT);
    outInsert.append(Symbol.ENTER_LINE);

    // 生成代码
    outInsert.append(JavaFormat.appendTab(4));
    outInsert.append(Symbol.POINT);
    outInsert.append(METHOD_CHECK_NULL).append(Symbol.BRACKET_LEFT);
    outInsert.append(dtoPackageInfo.getVarName()).append(Symbol.COMMA);
    // 错误码
    outInsert.append(errorCode.getClassName());
    outInsert.append(Symbol.POINT);
    outInsert.append(GenerateJavaErrorCode.enumNullCode(methodInfo.getName()));
    outInsert.append(Symbol.POINT).append(getErrorDataName);

    outInsert.append(Symbol.BRACKET_RIGHT);
    outInsert.append(Symbol.ENTER_LINE);

    return outInsert.toString();
  }

  /**
   * 数据参数空的检查
   *
   * @param columnInfo 空的参数信息
   * @param dtoPackageInfo dto的实体信息
   * @param errorCode 错误码
   * @return 构建的关空代码
   */
  private String checkNullParam(
      TableColumnDTO columnInfo, ImportPackageInfo dtoPackageInfo, ImportPackageInfo errorCode) {

    // 进行空的检查
    return this.checkNullParam(columnInfo, dtoPackageInfo, errorCode, Symbol.EMPTY);
  }

  /**
   * 分页查询的空验证
   *
   * @param columnInfo 空的参数信息
   * @param dtoPackageInfo dto的实体信息
   * @param errorCode 错误码
   * @return 构建的关空代码
   */
  private String checkNullParamPage(
      TableColumnDTO columnInfo, ImportPackageInfo dtoPackageInfo, ImportPackageInfo errorCode) {

    // 分页查询中的空验证
    return this.checkNullParam(columnInfo, dtoPackageInfo, errorCode, PAGE_GET_DATA);
  }

  private String checkNullParam(
      TableColumnDTO columnInfo,
      ImportPackageInfo dtoPackageInfo,
      ImportPackageInfo errorCode,
      String dataGet) {
    StringBuilder outInsert = new StringBuilder();

    // 1,检查当前的列是否允许为空，不允许为空，则进行类型的检查
    if (!columnInfo.getNullFlag()) {

      String codeDataName =
          NameProcess.INSTANCE.toJavaNameFirstUpper(
              ImportCodePackageKey.ERROR_DATA.getPackageInfo().getVarName());
      String getErrorDataName =
          JavaMethodName.GET + codeDataName + Symbol.BRACKET_LEFT + Symbol.BRACKET_RIGHT;

      String nameSuffix = NameProcess.INSTANCE.toJavaClassName(columnInfo.getColumnName());
      String getName = JavaMethodName.GET + nameSuffix;

      // 先添加注释，再添加代码
      outInsert.append(JavaFormat.appendTab(4));
      outInsert.append(Symbol.PATH).append(Symbol.PATH);
      outInsert.append(METHOD_CHECK_NULL_COMMENT);
      outInsert.append(columnInfo.getColumnMsg());
      outInsert.append(Symbol.ENTER_LINE);

      // 生成代码
      outInsert.append(JavaFormat.appendTab(4));
      outInsert.append(Symbol.POINT);
      outInsert.append(METHOD_CHECK_NULL).append(Symbol.BRACKET_LEFT);
      outInsert.append(dtoPackageInfo.getVarName());

      // 用户从对象中再次获取数据
      if (StringUtils.isNotEmpty(dataGet)) {
        outInsert.append(Symbol.POINT).append(dataGet);
      }
      outInsert.append(Symbol.POINT);
      outInsert.append(getName).append(Symbol.BRACKET_LEFT);
      outInsert.append(Symbol.BRACKET_RIGHT).append(Symbol.COMMA);
      // 错误码
      outInsert.append(errorCode.getClassName());
      outInsert.append(Symbol.POINT);
      outInsert.append(GenerateJavaErrorCode.enumNullCode(columnInfo.getColumnName()));

      outInsert.append(Symbol.POINT);
      outInsert.append(getErrorDataName);

      outInsert.append(Symbol.BRACKET_RIGHT);
      outInsert.append(Symbol.ENTER_LINE);
    }

    return outInsert.toString();
  }

  /**
   * 参数的最大长度检查
   *
   * @param columnInfo 空的参数信息
   * @param dtoPackageInfo dto的实体信息
   * @param errorCode 错误码
   * @return 构建的关空代码
   */
  private String checkMaxLengthParam(
      TableColumnDTO columnInfo,
      ImportPackageInfo dtoPackageInfo,
      ImportPackageInfo errorCode,
      ImportPackageInfo constantPkg) {

    return checkMaxLengthParamInvoke(
        columnInfo, dtoPackageInfo, errorCode, constantPkg, Symbol.EMPTY);
  }

  /**
   * 分页方法参数的最大长度检查
   *
   * @param columnInfo 空的参数信息
   * @param dtoPackageInfo dto的实体信息
   * @param errorCode 错误码
   * @return 构建的关空代码
   */
  private String checkMaxLengthParamPage(
      TableColumnDTO columnInfo,
      ImportPackageInfo dtoPackageInfo,
      ImportPackageInfo errorCode,
      ImportPackageInfo constantPkg) {

    return checkMaxLengthParamInvoke(
        columnInfo, dtoPackageInfo, errorCode, constantPkg, PAGE_GET_DATA);
  }

  /**
   * 进行检查最大的长度参数
   *
   * @param columnInfo
   * @param dtoPackageInfo
   * @param errorCode
   * @param constantPkg
   * @return
   */
  private String checkMaxLengthParamInvoke(
      TableColumnDTO columnInfo,
      ImportPackageInfo dtoPackageInfo,
      ImportPackageInfo errorCode,
      ImportPackageInfo constantPkg,
      String paramCheck) {
    StringBuilder outInsert = new StringBuilder();

    String codeDataName =
        NameProcess.INSTANCE.toJavaNameFirstUpper(
            ImportCodePackageKey.ERROR_DATA.getPackageInfo().getVarName());
    String getErrorDataName =
        JavaMethodName.GET + codeDataName + Symbol.BRACKET_LEFT + Symbol.BRACKET_RIGHT;

    String nameSuffix = NameProcess.INSTANCE.toJavaClassName(columnInfo.getColumnName());
    String getName = JavaMethodName.GET + nameSuffix;

    // 先添加注释，再添加代码
    outInsert.append(JavaFormat.appendTab(4));
    outInsert.append(Symbol.PATH).append(Symbol.PATH);
    outInsert.append(METHOD_CHECK_MAXLENGTH_COMMENT);
    outInsert.append(columnInfo.getColumnMsg());
    outInsert.append(Symbol.ENTER_LINE);

    // 生成代码
    outInsert.append(JavaFormat.appendTab(4));
    outInsert.append(Symbol.POINT);
    outInsert.append(METHOD_CHECK_MAXLENGTH).append(Symbol.BRACKET_LEFT);
    outInsert.append(dtoPackageInfo.getVarName());

    // 获取对象中的参数
    if (StringUtils.isNotEmpty(paramCheck)) {
      outInsert.append(Symbol.POINT).append(paramCheck);
    }

    outInsert.append(Symbol.POINT);
    outInsert.append(getName).append(Symbol.BRACKET_LEFT);
    outInsert.append(Symbol.BRACKET_RIGHT).append(Symbol.COMMA);

    // 常量类中的参数
    outInsert.append(constantPkg.getClassName()).append(Symbol.POINT);
    outInsert.append(GenerateJavaInterfaceConstant.name(columnInfo.getColumnName()));
    outInsert.append(Symbol.COMMA);

    // 错误码
    outInsert.append(errorCode.getClassName());
    outInsert.append(Symbol.POINT);
    outInsert.append(GenerateJavaErrorCode.enumMaxCode(columnInfo.getColumnName()));

    outInsert.append(Symbol.POINT);
    outInsert.append(getErrorDataName);

    outInsert.append(Symbol.BRACKET_RIGHT);
    outInsert.append(Symbol.ENTER_LINE);

    return outInsert.toString();
  }

  /**
   * 方法返回对象的声明
   *
   * @return
   */
  private String methodResultListDefine() {
    StringBuilder outInsert = new StringBuilder();
    // 声明错误对象头
    outInsert.append(JavaFormat.appendTab(2));
    outInsert.append(JavaKeyWord.LIST_TYPE);
    outInsert.append(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getClassName());
    outInsert.append(JavaKeyWord.LIST_TYPE_END);
    outInsert.append(Symbol.SPACE).append(JavaVarName.LIST_NAME);
    outInsert.append(Symbol.SPACE).append(Symbol.EQUAL).append(Symbol.SPACE);
    outInsert.append(JavaKeyWord.NEW).append(Symbol.SPACE);
    outInsert.append(JavaKeyWord.LIST_TYPE_ARRAYLIST).append(Symbol.BRACKET_LEFT);
    outInsert.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    outInsert.append(Symbol.ENTER_LINE);

    return outInsert.toString();
  }

  /**
   * 构建器对象的声明
   *
   * @return
   */
  private String methodBuilderDefine() {
    StringBuilder outInsert = new StringBuilder();
    // 声明错误对象头
    outInsert.append(JavaFormat.appendTab(2));
    outInsert.append(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getClassName());
    outInsert.append(Symbol.SPACE).append(VAR_CHECK_NAME);
    outInsert.append(Symbol.SPACE).append(Symbol.EQUAL).append(Symbol.SPACE);
    outInsert.append(ImportCodePackageKey.PARAM_CHECK.getPackageInfo().getClassName());
    outInsert.append(Symbol.POINT).append(JavaMethodName.PAGE_BUILDER);
    outInsert.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    outInsert.append(Symbol.ENTER_LINE);

    return outInsert.toString();
  }

  /**
   * 将错误码加入集合中
   *
   * @return
   */
  private String addListErrorData() {
    StringBuilder outInsert = new StringBuilder();
    // 声明错误对象头
    outInsert.append(JavaFormat.appendTab(2));
    outInsert.append(JavaVarName.LIST_NAME);
    outInsert.append(Symbol.POINT);
    outInsert.append(JavaMethodName.LIST_ADD);
    outInsert.append(Symbol.BRACKET_LEFT);
    outInsert.append(VAR_CHECK_NAME);
    outInsert.append(Symbol.BRACKET_RIGHT);
    outInsert.append(Symbol.SEMICOLON);
    outInsert.append(Symbol.ENTER_LINE);

    return outInsert.toString();
  }

  /**
   * 构建器对象的结束
   *
   * @return
   */
  private String methodBuilderFinish() {
    StringBuilder outInsert = new StringBuilder();
    // 声明错误对象头
    outInsert.append(JavaFormat.appendTab(3));
    outInsert.append(Symbol.POINT).append(CHECK_ERROR_METHOD_NAME);
    outInsert.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    outInsert.append(Symbol.SEMICOLON);
    outInsert.append(Symbol.ENTER_LINE);

    return outInsert.toString();
  }

  /**
   * 定义方法
   *
   * @param methodInfo 方法
   * @param dtoPackageInfo 数据传输包
   * @return
   */
  private String defineMethod(MethodInfo methodInfo, ImportPackageInfo dtoPackageInfo) {
    StringBuilder outInsert = new StringBuilder();

    List<JavaMethodArguments> arguments = new ArrayList<>();

    // 检查当前的参数类型
    String paramType;
    if (MethodUtils.checkBatch(methodInfo.getParamType())) {
      paramType = JavaKeyWord.LIST_TYPE + dtoPackageInfo.getClassName() + JavaKeyWord.LIST_TYPE_END;
    } else {
      paramType = dtoPackageInfo.getClassName();
    }

    // 检查错误
    arguments.add(
        JavaMethodArguments.builder()
            .type(paramType)
            .name(dtoPackageInfo.getVarName())
            .comment(dtoPackageInfo.getClassComment())
            .build());

    // 方法的声明
    JavaMethodEntity methodDefine =
        JavaMethodEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 静态标识
            .staticFlag(JavaKeyWord.STATIC)
            // 注释
            .comment(methodInfo.getComment() + CHECK_PARAM)
            // 返回注释
            .returnComment(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getClassComment())
            // 返回类型
            .type(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getClassName())
            // 方法名
            .name(getMethodName(methodInfo.getName()))
            // 参数
            .arguments(arguments)
            .build();

    JavaClassCodeUtils.methodDefine(outInsert, methodDefine);

    return outInsert.toString();
  }

  /**
   * 定义方法
   *
   * @param methodInfo 方法
   * @param dtoPackageInfo 数据传输包
   * @return
   */
  private String pageDefineMethod(MethodInfo methodInfo, ImportPackageInfo dtoPackageInfo) {
    StringBuilder outInsert = new StringBuilder();

    List<JavaMethodArguments> arguments = new ArrayList<>();

    String paramType =
        ImportCodeActionDefaultPackageKey.API_PAGE_REQUEST.getPackageInfo().getClassName()
            + Symbol.ANGLE_BRACKETS_LEFT
            + dtoPackageInfo.getClassName()
            + Symbol.ANGLE_BRACKETS_RIGHT;

    // 检查错误
    arguments.add(
        JavaMethodArguments.builder()
            .type(paramType)
            .name(dtoPackageInfo.getVarName())
            .comment(dtoPackageInfo.getClassComment())
            .build());

    // 方法的声明
    JavaMethodEntity methodDefine =
        JavaMethodEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 静态标识
            .staticFlag(JavaKeyWord.STATIC)
            // 注释
            .comment(methodInfo.getComment() + CHECK_PARAM)
            // 返回注释
            .returnComment(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getClassComment())
            // 返回类型
            .type(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getClassName())
            // 方法名
            .name(getMethodName(methodInfo.getName()))
            // 参数
            .arguments(arguments)
            .build();

    JavaClassCodeUtils.methodDefine(outInsert, methodDefine);

    return outInsert.toString();
  }

  /**
   * 获取方法名
   *
   * @param methodName
   * @return
   */
  public static final String getMethodName(String methodName) {
    return NAME_PREFIX + NameProcess.INSTANCE.toJavaNameFirstUpper(methodName);
  }

  /**
   * 获取导入的表信息
   *
   * @return
   */
  public List<String> getImportList(
      ImportPackageInfo packageInfo,
      ImportPackageInfo errorCodeEnum,
      ImportPackageInfo constantPkg) {
    List<String> importList = new ArrayList<>();

    importList.addAll(IMPORT_LIST);
    importList.add(packageInfo.packageOut());
    importList.add(errorCodeEnum.packageOut());
    importList.add(constantPkg.packageOut());

    return importList;
  }
}
