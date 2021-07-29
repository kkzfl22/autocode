package com.liujun.micro.autocode.generator.builder.operator.ddd.full;

import com.liujun.micro.autocode.config.generate.GenerateConfigLoader;
import com.liujun.micro.autocode.config.generate.entity.Generate;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.utils.FileReaderUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.javalanguage.serivce.JavaFormat;
import com.liujun.micro.autocode.utils.StreamUtils;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 总工程的maven的pom文件进行生成操作
 *
 * @author liujun
 * @version 1.0.0
 */
@Slf4j
public class JavaCodeProjectMavenPomCreate implements GenerateCodeInf {

  /** 项目的pom文件 */
  private static final String POM_NAME = "pom.xml";

  /** 配制的项目级project的文件 */
  private static final String CFG_PROJECT_POM_PATH = "config/project";

  /** 配制的service的pom文件 */
  private static final String CFG_SERVICE_POM_PATH = "config/project/service";

  /** 配制的api的pom文件 */
  private static final String CFG_API_POM_PATH = "config/project/api";

  /** 项目使用的版本号 */
  private static final String POM_PROJECT_VERSION = "\\$\\{project.version\\}";

  /** 总项目的定义 */
  private static final String PARENT_DEFINE = "#parent_define#";

  /** 模块名称的定义 */
  private static final String PARENT_MODULE_NAME = "#parent_module_name#";

  /** 父级中关于模块的定义 */
  private static final String PARENT_MODULE_DEFINE = "#parent_module_define#";

  /** 业务的定义 */
  private static final String SERVICE_DEFINE = "#service_define#";

  /** 业务模块的依赖 */
  private static final String SERVICE_MODULE_DEPENDENCY = "#service_module_dependency#";

  /** 模块的定义信息 */
  private static final String API_DEFINE = "#api_define#";

  enum PomTag {
    /** 组标识 */
    GROUP("<groupId>", "</groupId>"),

    /** 名称标识 */
    ARTIFACT("<artifactId>", "</artifactId>"),

    /** 版本 */
    VERSION("<version>", "</version>"),

    /** 模块名称 */
    MODULE("<module>", "</module>"),

    /** 依赖标签 */
    DEPENDENCY("<dependency>", "</dependency>"),

    /** 父级的标识 */
    PARENT("<parent>", "</parent>");

    PomTag(String start, String end) {
      this.start = start;
      this.end = end;
    }

    /** 开始 */
    private String start;

    /** 结束 */
    private String end;

    public String getStart() {
      return start;
    }

    public String getEnd() {
      return end;
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder("PomTag{");
      sb.append("start='").append(start).append('\'');
      sb.append(", end='").append(end).append('\'');
      sb.append('}');
      return sb.toString();
    }
  }

  public static final JavaCodeProjectMavenPomCreate INSTANCE = new JavaCodeProjectMavenPomCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {

    // 先执行对project目录下的pom.xml文件进行清理
    GenerateOutFileUtils.cleanFile(GeneratePathUtils.ouProjectPath(param), Symbol.EMPTY, POM_NAME);
    // 再执行对业务模块下的pom.xml文件的清理
    GenerateOutFileUtils.cleanFile(GeneratePathUtils.outServicePath(param), Symbol.EMPTY, POM_NAME);
    // 再清理api接口模块下的pom.xml
    GenerateOutFileUtils.cleanFile(GeneratePathUtils.outApiPath(param), Symbol.EMPTY, POM_NAME);

    // 总工程的pom.xml文件
    projectPomProcess(param);

    // 子模块业务工程pom.xml文件处理
    servicePomProcess(param);

    // 子模块的api的定义
    apiPomProcess(param);
  }

  /** 项目级的pom文件处理 */
  private void projectPomProcess(GenerateCodeContext param) {
    // 1,读取pom.xml文件模板
    String readData = FileReaderUtils.readFile(CFG_PROJECT_POM_PATH + Symbol.PATH + POM_NAME);

    // 数据处理
    String dataOutValue = projectDataProcess(readData, param);

    // 文件的输出操作
    GenerateOutFileUtils.outFile(
        dataOutValue, GeneratePathUtils.ouProjectPath(param), Symbol.EMPTY, POM_NAME, false);
  }

