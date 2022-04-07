package com.liujun.auto.generator.builder.operator.ddd.full.facade;

import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.constant.GenerateCommConstant;
import com.liujun.auto.generator.builder.constant.ImportJunitPkgKey;
import com.liujun.auto.generator.builder.constant.PkgBuildMethod;
import com.liujun.auto.generator.builder.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.operator.GenerateCodeInf;
import com.liujun.auto.generator.builder.operator.code.junit.GenerateJunitDefine;
import com.liujun.auto.generator.builder.operator.ddd.custom.def.facade.GenerateJavaFacadeJunitApi;
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
 * api层的单元测试
 *
 * @author liujun
 * @version 0.0.1
 * @since 2018年4月15日 下午4:11:42
 */
public class JavaCodeFacadeJunitCreate implements GenerateCodeInf {

  /** 注释 */
  private static final String DATABASE_DOC = "api的单元测试";

  public static final JavaCodeFacadeJunitCreate INSTANCE = new JavaCodeFacadeJunitCreate();

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
      ImportPackageInfo facadePackage =
          param.getPkg(tableName, GenerateCodePackageKey.INTERFACE_OBJECT);

      // 获取web
      ImportPackageInfo interfaceFacadePackage =
          param.getPkg(tableName, GenerateCodePackageKey.INTERFACE_FACADE);

      // 首字母大写
      String className =
          GenerateJunitDefine.TEST_SUFFIX_NAME
              + NameProcess.INSTANCE.toJavaNameFirstUpper(interfaceFacadePackage.getClassName());

      // 获取以领域服务的路径信息
      String interfaceFacadePackageStr =
          param.getJavaCodePackage().getInterfaceFacadeNode().outJavaPackage();

      // 包信息
      ImportPackageInfo junitDomainServicePkg =
          PkgBuildMethod.classInfoComment(interfaceFacadePackageStr, className, DATABASE_DOC);

      // 方法信息
      List<MethodInfo> methodList = param.getGenerateConfig().getGenerate().getMethodList();

      // 获取dao的完整路径
      ImportPackageInfo mybatisScanConfig =
          param.getPkg(
              GenerateCommConstant.GENERATE_JUNIT_CONFIG,
              GenerateCodePackageKey.REPOSITORY_MAPPER_CONFIG);

      // 进行单元测试代码的生成
      StringBuilder sb =
          GenerateJavaFacadeJunitApi.INSTANCE.generateJunitService(
              facadePackage,
              interfaceFacadePackage,
              junitDomainServicePkg,
              param.getTypeEnum(),
              columnList,
              primaryKeyList,
              methodList,
              tableColumnMap,
              getDependencyList(mybatisScanConfig),
              mybatisScanConfig,
              getRunImport(param, tableName),
              param.getGenerateConfig().getGenerate().getAuthor());

      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getTestJavaNode().outPath();
      interfaceFacadePackageStr = baseJavaPath + Symbol.PATH + interfaceFacadePackageStr;

      // 将数据库存储的文件输出
      GenerateOutFileUtils.outJavaFile(
          sb, GeneratePathUtils.outServicePath(param), interfaceFacadePackageStr, className);
    }
  }

  /**
   * 获取导入的包信息
   *
   * @param param 存储我包结构信息
   * @param tableName 表名
   * @return 集合
   */
  private List<ImportPackageInfo> getRunImport(GenerateCodeContext param, String tableName) {
    // 存储层实体
    ImportPackageInfo repositoryPackageInfo =
        param.getPkg(tableName, GenerateCodePackageKey.PERSIST_PERSISTENCE);

    // 存储层实体
    ImportPackageInfo domainServicePackageInfo =
        param.getPkg(tableName, GenerateCodePackageKey.DOMAIN_SERVICE);

    // 应用层实体
    ImportPackageInfo applicationServicePackageInfo =
        param.getPkg(tableName, GenerateCodePackageKey.APPLICATION_SERVICE);

    // api层的实现
    ImportPackageInfo facadePackageInfo =
        param.getPkg(tableName, GenerateCodePackageKey.INTERFACE_FACADE);

    return Arrays.asList(
        repositoryPackageInfo,
        domainServicePackageInfo,
        applicationServicePackageInfo,
        facadePackageInfo);
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
