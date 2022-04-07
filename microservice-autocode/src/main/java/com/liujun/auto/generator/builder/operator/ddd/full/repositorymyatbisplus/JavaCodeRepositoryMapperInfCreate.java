package com.liujun.auto.generator.builder.operator.ddd.full.repositorymyatbisplus;

import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.constant.MethodTypeEnum;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.constant.JavaVarName;
import com.liujun.auto.generator.builder.constant.PkgBuildMethod;
import com.liujun.auto.generator.builder.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.entity.JavaClassEntity;
import com.liujun.auto.generator.builder.operator.GenerateCodeInf;
import com.liujun.auto.generator.builder.operator.code.GenerateJavaDaoInterface;
import com.liujun.auto.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.auto.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 生成数据库的相关操作接口
 *
 * @author liujun
 * @version 1.0.0
 */
public class JavaCodeRepositoryMapperInfCreate implements GenerateCodeInf {

  private static final String DAO_SUFFIX = "Mapper";
  private static final String DAO_COMMENT = "的数据库操作";

  /** 继承的父类 */
  private static final String DAO_MAPPER_PARENT = "BaseMapper";

  public static final JavaCodeRepositoryMapperInfCreate INSTANCE =
      new JavaCodeRepositoryMapperInfCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();
    while (tableNameEntry.hasNext()) {
      Entry<String, List<TableColumnDTO>> tableNameItem = tableNameEntry.next();
      // 获取表信息
      TableInfoDTO tableInfo = param.getTableMap().get(tableNameItem.getKey());

      // 表名
      String tableName = tableNameItem.getKey();
      String className = this.getClassName(tableName);

      // 基础包的依赖构建
      this.daoRepositoryDependency(param, tableInfo);

      // 获取以java定义的package路径
      String javaPackageStr = param.getJavaCodePackage().getRepositoryDaoNode().outJavaPackage();

      // 进行dao的相关方法的生成
      StringBuilder sb = this.generateJavaInterface(param, tableName);

      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getSrcJavaNode().outPath();
      javaPackageStr = baseJavaPath + Symbol.PATH + javaPackageStr;

      // 进行存储层的接口输出
      GenerateOutFileUtils.outJavaFile(
          sb, GeneratePathUtils.outServicePath(param), javaPackageStr, className);
    }
  }

  /**
   * 生成java的接口信息
   *
   * @param param 上下文参数
   * @param tableName 名称信息
   * @return 生成的javabean对象
   */
  public StringBuilder generateJavaInterface(GenerateCodeContext param, String tableName) {

    // 获取实体信息
    ImportPackageInfo poPackageInfo = param.getPkg(tableName, GenerateCodePackageKey.PERSIST_PO);
    ImportPackageInfo daoPackageInfo = param.getPkg(tableName, GenerateCodePackageKey.PERSIST_DAO);
    List<MethodInfo> codeMethod = param.getGenerateConfig().getGenerate().getMethodList();
    String author = param.getGenerateConfig().getGenerate().getAuthor();

    // 导入mybatis-plus的父类包
    // 1,方法头的定义
    StringBuilder sb =
        this.defineClass(author, daoPackageInfo, poPackageInfo, buildImport(), codeMethod);

    for (MethodInfo methodItem : codeMethod) {
      // 方法执行修改操作,即所有的数据的，添加、修改、删除
      if (MethodTypeEnum.UPDATE.getType().equals(methodItem.getOperator())
          || batchAddCheck(methodItem)) {

        // 修改方法,包括，增加、删、改
        GenerateJavaDaoInterface.INSTANCE.updateMethod(
            sb, poPackageInfo.getClassName(), methodItem);
      }
      // 方法执行查询操作
      else if (MethodTypeEnum.QUERY_PAGE.getType().equals(methodItem.getOperator())
          || MethodTypeEnum.QUERY.getType().equals(methodItem.getOperator())) {
        // 分页查询查询方法
        GenerateJavaDaoInterface.INSTANCE.queryMethod(sb, poPackageInfo.getClassName(), methodItem);
      }
    }

    // 结束
    GenerateJavaDaoInterface.INSTANCE.classFinish(sb);

    return sb;
  }

  /**
   * 定义类相关的信息，包括包，导入包及类的定义
   *
   * @param author 作者
   * @param poPackageDefine 数据库实体
   * @param currPackageClass dao的包定义
   * @param importPackage 导入包信息
   * @param methodItemList 方法列表
   * @return 生成的代码对象
   */
  public StringBuilder defineClass(
      String author,
      ImportPackageInfo currPackageClass,
      ImportPackageInfo poPackageDefine,
      List<ImportPackageInfo> importPackage,
      List<MethodInfo> methodItemList) {

    // 导入包信息
    List<String> importList =
        GenerateJavaDaoInterface.INSTANCE.importMethodList(
            methodItemList, poPackageDefine, importPackage);

    StringBuilder dataClass = new StringBuilder();
    dataClass.append(DAO_MAPPER_PARENT);
    dataClass.append(Symbol.ANGLE_BRACKETS_LEFT);
    dataClass.append(poPackageDefine.getClassName());
    dataClass.append(Symbol.ANGLE_BRACKETS_RIGHT);

    JavaClassEntity classEntityInfo =
        JavaClassEntity.builder()
            // 类的关键字
            .classKey(JavaKeyWord.INTERFACE_KEY)
            // 类名
            .className(currPackageClass.getClassName())
            // 类注释
            .classComment(currPackageClass.getClassComment())
            // 包类路径信息
            .packagePath(currPackageClass.getPackagePath())
            // 导入包信息
            .importList(importList)
            // 继承的接口
            .extendClass(dataClass.toString())
            // 作者
            .author(author)
            .build();

    // 文件类定义
    return JavaClassCodeUtils.javaClassDefine(classEntityInfo);
  }

  private List<ImportPackageInfo> buildImport() {
    ImportPackageInfo importPkg =
        PkgBuildMethod.classInfo("com.baomidou.mybatisplus.core.mapper", DAO_MAPPER_PARENT);
    return Arrays.asList(importPkg);
  }

  private boolean batchAddCheck(MethodInfo methodItem) {
    return MethodTypeEnum.INSERT.getType().equals(methodItem.getOperator())
        && null != methodItem.getBatchFlag()
        && methodItem.getBatchFlag();
  }

  /**
   * 获取类名
   *
   * @param tableName
   * @return
   */
  private String getClassName(String tableName) {
    // 得到类名
    String tableClassName = NameProcess.INSTANCE.toJavaClassName(tableName);
    return tableClassName + DAO_SUFFIX;
  }

  /**
   * dao接口的依赖
   *
   * @param param
   * @param tableInfo
   */
  public void daoRepositoryDependency(GenerateCodeContext param, TableInfoDTO tableInfo) {
    // 获取以java定义的package路径
    String javaPackageStr = param.getJavaCodePackage().getRepositoryDaoNode().outJavaPackage();

    // 注释
    String docComment =
        tableInfo.getTableComment()
            + Symbol.BRACKET_LEFT
            + tableInfo.getTableName()
            + Symbol.BRACKET_RIGHT
            + DAO_COMMENT;

    String className = this.getClassName(tableInfo.getTableName());

    // 将dao信息进行储存至流程中
    ImportPackageInfo daoPackageInfo =
        PkgBuildMethod.classInfoVarInfo(
            javaPackageStr, className, docComment, JavaVarName.SPRING_INSTANCE_NAME);

    param.putPkg(tableInfo.getTableName(), GenerateCodePackageKey.PERSIST_DAO, daoPackageInfo);
  }
}
