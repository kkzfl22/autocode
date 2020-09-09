package com.liujun.micro.autocode.generator.builder.operator.ddd.code;

import com.liujun.micro.autocode.config.generate.entity.MethodInfo;
import com.liujun.micro.autocode.constant.MethodTypeEnum;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.CodeAnnotation;
import com.liujun.micro.autocode.generator.builder.constant.CodeComment;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.ImportCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.entity.JavaClassEntity;
import com.liujun.micro.autocode.generator.builder.entity.JavaClassFieldEntity;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodArguments;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodEntity;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.MethodUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.ReturnUtils;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.javalanguage.serivce.JavaFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
      Arrays.asList(
          "com.github.pagehelper.Page",
          "com.github.pagehelper.PageHelper",
          "com.github.pagehelper.PageInfo",
          ImportPackageUtils.packageOut(ImportCodePackageKey.PAGE_PARAM.getPackageInfo()),
          ImportPackageUtils.packageOut(ImportCodePackageKey.PAGE_RESULT.getPackageInfo()));

  /**
   * 生成领域服务方法
   *
   * @param packageMap 导入的包信息
   * @param methodList 方法
   * @param author 作者
   * @return 构建的存储层对象
   */
  public StringBuilder generateDomainService(
      Map<String, ImportPackageInfo> packageMap, List<MethodInfo> methodList, String author) {

    // 领域实体实体
    ImportPackageInfo domainPackageInfo = packageMap.get(GenerateCodePackageKey.DOMAIN_DO.getKey());

    // 领域层的存储接口
    ImportPackageInfo persistFacadePackageInfo =
        packageMap.get(GenerateCodePackageKey.DOMAIN_PERSIST_FACADE.getKey());

    // 领域的服务接口
    ImportPackageInfo domainService =
        packageMap.get(GenerateCodePackageKey.DOMAIN_SERVICE.getKey());

    // 类的声明
    StringBuilder sb =
        this.domainServiceDefine(
            persistFacadePackageInfo, domainPackageInfo, domainService, methodList, author);

    // 1,类属性的创建
    sb.append(
        JavaClassCodeUtils.getClassField(
            JavaClassFieldEntity.getPrivateAutowiredField(
                persistFacadePackageInfo.getClassName(),
                JavaVarName.FACADE_INSTANCE_NAME,
                persistFacadePackageInfo.getClassComment())));

    for (MethodInfo methodItem : methodList) {
      // 1,针对增删除改的方法进行调用
      if (MethodTypeEnum.INSERT.getType().equals(methodItem.getOperator())
          || MethodTypeEnum.UPDATE.getType().equals(methodItem.getOperator())
          || MethodTypeEnum.DELETE.getType().equals(methodItem.getOperator())) {
        this.updateMethod(sb, methodItem, domainPackageInfo);
      }
      // 如果当前为查询分页操作
      else if (MethodTypeEnum.QUERY.getType().equals(methodItem.getOperator())
          && methodItem.getPageQueryFlag() != null
          && methodItem.getPageQueryFlag()) {
        this.pageQueryMethod(sb, methodItem, domainPackageInfo);
      }
      // 如果当前为查询则进行查询调用操作
      else if (MethodTypeEnum.QUERY.getType().equals(methodItem.getOperator())) {
        this.queryMethod(sb, methodItem, domainPackageInfo);
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
   */
  private void updateMethod(StringBuilder sb, MethodInfo method, ImportPackageInfo domainPkg) {
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
    this.dataUpdateInvoke(sb, method);
    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 数据修改的调用
   *
   * @param sb
   * @param method 方法信息
   */
  private void dataUpdateInvoke(StringBuilder sb, MethodInfo method) {
    // 执行数据的插入操作
    sb.append(JavaFormat.appendTab(2)).append(JavaKeyWord.TYPE_BOOLEAN);
    sb.append(Symbol.SPACE).append(JavaVarName.INVOKE_METHOD_UPDATE_RSP);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(JavaVarName.FACADE_INSTANCE_NAME);
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.METHOD_PARAM_NAME).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 返回语句
    sb.append(JavaFormat.appendTab(2)).append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(JavaVarName.INVOKE_METHOD_UPDATE_RSP)
        .append(Symbol.SEMICOLON)
        .append(Symbol.ENTER_LINE);
  }

  /**
   * 数据修改的调用
   *
   * @param sb
   * @param method 方法信息
   * @param domainPkg 领域对象
   */
  private void dataNormalQueryInvoke(
      StringBuilder sb, MethodInfo method, ImportPackageInfo domainPkg) {

    // 检查结果是单对象还是结果集
    boolean rspListFlag = ReturnUtils.checkReturnList(method.getReturns());

    // 如果当前返回多结果集，则调用相关的多结果集方法
    if (rspListFlag) {
      this.queryList(sb, method, domainPkg);
    } else {
      this.queryRspOne(sb, method, domainPkg);
    }
  }

  /**
   * 查询返回结果集的方法
   *
   * @param sb
   * @param method
   * @param domainPkg
   */
  private void queryList(StringBuilder sb, MethodInfo method, ImportPackageInfo domainPkg) {
    // 执行数据的分页查询
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.LIST_TYPE)
        .append(domainPkg.getClassName())
        .append(JavaKeyWord.LIST_TYPE_END);
    sb.append(Symbol.SPACE).append(JavaVarName.QUERY_LIST_RETURN_DATA);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(JavaVarName.FACADE_INSTANCE_NAME);
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.METHOD_PARAM_NAME).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 返回语句
    sb.append(JavaFormat.appendTab(2)).append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(JavaVarName.QUERY_LIST_RETURN_DATA).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 查询返回单结果的方法
   *
   * @param sb
   * @param method
   * @param poPkg
   */
  private void queryRspOne(StringBuilder sb, MethodInfo method, ImportPackageInfo poPkg) {
    // 执行数据的分页查询
    sb.append(JavaFormat.appendTab(2));
    sb.append(poPkg.getClassName()).append(Symbol.SPACE).append(JavaVarName.QUERY_RETURN_DATA);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(JavaVarName.FACADE_INSTANCE_NAME);
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.METHOD_PARAM_NAME).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 返回语句
    sb.append(JavaFormat.appendTab(2)).append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(JavaVarName.QUERY_RETURN_DATA).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 数据查询相关的调用
   *
   * @param sb
   * @param method 方法信息
   * @param domainPkg 领域包信息
   */
  private void queryMethod(StringBuilder sb, MethodInfo method, ImportPackageInfo domainPkg) {

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
            .type(
                JavaClassCodeUtils.getTypeName(method.getReturnType(), domainPkg.getClassName()))
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
    this.dataNormalQueryInvoke(sb, method, domainPkg);
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
  private void pageQueryMethod(StringBuilder sb, MethodInfo method, ImportPackageInfo domainPkg) {

    List<JavaMethodArguments> argumentsList =
        Arrays.asList(
            JavaMethodArguments.builder()
                .type(domainPkg.getClassName())
                .name(JavaVarName.METHOD_PARAM_NAME)
                .comment(domainPkg.getClassComment())
                .build(),
            JavaMethodArguments.parsePackage(
                ImportCodePackageKey.PAGE_PARAM.getPackageInfo(), JavaVarName.PAGE_REQUEST));

    JavaMethodEntity methodEntity =
        JavaMethodEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 方法注释
            .comment(method.getComment())
            // 返回值
            .type(ImportCodePackageKey.PAGE_RESULT.getPackageInfo().getClassName())
            // 分页查询结果
            .returnComment(CodeComment.PAGE_RESPONSE_COMMENT)
            // 方法名
            .name(method.getName())
            // 参数
            .arguments(argumentsList)
            .build();

    // 1,创建方法定义
    JavaClassCodeUtils.methodDefine(sb, methodEntity);
    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 调用方法进行分页的方法构建
    pageMethodBuilder(sb, method);

    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 分页查询的方法构建
   *
   * @param sb
   * @param method 方法

   */
  private void pageMethodBuilder(StringBuilder sb, MethodInfo method) {

    // 执行数据的分页查询
    sb.append(JavaFormat.appendTab(2));
    sb.append(ImportCodePackageKey.PAGE_RESULT.getPackageInfo().getClassName());
    sb.append(Symbol.SPACE).append(JavaVarName.PAGE_RETURN_DATA);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(JavaVarName.FACADE_INSTANCE_NAME);
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.METHOD_PARAM_NAME).append(Symbol.COMMA);
    sb.append(JavaVarName.PAGE_REQUEST).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 返回语句
    sb.append(JavaFormat.appendTab(2)).append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(JavaVarName.PAGE_RETURN_DATA).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);
  }

  /**
   * 方法的定义
   *
   * @param repositoryFacade 领域存储的接口
   * @param domainEntityPackage 领域实体
   * @param domainService 领域服务
   * @param methodList 方法列表
   * @param author 作者
   * @return 构建的类头
   */
  private StringBuilder domainServiceDefine(
      ImportPackageInfo repositoryFacade,
      ImportPackageInfo domainEntityPackage,
      ImportPackageInfo domainService,
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
    importList.add(ImportPackageUtils.packageOut(domainEntityPackage));
    // 领域存储
    importList.add(ImportPackageUtils.packageOut(repositoryFacade));
    // 领域实体
    importList.add(ImportPackageUtils.packageOut(domainEntityPackage));

    // 导入接口
    importList.add(ImportPackageUtils.packageOut(repositoryFacade));

    JavaClassEntity classEntityInfo =
        JavaClassEntity.builder()
            // 类的关键字
            .classKey(JavaKeyWord.CLASS_KEY)
            // 类名
            .className(domainService.getClassName())
            // 类注释
            .classComment(domainService.getClassComment())
            // 包类路径信息
            .packagePath(domainService.getPackagePath())
            // 导入包信息
            .importList(importList)
            // 注解符
            .annotationList(Arrays.asList(CodeAnnotation.SERVICE, CodeAnnotation.SL4J))
            // 作者
            .author(author)
            .build();

    return JavaClassCodeUtils.javaClassDefine(classEntityInfo);
  }
}
