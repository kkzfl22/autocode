package com.liujun.micro.autocode.generator.builder.operator.ddd.full;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.code.GenerateJavaFacadeAction;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaCommentUtil;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * API层的实现，用于实现对外的接口服务
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeInterfaceFacadeApiCreate implements GenerateCodeInf {

  private static final String NAME_SUFFIX = "Api";
  private static final String CLASS_COMMENT = "的API服务接口";

  public static final JavaCodeInterfaceFacadeApiCreate INSTANCE =
      new JavaCodeInterfaceFacadeApiCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {
    Map<String, TableInfoDTO> tableMap = param.getTableMap();
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
          new ImportPackageInfo(
              javaPackageStr, className, docComment, JavaVarName.SPRING_INSTANCE_NAME);
      ImportPackageUtils.putPackageInfo(
          tableName,
          param.getPackageMap(),
          GenerateCodePackageKey.INTERFACE_FACADE_INTERFACE.getKey(),
          applicationServicePackage,
          tableMap.size());
      Map<String, ImportPackageInfo> packageMap = param.getPackageMap().get(tableName);

      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getSrcJavaNode().outPath();
      javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

      // api的接口
      StringBuilder sb =
          GenerateJavaFacadeAction.INSTANCE.generateAction(
              packageMap,
              param.getGenerateConfig().getGenerate().getCode(),
              param.getGenerateConfig().getGenerate().getAuthor());

      // 进行存储层的接口输出
      GenerateOutFileUtils.outJavaFile(
          sb, GeneratePathUtils.outApiPath(param), javaPackageStr, className);
    }
  }
}
