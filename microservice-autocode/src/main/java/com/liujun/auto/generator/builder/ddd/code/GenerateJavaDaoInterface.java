package com.liujun.auto.generator.builder.ddd.code;

import com.liujun.auto.generator.builder.utils.JavaClassCodeUtils;
import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.config.generate.entity.TypeInfo;
import com.liujun.auto.constant.GenerateDefineFlag;
import com.liujun.auto.constant.MethodOperatorEnum;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.constant.CodeComment;
import com.liujun.auto.generator.builder.ddd.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.JavaVarName;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.ddd.entity.JavaMethodArguments;
import com.liujun.auto.generator.builder.ddd.entity.JavaMethodEntity;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
   * @param param 上下文参数
   * @param tableName 名称信息
   * @return 生成的javabean对象
   */
  public StringBuilder generateJavaInterface(GenerateCodeContext param, String tableName) {

    // 获取实体信息
    ImportPackageInfo poPackageInfo = param.getPkg(tableName, GenerateCodePackageKey.PERSIST_PO);
    ImportPackageInfo daoPackageInfo = param.getPkg(tableName, GenerateCodePackageKey.PERSIST_MAPPER);

    List<MethodInfo> codeMethod = param.getGenerateConfig().getGenerate().getMethodList();
    String author = param.getGenerateConfig().getGenerate().getAuthor();

    // 1,方法头的定义
    StringBuilder sb = defineClass(author, daoPackageInfo, poPackageInfo, null, codeMethod);

    for (MethodInfo methodItem : codeMethod) {
      // 方法执行修改操作,即所有的数据的，添加、修改、删除
      if (MethodOperatorEnum.UPDATE.getType().equals(methodItem.getOperator())
          || MethodOperatorEnum.INSERT.getType().equals(methodItem.getOperator())
          || MethodOperatorEnum.DELETE.getType().equals(methodItem.getOperator())) {

        // 修改方法,包括，增加、删、改
        this.updateMethod(sb, poPackageInfo.getClassName(), methodItem);
      }
      // 方法执行查询操作
      else if (MethodOperatorEnum.QUERY_PAGE.getType().equals(methodItem.getOperator())
          || MethodOperatorEnum.QUERY.getType().equals(methodItem.getOperator())) {
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
  public List<String> importMethodList(
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
  public void updateMethod(StringBuilder sb, String poClassName, MethodInfo methodItem) {

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
  public void queryMethod(StringBuilder sb, String poClassName, MethodInfo methodItem) {

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
  public void classFinish(StringBuilder sb) {
    // 结束
    sb.append(Symbol.BRACE_RIGHT);
  }
}
