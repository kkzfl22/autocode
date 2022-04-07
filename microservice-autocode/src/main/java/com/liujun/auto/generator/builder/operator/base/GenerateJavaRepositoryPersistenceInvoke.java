package com.liujun.auto.generator.builder.operator.base;

import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.constant.MethodTypeEnum;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.constant.CodeAnnotation;
import com.liujun.auto.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.constant.ImportCodePackageKey;
import com.liujun.auto.generator.builder.constant.JavaMethodName;
import com.liujun.auto.generator.builder.constant.JavaVarName;
import com.liujun.auto.generator.builder.constant.JavaVarValue;
import com.liujun.auto.generator.builder.entity.DataParam;
import com.liujun.auto.generator.builder.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.entity.JavaClassEntity;
import com.liujun.auto.generator.builder.entity.JavaClassFieldEntity;
import com.liujun.auto.generator.builder.entity.JavaMethodArguments;
import com.liujun.auto.generator.builder.entity.JavaMethodEntity;
import com.liujun.auto.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.auto.generator.builder.operator.utils.JavaCommentUtil;
import com.liujun.auto.generator.builder.operator.utils.MethodUtils;
import com.liujun.auto.generator.builder.operator.utils.ReturnUtils;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.serivce.JavaFormat;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 进行存储层的方法调用
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaRepositoryPersistenceInvoke {

  /** 实例对象 */
  public static final GenerateJavaRepositoryPersistenceInvoke INSTANCE =
      new GenerateJavaRepositoryPersistenceInvoke();

  /** 导入的包信息 */
  private static final List<String> IMPORT_PKG =
      Arrays.asList(
          "lombok.extern.slf4j.Slf4j",
          "org.springframework.beans.factory.annotation.Autowired",
          "org.springframework.stereotype.Repository",
          JavaKeyWord.IMPORT_LIST);

  /** 分页相关 导入包 */
  private static final List<String> PAGE_IMPORT_PKG =
      Arrays.asList(
          "com.github.pagehelper.Page",
          "com.github.pagehelper.PageHelper",
          "com.github.pagehelper.PageInfo",
          ImportCodePackageKey.PAGE_PARAM.getPackageInfo().packageOut());

  /** 分页查询的响应名称 */
  private static final String METHOD_PAGE_RSP_NAME = "pageSet";

  /** 分页的代码设置 */
  private static final String PAGE_SET =
      "Page<PageInfo> "
          + METHOD_PAGE_RSP_NAME
          + " = PageHelper.startPage("
          + JavaVarName.PAGE_REQUEST
          + ".getPage(), "
          + JavaVarName.PAGE_REQUEST
          + ".getSize());";

  /**
   * 生成存储层的转换接口
   *
   * @param param 参数信息
   * @return 构建的存储层对象
   */
  public StringBuilder generateRepository(DataParam param) {

    // 获取数据库实体
    ImportPackageInfo poPackageInfo = param.getPkg(GenerateCodePackageKey.PERSIST_PO);

    // 领域实体实体
    ImportPackageInfo domainPackageInfo = param.getPkg(GenerateCodePackageKey.DOMAIN_DO);

    // 数据库接口实体
    ImportPackageInfo daoPackageInfo = param.getPkg(GenerateCodePackageKey.PERSIST_DAO);

    // 数据库与实体层的转换
    ImportPackageInfo daoAssemblerPackageInfo =
        param.getPkg(GenerateCodePackageKey.PERSIST_ASSEMBLER);

    // 领域的存储接口
    ImportPackageInfo persistFacadePackageInfo =
        param.getPkg(GenerateCodePackageKey.DOMAIN_PERSIST_FACADE);

    // 领域的存储接口
    ImportPackageInfo repositoryPersistencePackage =
        param.getPkg(GenerateCodePackageKey.PERSIST_PERSISTENCE);

    // 类的声明
    StringBuilder sb =
        this.persistenceDefine(
            repositoryPersistencePackage,
            persistFacadePackageInfo,
            daoPackageInfo,
            domainPackageInfo,
            daoAssemblerPackageInfo,
            poPackageInfo,
            param.getMethodList(),
            param.getAuthor());

    // 1,类属性的创建
    sb.append(
        JavaClassCodeUtils.getClassField(
            JavaClassFieldEntity.getPrivateAutowiredField(
                daoPackageInfo.getClassName(),
                JavaVarName.SPRING_INSTANCE_NAME,
                daoPackageInfo.getClassComment())));

    for (MethodInfo methodItem : param.getMethodList()) {
      // 1,针对增删除改的方法进行调用
      if (MethodTypeEnum.INSERT.getType().equals(methodItem.getOperator())
          || MethodTypeEnum.UPDATE.getType().equals(methodItem.getOperator())
          || MethodTypeEnum.DELETE.getType().equals(methodItem.getOperator())) {
        this.updateMethod(
            sb, methodItem, domainPackageInfo, daoAssemblerPackageInfo, poPackageInfo);
      }
      // 如果当前为查询分页操作
      else if (MethodTypeEnum.QUERY_PAGE.getType().equals(methodItem.getOperator())) {
        this.pqgeQueryMethod(
            sb, methodItem, domainPackageInfo, daoAssemblerPackageInfo, poPackageInfo);
      }
      // 如果当前为查询则进行查询调用操作
      else if (MethodTypeEnum.QUERY.getType().equals(methodItem.getOperator())) {
        this.queryMethod(sb, methodItem, domainPackageInfo, daoAssemblerPackageInfo, poPackageInfo);
      }
    }

    // 类的结束
    JavaClassCodeUtils.classEnd(sb);
    return sb;
  }

  /**
   * 数据修改相关的调用
   *
   * @param sb
   * @param method 方法信息
   * @param domainPkg 领域包信息
   * @param assemblerPkg 转换类信息
   * @param poPkg 数据库实体包
   */
  private void updateMethod(
      StringBuilder sb,
      MethodInfo method,
      ImportPackageInfo domainPkg,
      ImportPackageInfo assemblerPkg,
      ImportPackageInfo poPkg) {
    // 检查当前参数是否为集合
    boolean checkBatchFlag = MethodUtils.checkBatch(method.getParamType());

    List<JavaMethodArguments> argumentsList = null;

    // 如果当前批量添加操作
    if (checkBatchFlag) {
      argumentsList =
          Arrays.asList(
              JavaMethodArguments.builder()
                  .type(JavaClassCodeUtils.listType(domainPkg.getClassName()))
                  .name(JavaVarName.METHOD_PARAM_NAME)
                  .build());
    }
    // 非批量添加操作
    else {
      argumentsList =
          Arrays.asList(
              JavaMethodArguments.builder()
                  .type(domainPkg.getClassName())
                  .name(JavaVarName.METHOD_PARAM_NAME)
                  .build());
    }

    JavaMethodEntity methodEntity =
        JavaMethodEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 返回值
            .type(JavaKeyWord.TYPE_BOOLEAN)
            // 方法名
            .name(method.getName())
            // 重写标识
            .annotation(CodeAnnotation.OVERRIDE)
            // 参数
            .arguments(argumentsList)
            .build();
    // 1,创建方法定义
    JavaClassCodeUtils.methodDefine(sb, methodEntity);
    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 数据插入方法的调用
    this.dataInsert(sb, method, checkBatchFlag, assemblerPkg, poPkg);

    // 数据添加、修改、删除相关的调用
    this.dataUpdateInvoke(sb, method);
    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 数据插入方法的调用
   *
   * @param sb
   * @param method 方法信息
   * @param checkBatchFlag 批量插入操作
   * @param assemblerPkg 转换的实体
   * @param poPkg 实体类
   */
  private void dataInsert(
      StringBuilder sb,
      MethodInfo method,
      boolean checkBatchFlag,
      ImportPackageInfo assemblerPkg,
      ImportPackageInfo poPkg) {
    // 记录进入方法的的日志
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaVarName.LOG).append(Symbol.POINT).append(JavaMethodName.LOG_DEBUG);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.QUOTE);
    sb.append(method.getName()).append(Symbol.SPACE).append(JavaVarName.METHOD_PARAM_NAME);
    sb.append(Symbol.SPACE).append(Symbol.BRACE_LEFT).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.QUOTE).append(Symbol.COMMA);
    sb.append(JavaVarName.METHOD_PARAM_NAME);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 如果当前非集合，则调用单个转换
    if (checkBatchFlag) {
      // 调用单对象转换方法
      sb.append(JavaFormat.appendTab(2));
      sb.append(JavaClassCodeUtils.listType(poPkg.getClassName())).append(Symbol.SPACE);
      sb.append(JavaVarName.ASSEMBLER_PARSE_NAME).append(Symbol.SPACE).append(Symbol.EQUAL);
      sb.append(Symbol.SPACE).append(assemblerPkg.getClassName()).append(Symbol.POINT);
      sb.append(JavaMethodName.ASSEMBLER_ENTITY_PERSIST_LIST_NAME).append(Symbol.BRACKET_LEFT);
      sb.append(JavaVarName.METHOD_PARAM_NAME).append(Symbol.BRACKET_RIGHT);
      sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    }
    // 如果当前为集合，则调用集合的转换
    else {

      // 调用单对象转换方法
      sb.append(JavaFormat.appendTab(2));
      sb.append(poPkg.getClassName()).append(Symbol.SPACE);
      sb.append(JavaVarName.ASSEMBLER_PARSE_NAME).append(Symbol.SPACE).append(Symbol.EQUAL);
      sb.append(Symbol.SPACE).append(assemblerPkg.getClassName()).append(Symbol.POINT);
      sb.append(JavaMethodName.ASSEMBLER_ENTITY_PERSIST_NAME).append(Symbol.BRACKET_LEFT);
      sb.append(JavaVarName.METHOD_PARAM_NAME).append(Symbol.BRACKET_RIGHT);
      sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    }
  }

  /**
   * 数据修改的调用
   *
   * @param sb
   * @param method 方法信息
   */
  private void dataUpdateInvoke(StringBuilder sb, MethodInfo method) {
    // 执行数据的插入操作
    sb.append(JavaFormat.appendTab(2)).append(JavaKeyWord.INT_TYPE);
    sb.append(Symbol.SPACE).append(JavaVarName.INVOKE_METHOD_UPDATE_RSP);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(JavaVarName.SPRING_INSTANCE_NAME);
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.ASSEMBLER_PARSE_NAME).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 方法执行后的日志
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaVarName.LOG).append(Symbol.POINT).append(JavaMethodName.LOG_DEBUG);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.QUOTE);
    sb.append(method.getName()).append(Symbol.SPACE).append(JavaVarName.METHOD_PARAM_NAME);
    sb.append(Symbol.SPACE).append(Symbol.BRACE_LEFT).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.SPACE).append(JavaKeyWord.RETURN);
    sb.append(Symbol.SPACE).append(Symbol.BRACE_LEFT).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.QUOTE).append(Symbol.COMMA);
    sb.append(JavaVarName.METHOD_PARAM_NAME);
    sb.append(Symbol.COMMA).append(JavaVarName.INVOKE_METHOD_UPDATE_RSP);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 检查操作的结果，进行返回
    sb.append(JavaFormat.appendTab(2)).append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(JavaVarName.INVOKE_METHOD_UPDATE_RSP);
    sb.append(Symbol.SPACE).append(Symbol.ANGLE_BRACKETS_RIGHT).append(Symbol.SPACE);
    sb.append(JavaVarValue.ZERO).append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
  }

  /**
   * 数据修改的调用
   *
   * @param sb
   * @param method 方法信息
   * @param poPkg 存储层实体
   * @param domainPkg 领域对象
   * @param assemblerPkg 存储对象
   */
  private void dataNormalQueryInvoke(
      StringBuilder sb,
      MethodInfo method,
      ImportPackageInfo poPkg,
      ImportPackageInfo domainPkg,
      ImportPackageInfo assemblerPkg) {

    // 检查结果是单对象还是结果集
    boolean rspListFlag = ReturnUtils.checkReturnList(method.getReturns());

    // 如果当前返回多结果集，则调用相关的多结果集方法
    if (rspListFlag) {
      this.queryList(sb, method, poPkg, domainPkg, assemblerPkg);
    } else {
      this.queryRspOne(sb, method, poPkg, domainPkg, assemblerPkg);
    }
  }

  /**
   * 查询返回结果集的方法
   *
   * @param sb
   * @param method
   * @param poPkg
   * @param domainPkg
   * @param assemblerPkg
   */
  private void queryList(
      StringBuilder sb,
      MethodInfo method,
      ImportPackageInfo poPkg,
      ImportPackageInfo domainPkg,
      ImportPackageInfo assemblerPkg) {
    // 执行数据的分页查询
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.LIST_TYPE).append(poPkg.getClassName()).append(JavaKeyWord.LIST_TYPE_END);
    sb.append(Symbol.SPACE).append(JavaVarName.QUERY_LIST_RESPONSE);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(JavaVarName.SPRING_INSTANCE_NAME);
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.ASSEMBLER_PARSE_NAME).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 方法执行后的日志
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaVarName.LOG).append(Symbol.POINT).append(JavaMethodName.LOG_DEBUG);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.QUOTE);
    sb.append(method.getName()).append(Symbol.SPACE).append(JavaVarName.METHOD_PARAM_NAME);
    sb.append(Symbol.SPACE).append(Symbol.BRACE_LEFT).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.SPACE).append(JavaKeyWord.RETURN);
    sb.append(Symbol.SPACE).append(Symbol.BRACE_LEFT).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.QUOTE).append(Symbol.COMMA);
    sb.append(JavaVarName.METHOD_PARAM_NAME);
    sb.append(Symbol.COMMA).append(JavaVarName.QUERY_LIST_RESPONSE);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 进行的返回结果转换
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(assemblerPkg.getClassName()).append(Symbol.POINT);
    sb.append(JavaMethodName.ASSEMBLER_PERSIST_ENTITY_LIST_NAME).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.QUERY_LIST_RESPONSE).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
  }

  /**
   * 查询返回单结果的方法
   *
   * @param sb
   * @param method
   * @param poPkg
   * @param domainPkg
   * @param assemblerPkg
   */
  private void queryRspOne(
      StringBuilder sb,
      MethodInfo method,
      ImportPackageInfo poPkg,
      ImportPackageInfo domainPkg,
      ImportPackageInfo assemblerPkg) {
    // 执行数据的分页查询
    sb.append(JavaFormat.appendTab(2));
    sb.append(poPkg.getClassName()).append(Symbol.SPACE).append(JavaVarName.QUERY_RESPONSE_DATA);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(JavaVarName.SPRING_INSTANCE_NAME);
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.ASSEMBLER_PARSE_NAME).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 方法执行后的日志
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaVarName.LOG).append(Symbol.POINT).append(JavaMethodName.LOG_DEBUG);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.QUOTE);
    sb.append(method.getName()).append(Symbol.SPACE).append(JavaVarName.METHOD_PARAM_NAME);
    sb.append(Symbol.SPACE).append(Symbol.BRACE_LEFT).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.SPACE).append(JavaKeyWord.RETURN);
    sb.append(Symbol.SPACE).append(Symbol.BRACE_LEFT).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.QUOTE).append(Symbol.COMMA);
    sb.append(JavaVarName.METHOD_PARAM_NAME);
    sb.append(Symbol.COMMA).append(JavaVarName.QUERY_RESPONSE_DATA);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 进行的返回结果转换
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(assemblerPkg.getClassName()).append(Symbol.POINT);
    sb.append(JavaMethodName.ASSEMBLER_DOMAIN_ENTITY_NAME).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.QUERY_RESPONSE_DATA).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
  }

  /**
   * 数据查询相关的调用
   *
   * @param sb
   * @param method 方法信息
   * @param domainPkg 领域包信息
   * @param assemblerPkg 转换类信息
   * @param poPkg 数据库实体包
   */
  private void queryMethod(
      StringBuilder sb,
      MethodInfo method,
      ImportPackageInfo domainPkg,
      ImportPackageInfo assemblerPkg,
      ImportPackageInfo poPkg) {

    List<JavaMethodArguments> argumentsList =
        Arrays.asList(
            JavaMethodArguments.builder()
                .type(domainPkg.getClassName())
                .name(JavaVarName.METHOD_PARAM_NAME)
                .build());

    JavaMethodEntity methodEntity =
        JavaMethodEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 返回值
            .type(JavaClassCodeUtils.getTypeName(method.getReturnType(), domainPkg.getClassName()))
            // 方法名
            .name(method.getName())
            // 重写标识
            .annotation(CodeAnnotation.OVERRIDE)
            // 参数
            .arguments(argumentsList)
            .build();

    // 1,创建方法定义
    JavaClassCodeUtils.methodDefine(sb, methodEntity);
    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 记录进入方法的的日志
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaVarName.LOG).append(Symbol.POINT).append(JavaMethodName.LOG_DEBUG);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.QUOTE);
    sb.append(method.getName()).append(Symbol.SPACE).append(JavaVarName.METHOD_PARAM_NAME);
    sb.append(Symbol.SPACE).append(Symbol.BRACE_LEFT).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.QUOTE).append(Symbol.COMMA);
    sb.append(JavaVarName.METHOD_PARAM_NAME);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 调用单对象转换方法
    sb.append(JavaFormat.appendTab(2));
    sb.append(poPkg.getClassName()).append(Symbol.SPACE);
    sb.append(JavaVarName.ASSEMBLER_PARSE_NAME).append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(assemblerPkg.getClassName()).append(Symbol.POINT);
    sb.append(JavaMethodName.ASSEMBLER_ENTITY_PERSIST_NAME).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.METHOD_PARAM_NAME).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 数据添加、修改、删除相关的调用
    this.dataNormalQueryInvoke(sb, method, poPkg, domainPkg, assemblerPkg);
    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 分页相关的方法的调用
   *
   * @param sb
   * @param method 方法信息
   * @param domainPkg 领域包信息
   * @param assemblerPkg 转换类信息
   * @param poPkg 数据库实体包
   */
  private void pqgeQueryMethod(
      StringBuilder sb,
      MethodInfo method,
      ImportPackageInfo domainPkg,
      ImportPackageInfo assemblerPkg,
      ImportPackageInfo poPkg) {

    JavaMethodEntity methodEntity =
        JavaMethodEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 返回值
            .type(JavaCommentUtil.pageQueryReturn(domainPkg.getClassName()))
            // 方法名
            .name(method.getName())
            // 重写标识
            .annotation(CodeAnnotation.OVERRIDE)
            // 参数
            .arguments(JavaCommentUtil.pageQueryParam(method, domainPkg.getClassName()))
            .build();

    // 1,创建方法定义
    JavaClassCodeUtils.methodDefine(sb, methodEntity);
    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 调用方法进行分页的方法构建
    pageMethodBuilder(sb, method, poPkg, assemblerPkg, domainPkg);

    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 分页查询的方法构建
   *
   * @param sb
   * @param method 方法
   * @param poPkg 存储包
   * @param assemblerPkg 转换类
   * @param domainPkg 领域对象
   */
  private void pageMethodBuilder(
      StringBuilder sb,
      MethodInfo method,
      ImportPackageInfo poPkg,
      ImportPackageInfo assemblerPkg,
      ImportPackageInfo domainPkg) {
    // 记录进入方法的的日志
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaVarName.LOG).append(Symbol.POINT).append(JavaMethodName.LOG_DEBUG);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.QUOTE);
    sb.append(method.getName()).append(Symbol.SPACE).append(JavaVarName.METHOD_PARAM_NAME);
    sb.append(Symbol.SPACE).append(Symbol.BRACE_LEFT).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.QUOTE).append(Symbol.COMMA);
    sb.append(ImportCodePackageKey.PAGE_PARAM.getPackageInfo().getVarName());
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 调用单对象转换方法
    sb.append(JavaFormat.appendTab(2));
    sb.append(poPkg.getClassName()).append(Symbol.SPACE);
    sb.append(JavaVarName.ASSEMBLER_PARSE_NAME).append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(assemblerPkg.getClassName()).append(Symbol.POINT);
    sb.append(JavaMethodName.ASSEMBLER_ENTITY_PERSIST_NAME).append(Symbol.BRACKET_LEFT);
    sb.append(ImportCodePackageKey.PAGE_PARAM.getPackageInfo().getVarName()).append(Symbol.POINT);
    sb.append(JavaVarName.PAGE_GENERATE_DATE_METHOD)
        .append(Symbol.BRACKET_LEFT)
        .append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 进行分页信息的相关设置
    // 进行分页准备
    sb.append(JavaFormat.appendTab(2));
    sb.append(PAGE_SET).append(Symbol.ENTER_LINE);

    // 执行数据的分页查询
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.LIST_TYPE).append(poPkg.getClassName()).append(JavaKeyWord.LIST_TYPE_END);
    sb.append(Symbol.SPACE).append(JavaVarName.PAGE_RESPONSE);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(JavaVarName.SPRING_INSTANCE_NAME);
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.ASSEMBLER_PARSE_NAME).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 方法执行后的日志
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaVarName.LOG).append(Symbol.POINT).append(JavaMethodName.LOG_DEBUG);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.QUOTE);
    sb.append(method.getName()).append(Symbol.SPACE).append(JavaVarName.METHOD_PARAM_NAME);
    sb.append(Symbol.SPACE).append(Symbol.BRACE_LEFT).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.SPACE).append(JavaKeyWord.RETURN);
    sb.append(Symbol.SPACE).append(Symbol.BRACE_LEFT).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.QUOTE).append(Symbol.COMMA);
    sb.append(ImportCodePackageKey.PAGE_PARAM.getPackageInfo().getVarName());
    sb.append(Symbol.COMMA).append(JavaVarName.PAGE_RESPONSE);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 进行的返回结果转换
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.LIST_TYPE)
        .append(domainPkg.getClassName())
        .append(JavaKeyWord.LIST_TYPE_END);
    sb.append(Symbol.SPACE).append(JavaVarName.PAGE_RSP_ASSEMBLER_PARSE);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(assemblerPkg.getClassName()).append(Symbol.POINT);
    sb.append(JavaMethodName.ASSEMBLER_PERSIST_ENTITY_LIST_NAME).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.PAGE_RESPONSE).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 分页结果构建
    pageRspBuilder(sb, domainPkg.getClassName());
  }

  /**
   * 分页结果构建
   *
   * @param sb
   */
  private void pageRspBuilder(StringBuilder sb, String className) {
    // 构建返回对象
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.RETURN).append(Symbol.SPACE);

    sb.append(ImportCodePackageKey.PAGE_RESULT.getPackageInfo().getClassName());
    sb.append(Symbol.POINT);
    sb.append(Symbol.ANGLE_BRACKETS_LEFT);
    sb.append(JavaVarName.NAME_LIST_SUFFIX).append(Symbol.ANGLE_BRACKETS_LEFT);
    sb.append(className).append(Symbol.ANGLE_BRACKETS_RIGHT);
    sb.append(Symbol.ANGLE_BRACKETS_RIGHT);

    sb.append(JavaMethodName.PAGE_BUILDER);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.ENTER_LINE);
    // 当前页设置
    sb.append(JavaFormat.appendTab(10));
    sb.append(Symbol.POINT).append(JavaMethodName.PAGE_NUM);
    sb.append(Symbol.BRACKET_LEFT).append(METHOD_PAGE_RSP_NAME).append(Symbol.POINT);
    sb.append(JavaMethodName.PAGE_HELPER_NUM);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.ENTER_LINE);
    // 当前页大小设置
    sb.append(JavaFormat.appendTab(10));
    sb.append(Symbol.POINT).append(JavaMethodName.PAGE_SIZE);
    sb.append(Symbol.BRACKET_LEFT).append(METHOD_PAGE_RSP_NAME).append(Symbol.POINT);
    sb.append(JavaMethodName.PAGE_HELPER_SIZE);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.ENTER_LINE);
    // 总条数
    sb.append(JavaFormat.appendTab(10));
    sb.append(Symbol.POINT).append(JavaMethodName.PAGE_TOTAL_SIZE);
    sb.append(Symbol.BRACKET_LEFT).append(METHOD_PAGE_RSP_NAME).append(Symbol.POINT);
    sb.append(JavaMethodName.GET);
    sb.append(NameProcess.INSTANCE.toJavaNameFirstUpper(JavaMethodName.PAGE_TOTAL_SIZE));
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.ENTER_LINE);
    // 返回的数据
    sb.append(JavaFormat.appendTab(10));
    sb.append(Symbol.POINT).append(JavaMethodName.PAGE_DATA);
    sb.append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.PAGE_RSP_ASSEMBLER_PARSE);
    sb.append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.ENTER_LINE);

    // 构建方法调用
    sb.append(JavaFormat.appendTab(10));
    sb.append(Symbol.POINT).append(JavaMethodName.PAGE_BUILD);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 方法的定义
   *
   * @param repositoryPersistence 存储层
   * @param repositoryFacade 存储接口
   * @param daoClass 数据库操作接口
   * @param domainEntityPackage 领域实体
   * @param persistAssemblerPackage 转换类
   * @param poPackage 存储实体
   * @param author 作者
   * @param methodList 方法
   * @return 方法的信息
   */
  private StringBuilder persistenceDefine(
      ImportPackageInfo repositoryPersistence,
      ImportPackageInfo repositoryFacade,
      ImportPackageInfo daoClass,
      ImportPackageInfo domainEntityPackage,
      ImportPackageInfo persistAssemblerPackage,
      ImportPackageInfo poPackage,
      List<MethodInfo> methodList,
      String author) {
    List<String> importList = new ArrayList<>(16);

    // 集合
    importList.addAll(IMPORT_PKG);
    // 检查当前是否存在分页方法,当存在分页时，需要导入相关的包
    if (MethodUtils.checkPageQuery(methodList)) {
      // 分页数据包导入
      importList.addAll(PAGE_IMPORT_PKG);
    }
    // 导入实体
    importList.add(domainEntityPackage.packageOut());
    importList.add(poPackage.packageOut());
    // 转换类
    importList.add(persistAssemblerPackage.packageOut());
    // 导入数据库的dao
    importList.add(daoClass.packageOut());
    // 导入接口
    importList.add(repositoryFacade.packageOut());

    JavaClassEntity classEntityInfo =
        JavaClassEntity.builder()
            // 类的关键字
            .classKey(JavaKeyWord.CLASS_KEY)
            // 类名
            .className(repositoryPersistence.getClassName())
            // 类注释
            .classComment(repositoryPersistence.getClassComment())
            // 包类路径信息
            .packagePath(repositoryPersistence.getPackagePath())
            // 导入包信息
            .importList(importList)
            // 注解符
            .annotationList(
                Arrays.asList(
                    CodeAnnotation.REPOSITORY,
                    ImportCodePackageKey.SLF4J_ANNOTATION.getPackageInfo().getAnnotation()))
            // 实现的接口
            .interfaceClass(repositoryFacade.getClassName())
            // 作者
            .author(author)
            .build();

    return JavaClassCodeUtils.javaClassDefine(classEntityInfo);
  }
}
