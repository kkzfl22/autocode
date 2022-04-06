package com.liujun.micro.autocode.generator.builder.operator.base;

import com.liujun.micro.autocode.config.generate.entity.MethodInfo;
import com.liujun.micro.autocode.config.generate.entity.TypeInfo;
import com.liujun.micro.autocode.constant.GenerateDefineFlag;
import com.liujun.micro.autocode.constant.MethodTypeEnum;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.CodeComment;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.ImportCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.builder.entity.DataParam;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodArguments;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodEntity;
import com.liujun.micro.autocode.generator.builder.operator.code.GenerateJavaDaoInterface;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaCommentUtil;
import com.liujun.micro.autocode.generator.builder.operator.utils.MethodUtils;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成java的接口
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaRepositoryFacadeInterface {

  public static final GenerateJavaRepositoryFacadeInterface INSTANCE =
      new GenerateJavaRepositoryFacadeInterface();

  /**
   * 生成java的接口信息
   *
   * @param param 参数信息
   * @return 生成的javabean对象
   */
  public StringBuilder generateJavaInterface(DataParam param) {

    // 获得当前配制的方法

    // 检查当前是否存在分页查询
    boolean checkPage = MethodUtils.checkPageQuery(param.getMethodList());

    List<ImportPackageInfo> importPackage = null;

    // 如果当前存在分页查询，则需要导入相关分页查询的包
    if (checkPage) {
      importPackage = new ArrayList<>();
      // 导入分页参数类
      importPackage.add(ImportCodePackageKey.PAGE_PARAM.getPackageInfo());
      // 导入分页结果类
      importPackage.add(ImportCodePackageKey.PAGE_RESULT.getPackageInfo());
    }

    // 1,方法头的定义
    StringBuilder sb =
        GenerateJavaDaoInterface.INSTANCE.defineClass(
            param.getAuthor(),
            param.getPkg(GenerateCodePackageKey.DOMAIN_PERSIST_FACADE),
            param.getPkg(GenerateCodePackageKey.DOMAIN_DO),
            importPackage,
            param.getMethodList());

    ImportPackageInfo domainPkg = param.getPkg(GenerateCodePackageKey.DOMAIN_DO);

    for (MethodInfo methodItem : param.getMethodList()) {
      // 方法执行修改操作,即所有的数据的，添加、修改、删除
      if (MethodTypeEnum.UPDATE.getType().equals(methodItem.getOperator())
          || MethodTypeEnum.INSERT.getType().equals(methodItem.getOperator())
          || MethodTypeEnum.DELETE.getType().equals(methodItem.getOperator())) {

        // 修改方法,包括，增加、删、改
        this.updateMethod(sb, domainPkg.getClassName(), methodItem);
      }
      // 方法分页执行查询操作
      else if (MethodTypeEnum.QUERY_PAGE.getType().equals(methodItem.getOperator())) {
        // 分页查询查询方法
        this.pageQueryMethod(sb, domainPkg.getClassName(), methodItem);
      }
      // 方法执行查询操作
      else if (MethodTypeEnum.QUERY.getType().equals(methodItem.getOperator())) {
        // 分页查询查询方法
        this.queryMethod(sb, domainPkg.getClassName(), methodItem);
      }
    }

    // 结束
    this.classFinish(sb);

    return sb;
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
            .type(JavaKeyWord.TYPE_BOOLEAN)
            // 返回值注释
            .returnComment(CodeComment.METHOD_DAO_UPDATE_BOOLEAN_RETURN)
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
  private void pageQueryMethod(StringBuilder sb, String poClassName, MethodInfo methodItem) {

    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 方法注释
            .comment(methodItem.getComment())
            // 返回值
            .type(JavaCommentUtil.pageQueryReturn(poClassName))
            // 返回值注释
            .returnComment(ImportCodePackageKey.PAGE_RESULT.getPackageInfo().getClassComment())
            // 方法名
            .name(methodItem.getName())
            // 参数
            .arguments(JavaCommentUtil.pageQueryParam(methodItem, poClassName))
            .build();

    // 方法生成
    JavaClassCodeUtils.methodDefine(sb, methodInfo);

    // 接口声明结束
    sb.append(Symbol.SEMICOLON);
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

    // 添加参数,目前仅使用0个参数
    JavaMethodArguments argumentItem =
        JavaMethodArguments.builder()
            .type(JavaClassCodeUtils.getTypeName(methodItem.getParamType().get(0), poClassName))
            .name(JavaVarName.METHOD_PARAM_NAME)
            .comment(CodeComment.METHOD_PARAM_DOC)
            .build();
    argumentsList.add(argumentItem);

    // 方法实体信息
    JavaMethodEntity methodInfo =
        JavaMethodEntity.builder()
            // 方法注释
            .comment(methodItem.getComment())
            // 返回值
            .type(JavaClassCodeUtils.getTypeName(methodItem.getReturnType(), poClassName))
            // 返回值注释
            .returnComment(CodeComment.METHOD_QUERY_RESULT)
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
