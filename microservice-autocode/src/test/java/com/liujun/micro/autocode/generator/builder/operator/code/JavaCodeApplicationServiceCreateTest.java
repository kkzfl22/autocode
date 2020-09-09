package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeApplicationServiceCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeDomainObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeDomainServiceCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryAssemblerCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryDaoInfCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryFacadeInfCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryPersistenceCreate;
import org.junit.Test;

/**
 * 执行应用服务的创建操作
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeApplicationServiceCreateTest {

  @Test
  public void testGenerate() {
    // 领域实体
    JavaCodeDomainObjectCreate instance = new JavaCodeDomainObjectCreate();
    // 数据库实体
    JavaCodeRepositoryObjectCreate repositoryInstance = new JavaCodeRepositoryObjectCreate();
    // 数据库转换
    JavaCodeRepositoryAssemblerCreate assembler = new JavaCodeRepositoryAssemblerCreate();
    // 数据库dao
    JavaCodeRepositoryDaoInfCreate repositoryDaoInf = new JavaCodeRepositoryDaoInfCreate();
    // 领域存储接口
    JavaCodeRepositoryFacadeInfCreate repositoryFacadeInf = new JavaCodeRepositoryFacadeInfCreate();
    // 领域存储实现
    JavaCodeRepositoryPersistenceCreate repositoryPersistence =
        new JavaCodeRepositoryPersistenceCreate();

    // 领域服务
    JavaCodeDomainServiceCreate domainService = new JavaCodeDomainServiceCreate();
    // 生成应用服务
    JavaCodeApplicationServiceCreate applicationService = new JavaCodeApplicationServiceCreate();

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

    // 应用服务生成
    applicationService.generateCode(context);
  }
}
