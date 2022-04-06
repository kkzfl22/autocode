package com.liujun.micro.autocode.generator.builder.operator.ddd.custom.view.generatecode;

import com.liujun.micro.autocode.config.generate.entity.MethodInfo;
import com.liujun.micro.autocode.constant.MethodTypeEnum;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.CodeAnnotation;
import com.liujun.micro.autocode.generator.builder.constant.CodeComment;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.ImportCodeJavaPackageKey;
import com.liujun.micro.autocode.generator.builder.constant.ImportCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.JavaMethodName;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarValue;
import com.liujun.micro.autocode.generator.builder.entity.DataParam;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.entity.JavaAnnotation;
import com.liujun.micro.autocode.generator.builder.entity.JavaAnnotationList;
import com.liujun.micro.autocode.generator.builder.entity.JavaClassEntity;
import com.liujun.micro.autocode.generator.builder.entity.JavaClassFieldEntity;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodArguments;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodEntity;
import com.liujun.micro.autocode.generator.builder.operator.ddd.custom.def.facade.GenerateJavaCheck;
import com.liujun.micro.autocode.generator.builder.operator.ddd.custom.view.constant.ImportCodeActionViewPackageKey;
import com.liujun.micro.autocode.generator.builder.operator.ddd.custom.view.constant.JavaMethodViewName;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.MethodUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.ReturnUtils;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.javalanguage.serivce.JavaFormat;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用于生成API接口调用聚合层的实现
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaViewAction {

  /** 实例对象 */
  public static final GenerateJavaViewAction INSTANCE = new GenerateJavaViewAction();

  /** 导入的包信息 */
  private static final List<String> IMPORT_PKG =
      Arrays.asList(
          "lombok.extern.slf4j.Slf4j",
          "org.springframework.beans.factory.annotation.Autowired",
          "org.springframework.stereotype.Service",
          JavaKeyWord.IMPORT_COLLECTIONS,
          JavaKeyWord.IMPORT_LIST,
          ImportCodePackageKey.ANNOTATION_API_MODEL.getPackageInfo().packageOut(),
          ImportCodePackageKey.ANNOTATION_API_MODEL_PROPERTY.getPackageInfo().packageOut(),
          ImportCodePackageKey.SPRING_REST_CONTROLLER.getPackageInfo().packageOut(),
          ImportCodePackageKey.SPRING_REQUEST_MAPPING.getPackageInfo().packageOut(),
          ImportCodePackageKey.ANNOTATION_API.getPackageInfo().packageOut(),
          ImportCodePackageKey.ANNOTATION_API_OPERATION.getPackageInfo().packageOut(),
          ImportCodePackageKey.HTTP_SERVLET_REQUEST.getPackageInfo().packageOut(),
          ImportCodePackageKey.SPRING_REQUEST_METHOD.getPackageInfo().packageOut(),
          ImportCodePackageKey.ERROR_DATA.getPackageInfo().packageOut(),
          ImportCodePackageKey.ERROR_CODE_COMMON.getPackageInfo().packageOut(),
          ImportCodeActionViewPackageKey.API_RESPONSE.getPackageInfo().packageOut(),
          ImportCodeActionViewPackageKey.API_PAGE_RESPONSE.getPackageInfo().packageOut());

  /** 添加 */
  private static final String HTTP_GET = "RequestMethod.GET";

  /** 数据查询，推荐查询参数请求 */
  private static final String HTTP_POST = "RequestMethod.POST";

  /** 分页相关 导入包 */
  private static final List<String> PAGE_IMPORT_PKG =
      Arrays.asList(
          ImportCodePackageKey.PAGE_PARAM.getPackageInfo().packageOut(),
          ImportCodePackageKey.PAGE_RESULT.getPackageInfo().packageOut());

  /** 返回成功的检查 */
  private static final String CODE_SUCCESS = "APICodeEnum.SUCCESS.getErrorData()";

  /** 分页数据转换的对象 */
  private static final String PAGE_ASSEMBLER_NAME = "PageAssembler";

  /** 获取数据 */
  private static final String PAGE_GET_DATA = "getData";

  /** 响应的封装方法 */
  private static final String PAGE_API_RSP_METHOD = "page";

  /**
   * 生成action的领域的操作
   *
   * @param param 参数信息
   * @return 构建的存储层对象
   */
  public StringBuilder generateAction(DataParam param) {

    // 应用服务
    ImportPackageInfo applicationServicePackage =
        param.getPkg(GenerateCodePackageKey.APPLICATION_SERVICE);

    // 类的声明
    StringBuilder sb = GenerateJavaViewAction.INSTANCE.actionServiceDefine(param);

    // 1,类属性的创建
    sb.append(
        JavaClassCodeUtils.getClassField(
            JavaClassFieldEntity.getPrivateAutowiredField(applicationServicePackage)));

    // 检查是否需要执行输出公共分页类对象信息

    // 执行方法的生成
    sb.append(generateMethod(param));

    // 类的结束
    JavaClassCodeUtils.classEnd(sb);
    return sb;
  }


  private String outPageCode(DataParam param) {
    StringBuilder outMsg = new StringBuilder();
    if (this.checkOutPage(param)) {

    }

    return outMsg.toString();
  }




  /**
   * 检查方法是否需要执行输出操作
   *
   * @param param
   * @return
   */
  private boolean checkOutPage(DataParam param) {
    List<MethodInfo> methodList = param.getMethodList();

    for (MethodInfo methodItem : methodList) {
      if (MethodTypeEnum.QUERY_PAGE.getType().equals(methodItem.getOperator())) {
        return true;
      }
    }

    return false;
  }


  /**
   * 方法的生成操作
   *
   * @param param 参数信息
   */
  private String generateMethod(DataParam param) {

    StringBuilder sb = new StringBuilder();

    for (MethodInfo methodItem : param.getMethodList()) {
      // 1,针对增删除改的方法进行调用
      if (MethodTypeEnum.INSERT.getType().equals(methodItem.getOperator())
          || MethodTypeEnum.UPDATE.getType().equals(methodItem.getOperator())
          || MethodTypeEnum.DELETE.getType().equals(methodItem.getOperator())) {
        sb.append(this.updateMethod(param, methodItem, HTTP_POST));
      }
      // 如果当前为查询分页操作
      else if (MethodTypeEnum.QUERY_PAGE.getType().equals(methodItem.getOperator())) {
        sb.append(this.pageQueryMethod(param, methodItem));
      }
      // 如果当前为查询则进行查询调用操作
      else if (MethodTypeEnum.QUERY.getType().equals(methodItem.getOperator())) {
        sb.append(this.queryMethod(param, methodItem));
      }
    }

    return sb.toString();
  }

  /**
   * 数据修改相关的调用
   *
   * @param param 参数信息
   */
  public String updateMethod(DataParam param, MethodInfo method, String httpMethodName) {

    StringBuilder sb = new StringBuilder();

    ImportPackageInfo interfaceObject = param.getPkg(GenerateCodePackageKey.INTERFACE_OBJECT);

    JavaMethodEntity methodEntity =
        JavaMethodEntity.builder()
            // 方法注解
            .annotation(getAnnotation(method, httpMethodName))
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 方法注释
            .comment(method.getComment())
            // 返回值
            .type(
                ImportCodeActionViewPackageKey.API_RESPONSE
                    .getPackageInfo()
                    .getClassNameAndGeneric())
            // 返回注释
            .returnComment(
                ImportCodeActionViewPackageKey.API_RESPONSE.getPackageInfo().getClassComment())
            // 方法名
            .name(method.getName())
            // 批量操作参数
            .arguments(this.batchArgumentList(method, interfaceObject))
            .build();
    // 1,创建方法定义
    JavaClassCodeUtils.methodDefine(sb, methodEntity);
    // 方法开始
    JavaClassCodeUtils.methodStart(sb);

    // 数据添加、修改、删除相关的调用
    sb.append(this.dataUpdateInvoke(method, param));
    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
    return sb.toString();
  }

  /**
   * 获取参数的注解
   *
   * @param method 方法的信息
   * @param httpMethodName http方法名
   */
  private List<String> getAnnotation(MethodInfo method, String httpMethodName) {

    List<String> dataList = new ArrayList<>();

    JavaAnnotation swaggerAnnotation =
        JavaAnnotation.builder()
            // api请求的注解
            .annotation(
                ImportCodePackageKey.ANNOTATION_API_OPERATION.getPackageInfo().getAnnotation())
            .annotationValue(method.getComment())
            .build();

    // 添加swagger的注解
    dataList.add(swaggerAnnotation.outAnnotation());

    // spring的注解,@RequestMapping
    dataList.add(
        JavaAnnotation.builder()
            .annotation(
                ImportCodePackageKey.SPRING_REQUEST_MAPPING.getPackageInfo().getAnnotation())
            .annotationValue(
                CodeAnnotation.SPRING_REQUEST_MAPPING_VALUE, Symbol.PATH + method.getName())
            .annotationValue(true, CodeAnnotation.SPRING_REQUEST_MAPPING_METHOD, httpMethodName)
            .build()
            .outAnnotation());

    return dataList;
  }

  /**
   * 批量参数集合处理
   *
   * @param method 参数方法
   * @param transferPkg 领域实体对象
   * @return 参数集合对象
   */
  private List<JavaMethodArguments> batchArgumentList(
      MethodInfo method, ImportPackageInfo transferPkg) {
    // 检查当前参数是否为集合
    boolean checkBatchFlag = MethodUtils.checkBatch(method.getParamType());

    List<JavaMethodArguments> argumentsList = null;

    // 如果当前批量添加操作
    if (checkBatchFlag) {
      // 集合参数
      JavaMethodArguments dataTransferArgs =
          JavaMethodArguments.builder()
              .type(JavaClassCodeUtils.listType(transferPkg.getClassName()))
              .name(JavaVarName.METHOD_PARAM_NAME)
              .comment(transferPkg.getClassComment())
              .build();

      argumentsList = Arrays.asList(dataTransferArgs);
    }
    // 非批量添加操作
    else {
      JavaAnnotationList annotationList = new JavaAnnotationList();

      JavaAnnotation requestBody =
          JavaAnnotation.builder()
              .annotation(ImportCodePackageKey.SPRING_REQUEST_BODY.getPackageInfo().getAnnotation())
              .build();
      // 添加swagger的注解
      annotationList.add(requestBody);
      String validName =
          NameProcess.INSTANCE.toJavaClassName(method.getName()) + JavaKeyWord.CLASS_SUFFIX;

      JavaAnnotation paramCheckAnnotation =
          JavaAnnotation.builder()
              .annotation(ImportCodePackageKey.SPRING_VALID_PARAM.getPackageInfo().getAnnotation())
              .annotationValue(true, CodeAnnotation.SPRING_REQUEST_MAPPING_VALUE, validName)
              .build();
      // 添加swagger的注解
      annotationList.add(paramCheckAnnotation);

      JavaMethodArguments dataTransferItem =
          JavaMethodArguments.builder()
              .annotation(annotationList.outSpaceAnnotation())
              .type(transferPkg.getClassName())
              .name(JavaVarName.METHOD_PARAM_NAME)
              .comment(transferPkg.getClassComment())
              .build();

      argumentsList = Arrays.asList(dataTransferItem);
    }

    return argumentsList;
  }

  /**
   * 数据修改的调用
   *
   * @param method 方法信息
   * @param param 参数校验
   */
  private String dataUpdateInvoke(MethodInfo method, DataParam param) {
    StringBuilder sb = new StringBuilder();

    // 方法的参数检查
    // sb.append(this.checkParam(method, param));

    // 进行数据的添加、修改、删除的执行
    sb.append(this.invokeData(method, param));

    return sb.toString();
  }

  /**
   * 数据修改的执行
   *
   * @param method 方法
   * @param param 参数信息
   * @return
   */
  private String invokeData(MethodInfo method, DataParam param) {

    ImportPackageInfo domain = param.getPkg(GenerateCodePackageKey.DOMAIN_DO);
    ImportPackageInfo assemble = param.getPkg(GenerateCodePackageKey.INTERFACE_ASSEMBLER);
    ImportPackageInfo application = param.getPkg(GenerateCodePackageKey.APPLICATION_SERVICE);

    StringBuilder sb = new StringBuilder();

    // 默认调用单对象转换
    String invokeMethodName = JavaMethodName.ASSEMBLER_TRANSFER_DOMAIN_NAME;
    // 默认为音
    String resultType = domain.getClassName();

    // 检查当前参数请求是否为集合
    if (MethodUtils.checkBatch(method.getParamType())) {
      invokeMethodName = JavaMethodName.ASSEMBLER_TRANSFER_DOMAIN_LIST_NAME;
      resultType = JavaKeyWord.LIST_TYPE + resultType + JavaKeyWord.LIST_TYPE_END;
    }

    // 进行类型的转换操作
    sb.append(JavaFormat.appendTab(2));
    sb.append(resultType).append(Symbol.SPACE);
    sb.append(domain.getVarName()).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(assemble.getClassName()).append(Symbol.POINT);
    sb.append(invokeMethodName);
    sb.append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.METHOD_PARAM_NAME);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 执行数据的插入操作
    sb.append(JavaFormat.appendTab(2));
    sb.append(application.getVarName());
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(domain.getVarName()).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 填写成功返回
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(ImportCodeActionViewPackageKey.API_RESPONSE_UTILS.getPackageInfo().getClassName());
    sb.append(Symbol.POINT).append(JavaMethodViewName.RESPONSE_SUCCESS);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    return sb.toString();
  }

  /**
   * 参数对象检查的方法生成
   *
   * @param method 方法信息
   * @param param 生成的相关的代码检查信息
   * @return 代码信息
   */
  private String checkParam(MethodInfo method, DataParam param) {

    ImportPackageInfo paramCheckService =
        param.getPkg(GenerateCodePackageKey.INTERFACE_CHECK_PARAM);

    StringBuilder sb = new StringBuilder();
    // 1,执行参数的检查
    sb.append(JavaFormat.appendTab(2));
    // 返回类型
    sb.append(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getClassName());
    sb.append(Symbol.SPACE);
    sb.append(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getVarName());
    sb.append(Symbol.SPACE).append(Symbol.EQUAL).append(Symbol.SPACE);
    // 参数类的名称
    sb.append(paramCheckService.getClassName()).append(Symbol.POINT);
    sb.append(GenerateJavaCheck.getMethodName(method.getName()));
    sb.append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.METHOD_PARAM_NAME);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);
    // 返回类型的检查
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.IF).append(Symbol.BRACKET_LEFT);
    sb.append(CODE_SUCCESS).append(Symbol.SPACE).append(Symbol.EQUAL_NOT);
    sb.append(Symbol.SPACE).append(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getVarName());
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACE_LEFT);
    sb.append(Symbol.ENTER_LINE);

    // 发生错误了，直接返回
    sb.append(JavaFormat.appendTab(3));
    sb.append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(ImportCodeActionViewPackageKey.API_RESPONSE.getPackageInfo().getClassName());
    sb.append(Symbol.POINT).append(JavaMethodName.RESPONSE_FAIL);
    sb.append(Symbol.BRACKET_LEFT);
    sb.append(ImportCodePackageKey.ERROR_DATA.getPackageInfo().getVarName());
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    sb.append(JavaFormat.appendTab(2)).append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE);

    return sb.toString();
  }

  /**
   * 数据修改的调用
   *
   * @param method 方法信息
   * @param param 领域对象
   */
  private String dataNormalQueryInvoke(MethodInfo method, DataParam param) {

    StringBuilder sb = new StringBuilder();

    // 检查结果是单对象还是结果集
    boolean rspListFlag = ReturnUtils.checkReturnList(method.getReturns());

    // 如果当前返回多结果集，则调用相关的多结果集方法
    if (rspListFlag) {
      sb.append(this.queryList(method, param));
    } else {
      sb.append(this.queryRspOne(method, param));
    }

    return sb.toString();
  }

  /**
   * 查询返回结果集的方法
   *
   * @param method
   * @param param
   */
  private String queryList(MethodInfo method, DataParam param) {

    ImportPackageInfo domainPkg = param.getPkg(GenerateCodePackageKey.DOMAIN_DO);
    ImportPackageInfo assembler = param.getPkg(GenerateCodePackageKey.INTERFACE_ASSEMBLER);

    StringBuilder sb = new StringBuilder();

    // 数据对象的转换
    sb.append(JavaFormat.appendTab(2));
    sb.append(domainPkg.getClassName()).append(Symbol.SPACE);
    sb.append(domainPkg.getVarName()).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(assembler.getClassName());
    sb.append(Symbol.POINT).append(JavaMethodName.ASSEMBLER_TRANSFER_DOMAIN_NAME);
    sb.append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.METHOD_PARAM_NAME);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 执行数据的分页查询
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.LIST_TYPE)
        .append(domainPkg.getClassName())
        .append(JavaKeyWord.LIST_TYPE_END);
    sb.append(Symbol.SPACE).append(JavaVarName.QUERY_LIST_RETURN_DATA);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE)
        .append(param.getPkg(GenerateCodePackageKey.APPLICATION_SERVICE).getVarName());
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(domainPkg.getVarName()).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 进行响应的构建
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.IF).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.QUERY_LIST_RETURN_DATA).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL_NOT).append(Symbol.SPACE);
    sb.append(JavaVarValue.VALUE_NULL).append(Symbol.BRACKET_RIGHT).append(Symbol.BRACE_LEFT);
    sb.append(Symbol.ENTER_LINE);

    // 响应单集合对象的构建
    sb.append(JavaFormat.appendTab(3));
    sb.append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(ImportCodeActionViewPackageKey.API_RESPONSE.getPackageInfo().getClassName());
    sb.append(Symbol.POINT).append(JavaMethodName.RESPONSE_SUCCESS);
    sb.append(Symbol.BRACKET_LEFT);
    sb.append(assembler.getClassName()).append(Symbol.POINT);
    sb.append(JavaMethodName.ASSEMBLER_DOMAIN_TRANSFER_LIST_NAME);
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.QUERY_LIST_RETURN_DATA);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // else判断
    sb.append(JavaFormat.appendTab(2));
    sb.append(Symbol.BRACE_RIGHT).append(Symbol.SPACE);
    sb.append(JavaKeyWord.ELSE).append(Symbol.SPACE).append(Symbol.BRACE_LEFT);
    sb.append(Symbol.ENTER_LINE);

    // 响应空对象
    sb.append(JavaFormat.appendTab(3));
    sb.append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(ImportCodeActionViewPackageKey.API_RESPONSE.getPackageInfo().getClassName());
    sb.append(Symbol.POINT).append(JavaMethodName.RESPONSE_SUCCESS);
    sb.append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarValue.LIST_EMPTY_DEFAULT);
    sb.append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    sb.append(JavaFormat.appendTab(2));
    sb.append(Symbol.BRACE_RIGHT).append(Symbol.ENTER_LINE);

    return sb.toString();
  }

  /**
   * 查询返回单结果的方法
   *
   * @param method
   * @param param
   */
  private String queryRspOne(MethodInfo method, DataParam param) {

    ImportPackageInfo domainPkg = param.getPkg(GenerateCodePackageKey.DOMAIN_DO);
    ImportPackageInfo assembler = param.getPkg(GenerateCodePackageKey.INTERFACE_ASSEMBLER);

    StringBuilder sb = new StringBuilder();

    // 数据对象的转换
    sb.append(JavaFormat.appendTab(2));
    sb.append(domainPkg.getClassName()).append(Symbol.SPACE);
    sb.append(domainPkg.getVarName()).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(assembler.getClassName());
    sb.append(Symbol.POINT).append(JavaMethodName.ASSEMBLER_TRANSFER_DOMAIN_NAME);
    sb.append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.METHOD_PARAM_NAME);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 执行数据的查询
    sb.append(JavaFormat.appendTab(2));

    sb.append(domainPkg.getClassName()).append(Symbol.SPACE).append(JavaVarName.QUERY_RETURN_DATA);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE)
        .append(param.getPkg(GenerateCodePackageKey.APPLICATION_SERVICE).getVarName());
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(domainPkg.getVarName()).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 进行响应的构建
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.IF).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.QUERY_RETURN_DATA).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL_NOT).append(Symbol.SPACE);
    sb.append(JavaVarValue.VALUE_NULL).append(Symbol.BRACKET_RIGHT).append(Symbol.BRACE_LEFT);
    sb.append(Symbol.ENTER_LINE);

    // 响应单对象的构建
    sb.append(JavaFormat.appendTab(3));
    sb.append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(ImportCodeActionViewPackageKey.API_RESPONSE.getPackageInfo().getClassName());
    sb.append(Symbol.POINT).append(JavaMethodName.RESPONSE_SUCCESS);
    sb.append(Symbol.BRACKET_LEFT);
    sb.append(assembler.getClassName())
        .append(Symbol.POINT)
        .append(JavaMethodName.ASSEMBLER_DOMAIN_TRANSFER_NAME);
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.QUERY_RETURN_DATA);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // else判断
    sb.append(JavaFormat.appendTab(2));
    sb.append(Symbol.BRACE_RIGHT).append(Symbol.SPACE);
    sb.append(JavaKeyWord.ELSE).append(Symbol.SPACE).append(Symbol.BRACE_LEFT);
    sb.append(Symbol.ENTER_LINE);

    // 响应空对象
    sb.append(JavaFormat.appendTab(3));
    sb.append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(ImportCodeActionViewPackageKey.API_RESPONSE.getPackageInfo().getClassName());
    sb.append(Symbol.POINT).append(JavaMethodName.RESPONSE_SUCCESS);
    sb.append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarValue.MAP_EMPTY);
    sb.append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    sb.append(JavaFormat.appendTab(2));
    sb.append(Symbol.BRACE_RIGHT).append(Symbol.ENTER_LINE);

    return sb.toString();
  }

  /**
   * 数据查询相关的调用
   *
   * @param param
   * @param method 方法信息
   */
  public String queryMethod(DataParam param, MethodInfo method) {

    StringBuilder sb = new StringBuilder();

    ImportPackageInfo dtoPkg = param.getPkg(GenerateCodePackageKey.INTERFACE_OBJECT);

    List<JavaMethodArguments> argumentsList =
        Arrays.asList(
            JavaMethodArguments.builder()
                .type(dtoPkg.getClassName())
                .name(dtoPkg.getVarName())
                .comment(dtoPkg.getClassComment())
                .build());

    JavaMethodEntity methodEntity =
        JavaMethodEntity.builder()
            // 注解
            .annotation(getAnnotation(method, HTTP_POST))
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 返回注释
            .comment(method.getComment())
            // 返回值
            .type(ImportCodeActionViewPackageKey.API_RESPONSE.getPackageInfo().getClassName())
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

    // 参数检查
    sb.append(this.checkParam(method, param));

    // 数据查询相关的调用
    sb.append(this.dataNormalQueryInvoke(method, param));
    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);

    return sb.toString();
  }

  /**
   * 分页相关的方法的调用
   *
   * @param param 领域包信息
   * @param method 方法信息
   */
  public String pageQueryMethod(DataParam param, MethodInfo method) {

    StringBuilder sb = new StringBuilder();

    List<JavaMethodArguments> argumentsList =
        Arrays.asList(
            JavaMethodArguments.builder()
                .annotation(
                    ImportCodePackageKey.SPRING_REQUEST_BODY.getPackageInfo().getAnnotation())
                .type(
                    ImportCodeActionViewPackageKey.API_QUERY_FILE_PARAM
                        .getPackageInfo()
                        .getClassName())
                .name(
                    ImportCodeActionViewPackageKey.API_QUERY_FILE_PARAM
                        .getPackageInfo()
                        .getVarName())
                .comment(
                    ImportCodeActionViewPackageKey.API_QUERY_FILE_PARAM
                        .getPackageInfo()
                        .getClassComment())
                .build());

    JavaMethodEntity methodEntity =
        JavaMethodEntity.builder()
            // 注解
            .annotation(getAnnotation(method, HTTP_POST))
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 方法注释
            .comment(method.getComment())
            // 返回值
            .type(
                ImportCodeActionViewPackageKey.API_PAGE_RESULT_RESPONSE
                    .getPackageInfo()
                    .getClassNameAndGeneric())
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
    sb.append(pageMethodBuilder(method, param));

    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);

    return sb.toString();
  }

  /**
   * 分页查询的方法构建
   *
   * @param method 方法
   * @param param 参数信息
   */
  private String pageMethodBuilder(MethodInfo method, DataParam param) {

    StringBuilder sb = new StringBuilder();

    // 方法的参数检查
    // sb.append(this.checkParam(method, param));

    // 调用分页的相关的查询
    sb.append(this.pageInvoke(method, param));
    return sb.toString();
  }

  /**
   * 分页查询的分页方法的调用
   *
   * @return
   */
  private String pageInvoke(MethodInfo method, DataParam param) {

    ImportPackageInfo domainPkg = param.getPkg(GenerateCodePackageKey.DOMAIN_DO);
    ImportPackageInfo assembler = param.getPkg(GenerateCodePackageKey.INTERFACE_ASSEMBLER);

    StringBuilder sb = new StringBuilder();
    // 添加分页对象的转换
    sb.append(JavaFormat.appendTab(2));

    sb.append(ImportCodePackageKey.PAGE_PARAM.getPackageInfo().getClassName()).append(Symbol.SPACE);
    sb.append(JavaVarName.QUERY_PAGE_PARAM_VAR).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(PAGE_ASSEMBLER_NAME).append(Symbol.POINT);
    sb.append(JavaMethodName.ASSEMBLER_TRANSFER_DOMAIN_NAME);
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.METHOD_PARAM_NAME);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 数据对象的转换
    sb.append(JavaFormat.appendTab(2));
    sb.append(domainPkg.getClassName()).append(Symbol.SPACE);
    sb.append(domainPkg.getVarName()).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(assembler.getClassName());
    sb.append(Symbol.POINT).append(JavaMethodName.ASSEMBLER_TRANSFER_DOMAIN_NAME);
    sb.append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.METHOD_PARAM_NAME).append(Symbol.POINT).append(PAGE_GET_DATA);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 执行数据的分页查询
    sb.append(JavaFormat.appendTab(2));

    sb.append(ImportCodePackageKey.PAGE_PARAM.getPackageInfo().getClassName()).append(Symbol.SPACE);
    sb.append(Symbol.SPACE).append(JavaVarName.PAGE_RETURN_DATA);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE)
        .append(param.getPkg(GenerateCodePackageKey.APPLICATION_SERVICE).getVarName());
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(domainPkg.getVarName()).append(Symbol.COMMA);
    sb.append(JavaVarName.QUERY_PAGE_PARAM_VAR).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 返回语句
    sb.append(JavaFormat.appendTab(2)).append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(ImportCodeActionViewPackageKey.API_PAGE_RESPONSE.getPackageInfo().getClassName());
    sb.append(Symbol.POINT).append(PAGE_API_RSP_METHOD);
    sb.append(Symbol.BRACKET_LEFT)
        .append(JavaVarName.PAGE_RETURN_DATA)
        .append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    return sb.toString();
  }

  private String dataParse(ImportPackageInfo assembler) {
    StringBuilder sb = new StringBuilder();
    // 返回数据转换
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.IF).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.PAGE_RETURN_DATA).append(Symbol.POINT);
    sb.append(JavaMethodName.GET);
    sb.append(NameProcess.INSTANCE.toJavaNameFirstUpper(JavaMethodName.PAGE_DATA));
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SPACE)
        .append(Symbol.EQUAL_NOT)
        .append(JavaVarValue.VALUE_NULL)
        .append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SPACE).append(Symbol.BRACE_LEFT);
    sb.append(Symbol.ENTER_LINE);
    // 数数转换
    sb.append(JavaFormat.appendTab(3));
    sb.append(JavaVarName.PAGE_RETURN_DATA).append(Symbol.POINT);
    sb.append(JavaMethodName.SET);
    sb.append(NameProcess.INSTANCE.toJavaNameFirstUpper(JavaMethodName.PAGE_DATA));
    sb.append(Symbol.BRACKET_LEFT);
    sb.append(assembler.getClassName()).append(Symbol.POINT);
    sb.append(JavaMethodName.ASSEMBLER_DOMAIN_TRANSFER_LIST_NAME).append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.PAGE_RETURN_DATA).append(Symbol.POINT);
    sb.append(JavaMethodName.GET);
    sb.append(NameProcess.INSTANCE.toJavaNameFirstUpper(JavaMethodName.PAGE_DATA));
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON);
    sb.append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 判断结果
    sb.append(JavaFormat.appendTab(2));
    sb.append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE);

    // 为空，则设置一个空对象
    sb.append(JavaFormat.appendTab(2));
    sb.append(JavaKeyWord.ELSE).append(Symbol.BRACE_LEFT);
    sb.append(Symbol.ENTER_LINE);

    // 数数转换
    sb.append(JavaFormat.appendTab(3));
    sb.append(JavaVarName.PAGE_RETURN_DATA).append(Symbol.POINT);
    sb.append(JavaMethodName.SET);
    sb.append(NameProcess.INSTANCE.toJavaNameFirstUpper(JavaMethodName.PAGE_DATA));
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarValue.LIST_EMPTY_DEFAULT);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    sb.append(JavaFormat.appendTab(2));
    sb.append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE);

    return sb.toString();
  }

  /**
   * 方法的定义
   *
   * @param param 参数信息
   * @return 构建的类头
   */
  public StringBuilder actionServiceDefine(DataParam param) {

    // 参数较验的类名信息
    ImportPackageInfo checkParam = param.getPkg(GenerateCodePackageKey.INTERFACE_CHECK_PARAM);

    // API的服务接口
    ImportPackageInfo apiService = param.getPkg(GenerateCodePackageKey.INTERFACE_FACADE);

    // api接口层的实体
    ImportPackageInfo interfaceObject = param.getPkg(GenerateCodePackageKey.INTERFACE_OBJECT);

    // assembler转换类
    ImportPackageInfo assembler = param.getPkg(GenerateCodePackageKey.INTERFACE_ASSEMBLER);

    List<String> importList = new ArrayList<>(16);

    // 集合
    importList.addAll(IMPORT_PKG);
    // 检查当前是否存在分页方法,当存在分页时，需要导入相关的包
    if (MethodUtils.checkPageQuery(param.getMethodList())) {
      // 分页数据包导入
      importList.addAll(PAGE_IMPORT_PKG);
    }
    // 导入实体
    importList.add(param.getPkg(GenerateCodePackageKey.DOMAIN_DO).packageOut());
    // 领域存储
    importList.add(param.getPkg(GenerateCodePackageKey.APPLICATION_SERVICE).packageOut());
    // 导入验证类
    importList.add(checkParam.packageOut());
    // 导入传输层的对象
    importList.add(interfaceObject.packageOut());
    // 导入转换类
    importList.add(assembler.packageOut());
    // 导入control注解
    importList.add(ImportCodePackageKey.SPRING_REST_CONTROLLER.getPackageInfo().packageOut());
    // 请求的映射
    importList.add(ImportCodePackageKey.SPRING_REQUEST_MAPPING.getPackageInfo().packageOut());
    // spring验证标签
    importList.add(ImportCodePackageKey.SPRING_REQUEST_BODY.getPackageInfo().packageOut());
    importList.add(ImportCodePackageKey.SPRING_VALID_PARAM.getPackageInfo().packageOut());
    importList.add(ImportCodeActionViewPackageKey.API_RESPONSE_UTILS.getPackageInfo().packageOut());
    // 导入map
    importList.add(ImportCodeJavaPackageKey.MAP.getPackageInfo().packageOut());

    // 导入参数类信息
    List<MethodInfo> methodList = param.getMethodList();
    for (MethodInfo methodInfo : methodList) {
      ImportPackageInfo importItem = param.getPkg(methodInfo.getName());
      if (null != importItem) {
        importList.add(importItem.packageOut());
      }
    }

    JavaClassEntity classEntityInfo =
        JavaClassEntity.builder()
            // 注解
            .annotationList(annotation(apiService))
            // 类的关键字
            .classKey(JavaKeyWord.CLASS_KEY)
            // 类名
            .className(apiService.getClassName())
            // 类注释
            .classComment(apiService.getClassComment())
            // 包类路径信息
            .packagePath(apiService.getPackagePath())
            // 导入包信息
            .importList(importList)
            // 作者
            .author(param.getAuthor())
            .build();

    return JavaClassCodeUtils.javaClassDefine(classEntityInfo);
  }

  /**
   * 获取注解信息
   *
   * @param interfaceFacade
   * @return
   */
  private List<String> annotation(ImportPackageInfo interfaceFacade) {
    // 数据加载操作
    JavaAnnotation annotationOuts =
        JavaAnnotation.builder().annotation(CodeAnnotation.SPRING_CONTROLLER).build();
    // request的mapping操作
    String outApiUrl =
        Symbol.PATH + NameProcess.INSTANCE.toJavaNameFirstLower(interfaceFacade.getClassName());
    // 请求注解
    JavaAnnotation requestMapping =
        JavaAnnotation.builder()
            .annotation(CodeAnnotation.SPRING_REQUEST_MAPPING)
            .annotationValue(outApiUrl)
            .build();

    // 进行API注解的相关信息

    // APi操作
    JavaAnnotation api =
        JavaAnnotation.builder()
            .annotation(CodeAnnotation.SWAGGER_API)
            .annotationValue(CodeAnnotation.SWAGGER_API_TAGS, interfaceFacade.getClassComment())
            .annotationValue(CodeAnnotation.SWAGGER_API_VALUE, interfaceFacade.getClassComment())
            .build();

    List<String> outAnnotationList = new ArrayList<>();
    outAnnotationList.add(annotationOuts.outAnnotation());
    outAnnotationList.add(requestMapping.outAnnotation());
    outAnnotationList.add(api.outAnnotation());

    return outAnnotationList;
  }
}
