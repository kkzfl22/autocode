package com.liujun.auto.generator.builder.operator.ddd.full.domain;

import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.constant.GenerateCommConstant;
import com.liujun.auto.generator.builder.constant.ImportJunitPkgKey;
import com.liujun.auto.generator.builder.constant.PkgBuildMethod;
import com.liujun.auto.generator.builder.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.operator.GenerateCodeInf;
import com.liujun.auto.generator.builder.operator.base.GenerateJavaDomainJunitService;
import com.liujun.auto.generator.builder.operator.code.junit.GenerateJunitDefine;
import com.liujun.auto.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.auto.generator.builder.operator.utils.TableColumnUtils;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 生成领域服务的单元测试
 *
 * @author liujun
 * @version 0.0.1
 * @since 2018年4月15日 下午4:11:42
 */
public class JavaCodeDomainJunitServiceCreate implements GenerateCodeInf {

  /** 注释 */
  private static final String DATABASE_DOC = "领域服务的单元测试";

  public static final JavaCodeDomainJunitServiceCreate INSTANCE =
      new JavaCodeDomainJunitServiceCreate();

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
      ImportPackageInfo domainPackage = param.getPkg(tableName, GenerateCodePackageKey.DOMAIN_DO);

      // 获取领域层的服务对象
      ImportPackageInfo domainServicePackage =
          param.getPkg(tableName, GenerateCodePackageKey.DOMAIN_SERVICE);

      // 首字母大写
      String className =
          GenerateJunitDefine.TEST_SUFFIX_NAME
              + NameProcess.INSTANCE.toJavaNameFirstUpper(domainServicePackage.getClassName());

      // 获取以领域服务的路径信息
      String javaDaoPackageStr = param.getJavaCodePackage().getDomainServiceNode().outJavaPackage();

      // 包信息
      ImportPackageInfo junitDomainServicePkg =
          PkgBuildMethod.classInfoComment(javaDaoPackageStr, className, DATABASE_DOC);

      // 获取dao的完整路径
      ImportPackageInfo mybatisScanConfig =
          param.getPkg(
              GenerateCommConstant.GENERATE_JUNIT_CONFIG,
              GenerateCodePackageKey.REPOSITORY_MAPPER_CONFIG);

      // 存储层实体
      ImportPackageInfo repositoryPackageInfo =
          param.getPkg(tableName, GenerateCodePackageKey.PERSIST_PERSISTENCE);

      // 存储层实体
      ImportPackageInfo domainServicePackageInfo =
          param.getPkg(tableName, GenerateCodePackageKey.DOMAIN_SERVICE);

      // 方法信息
      List<MethodInfo> methodList = param.getGenerateConfig().getGenerate().getMethodList();

      // 进行单元测试代码的生成
      StringBuilder sb =
          GenerateJavaDomainJunitService.INSTANCE.generateJunitService(
              domainPackage,
              domainServicePackage,
              junitDomainServicePkg,
              param.getTypeEnum(),
              columnList,
              primaryKeyList,
              methodList,
              tableColumnMap,
              getDependencyList(mybatisScanConfig),
              mybatisScanConfig,
              Arrays.asList(domainServicePackageInfo, repositoryPackageInfo),
              param.getGenerateConfig().getGenerate().getAuthor());

      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getTestJavaNode().outPath();
      javaDaoPackageStr = baseJavaPath + Symbol.PATH + javaDaoPackageStr;

      // 将数据库存储的文件输出
      GenerateOutFileUtils.outJavaFile(
          sb, GeneratePathUtils.outServicePath(param), javaDaoPackageStr, className);
    }
  }

  private List<String> getDependencyList(ImportPackageInfo mybatisScanConfig) {

    List<String> dependencyList = new ArrayList<>(4);
    // 数据源的包
    dependencyList.add(
        ImportJunitPkgKey.SPRING_BOOT_TEST_DATA_SOURCE.getPackageInfo().getClassName());
    // mybatis的自动装类
    dependencyList.add(
        ImportJunitPkgKey.SPRING_BOOT_TEST_MYBATIS_AUTO_CONFIG.getPackageInfo().getClassName());
    // mybatis的扫包
    dependencyList.add(mybatisScanConfig.getClassName());

    return dependencyList;
  }
}
