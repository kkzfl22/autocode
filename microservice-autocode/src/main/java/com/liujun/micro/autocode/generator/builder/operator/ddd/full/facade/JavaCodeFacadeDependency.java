package com.liujun.micro.autocode.generator.builder.operator.ddd.full.facade;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.application.JavaCodeApplicationServiceCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.domain.JavaCodeDomainObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.domain.JavaCodeDomainServiceCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisImplementCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisJunitScanConfigCreate;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;

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
public class JavaCodeFacadeDependency implements GenerateCodeInf {

  public static final JavaCodeFacadeDependency INSTANCE = new JavaCodeFacadeDependency();

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
      JavaCodeDomainObjectCreate.INSTANCE.domainObjectPkg(param, tableInfo);

      // 应用对象的依赖
      JavaCodeApplicationServiceCreate.INSTANCE.applicationDependency(param, tableInfo);

      // mybatis的依赖
      JavaCodeRepositoryMyBatisJunitScanConfigCreate.INSTANCE.mybatisDependency(param);

      // 领域层的依赖
      JavaCodeDomainServiceCreate.INSTANCE.domainServiceDependency(param, tableInfo);

      // 导入领域层的存储依赖
      JavaCodeRepositoryMyBatisImplementCreate.INSTANCE.dependencyPersistence(param, tableInfo);
    }
  }
}
