package com.liujun.auto.generator.builder.operator.ddd.full;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.auto.generator.builder.operator.utils.JavaCommentUtil;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;
import com.liujun.auto.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.operator.GenerateCodeInf;
import com.liujun.auto.generator.builder.operator.ddd.code.GenerateJavaRepositoryFacadeInterface;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 生成领域层的数据存储接口
 *
 * @author liujun
 * @version 1.0.0
 */
public class JavaCodeDomainRepositoryFacadeCreate implements GenerateCodeInf {

  public static final String SUFFIX_NAME = "RepositoryInterface";
  private static final String COMMENT = "的领域存储接口";

  public static final JavaCodeDomainRepositoryFacadeCreate INSTANCE =
      new JavaCodeDomainRepositoryFacadeCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();
    while (tableNameEntry.hasNext()) {
      Entry<String, List<TableColumnDTO>> tableNameItem = tableNameEntry.next();
      // 获取表信息
      TableInfoDTO tableInfo = param.getTableMap().get(tableNameItem.getKey());

      // 表名
      String tableName = tableNameItem.getKey();

      // 得到类名
      String tableClassName = NameProcess.INSTANCE.toJavaClassName(tableName);
      String className = tableClassName + SUFFIX_NAME;

      // 获取以java定义的package路径
      String javaPackageStr = param.getJavaCodePackage().getRepositoryFacadeNode().outJavaPackage();

      // 注释
      String docComment = JavaCommentUtil.tableCommentProc(tableInfo.getTableComment()) + COMMENT;

      // 将dao信息进行储存至流程中
      ImportPackageInfo daoPackageInfo =
          new ImportPackageInfo(javaPackageStr, className, docComment);
      ImportPackageUtils.putPackageInfo(
          tableName,
          param.getPackageMap(),
          GenerateCodePackageKey.DOMAIN_PERSIST_FACADE.getKey(),
          daoPackageInfo,
          tableMap.size());

      // 存储层实体
      ImportPackageInfo domainPackageInfo =
          ImportPackageUtils.getDefineClass(
              param.getPackageMap(), GenerateCodePackageKey.DOMAIN_DO.getKey(), tableName);

      // 进行领域方法的相关方法的生成
      StringBuilder sb =
          GenerateJavaRepositoryFacadeInterface.INSTANCE.generateJavaInterface(
              daoPackageInfo,
              domainPackageInfo,
              param.getGenerateConfig().getGenerate().getCode(),
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