  /**
   * 业务项目的pom的文件处理
   *
   * @param param
   */
  private void servicePomProcess(GenerateCodeContext param) {
    // 1,读取pom.xml文件模板
    String readData = FileReaderUtils.readFile(CFG_SERVICE_POM_PATH + Symbol.PATH + POM_NAME);

    // 数据处理
    String serviceOutValue = serviceDataProcess(readData, param);

    // 文件的输出操作
    GenerateOutFileUtils.outFile(
        serviceOutValue, GeneratePathUtils.outServicePath(param), Symbol.EMPTY, POM_NAME, false);
  }

  /**
   * api接口层的pom文件的处理
   *
   * @param param
   */
  private void apiPomProcess(GenerateCodeContext param) {
    // 1,读取pom.xml文件模板
    String readData = FileReaderUtils.readFile(CFG_API_POM_PATH + Symbol.PATH + POM_NAME);

    // 数据处理
    String apiOutValue = apiDataProcess(readData, param);

    // 文件的输出操作
    GenerateOutFileUtils.outFile(
        apiOutValue, GeneratePathUtils.outApiPath(param), Symbol.EMPTY, POM_NAME, false);
  }

  /**
   * service的pom的处理操作
   *
   * @param readDataTmp 读取的内容模板原文
   * @param param 参数信息
   * @return 处理后的内容
   */
  private String serviceDataProcess(String readDataTmp, GenerateCodeContext param) {

    String readData = readDataTmp;
    // 设置当前的总工程的定义
    readData = readData.replaceAll(PARENT_DEFINE, parentDefine(param));
    // 设置当前的模块的定义
    readData = readData.replaceAll(SERVICE_DEFINE, serviceDefine(param));
    // 所有模块的定义
    readData =
        readData.replaceAll(
            SERVICE_MODULE_DEPENDENCY,
            serviceDependencyDefine(param, GeneratePathUtils.getApiModuleName(param)));

    return readData;
  }

  /**
   * api的pom的处理操作
   *
   * @param readDataTmp 读取的内容模板原文
   * @param param 参数信息
   * @return 处理后的内容
   */
  private String apiDataProcess(String readDataTmp, GenerateCodeContext param) {

    String readData = readDataTmp;
    // 设置当前的总工程的定义
    readData = readData.replaceAll(PARENT_DEFINE, parentDefine(param));
    // 设置当前的模块的定义
    readData = readData.replaceAll(API_DEFINE, apiDefine(param));

    return readData;
  }

  /**
   * 业务的父类定义
   *
   * @param param 配制信息
   * @return 定义信息
   */
  private String serviceDefine(GenerateCodeContext param) {
    StringBuilder outParentDefine = new StringBuilder();
    Generate generateInfo = param.getGenerateConfig().getGenerate();

    // artifact定义
    outParentDefine.append(JavaFormat.appendTab(1)).append(PomTag.ARTIFACT.getStart());
    outParentDefine.append(generateInfo.getModuleName());
    outParentDefine.append(PomTag.ARTIFACT.getEnd()).append(Symbol.ENTER_LINE);

    return outParentDefine.toString();
  }

  /**
   * 业务的父类定义
   *
   * @param param 配制信息
   * @return 定义信息
   */
  private String apiDefine(GenerateCodeContext param) {
    StringBuilder outParentDefine = new StringBuilder();
    Generate generateInfo = param.getGenerateConfig().getGenerate();

    // artifact定义
    outParentDefine.append(JavaFormat.appendTab(1)).append(PomTag.ARTIFACT.getStart());
    outParentDefine.append(GeneratePathUtils.getApiModuleName(param));
    outParentDefine.append(PomTag.ARTIFACT.getEnd()).append(Symbol.ENTER_LINE);

    return outParentDefine.toString();
  }

