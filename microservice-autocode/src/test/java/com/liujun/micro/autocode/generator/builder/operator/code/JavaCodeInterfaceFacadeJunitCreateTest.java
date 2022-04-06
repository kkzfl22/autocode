package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.application.JavaCodeApplicationServiceCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.domain.JavaCodeDomainObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.domain.JavaCodeDomainRepositoryFacadeCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.domain.JavaCodeDomainServiceCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.facade.JavaCodeFacadeAssemblerCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.facade.JavaCodeFacadeCheckCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.facade.JavaCodeFacadeConstantCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.custom.view.facade.JavaCodeFacadeCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.facade.JavaCodeFacadeErrorCodeCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.facade.JavaCodeFacadeJunitCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.facade.JavaCodeFacadeObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.other.JavaCodeResourceI18nEnUsCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.other.JavaCodeResourceI18nZhCnCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisImplementCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisJunitScanConfigCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisMapperInfCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.repositorymybatis.JavaCodeRepositoryMybatisConverterCreate;
import org.junit.Test;

/**
 * 执行应用服务的创建操作
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeInterfaceFacadeJunitCreateTest {

  @Test
  public void testGenerate() {
    // 领域实体
    JavaCodeDomainObjectCreate instance = new JavaCodeDomainObjectCreate();
    // 数据库配制文件
    JavaCodeRepositoryMyBatisJunitScanConfigCreate myBatisScanConfigCreate =
        JavaCodeRepositoryMyBatisJunitScanConfigCreate.INSTANCE;
    // 数据库实体
    JavaCodeRepositoryMyBatisObjectCreate repositoryInstance =
        new JavaCodeRepositoryMyBatisObjectCreate();
    // 数据库转换
    JavaCodeRepositoryMybatisConverterCreate assembler =
        new JavaCodeRepositoryMybatisConverterCreate();
    // 数据库dao
    JavaCodeRepositoryMyBatisMapperInfCreate repositoryDaoInf =
        new JavaCodeRepositoryMyBatisMapperInfCreate();
    // 领域存储接口
    JavaCodeDomainRepositoryFacadeCreate repositoryFacadeInf =
        new JavaCodeDomainRepositoryFacadeCreate();
    // 领域存储实现
    JavaCodeRepositoryMyBatisImplementCreate repositoryPersistence =
        new JavaCodeRepositoryMyBatisImplementCreate();

    // 领域服务
    JavaCodeDomainServiceCreate domainService = new JavaCodeDomainServiceCreate();
    // 生成应用服务
    JavaCodeApplicationServiceCreate applicationService = new JavaCodeApplicationServiceCreate();

    // 错误码
    JavaCodeFacadeErrorCodeCreate errorCode = new JavaCodeFacadeErrorCodeCreate();
    // 静态文件
    JavaCodeFacadeConstantCreate constantCode = new JavaCodeFacadeConstantCreate();
    // dto转换为领域的对象
    JavaCodeFacadeAssemblerCreate javaCode = new JavaCodeFacadeAssemblerCreate();

    // 国际化错误码
    JavaCodeResourceI18nEnUsCreate en = new JavaCodeResourceI18nEnUsCreate();
    JavaCodeResourceI18nZhCnCreate china = new JavaCodeResourceI18nZhCnCreate();

    // 错误验证的文件生成
    JavaCodeFacadeCheckCreate paramCheck = new JavaCodeFacadeCheckCreate();
    // api的服务
    JavaCodeFacadeCreate interfaceFacadeCreate = new JavaCodeFacadeCreate();
    // 对象存储
    JavaCodeFacadeObjectCreate dataTransfer = new JavaCodeFacadeObjectCreate();

    // web层的单元测试
    JavaCodeFacadeJunitCreate facadeJunitCreate = JavaCodeFacadeJunitCreate.INSTANCE;

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

    // 数据库的配制
    myBatisScanConfigCreate.generateCode(context);

    // 数据存储
    dataTransfer.generateCode(context);

    // 错误友
    errorCode.generateCode(context);
    // 静态常量文件
    constantCode.generateCode(context);

    // 国际化
    en.generateCode(context);
    china.generateCode(context);

    // 进行领域服务代码的生成
    domainService.generateCode(context);
    // 生成错误码

    // 参数的生成
    paramCheck.generateCode(context);

    // 应用服务生成
    applicationService.generateCode(context);

    // 转换层
    javaCode.generateCode(context);

    // 生成对外API
    interfaceFacadeCreate.generateCode(context);

    // web层的单元测试
    facadeJunitCreate.generateCode(context);
  }
}
