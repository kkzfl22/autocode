package com.liujun.auto.generator.builder.ddd.full.other;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.utils.FileReaderUtils;
import com.liujun.auto.generator.builder.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.utils.GeneratePathUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 工程的配制文件拷贝
 *
 * @author liujun
 * @version 1.0.0
 */
@Slf4j
public class JavaCodeProjectCfgCopyCreate implements GenerateCodeInf {

  /** 配制文件 */
  private static final String CONFIG_YML = "application.yml";

  /** 配制的日志 */
  private static final String CONFIG_LOG_BACK = "logback.xml";

  /** 配制的项目级project的文件 */
  private static final String CFG_SRC_RESOURCE_PATH = "config/project/service/src/main/resources";

  /** 配制的测试的的资源文件 */
  private static final String CFG_TEST_RESOURCE_PATH = "config/project/service/src/test/resources";

  public static final JavaCodeProjectCfgCopyCreate INSTANCE = new JavaCodeProjectCfgCopyCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {

    // 对资源文件进行清理操作
    GenerateOutFileUtils.cleanFile(
        GeneratePathUtils.outServicePath(param),
        param.getProjectPath().getResourceNode().outPath(),
        CONFIG_YML);
    GenerateOutFileUtils.cleanFile(
        GeneratePathUtils.outServicePath(param),
        param.getProjectPath().getResourceNode().outPath(),
        CONFIG_LOG_BACK);
    // 测试目录下的文件也执行清理
    GenerateOutFileUtils.cleanFile(
        GeneratePathUtils.outServicePath(param),
        param.getProjectPath().getTestResourceNode().outPath(),
        CONFIG_YML);

    // 再执行文件的拷贝动作
    fileProcess(
        param,
        CFG_SRC_RESOURCE_PATH,
        param.getProjectPath().getResourceNode().outPath(),
        CONFIG_YML);
    fileProcess(
        param,
        CFG_SRC_RESOURCE_PATH,
        param.getProjectPath().getResourceNode().outPath(),
        CONFIG_LOG_BACK);
    fileProcess(
        param,
        CFG_TEST_RESOURCE_PATH,
        param.getProjectPath().getTestResourceNode().outPath(),
        CONFIG_YML);
  }

  /** 项目级的pom文件处理 */
  private void fileProcess(
      GenerateCodeContext param, String srcPath, String targetPath, String fileName) {
    // 1,读取pom.xml文件模板
    String readData = FileReaderUtils.readFile(srcPath + Symbol.PATH + fileName);

    // 文件的输出操作
    GenerateOutFileUtils.outFile(
        readData, GeneratePathUtils.outServicePath(param), targetPath, fileName, false);
  }
}
