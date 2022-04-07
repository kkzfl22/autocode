package com.liujun.auto.generator.builder.operator.code;

import com.liujun.auto.generator.builder.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisImplementCreate;
import com.liujun.auto.generator.builder.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisMapperInfCreate;
import com.liujun.auto.generator.builder.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisObjectCreate;
import com.liujun.auto.generator.builder.ddd.full.repositorymybatis.JavaCodeRepositoryMybatisConverterCreate;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.full.domain.JavaCodeDomainObjectCreate;
import com.liujun.auto.generator.builder.ddd.full.domain.JavaCodeDomainRepositoryFacadeCreate;
import com.liujun.auto.generator.builder.ddd.full.domain.JavaCodeDomainServiceCreate;
import org.junit.Test;

/**
 * 执行领域的服务创建操作
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeDomainServiceCreateTest {

  @Test
  public void testGenerate() {
    // 领域实体
    JavaCodeDomainObjectCreate instance = new JavaCodeDomainObjectCreate();
    // 数据库实体
    JavaCodeRepositoryMyBatisObjectCreate repositoryInstance = new JavaCodeRepositoryMyBatisObjectCreate();
    // 数据库转换
    JavaCodeRepositoryMybatisConverterCreate assembler = new JavaCodeRepositoryMybatisConverterCreate();
    // 数据库dao
    JavaCodeRepositoryMyBatisMapperInfCreate repositoryDaoInf = new JavaCodeRepositoryMyBatisMapperInfCreate();
    // 领域存储接口
    JavaCodeDomainRepositoryFacadeCreate repositoryFacadeInf = new JavaCodeDomainRepositoryFacadeCreate();
    // 领域存储实现
    JavaCodeRepositoryMyBatisImplementCreate repositoryPersistence =
        new JavaCodeRepositoryMyBatisImplementCreate();

    JavaCodeDomainServiceCreate domainService = new JavaCodeDomainServiceCreate();

    GenerateCodeContext context = CodeBaseUtils.getBase();

    // 进行数据生成
    instance.generateCode(context);
    // 数据库实体
    repositoryInstance.generateCode(context);
    // 转换类
    assembler.generateCode(context);
    // 数据库掊
    repositoryDaoInf.generateCode(context);
    // 数据库存储接口
    repositoryFacadeInf.generateCode(context);
    // 领域存储实现
    repositoryPersistence.generateCode(context);

    // 进行领域服务代码的生成
    domainService.generateCode(context);
  }
}
