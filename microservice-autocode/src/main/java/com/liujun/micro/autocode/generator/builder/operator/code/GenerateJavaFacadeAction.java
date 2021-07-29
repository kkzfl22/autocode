package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.config.generate.entity.MethodInfo;
import com.liujun.micro.autocode.constant.MethodTypeEnum;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.CodeAnnotation;
import com.liujun.micro.autocode.generator.builder.constant.CodeComment;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.ImportCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.entity.JavaAnnotation;
import com.liujun.micro.autocode.generator.builder.entity.JavaClassEntity;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodArguments;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodEntity;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.MethodUtils;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
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
public class GenerateJavaFacadeAction {

  /** 实例对象 */
  public static final GenerateJavaFacadeAction INSTANCE = new GenerateJavaFacadeAction();

  /** 导入的包信息 */
  private static final List<String> IMPORT_PKG =
      Arrays.asList(
          ImportCodePackageKey.SPRING_REST_CONTROLLER.getPackageInfo().packageOut(),
          ImportCodePackageKey.SPRING_REQUEST_MAPPING.getPackageInfo().packageOut(),
          ImportCodePackageKey.ANNOTATION_API.getPackageInfo().packageOut(),
          ImportCodePackageKey.ANNOTATION_API_OPERATION.getPackageInfo().packageOut(),
          ImportCodePackageKey.SPRING_REQUEST_BODY.getPackageInfo().packageOut(),
          ImportCodePackageKey.SPRING_REQUEST_METHOD.getPackageInfo().packageOut(),
          ImportCodePackageKey.API_RESPONSE.getPackageInfo().packageOut(),
          JavaKeyWord.IMPORT_LIST,
          ImportCodePackageKey.PAGE_DTO.getPackageInfo().packageOut());

  /** 添加 */
  private static final String HTTP_ADD = "RequestMethod.POST";

  /** 修改 */
  private static final String HTTP_UPDATE = "RequestMethod.PUT";

  /** 删除 */
  private static final String HTTP_DELETE = "RequestMethod.DELETE";

  /** 数据查询，推荐查询参数请求 */
  private static final String HTTP_POST = "RequestMethod.POST";

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

    // API的服务接口
    ImportPackageInfo interfaceFacadeService =
        packageMap.get(GenerateCodePackageKey.INTERFACE_FACADE_INTERFACE.getKey());

    // api接口层的实体
    ImportPackageInfo apiObject = packageMap.get(GenerateCodePackageKey.INTERFACE_OBJECT.getKey());

    // 类的声明
    StringBuilder sb = this.actionServiceDefine(interfaceFacadeService, apiObject, author);

