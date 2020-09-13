package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.constant.GenerateDefineFlag;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.config.generate.entity.MethodInfo;
import com.liujun.micro.autocode.config.generate.entity.TypeInfo;
import com.liujun.micro.autocode.generator.builder.constant.CodeComment;
import com.liujun.micro.autocode.constant.MethodTypeEnum;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodArguments;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodEntity;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 生成java的接口
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaDaoInterface {

  public static final GenerateJavaDaoInterface INSTANCE = new GenerateJavaDaoInterface();

  /**
   * 生成java的接口信息
   *
   * @param poPackageInfo 实体包的信息
   * @param daoPackageInfo dao的类定义
   * @param codeMethod 需要生成的方法
   * @return 生成的javabean对象
   */
  public StringBuilder generateJavaInterface(
      ImportPackageInfo poPackageInfo,
      ImportPackageInfo daoPackageInfo,
      List<MethodInfo> codeMethod,
      String author) {

    // 获得当前配制的方法

      // 1,方法头的定义
    StringBuilder sb = defineClass(author, daoPackageInfo, poPackageInfo, null, codeMethod);

    for (MethodInfo methodItem : codeMethod) {
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
   * @param author 作者
   * @param poPackageDefine 数据库实体
   * @param currPackageClass dao的包定义
   * @param importPackage 导入包信息
   * @param methodItemList 方法列表
   * @return 生成的代码对象
   */
  public StringBuilder defineClass(
      String author,
      ImportPackageInfo currPackageClass,
      ImportPackageInfo poPackageDefine,
      List<ImportPackageInfo> importPackage,
      List<MethodInfo> methodItemList) {

    // 导入包信息
    List<String> importList = this.importMethodList(methodItemList, poPackageDefine, importPackage);

    // 生成接口的定义
    return JavaClassCodeUtils.interfaceDefine(currPackageClass, importList, author);
  }

  /**
   * 导包的列表
   *
   * @param methodList 方法列表
   * @param poPackageDefine 实体参数
   * @param importPackage 公共导入包信息
   */
  private List<String> importMethodList(
      List<MethodInfo> methodList,
      ImportPackageInfo poPackageDefine,
      List<ImportPackageInfo> importPackage) {
    Set<String> importSet = new HashSet<>();
    // 找出所有方法需要导入的对象
    for (MethodInfo methodItem : methodList) {
      getImportMethod(methodItem, importSet);
    }
    // 添加po的导入
    importSet.add(poPackageDefine.packageOut());

    if (null != importPackage && !importPackage.isEmpty()) {
      // 导入其他公共包
      for (ImportPackageInfo importPkg : importPackage) {
        importSet.add(importPkg.packageOut());
      }
    }

    List<String> resultDataList = new ArrayList<>(methodList.size());

    resultDataList.addAll(importSet);

    return resultDataList;
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

    List<JavaMethodArguments> argumentsList = new ArrayList<>(methodItem.getParamType().size());

    // 进行方法参数的输出
    for (TypeInfo typeinfo : methodItem.getParamType()) {
      String outClass = typeinfo.getImportClassName();
      if (outClass.indexOf(GenerateDefineFlag.TABLE_NAME.getDefineFlag()) != -1) {
        outClass = outClass.replaceAll(GenerateDefineFlag.TABLE_NAME.getDefineFlag(), poClassName);
      }

      JavaMethodArguments argumentItem =
          JavaMethodArguments.builder()
              .type(outClass)
              .name(JavaVarName.METHOD_PARAM_NAME)
              .comment(CodeComment.METHOD_PARAM_DOC)
              .build();

      argumentsList.add(argumentItem);
    }

    // 方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 方法注释
            .comment(methodItem.getComment())
            // 返回值
            .type(JavaKeyWord.INT_TYPE)
            // 返回值注释
            .returnComment(CodeComment.METHOD_DAO_UPDATE_INT_RETURN)
            // 方法名
            .name(methodItem.getName())
            // 参数
            .arguments(argumentsList)
            .build();

    // 方法生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);

    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
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

    List<JavaMethodArguments> argumentsList = new ArrayList<>(methodItem.getParamType().size());

    // 进行方法参数的输出
    for (TypeInfo typeinfo : methodItem.getParamType()) {
      String outClass = typeinfo.getImportClassName();
      if (outClass.indexOf(GenerateDefineFlag.TABLE_NAME.getDefineFlag()) != -1) {
        outClass = outClass.replaceAll(GenerateDefineFlag.TABLE_NAME.getDefineFlag(), poClassName);
      }

      JavaMethodArguments argumentItem =
          JavaMethodArguments.builder()
              .type(outClass)
              .name(JavaVarName.METHOD_PARAM_NAME)
              .comment(CodeComment.METHOD_PARAM_DOC)
              .build();

      argumentsList.add(argumentItem);
    }

    // 方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 方法注释
            .comment(methodItem.getComment())
            // 返回值
            .type(JavaClassCodeUtils.getTypeName(methodItem.getReturnType(), poClassName))
            // 返回值注释
            .returnComment(CodeComment.METHOD_DATABASE_QUERY_RESULT)
            // 方法名
            .name(methodItem.getName())
            // 参数
            .arguments(argumentsList)
            .build();

    // 方法生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);

    // 接口声明结束
    sb.append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);
    sb.append(Symbol.ENTER_LINE);
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
