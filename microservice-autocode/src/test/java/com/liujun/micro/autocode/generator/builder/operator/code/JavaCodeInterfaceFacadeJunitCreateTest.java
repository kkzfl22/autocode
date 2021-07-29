package com.liujun.micro.autocode.generator.builder.operator.code;

import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeApplicationServiceCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeDomainObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeDomainRepositoryFacadeCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeDomainServiceCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceAssemblerCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceCheckCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceConfigCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceConstantCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceErrorCodeCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceFacadeApiCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceFacadeCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceFacadeJunitCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryAssemblerCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryMapperInfCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryJunitMyBatisScanConfigCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryPersistenceCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeResourceI18nEnUsCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeResourceI18nZhCnCreate;
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
        //数据库配制文件
        JavaCodeRepositoryJunitMyBatisScanConfigCreate myBatisScanConfigCreate = JavaCodeRepositoryJunitMyBatisScanConfigCreate.INSTANCE;
        // 数据库实体
        JavaCodeRepositoryObjectCreate repositoryInstance = new JavaCodeRepositoryObjectCreate();
        // 数据库转换
        JavaCodeRepositoryAssemblerCreate assembler = new JavaCodeRepositoryAssemblerCreate();
        // 数据库dao
        JavaCodeRepositoryMapperInfCreate repositoryDaoInf = new JavaCodeRepositoryMapperInfCreate();
        // 领域存储接口
        JavaCodeDomainRepositoryFacadeCreate repositoryFacadeInf = new JavaCodeDomainRepositoryFacadeCreate();
        // 领域存储实现
        JavaCodeRepositoryPersistenceCreate repositoryPersistence =
                new JavaCodeRepositoryPersistenceCreate();

        // 领域服务
        JavaCodeDomainServiceCreate domainService = new JavaCodeDomainServiceCreate();
        // 生成应用服务
        JavaCodeApplicationServiceCreate applicationService = new JavaCodeApplicationServiceCreate();

        // 错误码
        JavaCodeInterfaceErrorCodeCreate errorCode = new JavaCodeInterfaceErrorCodeCreate();
        // 静态文件
        JavaCodeInterfaceConstantCreate constantCode = new JavaCodeInterfaceConstantCreate();
        // dto转换为领域的对象
        JavaCodeInterfaceAssemblerCreate javaCode = new JavaCodeInterfaceAssemblerCreate();

        // 国际化错误码
        JavaCodeResourceI18nEnUsCreate en = new JavaCodeResourceI18nEnUsCreate();
        JavaCodeResourceI18nZhCnCreate china = new JavaCodeResourceI18nZhCnCreate();

        // 错误验证的文件生成
        JavaCodeInterfaceCheckCreate paramCheck = new JavaCodeInterfaceCheckCreate();
        // api的服务
        JavaCodeInterfaceFacadeCreate interfaceFacadeCreate = new JavaCodeInterfaceFacadeCreate();
        // 对象存储
        JavaCodeInterfaceObjectCreate dataTransfer = new JavaCodeInterfaceObjectCreate();

        //方法的定义
        JavaCodeInterfaceFacadeApiCreate facadeApi = JavaCodeInterfaceFacadeApiCreate.INSTANCE;

        //web层的单元测试
        JavaCodeInterfaceFacadeJunitCreate facadeJunitCreate = JavaCodeInterfaceFacadeJunitCreate.INSTANCE;


        //配制的加载国际化文件
        JavaCodeInterfaceConfigCreate configInstance = JavaCodeInterfaceConfigCreate.INSTANCE;

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

        //数据库的配制
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

        //生成加载错误码
        configInstance.generateCode(context);

        //对外接口的定义
        facadeApi.generateCode(context);

        // 生成对外API
        interfaceFacadeCreate.generateCode(context);

        //web层的单元测试
        facadeJunitCreate.generateCode(context);
    }
}
