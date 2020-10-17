package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.*;
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
        JavaCodeRepositoryJunitMyBatisScanConfigCreate myBatisScanConfigCreate = JavaCodeRepositoryJunitMyBatisScanConfigCreate.INSTANCE;
        // 数据库实体
        JavaCodeRepositoryObjectCreate repositoryInstance = new JavaCodeRepositoryObjectCreate();
        // 数据库转换
        JavaCodeRepositoryAssemblerCreate assembler = new JavaCodeRepositoryAssemblerCreate();
        // 数据库dao
        JavaCodeRepositoryDaoInfCreate repositoryDaoInf = new JavaCodeRepositoryDaoInfCreate();
        // 领域存储接口
        JavaCodeDomainRepositoryFacadeCreate repositoryFacadeInf = new JavaCodeDomainRepositoryFacadeCreate();
        // 领域存储实现
        JavaCodeRepositoryPersistenceCreate repositoryPersistence =
                new JavaCodeRepositoryPersistenceCreate();

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
