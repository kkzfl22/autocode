package com.liujun.auto.generator.builder.ddd.code;

import com.liujun.auto.config.generate.entity.MethodInfo;
import com.liujun.auto.constant.MethodOperatorEnum;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.constant.JavaVarValue;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.utils.JavaClassCodeUtils;
import com.liujun.auto.generator.builder.utils.MethodUtils;
import com.liujun.auto.generator.database.constant.DatabaseTypeEnum;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.serivce.NameProcess;

import java.util.List;
import java.util.Map;

/**
 * 领域的单元测试服务
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaDomainJunitService {

  /** 进行构建操作 */
  public static final GenerateJavaDomainJunitService INSTANCE =
      new GenerateJavaDomainJunitService();

  /**
   * 生成单元测试的类信息
   *
   * @param entityPackage 实体的包定义
   * @param targetPackage 执行方法的目标类信息
   * @param junitPackage 单元测试的代码路径
   * @param type 类型
   * @param columnList 列信息
   * @param primaryKeyList 主键列信息
   * @param methodList 配制的方法信息
   * @param tableColumnMap 以map结构的表信息
   * @param author 作者
   * @return 构建的单元测试代码
   */
  public StringBuilder generateJunitService(
      ImportPackageInfo entityPackage,
      ImportPackageInfo targetPackage,
      ImportPackageInfo junitPackage,
      DatabaseTypeEnum type,
      List<TableColumnDTO> columnList,
      List<TableColumnDTO> primaryKeyList,
      List<MethodInfo> methodList,
      Map<String, TableColumnDTO> tableColumnMap,
      List<String> dependencyList,
      ImportPackageInfo mybatisScanConfig,
      List<ImportPackageInfo> importCfgList,
      String author) {

    // 文件头定义
    StringBuilder sb =
        GenerateJunitDefine.INSTANCE.defineHead(
            entityPackage,
            junitPackage,
            methodList,
            dependencyList,
            mybatisScanConfig,
            importCfgList,
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
      if (MethodOperatorEnum.INSERT.getType().equals(methodItem.getOperator())) {
        this.insertMethod(sb, methodItem, methodList, targetPackage);
      }
      // 修改方法
      else if (MethodOperatorEnum.UPDATE.getType().equals(methodItem.getOperator())) {
        // 插入方法
        MethodInfo insertMethod = MethodUtils.getInsertMethod(methodList);
        GenerateJunitUpdate.INSTANCE.oneUpdateMethod(
            sb,
            methodItem,
            insertMethod,
            JavaKeyWord.TYPE_BOOLEAN,
            Boolean.TRUE.toString(),
            targetPackage);
      }
      // 非主键的删除方法
      else if (MethodOperatorEnum.DELETE.getType().equals(methodItem.getOperator())
          && !Boolean.TRUE.equals(methodItem.getPrimaryFlag())) {
        GenerateJunitUpdate.INSTANCE.batchDelete(
            sb,
            methodItem,
            entityPackage,
            tableColumnMap,
            methodList,
            type,
            primaryKeyList,
            JavaKeyWord.TYPE_BOOLEAN,
            Boolean.TRUE.toString(),
            targetPackage);
      }
      // 分页查询方法
      else if (MethodOperatorEnum.QUERY_PAGE.getType().equals(methodItem.getOperator())) {
        this.pageQueryMethod(
            sb,
            methodItem,
            entityPackage,
            tableColumnMap,
            methodList,
            type,
            primaryKeyList,
            targetPackage);
      }
      // 数据查询
      else if (MethodOperatorEnum.QUERY.getType().equals(methodItem.getOperator())) {
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
          JavaKeyWord.TYPE_BOOLEAN,
          Boolean.TRUE.toString(),
          targetPackage);
    }

    // 结束
    sb.append(Symbol.BRACE_RIGHT);

    return sb;
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
          sb, method, methodList, JavaKeyWord.TYPE_BOOLEAN, Boolean.TRUE.toString(), targetPackage);
    } else {
      // 执行单个添加
      GenerateJunitUpdate.INSTANCE.oneInsertMethod(
          sb, method, JavaKeyWord.TYPE_BOOLEAN, Boolean.TRUE.toString(), targetPackage);
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

    // 查询方法的前的插入方法
    this.queryInsert(
        sb, queryMethod, tabIndex, poPackageInfo, columnMap, methodList, targetPackage);

    // 添加查询的代码
    GenerateJunitQuery.INSTANCE.junitQueryMethod(
        sb, queryMethod, poPackageInfo, columnMap, tabIndex, dbType, primaryList, targetPackage);

    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 分页查询
   *
   * @param sb 添加的对象
   * @param queryMethod 方法
   * @param poPackageInfo 导入的实体
   * @param columnMap 列集合
   * @param methodList 方法
   * @param dbType 类型信息
   * @param primaryList 主键列
   */
  public void pageQueryMethod(
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

    // 查询方法的前的插入方法
    this.queryInsert(
        sb, queryMethod, tabIndex, poPackageInfo, columnMap, methodList, targetPackage);

    // 分页查询的代码
    GenerateJunitQuery.INSTANCE.junitPageQueryMethod(
        sb, queryMethod, poPackageInfo, columnMap, tabIndex, dbType, primaryList, targetPackage);

    // 方法结束
    JavaClassCodeUtils.methodEnd(sb);
  }

  /**
   * 查询的数据插入操作
   *
   * @param sb
   * @param queryMethod 方法信息
   * @param tabIndex 索引号
   * @param poPackageInfo 实体
   * @param columnMap 列信息
   * @param methodList 列
   */
  public void queryInsert(
      StringBuilder sb,
      MethodInfo queryMethod,
      int tabIndex,
      ImportPackageInfo poPackageInfo,
      Map<String, TableColumnDTO> columnMap,
      List<MethodInfo> methodList,
      ImportPackageInfo targetPackage) {
    // 给查询方法生成添加数据
    // 1,检查当前方法是否结果为结果集,如果为结果集需要执行批量插入
    // 或者当前的请求条件中带有in条件，也需要批量插入
    if (GenerateJunitQuery.INSTANCE.batchFlag(queryMethod)) {
      // 进行in的处理
      GenerateJunitQuery.INSTANCE.conditionIn(sb, queryMethod, tabIndex, poPackageInfo, columnMap);

      // 调用批量添加方法
      sb.append(
          GenerateJunitUpdate.INSTANCE.invokeBatch(
              methodList, JavaKeyWord.TYPE_BOOLEAN, Boolean.TRUE.toString(), targetPackage));

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
              insertMethod, JavaKeyWord.TYPE_BOOLEAN, Boolean.TRUE.toString(), targetPackage));

      // 进行标识的设置操作
      sb.append(GenerateJunitDefine.INSTANCE.setBatchInsertFlag(JavaVarValue.INSERT_TYPE_ONE_KEY));
    }
  }
}
