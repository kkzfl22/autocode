package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.config.generate.entity.MethodInfo;
import com.liujun.micro.autocode.constant.MethodTypeEnum;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.CodeAnnotation;
import com.liujun.micro.autocode.generator.builder.constant.CodeComment;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.ImportCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.JavaMethodName;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarValue;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.entity.JavaClassEntity;
import com.liujun.micro.autocode.generator.builder.entity.JavaClassFieldEntity;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodArguments;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodEntity;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.MethodUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.ReturnUtils;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.javalanguage.serivce.JavaFormat;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 用于生成API接口调用聚合层的实现
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaAction {

  /** 实例对象 */
  public static final GenerateJavaAction INSTANCE = new GenerateJavaAction();

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
          ImportCodePackageKey.API_RESPONSE.getPackageInfo().packageOut(),
          ImportCodePackageKey.API_PAGE_RESPONSE.getPackageInfo().packageOut(),
          ImportCodePackageKey.API_PAGE_REQUEST.getPackageInfo().packageOut(),
          ImportCodePackageKey.API_ASSEMBLER_PAGE.getPackageInfo().packageOut(),
          ImportCodePackageKey.API_DATA_RESPONSE.getPackageInfo().packageOut());

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

  /** 成功方法 */
  private static final String QUERY_RSP_OK = "ok";

  /**
   * 生成action的领域的操作
   *
   * @param packageMap 导入的包信息
   * @param methodList 方法
   * @param author 作者
   * @return 构建的存储层对象
   */
  public StringBuilder generateAction(
      Map<String, ImportPackageInfo> packageMap, List<MethodInfo> methodList, String author) {
    // 领域实体实体
    ImportPackageInfo domainPackageInfo = packageMap.get(GenerateCodePackageKey.DOMAIN_DO.getKey());

    // 领域的服务接口
    ImportPackageInfo applicationServicePackage =
        packageMap.get(GenerateCodePackageKey.APPLICATION_SERVICE.getKey());

    // API的服务接口
    ImportPackageInfo interfaceFacadeService =
        packageMap.get(GenerateCodePackageKey.INTERFACE_FACADE.getKey());

    // 参数较验的类名信息
    ImportPackageInfo paramCheckService =
        packageMap.get(GenerateCodePackageKey.INTERFACE_CHECK_PARAM.getKey());

    // api接口层的实体
    ImportPackageInfo apiObject = packageMap.get(GenerateCodePackageKey.INTERFACE_OBJECT.getKey());

    // api接口层的实体
    ImportPackageInfo facadeInterface =
        packageMap.get(GenerateCodePackageKey.INTERFACE_FACADE_INTERFACE.getKey());

    // assembler转换类
    ImportPackageInfo assembler =
        packageMap.get(GenerateCodePackageKey.INTERFACE_ASSEMBLER.getKey());

    // 类的声明
    StringBuilder sb =
        GenerateJavaAction.INSTANCE.actionServiceDefine(
            applicationServicePackage,
            domainPackageInfo,
            interfaceFacadeService,
            paramCheckService,
            methodList,
            author,
            apiObject,
            assembler,
            facadeInterface);

    // 1,类属性的创建
    sb.append(
        JavaClassCodeUtils.getClassField(
            JavaClassFieldEntity.getPrivateAutowiredField(
                applicationServicePackage.getClassName(),
                JavaVarName.APPLICATION_INSTANCE_NAME,
                applicationServicePackage.getClassComment())));

    // 执行方法的生成
    generateMethod(methodList, sb, assembler, paramCheckService, apiObject, domainPackageInfo);

    // 类的结束
    JavaClassCodeUtils.classEnd(sb);
    return sb;
  }

  /**
   * 方法的生成操作
   *
   * @param methodList
   * @param sb
   * @param assembler
   * @param paramCheckService
   * @param apiObject
   * @param domainPackageInfo
   */
  private void generateMethod(
      List<MethodInfo> methodList,
      StringBuilder sb,
      ImportPackageInfo assembler,
      ImportPackageInfo paramCheckService,
      ImportPackageInfo apiObject,
      ImportPackageInfo domainPackageInfo) {
    for (MethodInfo methodItem : methodList) {
      // 1,针对增删除改的方法进行调用
      if (MethodTypeEnum.INSERT.getType().equals(methodItem.getOperator())) {
        this.updateMethod(
            sb,
            methodItem,
            assembler,
            JavaVarName.APPLICATION_INSTANCE_NAME,
            paramCheckService,
            apiObject,
            domainPackageInfo);
      }
      // 修改数据
      else if (MethodTypeEnum.UPDATE.getType().equals(methodItem.getOperator())) {
        this.updateMethod(
            sb,
            methodItem,
            assembler,
            JavaVarName.APPLICATION_INSTANCE_NAME,
            paramCheckService,
            apiObject,
            domainPackageInfo);
      }
      // 删除数据
      else if (MethodTypeEnum.DELETE.getType().equals(methodItem.getOperator())) {
        this.updateMethod(
            sb,
            methodItem,
            assembler,
            JavaVarName.APPLICATION_INSTANCE_NAME,
            paramCheckService,
            apiObject,
            domainPackageInfo);
      }
      // 如果当前为查询分页操作
      else if (MethodTypeEnum.QUERY_PAGE.getType().equals(methodItem.getOperator())) {
        this.pageQueryMethod(
            sb,
            methodItem,
            apiObject,
            JavaVarName.APPLICATION_INSTANCE_NAME,
            paramCheckService,
            assembler,
            domainPackageInfo);
      }
      // 如果当前为查询则进行查询调用操作
      else if (MethodTypeEnum.QUERY.getType().equals(methodItem.getOperator())) {
        this.queryMethod(
            sb,
            methodItem,
            domainPackageInfo,
            JavaVarName.APPLICATION_INSTANCE_NAME,
            paramCheckService,
            apiObject,
            assembler);
      }
    }
  }

  /**
   * 数据修改相关的调用
   *
   * @param sb
   * @param method 方法信息
   * @param assembler 转换类的信息
   * @param instanceName 实例对象的调用名称
   * @param paramCheckService 参数较验
   */
  public void updateMethod(
      StringBuilder sb,
      MethodInfo method,
      ImportPackageInfo assembler,
      String instanceName,
      ImportPackageInfo paramCheckService,
      ImportPackageInfo interfaceObject,
      ImportPackageInfo domainObject) {

    JavaMethodEntity methodEntity =
        JavaMethodEntity.builder()
            // 方法注解
            .annotation(getMethodAnnotation())
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 方法注释
            .comment(method.getComment())
            // 返回值
            .type(ImportCodePackageKey.API_RESPONSE.getPackageInfo().getClassName())
            // 返回注释
            .returnComment(ImportCodePackageKey.API_RESPONSE.getPackageInfo().getClassComment())
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
    sb.append(
        this.dataUpdateInvoke(method, instanceName, paramCheckService, domainObject, assembler));
    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
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
      JavaMethodArguments dataTransferItem =
          JavaMethodArguments.builder()
              .type(transferPkg.getClassName())
              .name(JavaVarName.METHOD_PARAM_NAME)
              .comment(transferPkg.getClassComment())
              .build();

      argumentsList = Arrays.asList(dataTransferItem);
    }

    return argumentsList;
  }

  /** 类注解 */
  private List<String> getClassAnnotation() {
    List<String> dataList = new ArrayList<>();
    // spring的service注解
    dataList.add(ImportCodePackageKey.SPRING_SERVICE_ANNOTATION.getPackageInfo().getAnnotation());
    // slf4j的注解
    dataList.add(ImportCodePackageKey.SLF4J_ANNOTATION.getPackageInfo().getAnnotation());

    return dataList;
  }

  /** 获取重写参数的注解 */
  private List<String> getMethodAnnotation() {
    List<String> dataList = new ArrayList<>();
    // Override注解
    dataList.add(CodeAnnotation.OVERRIDE);

    return dataList;
  }

  /**
   * 数据修改的调用
   *
   * @param method 方法信息
   * @param instanceName 实例类名
   * @param paramCheckService 参数较验类
   */
  private String dataUpdateInvoke(
      MethodInfo method,
      String instanceName,
      ImportPackageInfo paramCheckService,
      ImportPackageInfo domain,
      ImportPackageInfo assemble) {
    StringBuilder sb = new StringBuilder();

    // 方法的参数检查
    sb.append(this.checkParam(method, paramCheckService));

    // 进行数据的添加、修改、删除的执行
    sb.append(this.invokeData(method, instanceName, domain, assemble));

    return sb.toString();
  }

  /**
   * 数据修改的执行
   *
   * @param method 方法
   * @param instanceName 实例名
   * @param domain 领域存储对象
   * @param assemble 转换对象
   * @return
   */
  private String invokeData(
      MethodInfo method,
      String instanceName,
      ImportPackageInfo domain,
      ImportPackageInfo assemble) {

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
    sb.append(JavaFormat.appendTab(2)).append(JavaKeyWord.TYPE_BOOLEAN);
    sb.append(Symbol.SPACE).append(JavaVarName.INVOKE_METHOD_UPDATE_RSP);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(instanceName);
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(domain.getVarName()).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 针对返回数据进行判断
    sb.append(JavaFormat.appendTab(2)).append(JavaKeyWord.IF);
    sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.INVOKE_METHOD_UPDATE_RSP);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.BRACE_LEFT);
    sb.append(Symbol.ENTER_LINE);

    // 填写成功返回
    sb.append(JavaFormat.appendTab(3));
    sb.append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(ImportCodePackageKey.API_RESPONSE.getPackageInfo().getClassName());
    sb.append(Symbol.POINT).append(JavaMethodName.RESPONSE_SUCCESS);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    sb.append(JavaFormat.appendTab(2));
    sb.append(Symbol.BRACE_RIGHT);
    sb.append(Symbol.ENTER_LINE);

    // 返回语句
    sb.append(JavaFormat.appendTab(2)).append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(ImportCodePackageKey.API_RESPONSE.getPackageInfo().getClassName());
    sb.append(Symbol.POINT).append(JavaMethodName.RESPONSE_FAIL);
    sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    return sb.toString();
  }

  /**
   * 参数对象检查的方法生成
   *
   * @param method 方法信息
   * @param paramCheckService 生成的相关的代码检查信息
   * @return 代码信息
   */
  private String checkParam(MethodInfo method, ImportPackageInfo paramCheckService) {
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
    sb.append(ImportCodePackageKey.API_RESPONSE.getPackageInfo().getClassName());
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
   * @param domainPkg 领域对象
   */
  private String dataNormalQueryInvoke(
      MethodInfo method,
      ImportPackageInfo domainPkg,
      String instanceName,
      ImportPackageInfo assembler) {

    StringBuilder sb = new StringBuilder();

    // 检查结果是单对象还是结果集
    boolean rspListFlag = ReturnUtils.checkReturnList(method.getReturns());

    // 如果当前返回多结果集，则调用相关的多结果集方法
    if (rspListFlag) {
      sb.append(this.queryList(method, domainPkg, instanceName, assembler));
    } else {
      sb.append(this.queryRspOne(method, domainPkg, instanceName, assembler));
    }

    return sb.toString();
  }

  /**
   * 查询返回结果集的方法
   *
   * @param method
   * @param domainPkg
   */
  private String queryList(
      MethodInfo method,
      ImportPackageInfo domainPkg,
      String instanceName,
      ImportPackageInfo assembler) {

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
    sb.append(Symbol.SPACE).append(instanceName);
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
    sb.append(ImportCodePackageKey.API_DATA_RESPONSE.getPackageInfo().getClassName());
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
    sb.append(ImportCodePackageKey.API_DATA_RESPONSE.getPackageInfo().getClassName());
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
   * @param poPkg
   */
  private String queryRspOne(
      MethodInfo method,
      ImportPackageInfo poPkg,
      String instanceName,
      ImportPackageInfo assembler) {

    StringBuilder sb = new StringBuilder();

    // 数据对象的转换
    sb.append(JavaFormat.appendTab(2));
    sb.append(poPkg.getClassName()).append(Symbol.SPACE);
    sb.append(poPkg.getVarName()).append(Symbol.SPACE);
    sb.append(Symbol.EQUAL).append(Symbol.SPACE);
    sb.append(assembler.getClassName());
    sb.append(Symbol.POINT).append(JavaMethodName.ASSEMBLER_TRANSFER_DOMAIN_NAME);
    sb.append(Symbol.BRACKET_LEFT);
    sb.append(JavaVarName.METHOD_PARAM_NAME);
    sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
    sb.append(Symbol.ENTER_LINE);

    // 执行数据的查询
    sb.append(JavaFormat.appendTab(2));
    sb.append(poPkg.getClassName()).append(Symbol.SPACE).append(JavaVarName.QUERY_RETURN_DATA);
    sb.append(Symbol.SPACE).append(Symbol.EQUAL);
    sb.append(Symbol.SPACE).append(instanceName);
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(poPkg.getVarName()).append(Symbol.BRACKET_RIGHT);
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
    sb.append(ImportCodePackageKey.API_DATA_RESPONSE.getPackageInfo().getClassName());
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
    sb.append(ImportCodePackageKey.API_DATA_RESPONSE.getPackageInfo().getClassName());
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
   * @param sb
   * @param method 方法信息
   * @param domainPkg 领域包信息
   */
  public void queryMethod(
      StringBuilder sb,
      MethodInfo method,
      ImportPackageInfo domainPkg,
      String instanceName,
      ImportPackageInfo paramCheckService,
      ImportPackageInfo dtoPkg,
      ImportPackageInfo assembler) {

    List<JavaMethodArguments> argumentsList =
        Arrays.asList(
            JavaMethodArguments.builder()
                .type(dtoPkg.getClassName())
                .name(JavaVarName.METHOD_PARAM_NAME)
                .comment(domainPkg.getClassComment())
                .build());

    JavaMethodEntity methodEntity =
        JavaMethodEntity.builder()
            // 注解
            .annotation(getMethodAnnotation())
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 返回注释
            .comment(method.getComment())
            // 返回值
            .type(ImportCodePackageKey.API_RESPONSE.getPackageInfo().getClassName())
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
    sb.append(this.checkParam(method, paramCheckService));

    // 数据查询相关的调用
    sb.append(this.dataNormalQueryInvoke(method, domainPkg, instanceName, assembler));
    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 分页相关的方法的调用
   *
   * @param sb
   * @param method 方法信息
   * @param dataTransferPkg 领域包信息
   */
  public void pageQueryMethod(
      StringBuilder sb,
      MethodInfo method,
      ImportPackageInfo dataTransferPkg,
      String instanceName,
      ImportPackageInfo paramCheckService,
      ImportPackageInfo assembler,
      ImportPackageInfo domainPkg) {

    List<JavaMethodArguments> argumentsList =
        Arrays.asList(
            JavaMethodArguments.builder()
                .type(
                    ImportCodePackageKey.API_PAGE_REQUEST.getPackageInfo().getClassName()
                        + Symbol.ANGLE_BRACKETS_LEFT
                        + dataTransferPkg.getClassName()
                        + Symbol.ANGLE_BRACKETS_RIGHT)
                .name(JavaVarName.METHOD_PARAM_NAME)
                .comment(ImportCodePackageKey.API_PAGE_REQUEST.getPackageInfo().getClassComment())
                .build());

    JavaMethodEntity methodEntity =
        JavaMethodEntity.builder()
            // 注解
            .annotation(getMethodAnnotation())
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 方法注释
            .comment(method.getComment())
            // 返回值
            .type(ImportCodePackageKey.API_RESPONSE.getPackageInfo().getClassName())
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
    sb.append(pageMethodBuilder(method, instanceName, paramCheckService, domainPkg, assembler));

    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 分页查询的方法构建
   *
   * @param method 方法
   * @param instanceName 实例名称
   * @param paramCheckService 方法参数信息
   */
  private String pageMethodBuilder(
      MethodInfo method,
      String instanceName,
      ImportPackageInfo paramCheckService,
      ImportPackageInfo domainPkg,
      ImportPackageInfo assembler) {

    StringBuilder sb = new StringBuilder();

    // 方法的参数检查
    sb.append(this.checkParam(method, paramCheckService));

    // 调用分页的相关的查询
    sb.append(this.pageInvoke(method, instanceName, domainPkg, assembler));
    return sb.toString();
  }

  /**
   * 分页查询的分页方法的调用
   *
   * @return
   */
  private String pageInvoke(
      MethodInfo method,
      String instanceName,
      ImportPackageInfo domainPkg,
      ImportPackageInfo assembler) {
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
    sb.append(Symbol.SPACE).append(instanceName);
    sb.append(Symbol.POINT).append(method.getName()).append(Symbol.BRACKET_LEFT);
    sb.append(domainPkg.getVarName()).append(Symbol.COMMA);
    sb.append(JavaVarName.QUERY_PAGE_PARAM_VAR).append(Symbol.BRACKET_RIGHT);
    sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    // 数据转换操作
    sb.append(this.dataParse(assembler));

    // 返回语句
    sb.append(JavaFormat.appendTab(2)).append(JavaKeyWord.RETURN).append(Symbol.SPACE);
    sb.append(ImportCodePackageKey.API_PAGE_RESPONSE.getPackageInfo().getClassName());
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
   * @param interfaceFacade 领域存储的接口
   * @param domainEntityPackage 领域实体
   * @param apiService api的对象
   * @param methodList 方法列表
   * @param checkParam 参数验证
   * @param author 作者
   * @param interfaceObject 转换实体
   * @param assembler 转换类
   * @return 构建的类头
   */
  public StringBuilder actionServiceDefine(
      ImportPackageInfo interfaceFacade,
      ImportPackageInfo domainEntityPackage,
      ImportPackageInfo apiService,
      ImportPackageInfo checkParam,
      List<MethodInfo> methodList,
      String author,
      ImportPackageInfo interfaceObject,
      ImportPackageInfo assembler,
      ImportPackageInfo facadeInterface) {
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
    // 领域存储
    importList.add(interfaceFacade.packageOut());
    // 领域实体
    importList.add(domainEntityPackage.packageOut());
    // 导入验证类
    importList.add(checkParam.packageOut());
    // 导入接口
    importList.add(interfaceFacade.packageOut());
    // 导入传输层的对象
    importList.add(interfaceObject.packageOut());
    // 导入转换类
    importList.add(assembler.packageOut());

    JavaClassEntity classEntityInfo =
        JavaClassEntity.builder()
            // 注解
            .annotationList(getClassAnnotation())
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
            .author(author)
            // 继承类信息
            .interfaceClass(facadeInterface.getClassName())
            .build();

    return JavaClassCodeUtils.javaClassDefine(classEntityInfo);
  }
}
