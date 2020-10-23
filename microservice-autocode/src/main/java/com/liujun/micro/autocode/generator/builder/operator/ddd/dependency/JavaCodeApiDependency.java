package com.liujun.micro.autocode.generator.builder.operator.ddd.dependency;

import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeApplicationServiceCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeDomainObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeDomainServiceCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryJunitMyBatisScanConfigCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryPersistenceCreate;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaCommentUtil;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

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
public class JavaCodeApiDependency implements GenerateCodeInf {

  public static final JavaCodeApiDependency INSTANCE = new JavaCodeApiDependency();

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

      // 应用对象的依赖
      JavaCodeApplicationServiceCreate.INSTANCE.applicationDependency(
          param, tableInfo, tableMap.size());

      // mybatis的依赖
      JavaCodeRepositoryJunitMyBatisScanConfigCreate.INSTANCE.mybatisDependency(param);

      // 领域层的依赖
      JavaCodeDomainServiceCreate.INSTANCE.domainServiceDependency(
          param, tableInfo, tableMap.size());

      // 导入领域层的存储依赖
      JavaCodeRepositoryPersistenceCreate.INSTANCE.dependencyPersistence(
          param, tableInfo, tableMap.size());
    }
  }
}
