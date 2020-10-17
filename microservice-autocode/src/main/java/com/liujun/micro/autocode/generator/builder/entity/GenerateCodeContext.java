package com.liujun.micro.autocode.generator.builder.entity;

import com.liujun.micro.autocode.config.menutree.MenuTreeCodePackage;
import com.liujun.micro.autocode.config.menutree.MenuTreeProjectPath;
import com.liujun.micro.autocode.config.generate.entity.GenerateConfigEntity;
import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeEnum;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 额头于进行代码生成的上下文对象
 *
 * @author liujun
 * @version 0.0.1
 */
@Getter
@Setter
@ToString
public class GenerateCodeContext {

    /**
     * 文件的基本路径
     */
    private String fileBasePath;

    /**
     * java的包的基本命名空间
     */
    private String javaPackage;

    /**
     * 当前模块的名称
     */
    private String moduleName;

    /**
     * 当前领域的名称
     */
    private String domainName;

    /**
     * mybatis的namespace信息
     */
    private String mybatisBaseSpace;

    /**
     * 表空间名称
     */
    private String tableSpaceName;

    /**
     * 当前的数据库类型
     */
    private DatabaseTypeEnum typeEnum;

    /**
     * 列描述的map信息
     */
    private Map<String, List<TableColumnDTO>> columnMapList;

    /**
     * 以表名为key，再以列名为key
     */
    private Map<String, Map<String, TableColumnDTO>> columnMapMap;

    /**
     * 表的描述的map信息
     */
    private Map<String, TableInfoDTO> tableMap;

    /**
     * 数据配制操作
     */
    private GenerateConfigEntity generateConfig;

    /**
     * 公共的临时参数
     */
    private Map<String, Object> dataMap = new HashMap<>();

    /**
     * java代码的路径包信息
     */
    private MenuTreeCodePackage javaCodePackage;

    /**
     * 项目路径信息
     */
    private MenuTreeProjectPath projectPath;

    /**
     * 用来存储需要导包的信息
     */
    private Map<String, Map<String, ImportPackageInfo>> packageMap = new HashMap<>();

    //public GenerateCodeContext(
    //        String fileBasePath,
    //        String javaPackage,
    //        String domainName,
    //        DatabaseTypeEnum typeEnum,
    //        String tableSpaceName,
    //        GenerateConfigEntity configEntity) {
    //    this.fileBasePath = fileBasePath;
    //    this.javaPackage = javaPackage;
    //    this.domainName = domainName;
    //    // 类型枚举
    //    this.typeEnum = typeEnum;
    //    this.mybatisBaseSpace = javaPackage;
    //    this.tableSpaceName = tableSpaceName;
    //    this.generateConfig = configEntity;
    //    // 构建代码的目录树
    //    this.javaCodePackage = MenuTreeCodePackage.INSTANCE;
    //    // 构建项目目录树
    //    this.projectPath = MenuTreeProjectPath.INSTANCE;
    //}


    public GenerateCodeContext(GenerateConfigEntity configEntity) {
        this.fileBasePath = configEntity.getGenerate().getOutput();
        this.javaPackage = configEntity.getGenerate().getCodeMenuTree().getBaseMenu();
        this.moduleName = configEntity.getGenerate().getModuleName();
        this.domainName = configEntity.getGenerate().getCodeMenuTree().getDomainName();
        // 类型枚举
        this.typeEnum = DatabaseTypeEnum.getDbType(configEntity.getGenerate().getDatabaseType());
        this.mybatisBaseSpace = javaPackage;
        this.tableSpaceName = configEntity.getGenerate().getDatabaseTableSpaceName();
        this.generateConfig = configEntity;
        // 构建代码的目录树
        this.javaCodePackage = MenuTreeCodePackage.INSTANCE;
        // 构建项目目录树
        this.projectPath = MenuTreeProjectPath.INSTANCE;
    }


}
