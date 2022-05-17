package com.liujun.auto.generator.run.builder;

import com.liujun.auto.config.generate.entity.GenerateConfigEntity;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.ddd.custom.view.facade.JavaCodeFacadeCreate;
import com.liujun.auto.generator.builder.ddd.custom.view.facade.JavaCodeFacadeParamValidCreate;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.full.application.JavaCodeApplicationDependency;
import com.liujun.auto.generator.builder.ddd.full.application.JavaCodeApplicationServiceCreate;
import com.liujun.auto.generator.builder.ddd.full.domain.CodeDDDomainObjectCreate;
import com.liujun.auto.generator.builder.ddd.full.facade.JavaCodeFacadeAssemblerCreate;
import com.liujun.auto.generator.builder.ddd.full.facade.JavaCodeFacadeCheckCreate;
import com.liujun.auto.generator.builder.ddd.full.facade.JavaCodeFacadeConstantCreate;
import com.liujun.auto.generator.builder.ddd.full.facade.JavaCodeFacadeDependency;
import com.liujun.auto.generator.builder.ddd.full.facade.JavaCodeFacadeErrorCodeCreate;
import com.liujun.auto.generator.builder.ddd.full.facade.JavaCodeFacadeJunitCreate;
import com.liujun.auto.generator.builder.ddd.full.facade.JavaCodeFacadeObjectCreate;
import com.liujun.auto.generator.builder.ddd.full.other.JavaCodeProjectCfgCopyCreate;
import com.liujun.auto.generator.builder.ddd.full.other.JavaCodeProjectMavenPomCreate;
import com.liujun.auto.generator.builder.ddd.full.other.JavaCodeResourceI18nEnUsCreate;
import com.liujun.auto.generator.builder.ddd.full.other.JavaCodeResourceI18nZhCnCreate;
import com.liujun.auto.generator.builder.ddd.full.repositorymyatbisplus.CodeDDDRepositoryMyBatisPlusConverterCreate;
import com.liujun.auto.generator.builder.ddd.full.repositorymyatbisplus.CodeDDDRepositoryMyBatisPlusMapperCreate;
import com.liujun.auto.generator.builder.ddd.full.repositorymyatbisplus.CodeDDDRepositoryMyBatisPlusObjectCreate;
import com.liujun.auto.generator.builder.ddd.full.repositorymybatis.CodeDDDRepositoryMyBatisObjectCreate;
import com.liujun.auto.generator.builder.ddd.full.sql.OracleOutputDataToSqlCreate;
import com.liujun.auto.generator.builder.ddd.full.sql.OracleOutputSchemaToSqlCreate;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableIndexDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.database.service.DatabaseOperator;
import com.liujun.auto.generator.run.constant.GenerateScopeEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成的构建器
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateCodeBuilder {

  /** 范围的执行的map */
  private static final Map<GenerateScopeEnum, List<GenerateCodeInf>> SCOPE_MAP =
      new HashMap<>(8, 1);

  static {
    // 公共都需要执行的
    common();
    // 存储层的代码加入到生成流程中,使用mybatis
    repositoryMybatis();
    // 存储层的代码加入到生成流程中，使用mybatis-plus
    repositoryMybatisPlus();
    // 领域层的代码加入到生成器中
    domain();
    // 应用层的代码加入到生成器中
    application();
    // api层的代码加入到生成器
    api();

    // 数据库结构转换
    parseDb();

    // 数据库数据导出为标准的SQL
    mysqlOutput();
  }

  /** 将数据库中的数据导出为标准的SQL */
  private static void mysqlOutput() {
    List<GenerateCodeInf> dataList = new ArrayList<>(4);

    // 数据导出成标准的SQL
    dataList.add(OracleOutputDataToSqlCreate.INSTANCE);

    // 存储层的集合
    SCOPE_MAP.put(GenerateScopeEnum.MYSQL_OUTPUT, dataList);
  }

  /** 存储层代码生成的加入 */
  private static void common() {
    List<GenerateCodeInf> commonList = new ArrayList<>(4);

    // 1,项目配制文件的拷贝
    commonList.add(JavaCodeProjectCfgCopyCreate.INSTANCE);
    // 2,项目的maven的文件的生成
    commonList.add(JavaCodeProjectMavenPomCreate.INSTANCE);

    // 存储层的集合
    SCOPE_MAP.put(GenerateScopeEnum.COMMON, commonList);
  }

  private static void parseDb() {
    List<GenerateCodeInf> commonList = new ArrayList<>(2);

    // 1,项目配制文件的拷贝
    commonList.add(OracleOutputSchemaToSqlCreate.INSTANCE);

    // 存储层的集合
    SCOPE_MAP.put(GenerateScopeEnum.PARSE_DB, commonList);
  }

  /** 存储层代码生成的加入,使用mybatis */
  private static void repositoryMybatis() {
    List<GenerateCodeInf> repositoryList = new ArrayList<>(16);

    // 1,存储层实体的生成
    // repositoryList.add(JavaCodeRepositoryMyBatisObjectCreate.INSTANCE);
    repositoryList.add(CodeDDDRepositoryMyBatisObjectCreate.INSTANCE);

    //// 2,生成mapper对象
    // repositoryList.add(JavaCodeRepositoryMyBatisMapperInfCreate.INSTANCE);
    //// 3,mybatis的mapper文件
    // repositoryList.add(JavaCodeRepositoryMyBatisMapperXmlCreate.INSTANCE);
    //// 4,生成测试的配制文件
    // repositoryList.add(JavaCodeRepositoryMyBatisJunitScanConfigCreate.INSTANCE);
    //// 5，存储层的单元测试
    // repositoryList.add(JavaCodeRepositoryMyBatisJunitDaoCreate.INSTANCE);
    //// 4，存储层与领域层的实体转换
    // repositoryList.add(JavaCodeRepositoryMybatisConverterCreate.INSTANCE);
    //// 5,存储层接口的接口实现调用
    // repositoryList.add(JavaCodeRepositoryMyBatisImplementCreate.INSTANCE);

    // 存储层的集合
    SCOPE_MAP.put(GenerateScopeEnum.REPOSITORY_MYBATIS, repositoryList);
  }

  /** 存储层代码生成的加入,使用mybatis */
  private static void repositoryMybatisPlus() {
    List<GenerateCodeInf> repositoryList = new ArrayList<>(16);

    // 1.生成mybatis-plus的实体
    // repositoryList.add(JavaCodeRepositoryObjectCreate.INSTANCE);
    repositoryList.add(CodeDDDRepositoryMyBatisPlusObjectCreate.INSTANCE);
    //// 2.生成mybatis-plus的mapper的接口文件
    repositoryList.add(CodeDDDRepositoryMyBatisPlusMapperCreate.INSTANCE);

    // 2，存储层与领域层的实体转换
    repositoryList.add(CodeDDDRepositoryMyBatisPlusConverterCreate.INSTANCE);

    //// 生成xml文件
    // repositoryList.add(JavaCodeRepositoryMapperXmlCreate.INSTANCE);
    //// 生成单元测试配制文件
    // repositoryList.add(JavaCodeRepositoryJunitScanCfgCreate.INSTANCE);
    //// 生成单元测试文件
    // repositoryList.add(JavaCodeRepositoryJunitDaoCreate.INSTANCE);
    //// 存储层与领域层的实体转换
    // repositoryList.add(JavaCodeRepositoryConverterCreate.INSTANCE);
    //// 存储层接口的接口实现调用
    // repositoryList.add(JavaCodeRepositoryImplementCreate.INSTANCE);

    // 存储层的集合
    SCOPE_MAP.put(GenerateScopeEnum.REPOSITORY_MYBATIS_PLUS, repositoryList);
  }

  /** 领域层相关代码的生成 */
  private static void domain() {
    List<GenerateCodeInf> domainList = new ArrayList<>(8);

    // 基础依赖
    //  domainList.add(JavaCodeDomainDependency.INSTANCE);

    // 1,生成领域层的实体
    domainList.add(CodeDDDomainObjectCreate.INSTANCE);
    //// 2,领域的存储层接口
    // domainList.add(JavaCodeDomainRepositoryFacadeCreate.INSTANCE);
    //// 3,生成领域层的服务
    // domainList.add(JavaCodeDomainServiceCreate.INSTANCE);
    //
    //// 6.生成单元测试的mock存储实现
    //
    //// 6，领域层的单元测试
    // domainList.add(JavaCodeDomainJunitServiceCreate.INSTANCE);

    // 存储层的集合
    SCOPE_MAP.put(GenerateScopeEnum.DOMAIN, domainList);
  }

  /** 应用层的代码生成 */
  private static void application() {
    List<GenerateCodeInf> applicationList = new ArrayList<>(4);
    // 1,应用层的基础依赖
    applicationList.add(JavaCodeApplicationDependency.INSTANCE);
    // 2,生成应用层的代码
    applicationList.add(JavaCodeApplicationServiceCreate.INSTANCE);

    // 存储层的集合
    SCOPE_MAP.put(GenerateScopeEnum.APPLICATION, applicationList);
  }

  /** api层的相关代码 */
  private static void api() {
    List<GenerateCodeInf> apiList = new ArrayList<>(16);
    // 进行api依赖相关信息的生成
    apiList.add(JavaCodeFacadeDependency.INSTANCE);
    // 参数校验类生成
    apiList.add(JavaCodeFacadeParamValidCreate.INSTANCE);
    // 1,api层的实体的生成
    apiList.add(JavaCodeFacadeObjectCreate.INSTANCE);
    // 2,api层的数据转换层
    apiList.add(JavaCodeFacadeAssemblerCreate.INSTANCE);
    // 3,api层的常量定义
    apiList.add(JavaCodeFacadeConstantCreate.INSTANCE);
    // 4,api层的错误码定义
    apiList.add(JavaCodeFacadeErrorCodeCreate.INSTANCE);
    // 5,api层的错误码检查
    apiList.add(JavaCodeFacadeCheckCreate.INSTANCE);
    // 7,生成api的实现的代码
    apiList.add(JavaCodeFacadeCreate.INSTANCE);
    // 8,国际化中文相关的资源文件的生成
    apiList.add(JavaCodeResourceI18nZhCnCreate.INSTANCE);
    // 9,国际化英文相关的资源文件的生成
    apiList.add(JavaCodeResourceI18nEnUsCreate.INSTANCE);
    // 11,api的单元测试
    apiList.add(JavaCodeFacadeJunitCreate.INSTANCE);

    // 存储层的集合
    SCOPE_MAP.put(GenerateScopeEnum.API, apiList);
  }

  /** 上下文对象 */
  private GenerateCodeContext context;

  /**
   * 通过构建函数传递配制对象 构建context对象信息
   *
   * @param config 配制信息
   */
  public GenerateCodeBuilder(GenerateConfigEntity config) {
    this.context = new GenerateCodeContext(config);
  }

  /** 代码生成操作 */
  public void generate() {

    // 数据库基础库加载
    databaseLoader();

    // 1，检查当前的阶段，并添加对应的代码生成器,需要按依赖的原则，从最低层开始生成
    List<GenerateScopeEnum> scopeEnums =
        GenerateScopeEnum.generateScope(context.getGenerateConfig().getGenerate().getScope());

    // 1,进行领域层的代码生成
    generateScope(GenerateScopeEnum.DOMAIN, scopeEnums);

    // 2,进行存储层的代码生成
    generateScope(GenerateScopeEnum.REPOSITORY_MYBATIS, scopeEnums);

    // 进行存储层mybatis-plus的生成
    generateScope(GenerateScopeEnum.REPOSITORY_MYBATIS_PLUS, scopeEnums);

    // 3，应用层代码的生成
    generateScope(GenerateScopeEnum.APPLICATION, scopeEnums);

    // 4,api层的代码的生成
    generateScope(GenerateScopeEnum.API, scopeEnums);

    // 进行数据库转换脚本的生成
    generateScope(GenerateScopeEnum.PARSE_DB, scopeEnums);

    // 进行数据转换的生成
    generateScope(GenerateScopeEnum.MYSQL_OUTPUT, scopeEnums);

    // 5，公共的的文件的生成
    runGenerate(SCOPE_MAP.get(GenerateScopeEnum.COMMON));
  }

  /** 代码构建之前的数据库查询工作 */
  public void databaseLoader() {

    Map<String, TableInfoDTO> tableMap =
        DatabaseOperator.INSTANCE.getTableInfo(context.getTableSpaceName());

    if (null == tableMap || tableMap.isEmpty()) {
      throw new IllegalArgumentException("table is empty,please check!");
    }

    // 读取数据库中表信息
    context.setTableMap(tableMap);

    Map<String, List<TableColumnDTO>> tableColumnList =
        DatabaseOperator.INSTANCE.getColumnInfo(context.getTableSpaceName());
    // 读取数据库中列信息
    context.setColumnMapList(tableColumnList);

    Map<String, Map<String, TableIndexDTO>> tableIndexMap =
        DatabaseOperator.INSTANCE.getTableIndex(context.getTableSpaceName());

    // 读取表中的索引信息
    context.setTableIndexMap(tableIndexMap);

    // 进行表列的设置操作
    TableInfoDTO.setColumn(tableMap, tableColumnList);
    // 设置表的索引信息
    TableInfoDTO.setIndex(tableMap, context.getTableIndexMap());

    // 设置二层map，一层key为表名，二层key为列名
    context.setColumnMap(DatabaseOperator.INSTANCE.parseColumnMap(tableColumnList));
  }

  public GenerateCodeContext getContext() {
    return context;
  }

  /**
   * 指定阶段的代码进行生成操作
   *
   * @param scope 范围信息
   * @param scopeEnums 当前定义的生成枚举的阶段
   */
  private void generateScope(GenerateScopeEnum scope, List<GenerateScopeEnum> scopeEnums) {

    // 1,检查当前阶段是否配制，未配制，则不生成
    if (!checkScope(scope, scopeEnums)) {
      return;
    }

    // 执行生成
    runGenerate(SCOPE_MAP.get(scope));
  }

  /**
   * 项目代码生成的执行
   *
   * @param runList
   */
  private void runGenerate(List<GenerateCodeInf> runList) {
    // 执行代码的生成操作
    for (GenerateCodeInf scopeItem : runList) {
      scopeItem.generateCode(context);
    }
  }

  /**
   * 检查当前生成的是否存在
   *
   * @param scope
   * @param scopeEnums
   * @return
   */
  private boolean checkScope(GenerateScopeEnum scope, List<GenerateScopeEnum> scopeEnums) {
    for (GenerateScopeEnum scopeItem : scopeEnums) {
      if (scopeItem.equals(scope)) {
        return true;
      }
    }

    return false;
  }
}