  /**
   * 服务的的依赖定义
   *
   * @param param 配制信息
   * @param moduleDependency 依赖的模块信息
   * @return 定义信息
   */
  private String serviceDependencyDefine(GenerateCodeContext param, String moduleDependency) {
    StringBuilder outParentDefine = new StringBuilder();
    Generate generateInfo = param.getGenerateConfig().getGenerate();

    outParentDefine.append(JavaFormat.appendTab(2));
    outParentDefine.append(PomTag.DEPENDENCY.getStart()).append(Symbol.ENTER_LINE);

    // group定义
    outParentDefine.append(JavaFormat.appendTab(3)).append(PomTag.GROUP.getStart());
    outParentDefine.append(generateInfo.getMaven().getGroupId());
    outParentDefine.append(PomTag.GROUP.getEnd()).append(Symbol.ENTER_LINE);

    // artifact定义
    outParentDefine.append(JavaFormat.appendTab(3)).append(PomTag.ARTIFACT.getStart());
    outParentDefine.append(moduleDependency);
    outParentDefine.append(PomTag.ARTIFACT.getEnd()).append(Symbol.ENTER_LINE);

    outParentDefine.append(JavaFormat.appendTab(2));
    outParentDefine.append(PomTag.DEPENDENCY.getEnd()).append(Symbol.ENTER_LINE);

    return outParentDefine.toString();
  }

  /**
   * 业务的父类定义
   *
   * @param param 配制信息
   * @return 定义信息
   */
  private String parentDefine(GenerateCodeContext param) {
    StringBuilder outParentDefine = new StringBuilder();
    Generate generateInfo = param.getGenerateConfig().getGenerate();
    // parent定义
    outParentDefine.append(JavaFormat.appendTab(1)).append(PomTag.PARENT.getStart());
    outParentDefine.append(Symbol.ENTER_LINE);

    // 项目的
    outParentDefine.append(JavaFormat.appendTab(2));
    outParentDefine.append(PomTag.GROUP.getStart());
    outParentDefine.append(generateInfo.getMaven().getGroupId());
    outParentDefine.append(PomTag.GROUP.getEnd()).append(Symbol.ENTER_LINE);

    // artifact定义
    outParentDefine.append(JavaFormat.appendTab(2)).append(PomTag.ARTIFACT.getStart());
    outParentDefine.append(generateInfo.getProjectName());
    outParentDefine.append(PomTag.ARTIFACT.getEnd()).append(Symbol.ENTER_LINE);

    // version定义
    outParentDefine.append(JavaFormat.appendTab(2)).append(PomTag.VERSION.getStart());
    outParentDefine.append(generateInfo.getMaven().getVersion());
    outParentDefine.append(PomTag.VERSION.getEnd()).append(Symbol.ENTER_LINE);

    outParentDefine.append(JavaFormat.appendTab(1)).append(PomTag.PARENT.getEnd());
    outParentDefine.append(Symbol.ENTER_LINE);

    return outParentDefine.toString();
  }

  /**
   * 进行内容的处理操作
   *
   * @param readDataTmp 读取的内容模板原文
   * @param param 参数信息
   * @return 处理后的内容
   */
  private String projectDataProcess(String readDataTmp, GenerateCodeContext param) {

    String readData = readDataTmp;
    // 设置当前的总工程的定义
    readData = readData.replaceAll(PARENT_DEFINE, projectDefine(param));
    // 设置当前的模块的定义
    readData = readData.replaceAll(PARENT_MODULE_NAME, projectModuleDefine(param));
    // 所有模块的定义
    readData = readData.replaceAll(PARENT_MODULE_DEFINE, projectAllDependency(param));

    return readData;
  }

  /**
   * 所有模块的在父级中的定义
   *
   * @param param
   * @return
   */
  private String projectAllDependency(GenerateCodeContext param) {
    StringBuilder outDependency = new StringBuilder();

    outDependency.append(projectDependencyDefine(param, param.getModuleName()));
    outDependency.append(Symbol.ENTER_LINE);
    outDependency.append(projectDependencyDefine(param, GeneratePathUtils.getApiModuleName(param)));

    return outDependency.toString();
  }

