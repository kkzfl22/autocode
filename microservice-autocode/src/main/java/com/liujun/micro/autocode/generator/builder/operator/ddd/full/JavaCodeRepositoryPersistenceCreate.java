package com.liujun.micro.autocode.generator.builder.operator.ddd.full;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.ddd.code.GenerateJavaRepositoryPersistenceInvoke;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaCommentUtil;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 领域层存储接口的实现，实现与dao层的调用
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeRepositoryPersistenceCreate implements GenerateCodeInf {

  private static final String NAME_SUFFIX = "RepositoryImpl";
  private static final String CLASS_COMMENT = "的领域存储接口实现";

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

      // 获取以java定义的package路径
      String javaPackageStr =
          param.getJavaCodePackage().getRepositoryPersistenceNode().outJavaPackage();

      // 将领域的存储实现存至流程中
      ImportPackageInfo repositoryPersistPackage =
          new ImportPackageInfo(javaPackageStr, className, docComment);
      ImportPackageUtils.putPackageInfo(
          tableName,
          param.getPackageMap(),
          GenerateCodePackageKey.PERSIST_PERSISTENCE.getKey(),
          repositoryPersistPackage,
          tableMap.size());
      Map<String, ImportPackageInfo> packageMap = param.getPackageMap().get(tableName);

      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getSrcJavaNode().outPath();
      javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

      // 调用存储接口
      StringBuilder sb =
          GenerateJavaRepositoryPersistenceInvoke.INSTANCE.generateRepository(
              packageMap,
              param.getGenerateConfig().getGenerate().getCode(),
              param.getGenerateConfig().getGenerate().getAuthor());

      // 进行存储层的接口输出
      GenerateOutFileUtils.outJavaFile(sb, param.getFileBasePath(), javaPackageStr, className);
    }
  }
}