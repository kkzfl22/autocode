package com.liujun.micro.autocode.generator.builder.operator.ddd.full;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.entity.config.MethodInfo;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCommImport;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.code.junit.GenerateJunitDefine;
import com.liujun.micro.autocode.generator.builder.operator.ddd.code.GenerateJavaDomainJunitService;
import com.liujun.micro.autocode.generator.builder.operator.ddd.code.GenerateJavaRepositoryJunitDao;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.TableColumnUtils;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 生成领域服务的单元测试
 *
 * @since 2018年4月15日 下午4:11:42
 * @version 0.0.1
 * @author liujun
 */
public class JavaCodeDomainJunitServiceCreate implements GenerateCodeInf {

  /** 注释 */
  private static final String DATABASE_DOC = "领域服务的单元测试";

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();
    while (tableNameEntry.hasNext()) {
      Entry<String, List<TableColumnDTO>> entry = tableNameEntry.next();
      List<TableColumnDTO> columnList = entry.getValue();
      String tableName = entry.getKey();

      // 获取当前主键列表
      List<TableColumnDTO> primaryKeyList = TableColumnUtils.getPrimaryKey(columnList);

      // 获取所有列的信息
      Map<String, TableColumnDTO> tableColumnMap = param.getColumnMapMap().get(entry.getKey());

      // 获取domain领域对象实体
      ImportPackageInfo domainPackage =
          ImportPackageUtils.getDefineClass(
              param.getPackageMap(), GenerateCodePackageKey.DOMAIN_DO.getKey(), tableName);

      // 获取领域层的服务对象
      ImportPackageInfo domainServicePackage =
          ImportPackageUtils.getDefineClass(
              param.getPackageMap(), GenerateCodePackageKey.DOMAIN_SERVICE.getKey(), tableName);

      // 首字母大写
      String className =
          GenerateJunitDefine.TEST_SUFFIX_NAME
              + NameProcess.INSTANCE.toJavaNameFirstUpper(domainServicePackage.getClassName());

      // 获取以领域服务的路径信息
      String javaDaoPackageStr = param.getJavaCodePackage().getDomainServiceNode().outJavaPackage();

      // 包信息
      ImportPackageInfo junitDomainServicePkg =
          new ImportPackageInfo(javaDaoPackageStr, className, DATABASE_DOC);

      // 单元测试的父类的路径
      String junitParentPackageStr = param.getJavaCodePackage().getPkgRoot().outJavaPackage();

      // 包信息
      ImportPackageInfo junitParent =
          new ImportPackageInfo(
              junitParentPackageStr, GenerateCommImport.JUNIT_PARENT_NAME, Symbol.EMPTY);

      // 方法信息
      List<MethodInfo> methodList = param.getGenerateConfig().getGenerate().getCode();

      // 进行单元测试代码的生成
      StringBuilder sb =
          GenerateJavaDomainJunitService.INSTANCE.generateJunitService(
              junitParent,
              domainPackage,
              domainServicePackage,
              junitDomainServicePkg,
              param.getTypeEnum(),
              columnList,
              primaryKeyList,
              methodList,
              tableColumnMap,
              param.getGenerateConfig().getGenerate().getAuthor());

      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getTestJavaNode().outPath();
      javaDaoPackageStr = baseJavaPath + Symbol.PATH + javaDaoPackageStr;

      // 将数据库存储的文件输出
      GenerateOutFileUtils.outJavaFile(sb, param.getFileBasePath(), javaDaoPackageStr, className);
    }
  }
}