  /**
   * 项目的依赖定义
   *
   * @param param 配制信息
   * @return 定义信息
   */
  private String projectDependencyDefine(GenerateCodeContext param, String moduleName) {
    StringBuilder outParentDefine = new StringBuilder();
    Generate generateInfo = param.getGenerateConfig().getGenerate();

    outParentDefine.append(JavaFormat.appendTab(3));
    outParentDefine.append(PomTag.DEPENDENCY.getStart()).append(Symbol.ENTER_LINE);

    // group定义
    outParentDefine.append(JavaFormat.appendTab(4)).append(PomTag.GROUP.getStart());
    outParentDefine.append(generateInfo.getMaven().getGroupId());
    outParentDefine.append(PomTag.GROUP.getEnd()).append(Symbol.ENTER_LINE);

    // artifact定义
    outParentDefine.append(JavaFormat.appendTab(4)).append(PomTag.ARTIFACT.getStart());
    outParentDefine.append(moduleName);
    outParentDefine.append(PomTag.ARTIFACT.getEnd()).append(Symbol.ENTER_LINE);

    // version定义
    outParentDefine.append(JavaFormat.appendTab(4)).append(PomTag.VERSION.getStart());
    outParentDefine.append(POM_PROJECT_VERSION);
    outParentDefine.append(PomTag.VERSION.getEnd()).append(Symbol.ENTER_LINE);

    outParentDefine.append(JavaFormat.appendTab(3));
    outParentDefine.append(PomTag.DEPENDENCY.getEnd()).append(Symbol.ENTER_LINE);

    return outParentDefine.toString();
  }

  /**
   * 模块的定义
   *
   * @param param 名称信息
   * @return 模块的定义信息
   */
  private String projectModuleDefine(GenerateCodeContext param) {
    StringBuilder outParentDefine = new StringBuilder();
    Generate generateInfo = param.getGenerateConfig().getGenerate();
    // 模块的定义
    outParentDefine.append(JavaFormat.appendTab(2)).append(PomTag.MODULE.getStart());
    outParentDefine.append(generateInfo.getModuleName());
    outParentDefine.append(PomTag.MODULE.getEnd()).append(Symbol.ENTER_LINE);

    // api模块的定义
    outParentDefine.append(JavaFormat.appendTab(2)).append(PomTag.MODULE.getStart());
    outParentDefine.append(GeneratePathUtils.getApiModuleName(param));
    outParentDefine.append(PomTag.MODULE.getEnd()).append(Symbol.ENTER_LINE);

    return outParentDefine.toString();
  }

  /**
   * 项目的定义
   *
   * @param param 配制信息
   * @return 定义信息
   */
  private String projectDefine(GenerateCodeContext param) {
    StringBuilder outParentDefine = new StringBuilder();
    Generate generateInfo = param.getGenerateConfig().getGenerate();
    // group定义
    outParentDefine.append(JavaFormat.appendTab(1)).append(PomTag.GROUP.getStart());
    outParentDefine.append(generateInfo.getMaven().getGroupId());
    outParentDefine.append(PomTag.GROUP.getEnd()).append(Symbol.ENTER_LINE);

    // artifact定义
    outParentDefine.append(JavaFormat.appendTab(1)).append(PomTag.ARTIFACT.getStart());
    outParentDefine.append(generateInfo.getProjectName());
    outParentDefine.append(PomTag.ARTIFACT.getEnd()).append(Symbol.ENTER_LINE);

    // version定义
    outParentDefine.append(JavaFormat.appendTab(1)).append(PomTag.VERSION.getStart());
    outParentDefine.append(generateInfo.getMaven().getVersion());
    outParentDefine.append(PomTag.VERSION.getEnd()).append(Symbol.ENTER_LINE);

    return outParentDefine.toString();
  }
}
