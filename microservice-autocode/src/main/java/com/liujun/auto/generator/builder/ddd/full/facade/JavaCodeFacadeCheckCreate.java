package com.liujun.auto.generator.builder.ddd.full.facade;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.PkgBuildMethod;
import com.liujun.auto.generator.builder.ddd.custom.def.facade.GenerateJavaCheck;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.utils.GeneratePathUtils;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

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
public class JavaCodeFacadeCheckCreate implements GenerateCodeInf {

  private static final String NAME_SUFFIX = "Check";
  private static final String NAME_COMMENT = "参数校验";

  public static final JavaCodeFacadeCheckCreate INSTANCE = new JavaCodeFacadeCheckCreate();

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
          PkgBuildMethod.classInfoComment(javaPackageStr, className, NAME_COMMENT);

      param.putPkg(tableName, GenerateCodePackageKey.INTERFACE_CHECK_PARAM, paramCheckPkg);

      // 获取实体信息
      ImportPackageInfo transferPackageInfo =
          param.getPkg(tableName, GenerateCodePackageKey.INTERFACE_OBJECT);

      // 获取实体信息
      ImportPackageInfo errorCodePkg =
          param.getPkg(tableName, GenerateCodePackageKey.INTERFACE_ERROR_CODE);

      // 获取参数校验的常量类
      ImportPackageInfo constantPackage =
          param.getPkg(tableName, GenerateCodePackageKey.INTERFACE_ERROR_CONSTANT);

      // 进行转换方法的生成
      StringBuilder sb =
          GenerateJavaCheck.INSTANCE.generateCheck(
              paramCheckPkg,
              param.getGenerateConfig().getGenerate().getMethodList(),
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
