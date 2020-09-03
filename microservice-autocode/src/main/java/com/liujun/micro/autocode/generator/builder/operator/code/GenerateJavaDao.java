package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.constant.GenerateDefineFlag;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.entity.config.MethodInfo;
import com.liujun.micro.autocode.entity.config.TypeInfo;
import com.liujun.micro.autocode.generator.builder.constant.CodeComment;
import com.liujun.micro.autocode.generator.builder.constant.MethodTypeEnum;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.javalanguage.serivce.JavaFormat;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 生成数据库操作的DAO接口
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaDao {

  public static final GenerateJavaDao INSTANCE = new GenerateJavaDao();

  /**
   * 进行dao的接口生成
   *
   * @param tableInfo 表信息
   * @param className 类名
   * @param poPackageInfo 实体包的信息
   * @param daoPackageInfo dao的类定义
   * @param codeMethod 需要生成的方法
   * @param dataComment
   * @return 生成的javabean对象
   */
  public StringBuilder generateJavaDao(
      TableInfoDTO tableInfo,
      String className,
      ImportPackageInfo poPackageInfo,
      ImportPackageInfo daoPackageInfo,
      List<MethodInfo> codeMethod,
      String dataComment) {

    // 获得当前配制的方法
    List<MethodInfo> methodList = codeMethod;

    // 1,方法头的定义
    StringBuilder sb =
        defineClass(tableInfo, className, poPackageInfo, daoPackageInfo, methodList, dataComment);

    for (MethodInfo methodItem : methodList) {
      // 方法执行修改操作,即所有的数据的，添加、修改、删除
      if (MethodTypeEnum.UPDATE.getType().equals(methodItem.getOperator())
          || MethodTypeEnum.INSERT.getType().equals(methodItem.getOperator())
          || MethodTypeEnum.DELETE.getType().equals(methodItem.getOperator())) {

        // 修改方法,包括，增加、删、改
        this.updateMethod(sb, poPackageInfo.getClassName(), methodItem);
      }
      // 方法执行查询操作
      else if (MethodTypeEnum.QUERY.getType().equals(methodItem.getOperator())) {
        // 分页查询查询方法
        this.queryMethod(sb, poPackageInfo.getClassName(), methodItem);
      }
    }

    // 结束
    this.classFinish(sb);

    return sb;
  }

  /**
   * 定义类相关的信息，包括包，导入包及类的定义
   *
   * @param tableInfo 表信息
   * @param className 类名
   * @param poPackageDefine 数据库实体
   * @param daoPackageDefine dao的包定义
   * @param methodItemList 方法列表
   * @param dataComment 类的注释
   * @return 生成的代码对象
   */
  private StringBuilder defineClass(
      TableInfoDTO tableInfo,
      String className,
      ImportPackageInfo poPackageDefine,
      ImportPackageInfo daoPackageDefine,
      List<MethodInfo> methodItemList,
      String dataComment) {

    StringBuilder sb = new StringBuilder();

    // 定义package
    sb.append(JavaKeyWord.PACKAGE)
        .append(Symbol.SPACE)
        .append(daoPackageDefine.getPackagePath())
        .append(Symbol.SEMICOLON)
        .append(Symbol.ENTER_LINE)
        .append(Symbol.ENTER_LINE);

    // 导包的信息
    this.importMethodList(methodItemList, poPackageDefine, sb);

    // 添加类注释信息
    sb.append(JavaKeyWord.ANNO_CLASS).append(Symbol.ENTER_LINE);
    sb.append(JavaKeyWord.ANNO_CLASS_MID)
        .append(Symbol.SPACE)
        .append(tableInfo.getTableComment())
        .append(Symbol.BRACKET_LEFT)
        .append(tableInfo.getTableName())
        .append(Symbol.BRACKET_RIGHT)
        .append(dataComment)
        .append(Symbol.ENTER_LINE);

    // 空格
    sb.append(JavaKeyWord.ANNO_CLASS_MID).append(Symbol.ENTER_LINE);

    sb.append(JavaKeyWord.DOC_VERSION).append(Symbol.ENTER_LINE);
    sb.append(JavaKeyWord.DOC_AUTH).append(Symbol.ENTER_LINE);
    sb.append(JavaKeyWord.ANNO_OVER).append(Symbol.ENTER_LINE);

    // 类的定义
    sb.append(JavaKeyWord.PUBLIC)
        .append(Symbol.SPACE)
        .append(JavaKeyWord.INTERFACE)
        .append(Symbol.SPACE)
        .append(className)
        .append(Symbol.SPACE)
        .append(Symbol.BRACE_LEFT)
        .append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);

    return sb;
  }

  /**
   * 导包的列表
   *
   * @param methodList 方法列表
   * @param poPackageDefine 实体参数
   * @param sb 插入的对象
   */
  private void importMethodList(
      List<MethodInfo> methodList, ImportPackageInfo poPackageDefine, StringBuilder sb) {
    Set<String> importSet = new HashSet<>();
    // 找出所有方法需要导入的对象
    for (MethodInfo methodItem : methodList) {
      getImportMethod(methodItem, importSet);
    }
    // 添加po的导入
    importSet.add(ImportPackageUtils.packageOut(poPackageDefine));

    for (String importItem : importSet) {
      sb.append(JavaKeyWord.IMPORT)
          .append(Symbol.SPACE)
          .append(importItem)
          .append(Symbol.SEMICOLON)
          .append(Symbol.ENTER_LINE);
    }
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 获取所有需要导入的包
   *
   * @param methodItem
   * @return
   */
  private void getImportMethod(MethodInfo methodItem, Set<String> importSet) {

    // 1，所有的参数
    for (TypeInfo paramInfo : methodItem.getParamType()) {
      // 在加入导包前需要检查当前是否存在路径，存在路径，则才需要导入
      if (StringUtils.isNotEmpty(paramInfo.getImportPath())) {
        importSet.add(paramInfo.getImportPath());
      }
    }

    // 返回类型同样需样检查
    if (null != methodItem.getReturnType()
        && StringUtils.isNotEmpty(methodItem.getReturnType().getImportPath())) {
      // 返回类型
      importSet.add(methodItem.getReturnType().getImportPath());
    }
  }

  /**
   * 执行数据库修改操作的方法生成，包括添加、修改、删除
   *
   * @param sb
   * @param poClassName
   * @param methodItem
   */
  private void updateMethod(StringBuilder sb, String poClassName, MethodInfo methodItem) {

    // 修改方法
    sb.append(JavaFormat.appendTab(1)).append(JavaKeyWord.ANNO_CLASS).append(Symbol.ENTER_LINE);
    // 方法注释
    sb.append(JavaFormat.appendTab(1))
        .append(JavaKeyWord.ANNO_CLASS_MID)
        .append(Symbol.SPACE)
        .append(methodItem.getComment())
        .append(Symbol.ENTER_LINE);

    // 空格
    sb.append(JavaFormat.appendTab(1)).append(JavaKeyWord.ANNO_CLASS_MID).append(Symbol.ENTER_LINE);

    // 添加参数注释
    sb.append(JavaFormat.appendTab(1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(JavaKeyWord.METHOD_PARAM).append(JavaVarName.METHOD_PARAM_NAME).append(Symbol.SPACE);
    sb.append(CodeComment.METHOD_PARAM_DOC).append(Symbol.ENTER_LINE);
    // 添加返回注释
    sb.append(JavaFormat.appendTab(1))
        .append(JavaKeyWord.ANNO_CLASS_MID)
        .append(JavaKeyWord.METHOD_RETURN_COMMENT)
        .append(CodeComment.METHOD_DAO_UPDATE_RETURN)
        .append(Symbol.ENTER_LINE);
    // 注释结束
    sb.append(JavaFormat.appendTab(1)).append(JavaKeyWord.ANNO_OVER).append(Symbol.ENTER_LINE);

    // 方法定义
    sb.append(JavaFormat.appendTab(1))
        .append(JavaKeyWord.INT_TYPE)
        .append(Symbol.SPACE)
        .append(methodItem.getName())
        .append(Symbol.BRACKET_LEFT);
    // 进行方法参数的输出
    for (TypeInfo typeinfo : methodItem.getParamType()) {
      String outClass = typeinfo.getImportClassName();

      if (outClass.indexOf(GenerateDefineFlag.TABLE_NAME.getDefineFlag()) != -1) {
        outClass = outClass.replaceAll(GenerateDefineFlag.TABLE_NAME.getDefineFlag(), poClassName);
      }
      sb.append(outClass);
      sb.append(Symbol.SPACE);
      sb.append(JavaVarName.METHOD_PARAM_NAME);
      sb.append(Symbol.COMMA);
    }

    // 最后一个逗号的删除
    sb.deleteCharAt(sb.length() - 1);

    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 分页查询方法
   *
   * @param sb 代码信息
   * @param poClassName 实体的名称
   * @param methodItem 方法实体
   */
  private void queryMethod(StringBuilder sb, String poClassName, MethodInfo methodItem) {
    // 普通查询查询
    sb.append(JavaFormat.appendTab(1)).append(JavaKeyWord.ANNO_CLASS).append(Symbol.ENTER_LINE);
    sb.append(JavaFormat.appendTab(1))
        .append(JavaKeyWord.ANNO_CLASS_MID)
        .append(Symbol.SPACE)
        .append(methodItem.getComment())
        .append(Symbol.ENTER_LINE);

    // 空格
    sb.append(JavaFormat.appendTab(1)).append(JavaKeyWord.ANNO_CLASS_MID).append(Symbol.ENTER_LINE);

    // 添加参数注释
    sb.append(JavaFormat.appendTab(1)).append(JavaKeyWord.ANNO_CLASS_MID);
    sb.append(JavaKeyWord.METHOD_PARAM).append(JavaVarName.METHOD_PARAM_NAME);
    sb.append(Symbol.SPACE).append(CodeComment.METHOD_PARAM_DOC).append(Symbol.ENTER_LINE);
    // 添加返回注释
    sb.append(JavaFormat.appendTab(1))
        .append(JavaKeyWord.ANNO_CLASS_MID)
        .append(JavaKeyWord.METHOD_RETURN_COMMENT)
        .append(CodeComment.METHOD_QUERY_RESULT)
        .append(Symbol.ENTER_LINE);
    // 注释结束
    sb.append(JavaFormat.appendTab(1)).append(JavaKeyWord.ANNO_OVER).append(Symbol.ENTER_LINE);

    // 查询方法的定义
    sb.append(JavaFormat.appendTab(1));
    // 1,返回对象
    sb.append(this.getTypeName(methodItem.getReturnType(), poClassName));
    // 添加空格
    sb.append(Symbol.SPACE);
    sb.append(methodItem.getName());
    // 左括号
    sb.append(Symbol.BRACKET_LEFT);
    // 添加方法的参数
    sb.append(typeParam(methodItem, poClassName));
    sb.append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 参数类型
   *
   * @param methodItem 方法类型
   * @param poClassName 实体参数类名
   * @return 参数信息
   */
  private String typeParam(MethodInfo methodItem, String poClassName) {

    StringBuilder outParam = new StringBuilder();

    for (TypeInfo typeInfo : methodItem.getParamType()) {
      outParam.append(this.getTypeName(typeInfo, poClassName));
      outParam.append(Symbol.SPACE);
      outParam.append(JavaVarName.METHOD_PARAM_NAME);
      outParam.append(Symbol.COMMA);
    }
    // 去除最后一个逗号
    outParam.deleteCharAt(outParam.length() - 1);

    return outParam.toString();
  }

  /**
   * 获取类型名称
   *
   * @param typeInfo 方法选项
   * @param poClassName 实体的类名
   * @return
   */
  private String getTypeName(TypeInfo typeInfo, String poClassName) {
    String className = typeInfo.getImportClassName();
    // 执行类的泛型替换操作
    if (className.indexOf(GenerateDefineFlag.TABLE_NAME.getDefineFlag()) != -1) {
      className = className.replaceAll(GenerateDefineFlag.TABLE_NAME.getDefineFlag(), poClassName);
    }
    ;

    return className;
  }

  /**
   * class文件结束
   *
   * @param sb 字符信息
   */
  private void classFinish(StringBuilder sb) {
    // 结束
    sb.append(Symbol.BRACE_RIGHT);
  }
}
