package com.liujun.micro.autocode.generator.builder.operator.ddd.full;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.ImportCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.ImportJunitPkgKey;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarValue;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.entity.JavaClassEntity;
import com.liujun.micro.autocode.generator.builder.entity.JavaMethodEntity;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.javalanguage.serivce.JavaFormat;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 国际化启动加载
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeInterfaceConfigCreate implements GenerateCodeInf {

  /** 错误名的前缀 */
  private static final String ERROR_CODE_LOAD_PREFIX = "ErrorCodeConfig";

  /** 错误名的前缀 */
  private static final String METHOD_INIT_PREFIX = "i18n";

  /** 名称后缀 */
  private static final String ERROR_CODE_SUFFIX = "Loader";

  /** 注释 */
  private static final String ERROR_CODE_CONFIG_COMMENT = "错误码加载配制";

  /** 错误的注释 */
  private static final String LOADER_METHOD = "错误码加载至容器";

  /** 加载的方法名 */
  private static final String LOADER_NAME = "loader";

  /** 初始化方法 */
  private static final String INIT_METHOD = ".init()";

  /** 错误的注释 */
  private static final String I18N_METHOD = "资源加载方法";

  /** 方法加载 */
  private static final String LOADER_METHOD_CODE =
      "        for (Locale locale : CommonSysConfig.LOCALE) {\n"
          + "            Map<String, String> errorCodeMap = ResourceI18nErrorCodeMsgLoader.\n"
          + "                    getI18nErrorCodeInstance(#enumLoader#).loadErrorCodeMsg(locale);\n"
          + "\n"
          + "            I18nCollect.INSTANCE.addI18nModuleValue(locale, #enumLoader#, errorCodeMap);\n"
          + "        }\n";

  /** 替换的标识 */
  private static final String REPLACE_FLAG = "#enumLoader#";

  private static final List<String> DATA_LIST =
      Arrays.asList(
          "java.util.Locale",
          "com.common.i18n.ResourceI18nErrorCodeMsgLoader",
          "com.common.collect.I18nCollect",
          "com.common.constant.CommonSysConfig",
          "org.springframework.context.annotation.Configuration",
          "java.util.Map");

  public static final JavaCodeInterfaceConfigCreate INSTANCE = new JavaCodeInterfaceConfigCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {
    // 数据的定义
    StringBuilder outData = codeEnumDefine(param);

    // 定义项目内的完整目录结构
    String javaPackageStr =
        param.getProjectPath().getSrcJavaNode().outPath()
            + Symbol.PATH
            + param.getJavaCodePackage().getInterfaceConfigNode().outJavaPackage();

    // 进行存储层的接口输出
    GenerateOutFileUtils.outJavaFile(
        outData,
        GeneratePathUtils.outServicePath(param),
        javaPackageStr,
        getName(param.getModuleName()));
  }

  private StringBuilder codeEnumDefine(GenerateCodeContext param) {
    // 定义类文件
    StringBuilder classDefine = this.classDefine(param);

    classDefine.append(Symbol.ENTER_LINE);

    // 方法定义
    classDefine.append(methodDefineStart());
    // 方法开始
    JavaClassCodeUtils.methodStart(classDefine);
    // 错误码加载
    classDefine.append(enumInitInvoke(param));
    // 方法结束
    JavaClassCodeUtils.methodEnd(classDefine);

    // 资源方法的生成
    classDefine.append(loaderMethod(param));

    // 结束
    JavaClassCodeUtils.classEnd(classDefine);

    return classDefine;
  }

  /**
   * 枚举方法调用
   *
   * @param param 参数信息
   * @return 方法调用
   */
  private String enumInitInvoke(GenerateCodeContext param) {
    StringBuilder loaderInfo = new StringBuilder();

    for (ImportPackageInfo pkg : this.getErrorCodeEnum(param.getPackageMap())) {
      loaderInfo.append(JavaFormat.appendTab(2));
      loaderInfo.append(pkg.getClassName()).append(INIT_METHOD).append(Symbol.SEMICOLON);
      loaderInfo.append(Symbol.ENTER_LINE);
    }

    loaderInfo.append(Symbol.ENTER_LINE);
    loaderInfo.append(JavaFormat.appendTab(2));
    loaderInfo.append(i18nMethodName(param.getModuleName()));
    loaderInfo.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
    loaderInfo.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

    return loaderInfo.toString();
  }

  private String loaderMethod(GenerateCodeContext param) {
    StringBuilder method = new StringBuilder();

    JavaMethodEntity methodEntity =
        JavaMethodEntity.builder()
            // 访问修饰符
            .visit(JavaKeyWord.PRIVATE)
            // 方法注释
            .comment(I18N_METHOD)
            // 返回值
            .type(JavaKeyWord.VOID)
            // 方法名
            .name(i18nMethodName(param.getModuleName()))
            .build();

    JavaClassCodeUtils.methodDefine(method, methodEntity);

    JavaClassCodeUtils.methodStart(method);

    method.append(LOADER_METHOD_CODE.replaceAll(REPLACE_FLAG, enumModuleName(param)));

    // 方法结束
    JavaClassCodeUtils.methodEnd(method);

    return method.toString();
  }

  /**
   * 方法定义
   *
   * @return
   */
  private String methodDefineStart() {
    StringBuilder method = new StringBuilder();

    JavaMethodEntity methodEntity =
        JavaMethodEntity.builder()
            // 方法注解
            .annotation(
                ImportCodePackageKey.API_CONFIG_LOAD_ANNOTATION.getPackageInfo().getAnnotation())
            // 访问修饰符
            .visit(JavaKeyWord.PUBLIC)
            // 方法注释
            .comment(LOADER_METHOD)
            // 返回值
            .type(JavaKeyWord.VOID)
            // 返回注释
            .returnComment(ImportCodePackageKey.API_RESPONSE.getPackageInfo().getClassComment())
            // 方法名
            .name(LOADER_NAME)
            .build();

    JavaClassCodeUtils.methodDefine(method, methodEntity);

    return method.toString();
  }

  /**
   * 类文件定义
   *
   * @param param 参数
   * @return 构建
   */
  private StringBuilder classDefine(GenerateCodeContext param) {
    ImportPackageInfo errorCodeConfigPkg =
        new ImportPackageInfo(
            param.getJavaCodePackage().getInterfaceConfigNode().outJavaPackage(),
            getName(param.getModuleName()),
            ERROR_CODE_CONFIG_COMMENT);

    // 头定义
    JavaClassEntity classEntityInfo =
        JavaClassEntity.builder()
            // 注解
            .annotationList(
                Arrays.asList(ImportJunitPkgKey.SPRING_CONFIG.getPackageInfo().getAnnotation()))
            // 类的关键字
            .classKey(JavaKeyWord.CLASS_KEY)
            // 类名
            .className(errorCodeConfigPkg.getClassName())
            // 类注释
            .classComment(errorCodeConfigPkg.getClassComment())
            // 包类路径信息
            .packagePath(errorCodeConfigPkg.getPackagePath())
            // 导入包信息
            .importList(importList(param))
            // 作者
            .author(param.getGenerateConfig().getGenerate().getAuthor())
            // 继承类信息
            .interfaceClass(
                ImportCodePackageKey.API_CONFIG_INTERFACE.getPackageInfo().getClassName())
            .build();

    return JavaClassCodeUtils.javaClassDefine(classEntityInfo);
  }

  /**
   * 导入信息
   *
   * @param param 参数
   * @return
   */
  private List<String> importList(GenerateCodeContext param) {
    // 错误码
    List<ImportPackageInfo> dataCodeEnum = this.getErrorCodeEnum(param.getPackageMap());

    List<String> dataImportList = new ArrayList<>(dataCodeEnum.size() + 8);

    for (ImportPackageInfo pkg : dataCodeEnum) {
      dataImportList.add(pkg.packageOut());
    }

    dataImportList.add(ImportCodePackageKey.API_CONFIG_INTERFACE.getPackageInfo().packageOut());
    dataImportList.add(
        ImportCodePackageKey.API_CONFIG_LOAD_ANNOTATION.getPackageInfo().packageOut());
    dataImportList.addAll(DATA_LIST);

    return dataImportList;
  }

  private List<ImportPackageInfo> getErrorCodeEnum(
      Map<String, Map<String, ImportPackageInfo>> packageMap) {
    List<ImportPackageInfo> dataList = new ArrayList<>(packageMap.size());

    for (Map.Entry<String, Map<String, ImportPackageInfo>> itemPkg : packageMap.entrySet()) {
      if (itemPkg.getValue().containsKey(GenerateCodePackageKey.INTERFACE_ERROR_CODE.getKey())) {
        dataList.add(itemPkg.getValue().get(GenerateCodePackageKey.INTERFACE_ERROR_CODE.getKey()));
      }
    }

    return dataList;
  }

  private String enumModuleName(GenerateCodeContext param) {
    ImportPackageInfo first = null;

    for (Map.Entry<String, Map<String, ImportPackageInfo>> itemPkg :
        param.getPackageMap().entrySet()) {
      first = itemPkg.getValue().get(GenerateCodePackageKey.INTERFACE_ERROR_CODE.getKey());
      break;
    }

    return first.getClassName() + Symbol.POINT + JavaVarName.MODULE_NAME_VAR;
  }

  /**
   * 当前类名
   *
   * @param name
   * @return
   */
  private String getName(String name) {
    return ERROR_CODE_LOAD_PREFIX
        + NameProcess.INSTANCE.toJavaNameFirstUpper(name)
        + ERROR_CODE_SUFFIX;
  }

  /**
   * 当前类名
   *
   * @param name
   * @return
   */
  private String i18nMethodName(String name) {
    return METHOD_INIT_PREFIX + NameProcess.INSTANCE.toJavaNameFirstUpper(name) + ERROR_CODE_SUFFIX;
  }
}
