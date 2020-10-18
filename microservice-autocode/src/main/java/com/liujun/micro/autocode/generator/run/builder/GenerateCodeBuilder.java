package com.liujun.micro.autocode.generator.run.builder;

import com.liujun.micro.autocode.config.generate.entity.GenerateConfigEntity;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeApplicationServiceCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeDomainJunitServiceCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeDomainObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeDomainRepositoryFacadeCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeDomainServiceCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceAssemblerCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceCheckCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceConstantCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceErrorCodeCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceFacadeApiCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceFacadeCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceFacadeJunitCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeInterfaceObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeProjectCfgCopyCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeProjectMavenPomCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryAssemblerCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryDaoInfCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryJunitDaoCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryJunitMyBatisScanConfigCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryMyBatisMapperCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryPersistenceCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeResourceI18nEnUsCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeResourceI18nZhCnCreate;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.service.DatabaseOperator;
import com.liujun.micro.autocode.generator.run.constant.GenerateScopeEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成的构建器
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateCodeBuilder {

    /**
     * 范围的执行的map
     */
    private static final Map<GenerateScopeEnum, List<GenerateCodeInf>> SCOPE_MAP = new HashMap<>(4);


    static {
        //公共都需要执行的
        common();
        //存储层的代码加入到生成流程中
        repository();
        //领域层的代码加入到生成器中
        domain();
        //应用层的代码加入到生成器中
        application();
        //api层的代码加入到生成器
        api();

    }

    /**
     * 存储层代码生成的加入
     */
    private static void common() {
        List<GenerateCodeInf> commonList = new ArrayList<>(4);
        //1,项目配制文件的拷贝
        commonList.add(JavaCodeProjectCfgCopyCreate.INSTANCE);
        //2,项目的maven的文件的生成
        commonList.add(JavaCodeProjectMavenPomCreate.INSTANCE);


        //存储层的集合
        SCOPE_MAP.put(GenerateScopeEnum.COMMON, commonList);
    }


    /**
     * 存储层代码生成的加入
     */
    private static void repository() {
        List<GenerateCodeInf> repositoryList = new ArrayList<>(6);
        //1,存储层实体的生成
        repositoryList.add(JavaCodeRepositoryObjectCreate.INSTANCE);
        //2,生成mapper对象
        repositoryList.add(JavaCodeRepositoryDaoInfCreate.INSTANCE);
        //3,mybatis的mapper文件
        repositoryList.add(JavaCodeRepositoryMyBatisMapperCreate.INSTANCE);
        //4,生成测试的配制文件
        repositoryList.add(JavaCodeRepositoryJunitMyBatisScanConfigCreate.INSTANCE);
        //5，存储层的单元测试
        repositoryList.add(JavaCodeRepositoryJunitDaoCreate.INSTANCE);


        //存储层的集合
        SCOPE_MAP.put(GenerateScopeEnum.REPOSITORY, repositoryList);
    }

    /**
     * 领域层相关代码的生成
     */
    private static void domain() {
        List<GenerateCodeInf> domainList = new ArrayList<>(8);
        //1,生成领域层的实体
        domainList.add(JavaCodeDomainObjectCreate.INSTANCE);
        //2,领域的存储层接口
        domainList.add(JavaCodeDomainRepositoryFacadeCreate.INSTANCE);
        //3,生成领域层的服务
        domainList.add(JavaCodeDomainServiceCreate.INSTANCE);
        //4，存储层与领域层的实体转换
        domainList.add(JavaCodeRepositoryAssemblerCreate.INSTANCE);
        //5,存储层接口的接口实现调用
        domainList.add(JavaCodeRepositoryPersistenceCreate.INSTANCE);
        //6，领域层的单元测试
        domainList.add(JavaCodeDomainJunitServiceCreate.INSTANCE);

        //存储层的集合
        SCOPE_MAP.put(GenerateScopeEnum.DOMAIN, domainList);
    }

    /**
     * 应用层的代码生成
     */
    private static void application() {
        List<GenerateCodeInf> applicationList = new ArrayList<>(4);
        //1,生成应用层的代码
        applicationList.add(JavaCodeApplicationServiceCreate.INSTANCE);

        //存储层的集合
        SCOPE_MAP.put(GenerateScopeEnum.APPLICATION, applicationList);
    }

    /**
     * api层的相关代码
     */
    private static void api() {
        List<GenerateCodeInf> apiList = new ArrayList<>(12);
        //1,api层的实体的生成
        apiList.add(JavaCodeInterfaceObjectCreate.INSTANCE);
        //2,api层的数据转换层
        apiList.add(JavaCodeInterfaceAssemblerCreate.INSTANCE);
        //3,api层的常量定义
        apiList.add(JavaCodeInterfaceConstantCreate.INSTANCE);
        //4,api层的错误码定义
        apiList.add(JavaCodeInterfaceErrorCodeCreate.INSTANCE);
        //5,api层的错误码检查
        apiList.add(JavaCodeInterfaceCheckCreate.INSTANCE);
        //6,生成api接口
        apiList.add(JavaCodeInterfaceFacadeApiCreate.INSTANCE);
        //7,生成api的实现的代码
        apiList.add(JavaCodeInterfaceFacadeCreate.INSTANCE);
        //8,国际化中文相关的资源文件的生成
        apiList.add(JavaCodeResourceI18nZhCnCreate.INSTANCE);
        //9,国际化英文相关的资源文件的生成
        apiList.add(JavaCodeResourceI18nEnUsCreate.INSTANCE);
        //10,api的单元测试
        apiList.add(JavaCodeInterfaceFacadeJunitCreate.INSTANCE);

        //存储层的集合
        SCOPE_MAP.put(GenerateScopeEnum.API, apiList);
    }


    /**
     * 上下文对象
     */
    private GenerateCodeContext context;


    /**
     * 通过构建函数传递配制对象 构建context对象信息
     *
     * @param config 配制信息
     */
    public GenerateCodeBuilder(GenerateConfigEntity config) {
        this.context = new GenerateCodeContext(config);
    }


    /**
     * 代码生成操作
     */
    public void generate() {

        //数据库基础库加载
        databaseLoader();

        //1，检查当前的阶段，并添加对应的代码生成器,需要按依赖的原则，从最低层开始生成
        List<GenerateScopeEnum> scopeEnums = GenerateScopeEnum.generateScope(context.getGenerateConfig().getGenerate().getScope());

        //1,进行存储层的代码生成
        generateScope(GenerateScopeEnum.REPOSITORY, scopeEnums);

        //2,进行领域层的代码生成
        generateScope(GenerateScopeEnum.DOMAIN, scopeEnums);

        //3，应用层代码的生成
        generateScope(GenerateScopeEnum.APPLICATION, scopeEnums);

        //4,api层的代码的生成
        generateScope(GenerateScopeEnum.API, scopeEnums);

        //5，公共的的文件的生成
        runGenerate(SCOPE_MAP.get(GenerateScopeEnum.COMMON));
    }


    /**
     * 代码构建之前的数据库查询工作
     */
    public void databaseLoader() {
        // 读取数据库中表信息
        context.setTableMap(DatabaseOperator.INSTANCE.getTableInfo(context.getTableSpaceName()));

        Map<String, List<TableColumnDTO>> tableColumnList =
                DatabaseOperator.INSTANCE.getColumnInfo(context.getTableSpaceName());
        // 读取数据库中列信息
        context.setColumnMapList(tableColumnList);

        // 设置二层map，一层key为表名，二层key为列名
        context.setColumnMapMap(DatabaseOperator.INSTANCE.parseColumnMap(tableColumnList));
    }


    public GenerateCodeContext getContext() {
        return context;
    }

    /**
     * 指定阶段的代码进行生成操作
     *
     * @param scope      范围信息
     * @param scopeEnums 当前定义的生成枚举的阶段
     */
    private void generateScope(GenerateScopeEnum scope, List<GenerateScopeEnum> scopeEnums) {

        //1,检查当前阶段是否配制，未配制，则不生成
        if (!checkScope(scope, scopeEnums)) {
            return;
        }

        //执行生成
        runGenerate(SCOPE_MAP.get(scope));
    }

    /**
     * 项目代码生成的执行
     *
     * @param runList
     */
    private void runGenerate(List<GenerateCodeInf> runList) {
        //执行代码的生成操作
        for (GenerateCodeInf scopeItem : runList) {
            scopeItem.generateCode(context);
        }
    }

    /**
     * 检查当前生成的是否存在
     *
     * @param scope
     * @param scopeEnums
     * @return
     */
    private boolean checkScope(GenerateScopeEnum scope, List<GenerateScopeEnum> scopeEnums) {
        for (GenerateScopeEnum scopeItem : scopeEnums) {
            if (scopeItem.equals(scope)) {
                return true;
            }
        }

        return false;

    }


}
