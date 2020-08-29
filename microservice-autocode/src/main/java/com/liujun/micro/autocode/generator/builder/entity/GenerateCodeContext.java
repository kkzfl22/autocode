package com.liujun.micro.autocode.generator.builder.entity;

import com.liujun.micro.autocode.config.menuTree.DomainMenuTree;
import com.liujun.micro.autocode.config.menuTree.ProjectMenuTree;
import com.liujun.micro.autocode.entity.config.GenerateConfigEntity;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 额头于进行代码生成的上下文对象
 *
 * @author liujun
 * @version 0.0.1
 */
@Data
@ToString
public class GenerateCodeContext {

  /** 文件的基本路径 */
  private String fileBasePath;

  /** java的包的基本命名空间 */
  private String javaPackage;

  /** 当前模块的名称 */
  private String modelName;

  /** mybatis的namespace信息 */
  private String mybatisBaseSpace;

  /** 表空间名称 */
  private String tableSpaceName;

  /** 列描述的map信息 */
  private Map<String, List<TableColumnDTO>> columnMapList;

  /** 以表名为key，再以列名为key */
  private Map<String, Map<String, TableColumnDTO>> columnMapMap;

  /** 表的描述的map信息 */
  private Map<String, TableInfoDTO> tableMap;

  /** 数据配制操作 */
  private GenerateConfigEntity generateConfig;

  /** 公共的临时参数 */
  private Map<String, Object> dataMap = new HashMap<>();

  /** 代码目录树，纯代码目录 */
  private DomainMenuTree menuTree;

  /** 项目目录树,最顶层 */
  private ProjectMenuTree projectMenuTree;

  /** 用来存储需要导包的信息 */
  private Map<String, Map<String, ImportPackageInfo>> packageMap = new HashMap<>();

  public GenerateCodeContext(
      String fileBasePath,
      String javaPackage,
      String modelName,
      String tableSpaceName,
      GenerateConfigEntity configEntity) {
    this.fileBasePath = fileBasePath;
    this.javaPackage = javaPackage;
    this.modelName = modelName;
    this.mybatisBaseSpace = javaPackage;
    this.tableSpaceName = tableSpaceName;
    this.generateConfig = configEntity;
    // 构建代码的目录树
    this.menuTree = new DomainMenuTree(this.javaPackage, modelName);
    // 构建项目目录树
    this.projectMenuTree = new ProjectMenuTree(modelName);
  }
}
