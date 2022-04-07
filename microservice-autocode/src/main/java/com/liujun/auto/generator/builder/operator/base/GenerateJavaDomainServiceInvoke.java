package com.liujun.auto.generator.builder.operator.base;

import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.constant.MethodTypeEnum;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.constant.CodeComment;
import com.liujun.auto.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.constant.ImportCodePackageKey;
import com.liujun.auto.generator.builder.constant.JavaMethodName;
import com.liujun.auto.generator.builder.constant.JavaVarName;
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
import com.liujun.auto.generator.builder.operator.utils.TableColumnUtils;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.serivce.JavaFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 领域服务的调用
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaDomainServiceInvoke {

  /** 实例对象 */
  public static final GenerateJavaDomainServiceInvoke INSTANCE =
      new GenerateJavaDomainServiceInvoke();

  /** 导入的包信息 */
  private static final List<String> IMPORT_PKG =
      Arrays.asList(
          "lombok.extern.slf4j.Slf4j",
          "org.springframework.beans.factory.annotation.Autowired",
          "org.springframework.stereotype.Service",
          JavaKeyWord.IMPORT_LIST);

  /** 分页相关 导入包 */
  private static final List<String> PAGE_IMPORT_PKG =
      Arrays.asList(ImportCodePackageKey.PAGE_PARAM.getPackageInfo().packageOut());

  /**
   * 生成领域服务方法
   *
   * @param param 参数信息
   * @return 构建的存储层对象
   */
  public StringBuilder generateDomainService(DataParam param) {

    // 领域实体实体
    ImportPackageInfo domainPackageInfo = param.getPkg(GenerateCodePackageKey.DOMAIN_DO);

    // 领域层的存储接口
    ImportPackageInfo persistFacadePkg = param.getPkg(GenerateCodePackageKey.DOMAIN_PERSIST_FACADE);

    // 类的声明
    StringBuilder sb = this.domainServiceDefine(param);

    // 1,类属性的创建
    sb.append(
        JavaClassCodeUtils.getClassField(
            JavaClassFieldEntity.getPrivateAutowiredField(
                persistFacadePkg.getClassName(),
                JavaVarName.REPOSITORY_INSTANCE_NAME,
                persistFacadePkg.getClassComment())));

    // 检查当前是否需要进行代码的生成操作
    if (TableColumnUtils.primaryKeyUid(param.getColumnList(), param.getTypeEnum())) {
      // 生成id的引入
      sb.append(
          JavaClassCodeUtils.getClassField(
              JavaClassFieldEntity.getPrivateAutowiredField(
                  ImportCodePackageKey.DOMAIN_ID_GENERATE.getPackageInfo())));
    }

    for (MethodInfo methodItem : param.getMethodList()) {

      // 添加数据的方法
      if (MethodTypeEnum.INSERT.getType().equals(methodItem.getOperator())) {
        sb.append(this.insertMethod(param, methodItem, JavaVarName.REPOSITORY_INSTANCE_NAME));
      }
      // 1,针对增删除改的方法进行调用
      else if (MethodTypeEnum.UPDATE.getType().equals(methodItem.getOperator())
          || MethodTypeEnum.DELETE.getType().equals(methodItem.getOperator())) {
        this.updateMethod(sb, methodItem, domainPackageInfo, JavaVarName.REPOSITORY_INSTANCE_NAME);
      }
      // 如果当前为查询分页操作
      else if (MethodTypeEnum.QUERY_PAGE.getType().equals(methodItem.getOperator())) {
        this.pageQueryMethod(
            sb, methodItem, domainPackageInfo, JavaVarName.REPOSITORY_INSTANCE_NAME);
      }
      // 如果当前为查询则进行查询调用操作
      else if (MethodTypeEnum.QUERY.getType().equals(methodItem.getOperator())) {
        this.queryMethod(sb, methodItem, domainPackageInfo, JavaVarName.REPOSITORY_INSTANCE_NAME);
      }
    }

    // 类的结束
    JavaClassCodeUtils.classEnd(sb);
    return sb;
  }

  /**
   * 数据修改相关的调用
   *
   * @param codeParam 参数信息
   * @param method 方法信息
   * @param instanceName 实例对象的调用名称
   */
  public String insertMethod(DataParam codeParam, MethodInfo method, String instanceName) {
    StringBuilder sb = new StringBuilder();
    // 检查当前参数是否为集合
    boolean checkBatchFlag = MethodUtils.checkBatch(method.getParamType());

    // 如果当前批量添加操作
    if (checkBatchFlag) {
      List<JavaMethodArguments> argumentsList =
          Arrays.asList(
              JavaMethodArguments.builder()
                  .type(
                      JavaClassCodeUtils.listType(
                          codeParam.getPkg(GenerateCodePackageKey.DOMAIN_DO).getClassName()))
                  .name(JavaVarName.METHOD_PARAM_NAME)
                  .comment(codeParam.getPkg(GenerateCodePackageKey.DOMAIN_DO).getClassComment())
                  .build());

      JavaMethodEntity methodEntity =
          JavaMethodEntity.builder()
              // 访问修饰符
              .visit(JavaKeyWord.PUBLIC)
              // 方法注释
              .comment(method.getComment())
              // 返回值
              .type(JavaKeyWord.TYPE_BOOLEAN)
              // 返回注释
              .returnComment(CodeComment.DOMAIN_UPDATE_COMMENT)
              // 方法名
              .name(method.getName())
              // 参数
              .arguments(argumentsList)
              .build();
      // 1,创建方法定义
      JavaClassCodeUtils.methodDefine(sb, methodEntity);
      // 方法开始
      JavaClassCodeUtils.methodStart(sb);

      // 批量添加
      sb.append(this.dataBatchInsertInvoke(codeParam, method, instanceName));
      // 方法结束
      JavaClassCodeUtils.methodEnd(sb);

      return sb.toString();

    }
    // 非批量添加操作
    else {
      List<JavaMethodArguments> argumentsList =
          Arrays.asList(
              JavaMethodArguments.builder()
                  .type(codeParam.getPkg(GenerateCodePackageKey.DOMAIN_DO).getClassName())
                  .name(JavaVarName.METHOD_PARAM_NAME)
                  .comment(codeParam.getPkg(GenerateCodePackageKey.DOMAIN_DO).getClassComment())
                  .build());

      JavaMethodEntity methodEntity =
          JavaMethodEntity.builder()
              // 访问修饰符
              .visit(JavaKeyWord.PUBLIC)
              // 方法注释
              .comment(method.getComment())
              // 返回值
              .type(JavaKeyWord.TYPE_BOOLEAN)
              // 返回注释
              .returnComment(CodeComment.DOMAIN_UPDATE_COMMENT)
              // 方法名
              .name(method.getName())
              // 参数
              .arguments(argumentsList)
              .build();
      // 1,创建方法定义
      JavaClassCodeUtils.methodDefine(sb, methodEntity);
      // 方法开始
      JavaClassCodeUtils.methodStart(sb);

      // 数据添加、修改、删除相关的调用
      sb.append(this.dataInsertInvoke(codeParam, method, instanceName));
      // 方法结束
      JavaClassCodeUtils.methodEnd(sb);

      return sb.toString();
    }
  }

  /**
   * 数据修改相关的调用
   *
   * @param sb
   * @param method 方法信息
   * @param domainPkg 领域包信息
   * @param instanceName 实例对象的调用名称
   */
  public void updateMethod(
      StringBuilder sb, MethodInfo method, ImportPackageInfo domainPkg, String instanceName) {
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
                  .comment(domainPkg.getClassComment())
                  .build());
    }
    // 非批量添加操作
    else {
      argumentsList =
          Arrays.asList(
              JavaMethodArguments.builder()
                  .type(domainPkg.getClassName())
                  .name(JavaVarName.METHOD_PARAM_NAME)
                  .comment(domainPkg.getClassComment())
                  .build());
    }

    JavaMethodEntity methodEntity =
        JavaMethodEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 方法注释
            .comment(method.getComment())
            // 返回值
            .type(JavaKeyWord.TYPE_BOOLEAN)
            // 返回注释
            .returnComment(CodeComment.DOMAIN_UPDATE_COMMENT)
            // 方法名
            .name(method.getName())
            // 参数
            .arguments(argumentsList)
            .build();
    // 1,创建方法定义
    JavaClassCodeUtils.methodDefine(sb, methodEntity);
    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 数据添加、修改、删除相关的调用
    sb.append(this.dataUpdateInvoke(method, instanceName));
    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 数据修改的调用
   *
   * @param codeParam 参数信息
   * @param method 方法信息
   * @param instanceName 实例信息
   */
  private String dataBatchInsertInvoke(
      DataParam codeParam, MethodInfo method, String instanceName) {
    StringBuilder sb = new StringBuilder();

    // 1,检查当前是否存在需要添加生成id的情况
    if (TableColumnUtils.primaryKeyUid(codeParam.getColumnList(), codeParam.getTypeEnum())) {
      // 执行数据的插入操作
      sb.append(JavaFormat.appendTab(2));
      sb.append(codeParam.getPkg(GenerateCodePackageKey.DOMAIN_DO).getClassName())
          .append(Symbol.POINT);
      sb.append(JavaMethodName.DOMAIN_BATCH_INSERT_UID);
      sb.append(Symbol.BRACKET_LEFT);
      sb.append(ImportCodePackageKey.DOMAIN_ID_GENERATE.getPackageInfo().getVarName());
      sb.append(Symbol.COMMA);
      sb.append(Symbol.SPACE).append(JavaVarName.METHOD_PARAM_NAME);
      sb.append(Symbol.BRACKET_RIGHT);
      sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    }

    // 执行数据的插入操作
    sb.append(JavaFormat.appendTab(2)).append(JavaKeyWord.RETURN);
    sb.append(Symbol.SPACE).append(instanceName);
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.METHOD_PARAM_NAME).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    return sb.toString();
  }

  /**
   * 数据修改的调用
   *
   * @param codeParam 参数信息
   * @param method 方法信息
   * @param instanceName 实例信息
   */
  private String dataInsertInvoke(DataParam codeParam, MethodInfo method, String instanceName) {
    StringBuilder sb = new StringBuilder();

    // 1,检查当前是否存在需要添加生成id的情况
    if (TableColumnUtils.primaryKeyUid(codeParam.getColumnList(), codeParam.getTypeEnum())) {
      // 执行数据的插入操作
      sb.append(JavaFormat.appendTab(2));
      sb.append(JavaVarName.METHOD_PARAM_NAME)
          .append(Symbol.POINT)
          .append(JavaMethodName.DOMAIN_INSERT_UID);
      sb.append(Symbol.BRACKET_LEFT);
      sb.append(ImportCodePackageKey.DOMAIN_ID_GENERATE.getPackageInfo().getVarName())
          .append(Symbol.BRACKET_RIGHT);
      sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    }

    // 执行数据的插入操作
    sb.append(JavaFormat.appendTab(2)).append(JavaKeyWord.RETURN);
    sb.append(Symbol.SPACE).append(instanceName);
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.METHOD_PARAM_NAME).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    return sb.toString();
  }

  /**
   * 数据修改的调用
   *
   * @param method 方法信息
   * @param instanceName 实例信息
   */
  private String dataUpdateInvoke(MethodInfo method, String instanceName) {
    StringBuilder sb = new StringBuilder();

    // 执行数据的插入操作
    sb.append(JavaFormat.appendTab(2)).append(JavaKeyWord.RETURN);
    sb.append(Symbol.SPACE).append(instanceName);
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.METHOD_PARAM_NAME).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    return sb.toString();
  }

  /**
   * 数据修改的调用
   *
   * @param sb
   * @param method 方法信息
   * @param domainPkg 领域对象
   */
  private void dataNormalQueryInvoke(
      StringBuilder sb, MethodInfo method, ImportPackageInfo domainPkg, String instanceName) {

    // 检查结果是单对象还是结果集
    boolean rspListFlag = ReturnUtils.checkReturnList(method.getReturns());

    // 如果当前返回多结果集，则调用相关的多结果集方法
    if (rspListFlag) {
      this.queryList(sb, method, domainPkg, instanceName);
    } else {
      this.queryRspOne(sb, method, domainPkg, instanceName);
    }
  }

  /**
   * 查询返回结果集的方法
   *
   * @param sb
   * @param method
   * @param domainPkg
   */
  private void queryList(
      StringBuilder sb, MethodInfo method, ImportPackageInfo domainPkg, String instanceName) {
    // 执行数据的分页查询
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.RETURN);
    sb.append(Symbol.SPACE).append(instanceName);
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.METHOD_PARAM_NAME).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
  }

  /**
   * 查询返回单结果的方法
   *
   * @param sb
   * @param method
   * @param poPkg
   */
  private void queryRspOne(
      StringBuilder sb, MethodInfo method, ImportPackageInfo poPkg, String instanceName) {
    // 执行数据的分页查询
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.RETURN);
    sb.append(Symbol.SPACE).append(instanceName);
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.METHOD_PARAM_NAME).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
  }

  /**
   * 数据查询相关的调用
   *
   * @param sb
   * @param method 方法信息
   * @param domainPkg 领域包信息
   */
  public void queryMethod(
      StringBuilder sb, MethodInfo method, ImportPackageInfo domainPkg, String instanceName) {

    List<JavaMethodArguments> argumentsList =
        Arrays.asList(
            JavaMethodArguments.builder()
                .type(domainPkg.getClassName())
                .name(JavaVarName.METHOD_PARAM_NAME)
                .comment(domainPkg.getClassComment())
                .build());

    JavaMethodEntity methodEntity =
        JavaMethodEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 返回注释
            .comment(method.getComment())
            // 返回值
            .type(JavaClassCodeUtils.getTypeName(method.getReturnType(), domainPkg.getClassName()))
            // 返回注释
            .returnComment(CodeComment.JUNIT_PARSE_LIST_COMMENT)
            // 方法名
            .name(method.getName())
            // 参数
            .arguments(argumentsList)
            .build();

    // 1,创建方法定义
    JavaClassCodeUtils.methodDefine(sb, methodEntity);
    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 数据查询相关的调用
    this.dataNormalQueryInvoke(sb, method, domainPkg, instanceName);
    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 分页相关的方法的调用
   *
   * @param sb
   * @param method 方法信息
   * @param domainPkg 领域包信息
   */
  public void pageQueryMethod(
      StringBuilder sb, MethodInfo method, ImportPackageInfo domainPkg, String instanceName) {

    JavaMethodEntity methodEntity =
        JavaMethodEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 方法注释
            .comment(method.getComment())
            // 返回值
            .type(JavaCommentUtil.pageQueryReturn(domainPkg.getClassName()))
            // 分页查询结果
            .returnComment(CodeComment.PAGE_RESPONSE_COMMENT)
            // 方法名
            .name(method.getName())
            // 参数
            .arguments(JavaCommentUtil.pageQueryParam(method, domainPkg.getClassName()))
            .build();

    // 1,创建方法定义
    JavaClassCodeUtils.methodDefine(sb, methodEntity);
    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 调用方法进行分页的方法构建
    pageMethodBuilder(sb, method, instanceName, domainPkg.getClassName());

    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 分页查询的方法构建
   *
   * @param sb
   * @param method 方法
   */
  private void pageMethodBuilder(
      StringBuilder sb, MethodInfo method, String instanceName, String classType) {
    // 执行数据的分页查询
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.RETURN);
    sb.append(Symbol.SPACE).append(instanceName);
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.PAGE_REQUEST).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
  }

  /**
   * 方法的定义
   *
   * @param codeParam 参数信息
   * @return 构建的类头
   */
  public StringBuilder domainServiceDefine(DataParam codeParam) {
    List<String> importList = new ArrayList<>(16);

    // 集合
    importList.addAll(IMPORT_PKG);
    // 检查当前是否存在分页方法,当存在分页时，需要导入相关的包
    if (MethodUtils.checkPageQuery(codeParam.getMethodList())) {
      // 分页数据包导入
      importList.addAll(PAGE_IMPORT_PKG);
    }

    // 检查当前是否存在需要使用id生成的代码
    if (TableColumnUtils.primaryKeyUid(codeParam.getColumnList(), codeParam.getTypeEnum())) {
      importList.add(ImportCodePackageKey.DOMAIN_ID_GENERATE.getPackageInfo().packageOut());
      importList.add(ImportCodePackageKey.SPRING_BOOT_QUALIFIER.getPackageInfo().packageOut());
    }

    // 导入领域实体
    importList.add(codeParam.getPkg(GenerateCodePackageKey.DOMAIN_DO).packageOut());
    // 领域存储接口导入
    importList.add(codeParam.getPkg(GenerateCodePackageKey.DOMAIN_PERSIST_FACADE).packageOut());

    JavaClassEntity classEntityInfo =
        JavaClassEntity.builder()
            // 类的关键字
            .classKey(JavaKeyWord.CLASS_KEY)
            // 类名
            .className(codeParam.getPkg(GenerateCodePackageKey.DOMAIN_SERVICE).getClassName())
            // 类注释
            .classComment(codeParam.getPkg(GenerateCodePackageKey.DOMAIN_SERVICE).getClassComment())
            // 包类路径信息
            .packagePath(codeParam.getPkg(GenerateCodePackageKey.DOMAIN_SERVICE).getPackagePath())
            // 导入包信息
            .importList(importList)
            // 注解符
            .annotationList(
                Arrays.asList(
                    ImportCodePackageKey.SPRING_SERVICE_ANNOTATION.getPackageInfo().getAnnotation(),
                    ImportCodePackageKey.SLF4J_ANNOTATION.getPackageInfo().getAnnotation()))
            // 作者
            .author(codeParam.getAuthor())
            .build();

    return JavaClassCodeUtils.javaClassDefine(classEntityInfo);
  }
}
