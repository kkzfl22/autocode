package com.liujun.micro.autocode.generator.builder.operator.ddd.full;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.code.GenerateJavaInterfaceConstant;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 生成接口层的参数校验相关的静态参数
 *
 * @author liujun
 * @version 1.0.0
 */
public class JavaCodeInterfaceConstantCreate implements GenerateCodeInf {

  /** 错误码后缀 */
  private static final String NAME_SUFFIX = "Constant";

  private static final String NAME_COMMENT = "常量信息";

  public static final JavaCodeInterfaceConstantCreate INSTANCE =
      new JavaCodeInterfaceConstantCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();
    while (tableNameEntry.hasNext()) {
      Entry<String, List<TableColumnDTO>> tableNameItem = tableNameEntry.next();

      // 表名
      String tableName = tableNameItem.getKey();

      // 构建常量包信息
      ImportPackageInfo constantPkg = this.generatePackageInfo(param, tableName);

      // 进行存储至上下文中
      ImportPackageUtils.putPackageInfo(
          tableName,
          param.getPackageMap(),
          GenerateCodePackageKey.INTERFACE_ERROR_CONSTANT.getKey(),
          constantPkg,
          tableMap.size());

      // 代码的生成操作
      StringBuilder sb =
          GenerateJavaInterfaceConstant.INSTANCE.generateInterfaceConstant(
              param.getGenerateConfig().getGenerate().getCode(),
              param.getTypeEnum(),
              constantPkg,
              tableNameItem.getValue(),
              param.getGenerateConfig().getGenerate().getAuthor());

      // 定义项目内的完整目录结构
      String javaPackageStr =
          param.getProjectPath().getSrcJavaNode().outPath()
              + Symbol.PATH
              + constantPkg.getPackagePath();

      // 进行存储层的接口输出
      GenerateOutFileUtils.outJavaFile(
          sb, GeneratePathUtils.outServicePath(param), javaPackageStr, constantPkg.getClassName());
    }
  }

  /**
   * 构建常量类的包信息
   *
   * @param param 公共上下文信息
   * @param tableName 表名
   * @return 包信息
   */
  private ImportPackageInfo generatePackageInfo(GenerateCodeContext param, String tableName) {
    // 得到类名
    String tableClassName = NameProcess.INSTANCE.toJavaClassName(tableName);
    String className = tableClassName + NAME_SUFFIX;

    // 获取常量包的路径
    String javaPackageStr = param.getJavaCodePackage().getInterfaceConstant().outJavaPackage();

    // 将常量包的路径进行存储至流程中
    ImportPackageInfo errorCodeAssemblerPkg =
        new ImportPackageInfo(javaPackageStr, className, NAME_COMMENT);

    return errorCodeAssemblerPkg;
  }
}
