package com.liujun.auto.generator.builder.operator.ddd.dependency;

import com.liujun.auto.generator.builder.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.operator.GenerateCodeInf;
import com.liujun.auto.generator.builder.operator.ddd.full.JavaCodeDomainObjectCreate;
import com.liujun.auto.generator.builder.operator.ddd.full.JavaCodeRepositoryMapperInfCreate;
import com.liujun.auto.generator.builder.operator.ddd.full.JavaCodeRepositoryJunitMyBatisScanConfigCreate;
import com.liujun.auto.generator.builder.operator.ddd.full.JavaCodeRepositoryObjectCreate;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * api层的依赖构建
 *
 * @author liujun
 * @version 0.0.1
 * @since 2020/04/08
 */
public class JavaCodeDomainDependency implements GenerateCodeInf {

  public static final JavaCodeDomainDependency INSTANCE = new JavaCodeDomainDependency();

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Map.Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();

    while (tableNameEntry.hasNext()) {
      Map.Entry<String, List<TableColumnDTO>> tableEntry = tableNameEntry.next();
      // 表名
      String tableName = tableEntry.getKey();

      // 表信息
      TableInfoDTO tableInfo = tableMap.get(tableName);

      // 领域对象的依赖
      JavaCodeDomainObjectCreate.INSTANCE.domainObjectPkg(param, tableInfo, tableMap.size());

      // mybatis的依赖
      JavaCodeRepositoryJunitMyBatisScanConfigCreate.INSTANCE.mybatisDependency(param);

      // 存储层实体的依赖
      JavaCodeRepositoryObjectCreate.INSTANCE.repositoryObjectDependency(
          param, tableInfo, tableMap.size());

      // 领域数据库接口
      JavaCodeRepositoryMapperInfCreate.INSTANCE.daoRepositoryDependency(
          param, tableInfo, tableMap.size());
    }
  }
}
