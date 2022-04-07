package com.liujun.auto.generator.builder.operator.ddd.full;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.operator.GenerateCodeInf;
import com.liujun.auto.generator.builder.operator.code.GenerateJavaCheck;
import com.liujun.auto.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 生成从传输层对象的检查方法
 *
 * @author liujun
 * @version 1.0.0
 */
public class JavaCodeInterfaceCheckCreate implements GenerateCodeInf {

  private static final String NAME_SUFFIX = "Check";
  private static final String NAME_COMMENT = "参数校验";

  public static final JavaCodeInterfaceCheckCreate INSTANCE = new JavaCodeInterfaceCheckCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();
    while (tableNameEntry.hasNext()) {
      Entry<String, List<TableColumnDTO>> tableNameItem = tableNameEntry.next();

      // 表名
      String tableName = tableNameItem.getKey();

      // 得到类名
      String tableClassName = NameProcess.INSTANCE.toJavaClassName(tableName);
      String className = tableClassName + NAME_SUFFIX;

      // 验证包的路径
      String javaPackageStr = param.getJavaCodePackage().getApiCheckParamNode().outJavaPackage();

      // 将dao信息进行储存至流程中
      ImportPackageInfo paramCheckPkg =
          new ImportPackageInfo(javaPackageStr, className, NAME_COMMENT);
      ImportPackageUtils.putPackageInfo(
          tableName,
          param.getPackageMap(),
          GenerateCodePackageKey.INTERFACE_CHECK_PARAM.getKey(),
          paramCheckPkg,
          tableMap.size());

      // 获取实体信息
      ImportPackageInfo transferPackageInfo =
          ImportPackageUtils.getDefineClass(
              param.getPackageMap(), GenerateCodePackageKey.INTERFACE_OBJECT.getKey(), tableName);

      // 获取实体信息
      ImportPackageInfo errorCodePkg =
          ImportPackageUtils.getDefineClass(
              param.getPackageMap(),
              GenerateCodePackageKey.INTERFACE_ERROR_CODE.getKey(),
              tableName);

      // 获取参数校验的常量类
      ImportPackageInfo constantPackage =
          ImportPackageUtils.getDefineClass(
              param.getPackageMap(),
              GenerateCodePackageKey.INTERFACE_ERROR_CONSTANT.getKey(),
              tableName);

      // 进行转换方法的生成
      StringBuilder sb =
          GenerateJavaCheck.INSTANCE.generateCheck(
              paramCheckPkg,
              param.getGenerateConfig().getGenerate().getCode(),
              tableNameItem.getValue(),
              transferPackageInfo,
              errorCodePkg,
              constantPackage,
              param.getGenerateConfig().getGenerate().getAuthor());

      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getSrcJavaNode().outPath();

      javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

      // 进行存储层的接口输出
      GenerateOutFileUtils.outJavaFile(
          sb, GeneratePathUtils.outServicePath(param), javaPackageStr, className);
    }
  }
}
