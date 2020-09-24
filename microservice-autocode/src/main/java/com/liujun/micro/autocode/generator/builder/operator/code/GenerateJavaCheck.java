package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.config.generate.entity.MethodInfo;
import com.liujun.micro.autocode.constant.MethodTypeEnum;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.ImportCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.JavaMethodName;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodArguments;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodEntity;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.MethodUtils;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.javalanguage.serivce.JavaFormat;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 生成数据参数校验
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaCheck {

  /** 文件前缀 */
  private static final String NAME_PREFIX = "check";

  /** 错误检查的临时变量名 */
  private static final String VAR_CHECK_NAME = "errorData";

  /** 空检查的代码 */
  private static final String METHOD_CHECK_NULL = "checkNull";

  /** 方法的参数检查 */
  private static final String CHECK_ERROR_METHOD_NAME = "checkErrorToOne";

  public static final GenerateJavaCheck INSTANCE = new GenerateJavaCheck();

  private static final List<String> IMPORT_LIST =
      Arrays.asList(
          // 错误对象
          ImportCodePackageKey.ERROR_DATA.getPackageInfo().packageOut(),
          // 验证对象
          ImportCodePackageKey.PARAM_CHECK.getPackageInfo().packageOut());

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
      String author) {

    // 类的定义
    StringBuilder sb =
        JavaClassCodeUtils.classDefine(
            checkPackage, getImportList(dtoPackageInfo, errorCodeEnum), null, author);

    // 1, 提取出主键
    // List<TableColumnDTO> primaryColumnList = TableColumnUtils.getPrimaryKey(tableColumnList);

    // 将主键转换为map
    // Map<String, TableColumnDTO> primaryKeyMap = TableColumnUtils.parseToMap(primaryColumnList);

    for (MethodInfo methodInfo : methodList) {
      // 如果当前数据为添加
      if (MethodTypeEnum.INSERT.getType().equals(methodInfo.getOperator())
          && !MethodUtils.checkBatch(methodInfo.getParamType())) {
        sb.append(this.checkInsert(methodInfo, tableColumnList, errorCodeEnum, dtoPackageInfo));
      }
      // 如果当前的数据为批量添加
      else if (MethodTypeEnum.INSERT.getType().equals(methodInfo.getOperator())
          && MethodUtils.checkBatch(methodInfo.getParamType())) {
        sb.append(
            this.checkInsertBatch(methodInfo, tableColumnList, errorCodeEnum, dtoPackageInfo));
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
      MethodInfo methodInfo,
      List<TableColumnDTO> tableColumnList,
      ImportPackageInfo errorCode,
      ImportPackageInfo dtoPackageInfo) {
    StringBuilder outInsert = new StringBuilder();

    // 添加方法的定义
    outInsert.append(defineMethod(methodInfo, dtoPackageInfo));
    // 方法开始
    JavaClassCodeUtils.methodStart(outInsert);
    // 构建器对象的声明
    outInsert.append(methodBuilderDefine());

    for (TableColumnDTO columnInfo : tableColumnList) {
      // 加入空的参数检查判断
      outInsert.append(this.checkNullParam(columnInfo, dtoPackageInfo, errorCode));
      // 2，超过长度的检查
      String outPropertyKeyMax =
          GenerateJavaErrorCode.propertiesErrorKeyMax(
              columnInfo.getTableName(), columnInfo.getColumnName());
    }

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
   * @param methodInfo
   * @param tableColumnList
   * @param errorCode
   * @param dtoPackageInfo
   * @return
   */
  private String checkInsert(
      MethodInfo methodInfo,
      List<TableColumnDTO> tableColumnList,
      ImportPackageInfo errorCode,
      ImportPackageInfo dtoPackageInfo) {
    StringBuilder outInsert = new StringBuilder();

    // 添加方法的定义
    outInsert.append(defineMethod(methodInfo, dtoPackageInfo));
    // 方法开始
    JavaClassCodeUtils.methodStart(outInsert);
    // 构建器对象的声明
    outInsert.append(methodBuilderDefine());

    for (TableColumnDTO columnInfo : tableColumnList) {

      // 加入空的参数检查判断
      outInsert.append(this.checkNullParam(columnInfo, dtoPackageInfo, errorCode));
      // 2，超过长度的检查
      String outPropertyKeyMax =
          GenerateJavaErrorCode.propertiesErrorKeyMax(
              columnInfo.getTableName(), columnInfo.getColumnName());
    }

    // 当前方法的结束
    outInsert.append(methodBuilderFinish());

    // 加入返回语句
    outInsert.append(returnCode());

    // 方法结束
    JavaClassCodeUtils.methodEnd(outInsert);

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
   * 数据参数空的检查
   *
   * @param columnInfo 空的参数信息
   * @param dtoPackageInfo dto的实体信息
   * @param errorCode 错误码
   * @return 构建的关空代码
   */
  private String checkNullParam(
      TableColumnDTO columnInfo, ImportPackageInfo dtoPackageInfo, ImportPackageInfo errorCode) {
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
      outInsert.append(columnInfo.getColumnMsg());
      outInsert.append(Symbol.ENTER_LINE);

      // 生成代码
      outInsert.append(JavaFormat.appendTab(4));
      outInsert.append(Symbol.POINT);
      outInsert.append(METHOD_CHECK_NULL).append(Symbol.BRACKET_LEFT);
      outInsert.append(dtoPackageInfo.getVarName()).append(Symbol.POINT);
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
   * @param methodInfo
   * @param dtoPackageInfo
   * @return
   */
  private String defineMethod(MethodInfo methodInfo, ImportPackageInfo dtoPackageInfo) {
    StringBuilder outInsert = new StringBuilder();

    List<JavaMethodArguments> arguments = new ArrayList<>();

    // 检查错误
    arguments.add(
        JavaMethodArguments.builder()
            .type(dtoPackageInfo.getClassName())
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
            .comment(methodInfo.getComment())
            // 返回注释
            .returnComment(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getClassComment())
            // 返回类型
            .type(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getClassName())
            // 方法名
            .name(NAME_PREFIX + NameProcess.INSTANCE.toJavaNameFirstUpper(methodInfo.getName()))
            // 参数
            .arguments(arguments)
            .build();

    JavaClassCodeUtils.methodDefine(outInsert, methodDefine);

    return outInsert.toString();
  }

  /**
   * 获取导入的表信息
   *
   * @return
   */
  public List<String> getImportList(
      ImportPackageInfo packageInfo, ImportPackageInfo errorCodeEnum) {
    List<String> importList = new ArrayList<>();

    importList.addAll(IMPORT_LIST);
    importList.add(packageInfo.packageOut());
    importList.add(errorCodeEnum.packageOut());

    return importList;
  }
}