    for (MethodInfo methodItem : methodList) {
      // 1,针对增删除改的方法进行调用
      if (MethodTypeEnum.INSERT.getType().equals(methodItem.getOperator())) {
        this.updateMethod(sb, methodItem, apiObject, HTTP_ADD);
      }
      // 修改数据
      else if (MethodTypeEnum.UPDATE.getType().equals(methodItem.getOperator())) {
        this.updateMethod(sb, methodItem, apiObject, HTTP_UPDATE);
      }
      // 删除数据
      else if (MethodTypeEnum.DELETE.getType().equals(methodItem.getOperator())) {
        this.updateMethod(sb, methodItem, apiObject, HTTP_DELETE);
      }
      // 如果当前为查询分页操作
      else if (MethodTypeEnum.QUERY_PAGE.getType().equals(methodItem.getOperator())) {
        this.pageQueryMethod(sb, methodItem, apiObject);
      }
      // 如果当前为查询则进行查询调用操作
      else if (MethodTypeEnum.QUERY.getType().equals(methodItem.getOperator())) {
        this.queryMethod(sb, methodItem, apiObject);
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
   * @param httpMethodName 方法
   * @param interfaceObject 接口
   */
  public void updateMethod(
      StringBuilder sb,
      MethodInfo method,
      ImportPackageInfo interfaceObject,
      String httpMethodName) {

    JavaMethodEntity methodEntity =
        JavaMethodEntity.builder()
            // 方法注解
            .annotation(getAnnotation(method, httpMethodName))
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

    // 方法定义结束
    JavaClassCodeUtils.interfaceEnd(sb);
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
              .annotation(ImportCodePackageKey.SPRING_REQUEST_BODY.getPackageInfo().getAnnotation())
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
              .annotation(ImportCodePackageKey.SPRING_REQUEST_BODY.getPackageInfo().getAnnotation())
              .type(transferPkg.getClassName())
              .name(JavaVarName.METHOD_PARAM_NAME)
              .comment(transferPkg.getClassComment())
              .build();

      argumentsList = Arrays.asList(dataTransferItem);
    }

    return argumentsList;
  }

  /**
   * 获取参数的注解
   *
   * @param method 方法的信息
   * @param httpMethodName http方法名
   */
  private List<String> getAnnotation(MethodInfo method, String httpMethodName) {

    // 检查当前参数是否为集合
    boolean checkBatchFlag = MethodUtils.checkBatch(method.getParamType());

    List<String> dataList = new ArrayList<>();

    // 添加swagger的注解
    dataList.add(getSwaggerAnnotation(method).outAnnotation());

    if (checkBatchFlag) {
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
    } else {
      // spring的注解,@RequestMapping
      dataList.add(
          JavaAnnotation.builder()
              .annotation(
                  ImportCodePackageKey.SPRING_REQUEST_MAPPING.getPackageInfo().getAnnotation())
              .annotationValue(true, CodeAnnotation.SPRING_REQUEST_MAPPING_METHOD, httpMethodName)
              .build()
              .outAnnotation());
    }
    return dataList;
  }

  /**
   * 获取swagger的注解
   *
   * @param method
   * @return
   */
  private JavaAnnotation getSwaggerAnnotation(MethodInfo method) {

    JavaAnnotation annotation =
        JavaAnnotation.builder()
            // api请求的注解
            .annotation(
                ImportCodePackageKey.ANNOTATION_API_OPERATION.getPackageInfo().getAnnotation())
            .annotationValue(method.getComment())
            .build();

    return annotation;
  }

  /**
   * 获取参数的注解
   *
   * @param method 方法的信息
   * @param httpMethodName http方法名
   */
  private List<String> getQueryPageAnnotation(MethodInfo method, String httpMethodName) {

    List<String> dataList = new ArrayList<>();

    // 添加swagger的注解
    dataList.add(getSwaggerAnnotation(method).outAnnotation());
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
   * 数据查询相关的调用
   *
   * @param sb
   * @param method 方法信息
   */
  public void queryMethod(StringBuilder sb, MethodInfo method, ImportPackageInfo dtoPkg) {

    List<JavaMethodArguments> argumentsList =
        Arrays.asList(
            JavaMethodArguments.builder()
                // 注解信息
                .annotation(
                    ImportCodePackageKey.SPRING_REQUEST_BODY.getPackageInfo().getAnnotation())
                .type(dtoPkg.getClassName())
                .name(JavaVarName.METHOD_PARAM_NAME)
                .comment(dtoPkg.getClassComment())
                .build());

    JavaMethodEntity methodEntity =
        JavaMethodEntity.builder()
            // 注解
            .annotation(getQueryPageAnnotation(method, HTTP_POST))
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

    // 方法定义结束
    JavaClassCodeUtils.interfaceEnd(sb);
  }

  /**
   * 分页相关的方法的调用
   *
   * @param sb
   * @param method 方法信息
   * @param dataTransferPkg 领域包信息
   */
  public void pageQueryMethod(
      StringBuilder sb, MethodInfo method, ImportPackageInfo dataTransferPkg) {

    List<JavaMethodArguments> argumentsList =
        Arrays.asList(
            JavaMethodArguments.builder()
                // 注解信息
                .annotation(
                    ImportCodePackageKey.SPRING_REQUEST_BODY.getPackageInfo().getAnnotation())
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
            .annotation(getQueryPageAnnotation(method, HTTP_POST))
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
    // 方法定义结束
    JavaClassCodeUtils.interfaceEnd(sb);
  }

  /**
   * 方法的定义
   *
   * @param apiService api的对象
   * @param author 作者
   * @return 构建的类头
   */
  public StringBuilder actionServiceDefine(
      ImportPackageInfo apiService, ImportPackageInfo apiObject, String author) {
    List<String> importList = new ArrayList<>(16);

    // 集合
    importList.addAll(IMPORT_PKG);
    // 导入dto的实体类
    importList.add(apiObject.packageOut());

    JavaClassEntity classEntityInfo =
        JavaClassEntity.builder()
            // 注解
            .annotationList(annotation(apiService))
            // 类的关键字
            .classKey(JavaKeyWord.INTERFACE_KEY)
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
