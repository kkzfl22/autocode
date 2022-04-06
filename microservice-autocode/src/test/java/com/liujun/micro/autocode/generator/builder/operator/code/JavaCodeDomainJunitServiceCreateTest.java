package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.domain.JavaCodeDomainJunitServiceCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.domain.JavaCodeDomainObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.domain.JavaCodeDomainRepositoryFacadeCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.domain.JavaCodeDomainServiceCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.repositorymybatis.JavaCodeRepositoryMybatisConverterCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisJunitScanConfigCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisMapperInfCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisImplementCreate;
import org.junit.Test;

/**
 * 测试领域的单元测试
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeDomainJunitServiceCreateTest {

    @Test
    public void testGenerate() {
        // 领域实体
        JavaCodeDomainObjectCreate instance = new JavaCodeDomainObjectCreate();
        //数据库配制文件
        JavaCodeRepositoryMyBatisJunitScanConfigCreate myBatisScanConfigCreate = JavaCodeRepositoryMyBatisJunitScanConfigCreate.INSTANCE;
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

        // 领域服务
        JavaCodeDomainServiceCreate domainService = new JavaCodeDomainServiceCreate();

        // 领域的单元测试
        JavaCodeDomainJunitServiceCreate domainJunitServiceCreate =
                new JavaCodeDomainJunitServiceCreate();

        GenerateCodeContext context = CodeBaseUtils.getBase();

        // 进行数据生成
        instance.generateCode(context);
        //数据库的配制文件
        myBatisScanConfigCreate.generateCode(context);
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

        // 领域服务的单元测试
        domainJunitServiceCreate.generateCode(context);
    }
}
