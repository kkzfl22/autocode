package com.liujun.auto.generator.builder.ddd.entity;

import com.liujun.auto.config.generate.entity.Generate;
import com.liujun.auto.config.generate.entity.GenerateConfigEntity;
import com.liujun.auto.generator.builder.ddd.config.MenuTreeCodePackage;
import com.liujun.auto.generator.builder.ddd.config.MenuTreeProjectPath;
import com.liujun.auto.generator.database.constant.DatabaseTypeEnum;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableIndexDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.builder.ddd.constant.GenerateCodePackageKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 额头于进行代码生成的上下文对象,每个生成器对象共享
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateCodeContext {

  /** 文件的基本路径 */
  private String fileBasePath;

  /** java的包的基本命名空间 */
  private String javaPackage;

  /** 当前模块的名称 */
  private String moduleName;

  /** 当前领域的名称 */
  private String domainName;

  /** mybatis的namespace信息 */
  private String mybatisBaseSpace;

  /** 表空间名称 */
  private String tableSpaceName;

  /** 当前的数据库类型 */
  private DatabaseTypeEnum typeEnum;

  /** 列描述的map信息 */
  private Map<String, List<TableColumnDTO>> columnMapList;

  /** 以表名为key，再以列名为key */
  private Map<String, Map<String, TableColumnDTO>> columnMap;

  /** 表的索引map,一级表名为key，二级索引名为key */
  private Map<String, Map<String, TableIndexDTO>> tableIndexMap;

  /** 表的描述的map信息 */
  private Map<String, TableInfoDTO> tableMap;

  /** 配制信息 */
  private GenerateConfigEntity generateConfig;

  /** 公共的临时参数 */
  private Map<String, Object> dataMap = new HashMap<>();

  /** java代码的路径包信息 */
  private MenuTreeCodePackage javaCodePackage;

  /** 项目路径信息 */
  private MenuTreeProjectPath projectPath;

  /** 用来存储需要导包的信息 */
  private Map<String, Map<String, ImportPackageInfo>> packageMap = new HashMap<>();

  /** 生成的相关配制信息 */
  private Generate generateCfg;

  public GenerateCodeContext(GenerateConfigEntity configEntity) {
    this.fileBasePath = configEntity.getGenerate().getOutput();
    this.javaPackage = configEntity.getGenerate().getCodeMenuTree().getBaseDir();
    this.moduleName = configEntity.getGenerate().getModuleName();
    this.domainName = configEntity.getGenerate().getCodeMenuTree().getDomainName();
    // 类型枚举
    this.typeEnum = DatabaseTypeEnum.getDbType(configEntity.getGenerate().getDatabaseType());
    this.mybatisBaseSpace = javaPackage;
    this.tableSpaceName = configEntity.getGenerate().getDatabaseTableSpaceName();
    this.generateConfig = configEntity;
    // 构建代码的目录树
    this.javaCodePackage = MenuTreeCodePackage.INSTANCE;
    // 构建项目目录树
    this.projectPath = MenuTreeProjectPath.INSTANCE;
    this.generateCfg = configEntity.getGenerate();
  }

  public String getFileBasePath() {
    return fileBasePath;
  }

  public void setFileBasePath(String fileBasePath) {
    this.fileBasePath = fileBasePath;
  }

  public String getJavaPackage() {
    return javaPackage;
  }

  public void setJavaPackage(String javaPackage) {
    this.javaPackage = javaPackage;
  }

  public String getModuleName() {
    return moduleName;
  }

  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }

  public String getDomainName() {
    return domainName;
  }

  public void setDomainName(String domainName) {
    this.domainName = domainName;
  }

  public String getMybatisBaseSpace() {
    return mybatisBaseSpace;
  }

  public void setMybatisBaseSpace(String mybatisBaseSpace) {
    this.mybatisBaseSpace = mybatisBaseSpace;
  }

  public String getTableSpaceName() {
    return tableSpaceName;
  }

  public void setTableSpaceName(String tableSpaceName) {
    this.tableSpaceName = tableSpaceName;
  }

  public DatabaseTypeEnum getTypeEnum() {
    return typeEnum;
  }

  public void setTypeEnum(DatabaseTypeEnum typeEnum) {
    this.typeEnum = typeEnum;
  }

  public Map<String, List<TableColumnDTO>> getColumnMapList() {
    return columnMapList;
  }

  public void setColumnMapList(Map<String, List<TableColumnDTO>> columnMapList) {
    this.columnMapList = columnMapList;
  }

  public Map<String, Map<String, TableColumnDTO>> getColumnMap() {
    return columnMap;
  }

  public void setColumnMap(Map<String, Map<String, TableColumnDTO>> columnMap) {
    this.columnMap = columnMap;
  }

  public Map<String, TableInfoDTO> getTableMap() {
    return tableMap;
  }

  public void setTableMap(Map<String, TableInfoDTO> tableMap) {
    this.tableMap = tableMap;
  }

  public GenerateConfigEntity getGenerateConfig() {
    return generateConfig;
  }

  public void setGenerateConfig(GenerateConfigEntity generateConfig) {
    this.generateConfig = generateConfig;
  }

  public Map<String, Object> getDataMap() {
    return dataMap;
  }

  public void setDataMap(Map<String, Object> dataMap) {
    this.dataMap = dataMap;
  }

  public MenuTreeCodePackage getJavaCodePackage() {
    return javaCodePackage;
  }

  public void setJavaCodePackage(MenuTreeCodePackage javaCodePackage) {
    this.javaCodePackage = javaCodePackage;
  }

  public MenuTreeProjectPath getProjectPath() {
    return projectPath;
  }

  public void setProjectPath(MenuTreeProjectPath projectPath) {
    this.projectPath = projectPath;
  }

  public Generate getGenerateCfg() {
    return generateCfg;
  }

  public void setGenerateCfg(Generate generateCfg) {
    this.generateCfg = generateCfg;
  }

  public Map<String, Map<String, TableIndexDTO>> getTableIndexMap() {
    return tableIndexMap;
  }

  public void setTableIndexMap(Map<String, Map<String, TableIndexDTO>> tableIndexMap) {
    this.tableIndexMap = tableIndexMap;
  }

  /**
   * 获取包数所信息，按定义的枚举值
   *
   * @param tableName 表名
   * @param key 固定的枚举key
   * @return 包路径定义信息
   */
  public ImportPackageInfo getPkg(String tableName, GenerateCodePackageKey key) {
    Map<String, ImportPackageInfo> pkgMap = packageMap.get(tableName);
    if (dataMap != null) {
      return pkgMap.get(key.getKey());
    }

    return null;
  }

  /**
   * 向容器中放入对象
   *
   * @param tableName 表名
   * @param key 参数的key
   * @param pkgInfo 放入的文件定义信息
   */
  public void putPkg(String tableName, GenerateCodePackageKey key, ImportPackageInfo pkgInfo) {
    Map<String, ImportPackageInfo> pkgMap = packageMap.get(tableName);
    if (pkgMap == null) {
      pkgMap = new HashMap<>(32);
      packageMap.put(tableName, pkgMap);
    }
    pkgMap.put(key.getKey(), pkgInfo);
  }

  /**
   * 获取校验参数类的信息
   *
   * @param tableName 表名
   * @param key 参数key
   * @return
   */
  public ImportPackageInfo getPkg(String tableName, String key) {
    Map<String, ImportPackageInfo> pkgMap = packageMap.get(tableName);
    if (dataMap != null) {
      return pkgMap.get(key);
    }

    return null;
  }

  /**
   * 放入校验参数包信息
   *
   * @param tableName
   * @param key
   * @param pkgInfo
   */
  public void putPkg(String tableName, String key, ImportPackageInfo pkgInfo) {
    Map<String, ImportPackageInfo> pkgMap = packageMap.get(tableName);
    if (pkgMap == null) {
      pkgMap = new HashMap<>(32);
      packageMap.put(tableName, pkgMap);
    }
    pkgMap.put(key, pkgInfo);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("GenerateCodeContext{");
    sb.append("fileBasePath='").append(fileBasePath).append('\'');
    sb.append(", javaPackage='").append(javaPackage).append('\'');
    sb.append(", moduleName='").append(moduleName).append('\'');
    sb.append(", domainName='").append(domainName).append('\'');
    sb.append(", mybatisBaseSpace='").append(mybatisBaseSpace).append('\'');
    sb.append(", tableSpaceName='").append(tableSpaceName).append('\'');
    sb.append(", typeEnum=").append(typeEnum);
    sb.append(", columnMapList=").append(columnMapList);
    sb.append(", columnMapMap=").append(columnMap);
    sb.append(", tableMap=").append(tableMap);
    sb.append(", generateConfig=").append(generateConfig);
    sb.append(", dataMap=").append(dataMap);
    sb.append(", javaCodePackage=").append(javaCodePackage);
    sb.append(", projectPath=").append(projectPath);
    sb.append(", packageMap=").append(packageMap);
    sb.append(", generateCfg=").append(generateCfg);
    sb.append(", tableIndexMap=").append(tableIndexMap);
    sb.append('}');
    return sb.toString();
  }
}
