package com.liujun.auto.generator.builder.ddd.custom.view.facade;

import com.liujun.auto.generator.builder.ddd.custom.view.generatecode.GenerateJavaViewAction;
import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.JavaVarName;
import com.liujun.auto.generator.builder.ddd.constant.PkgBuildMethod;
import com.liujun.auto.generator.builder.ddd.entity.DataParam;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.utils.GeneratePathUtils;
import com.liujun.auto.generator.builder.utils.JavaCommentUtil;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * API层的实现，用于实现对外的接口服务
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeFacadeCreate implements GenerateCodeInf {

  private static final String NAME_SUFFIX = "Facade";
  private static final String CLASS_COMMENT = "的API服务";

  public static final JavaCodeFacadeCreate INSTANCE = new JavaCodeFacadeCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Map.Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();
    while (tableNameEntry.hasNext()) {
      Map.Entry<String, List<TableColumnDTO>> tableNameItem = tableNameEntry.next();
      // 获取表信息
      TableInfoDTO tableInfo = param.getTableMap().get(tableNameItem.getKey());

      // 表名
      String tableName = tableNameItem.getKey();

      // 得到类名
      String tableClassName = NameProcess.INSTANCE.toJavaClassName(tableName);
      String className = tableClassName + NAME_SUFFIX;

      // 注释
      String docComment =
          JavaCommentUtil.tableCommentProc(tableInfo.getTableComment()) + CLASS_COMMENT;

      // 获取以java的接口对象
      String javaPackageStr = param.getJavaCodePackage().getInterfaceFacadeNode().outJavaPackage();

      // 将领域的存储实现存至流程中
      ImportPackageInfo applicationServicePackage =
          PkgBuildMethod.classInfoVarInfo(
              javaPackageStr, className, docComment, JavaVarName.FACADE_INSTANCE_NAME);
      param.putPkg(tableName, GenerateCodePackageKey.INTERFACE_FACADE, applicationServicePackage);

      // 构建参数信息
      DataParam generateParam =
          DataParam.builder()
              .putPkg(
                  GenerateCodePackageKey.DOMAIN_DO,
                  param.getPkg(tableName, GenerateCodePackageKey.DOMAIN_DO))
              .putPkg(
                  GenerateCodePackageKey.APPLICATION_SERVICE,
                  param.getPkg(tableName, GenerateCodePackageKey.APPLICATION_SERVICE))
              .putPkg(
                  GenerateCodePackageKey.INTERFACE_FACADE,
                  param.getPkg(tableName, GenerateCodePackageKey.INTERFACE_FACADE))
              .putPkg(
                  GenerateCodePackageKey.INTERFACE_CHECK_PARAM,
                  param.getPkg(tableName, GenerateCodePackageKey.INTERFACE_CHECK_PARAM))
              .putPkg(
                  GenerateCodePackageKey.INTERFACE_OBJECT,
                  param.getPkg(tableName, GenerateCodePackageKey.INTERFACE_OBJECT))
              .putPkg(
                  GenerateCodePackageKey.INTERFACE_ASSEMBLER,
                  param.getPkg(tableName, GenerateCodePackageKey.INTERFACE_ASSEMBLER))
              // 加入所有包信息
              .putPkg(getMethodCheck(param, tableName))
              .author(param.getGenerateConfig().getGenerate().getAuthor())
              .methodList(param.getGenerateConfig().getGenerate().getMethodList())
              .tableInfo(tableInfo)
              .databaseType(param.getTypeEnum())
              .columnList(tableNameItem.getValue())
              .build();

      // api的接口实现
      StringBuilder sb = GenerateJavaViewAction.INSTANCE.generateAction(generateParam);

      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getSrcJavaNode().outPath();
      javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

      // 进行存储层的接口输出
      GenerateOutFileUtils.outJavaFile(
          sb, GeneratePathUtils.outServicePath(param), javaPackageStr, className);
    }
  }

  /**
   * 获取需要导入的数据
   *
   * @param param 参数信息
   * @param tableName 表名
   * @return map信息
   */
  public Map<String, ImportPackageInfo> getMethodCheck(
      GenerateCodeContext param, String tableName) {

    List<MethodInfo> methodList = param.getGenerateConfig().getGenerate().getMethodList();
    Map<String, ImportPackageInfo> dataResult = new HashMap<>(methodList.size());

    for (MethodInfo methodItem : methodList) {
      ImportPackageInfo dataImport = param.getPkg(tableName, methodItem.getName());

      if (null != dataImport) {
        dataResult.put(methodItem.getName(), dataImport);
      }
    }

    return dataResult;
  }
}
