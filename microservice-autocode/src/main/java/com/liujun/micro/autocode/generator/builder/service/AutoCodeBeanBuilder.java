package com.liujun.micro.autocode.generator.builder.service;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.ddd.JavaCodeRepositoryObjectCreate;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.service.DatabaseOperator;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 自动生成代码建造器
 *
 * @version 0.0.1
 * @author liujun
 */
public class AutoCodeBeanBuilder {

  /** 添加集合接入 autoList */
  private List<GenerateCodeInf> autoList = new LinkedList<>();

  private final GenerateCodeContext paramInfo;

  public AutoCodeBeanBuilder(GenerateCodeContext paramInfo) {
    this.paramInfo = paramInfo;
  }

  /** 代码构建之前的数据准备工作 */
  public void builderInit() {
    // 读取数据库中表信息
    paramInfo.setTableMap(DatabaseOperator.INSTANCE.getTableInfo(paramInfo.getTableSpaceName()));

    Map<String, List<TableColumnDTO>> tableColumnList =
        DatabaseOperator.INSTANCE.getColumnInfo(paramInfo.getTableSpaceName());
    // 读取数据库中列信息
    paramInfo.setColumnMapList(tableColumnList);

    // 设置二层的map
    paramInfo.setColumnMapMap(DatabaseOperator.INSTANCE.parseColumnMap(tableColumnList));
  }

  /** 添加数据库实体的方法 */
  public AutoCodeBeanBuilder addPersistObject() {
    this.autoList.add(new JavaCodeRepositoryObjectCreate());
    return this;
  }

  /** 进行相关的代码生成 */
  public void generateCode() {
    // 执行代码生成
    for (GenerateCodeInf generator : autoList) {
      generator.generateCode(paramInfo);
    }
  }

  public GenerateCodeContext getParamInfo() {
    return paramInfo;
  }
}
