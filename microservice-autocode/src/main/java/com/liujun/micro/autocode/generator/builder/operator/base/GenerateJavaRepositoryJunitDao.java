package com.liujun.micro.autocode.generator.builder.operator.base;

import com.liujun.micro.autocode.config.generate.entity.MethodInfo;
import com.liujun.micro.autocode.constant.MethodTypeEnum;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.ImportJunitPkgKey;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarValue;
import com.liujun.micro.autocode.generator.builder.entity.DataParam;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.code.junit.GenerateJunitDefine;
import com.liujun.micro.autocode.generator.builder.operator.code.junit.GenerateJunitQuery;
import com.liujun.micro.autocode.generator.builder.operator.code.junit.GenerateJunitUpdate;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.MethodUtils;
import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeEnum;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 数据库的层的单元测试
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaRepositoryJunitDao {

  /** 进行构建操作 */
  public static final GenerateJavaRepositoryJunitDao INSTANCE =
      new GenerateJavaRepositoryJunitDao();

  /**
   * 生成单元测试的类信息
   *
   * @param generateParam 参数信息
   */
  public StringBuilder generateJunitService(DataParam generateParam) {

    ImportPackageInfo entityPackage = generateParam.getPkg(GenerateCodePackageKey.DOMAIN_DO);
    ImportPackageInfo targetPackage = generateParam.getPkg(GenerateCodePackageKey.PERSIST_PO);
    ImportPackageInfo junitPackage = generateParam.getPkg(GenerateCodePackageKey.PERSIST_JUNIT_DAO);
    ImportPackageInfo mybatisScanConfig =
        generateParam.getPkg(GenerateCodePackageKey.REPOSITORY_MAPPER_CONFIG);
    DatabaseTypeEnum type = generateParam.getTypeEnum();
    List<TableColumnDTO> columnList = generateParam.getColumnList();
    List<TableColumnDTO> primaryKeyList = generateParam.getPrimaryKeyList();
    List<MethodInfo> methodList = generateParam.getMethodList();
    Map<String, TableColumnDTO> tableColumnMap = generateParam.getColumnMap();
    String author = generateParam.getAuthor();

    // 文件头定义
    StringBuilder sb =
        GenerateJunitDefine.INSTANCE.defineHead(
            entityPackage,
            junitPackage,
            methodList,
            dependencyList(mybatisScanConfig),
            mybatisScanConfig,
            Collections.emptyList(),
            author);

    // 操作数据之前的初始化相关的工作
    GenerateJunitDefine.INSTANCE.beforeMethod(
        sb, entityPackage, targetPackage, type, columnList, primaryKeyList);

    // 公共的数据对比方法
    GenerateJunitDefine.INSTANCE.queryResponseListAssert(
        sb, columnList, entityPackage, methodList, primaryKeyList);

    // 单对象的据对比方法
    GenerateJunitDefine.INSTANCE.queryResponseDataAssert(sb, columnList, entityPackage);

    for (MethodInfo methodItem : methodList) {
      // 添加方法
      if (MethodTypeEnum.INSERT.getType().equals(methodItem.getOperator())) {
        this.insertMethod(sb, methodItem, methodList, targetPackage);
      }
      // 修改方法
      else if (MethodTypeEnum.UPDATE.getType().equals(methodItem.getOperator())) {
        // 插入方法
        MethodInfo insertMethod = MethodUtils.getInsertMethod(methodList);
        GenerateJunitUpdate.INSTANCE.oneUpdateMethod(
            sb,
            methodItem,
            insertMethod,
            JavaKeyWord.INT_TYPE,
            JavaVarValue.DEFAULT_ADD_RSP,
            targetPackage);
      }
      // 非主键的删除方法
      else if (MethodTypeEnum.DELETE.getType().equals(methodItem.getOperator())
          && !Boolean.TRUE.equals(methodItem.getPrimaryFlag())) {
        GenerateJunitUpdate.INSTANCE.batchDelete(
            sb,
            methodItem,
            entityPackage,
            tableColumnMap,
            methodList,
            type,
            primaryKeyList,
            JavaKeyWord.INT_TYPE,
            JavaVarName.FINAL_BATCH_INSERT_NUM,
            targetPackage);
      }
      // 数据查询
      else if (MethodTypeEnum.QUERY.getType().equals(methodItem.getOperator())) {
        this.queryMethod(
            sb,
            methodItem,
            entityPackage,
            tableColumnMap,
            methodList,
            type,
            primaryKeyList,
            targetPackage);
      }
    }

    // 最后执执行after的清理方法，调用主键删除
    MethodInfo methodItem = MethodUtils.getPrimaryDeleteMethod(methodList);
    if (null != methodItem) {
      GenerateJunitUpdate.INSTANCE.deleteMethod(
          sb,
          methodItem,
          entityPackage,
          JavaKeyWord.INT_TYPE,
          JavaVarValue.DEFAULT_ADD_RSP,
          targetPackage);
    }

    // 结束
    sb.append(Symbol.BRACE_RIGHT);

    return sb;
  }

  /**
   * 单元测试所像依赖的最小文件
   *
   * @return
   */
  private List<String> dependencyList(ImportPackageInfo mybatisScanConfig) {
    List<String> dependencyList = new ArrayList<>(4);
    // 数据源的包
    dependencyList.add(
        ImportJunitPkgKey.SPRING_BOOT_TEST_DATA_SOURCE.getPackageInfo().getClassName());
    // mybatis的自动装类
    dependencyList.add(
        ImportJunitPkgKey.SPRING_BOOT_TEST_MYBATIS_AUTO_CONFIG.getPackageInfo().getClassName());
    // mybatis的扫包
    dependencyList.add(mybatisScanConfig.getClassName());

    return dependencyList;
  }

  /**
   * 数据的操作方法
   *
   * @param sb
   */
  public void insertMethod(
      StringBuilder sb,
      MethodInfo method,
      List<MethodInfo> methodList,
      ImportPackageInfo targetPackage) {

    // 1,检查当前的添加方法是否为批量添加
    boolean batchFlag = MethodUtils.checkBatch(method.getParamType());

    if (batchFlag) {
      // 执行批量的添加
      GenerateJunitUpdate.INSTANCE.batchInsertMethod(
          sb,
          method,
          methodList,
          JavaKeyWord.INT_TYPE,
          JavaVarName.FINAL_BATCH_INSERT_NUM,
          targetPackage);
    } else {
      // 执行单个添加
      GenerateJunitUpdate.INSTANCE.oneInsertMethod(
          sb, method, JavaKeyWord.INT_TYPE, JavaVarValue.DEFAULT_ADD_RSP, targetPackage);
    }
  }

  /**
   * 数据查询方法
   *
   * @param sb 添加的对象
   * @param queryMethod 方法
   * @param poPackageInfo 导入的实体
   * @param columnMap 列集合
   * @param methodList 方法
   * @param dbType 类型信息
   * @param primaryList 主键列
   */
  public void queryMethod(
      StringBuilder sb,
      MethodInfo queryMethod,
      ImportPackageInfo poPackageInfo,
      Map<String, TableColumnDTO> columnMap,
      List<MethodInfo> methodList,
      DatabaseTypeEnum dbType,
      List<TableColumnDTO> primaryList,
      ImportPackageInfo targetPackage) {
    String methodName = NameProcess.INSTANCE.toJavaNameFirstUpper(queryMethod.getName());

    int tabIndex = 0;

    // 查询方法的定义
    GenerateJunitQuery.INSTANCE.queryMethodDefine(sb, queryMethod, methodName);

    // 数据查询前的插入
    sb.append(this.queryInsert(queryMethod, poPackageInfo, columnMap, methodList, targetPackage));

    // 添加查询的代码
    GenerateJunitQuery.INSTANCE.junitQueryMethod(
        sb, queryMethod, poPackageInfo, columnMap, tabIndex, dbType, primaryList, targetPackage);

    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 数据查询前的插入
   *
   * @param queryMethod 查询方法
   * @param poPackageInfo 实体信息
   * @param columnMap 类
   * @param methodList 方法集
   */
  private String queryInsert(
      MethodInfo queryMethod,
      ImportPackageInfo poPackageInfo,
      Map<String, TableColumnDTO> columnMap,
      List<MethodInfo> methodList,
      ImportPackageInfo targetPackage) {
    int tabIndex = 0;

    StringBuilder sb = new StringBuilder();
    // 给查询方法生成添加数据
    // 1,检查当前方法是否结果为结果集,如果为结果集需要执行批量插入
    // 或者当前的请求条件中带有in条件，也需要批量插入
    if (GenerateJunitQuery.INSTANCE.batchFlag(queryMethod)) {
      // 进行in的处理
      GenerateJunitQuery.INSTANCE.conditionIn(sb, queryMethod, tabIndex, poPackageInfo, columnMap);

      // 调用批量添加方法
      sb.append(
          GenerateJunitUpdate.INSTANCE.invokeBatch(
              methodList, JavaKeyWord.INT_TYPE, JavaVarName.FINAL_BATCH_INSERT_NUM, targetPackage));

      // 进行标识的设置操作
      sb.append(
          GenerateJunitDefine.INSTANCE.setBatchInsertFlag(JavaVarValue.INSERT_TYPE_BATCH_KEY));

    }
    // 再执行单个添加操作
    else {
      // 单个添加的方法
      MethodInfo insertMethod = MethodUtils.getInsertMethod(methodList);
      // 调用单个添加方法
      sb.append(
          GenerateJunitUpdate.INSTANCE.insertInvokeMethod(
              insertMethod, JavaKeyWord.INT_TYPE, JavaVarValue.DEFAULT_ADD_RSP, targetPackage));

      // 进行标识的设置操作
      sb.append(GenerateJunitDefine.INSTANCE.setBatchInsertFlag(JavaVarValue.INSERT_TYPE_ONE_KEY));
    }

    return sb.toString();
  }
}
