package com.liujun.micro.autocode.generator.builder.operator.ddd.code;

import com.liujun.micro.autocode.config.generate.entity.MethodInfo;
import com.liujun.micro.autocode.config.generate.entity.TypeInfo;
import com.liujun.micro.autocode.constant.GenerateDefineFlag;
import com.liujun.micro.autocode.constant.MethodTypeEnum;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.ImportCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.JavaMethodName;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarValue;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.code.junit.GenerateJunitDefine;
import com.liujun.micro.autocode.generator.builder.operator.code.junit.GenerateJunitQuery;
import com.liujun.micro.autocode.generator.builder.operator.code.junit.GenerateJunitUpdate;
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaClassCodeUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.MethodUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.ReturnUtils;
import com.liujun.micro.autocode.generator.database.constant.DatabaseTypeEnum;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.micro.autocode.generator.javalanguage.serivce.JavaFormat;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * api层的单元测试服务
 *
 * @author liujun
 * @version 0.0.1
 */
public class GenerateJavaFacadeJunitApi {

    /**
     * 进行构建操作
     */
    public static final GenerateJavaFacadeJunitApi INSTANCE =
            new GenerateJavaFacadeJunitApi();


    /**
     * 成功的响应码
     */
    private static final String SUCCESS_CODE = "APICodeEnum.SUCCESS.getErrorData().getCode()";

    /**
     * 调用的获取结果的方法
     */
    private static final String GET_METHOD = ".getCode()";

    /**
     * 生成单元测试的类信息
     *
     * @param entityPackage  实体的包定义
     * @param targetPackage  执行方法的目标类信息
     * @param junitPackage   单元测试的代码路径
     * @param type           类型
     * @param columnList     列信息
     * @param primaryKeyList 主键列信息
     * @param methodList     配制的方法信息
     * @param tableColumnMap 以map结构的表信息
     * @param author         作者
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
                        entityPackage, junitPackage, methodList, dependencyList, mybatisScanConfig, importCfgList, facadeImport(), author);

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
                this.insertMethod(sb, methodItem, methodList);
            }
            // 修改方法
            else if (MethodTypeEnum.UPDATE.getType().equals(methodItem.getOperator())) {
                // 插入方法
                MethodInfo insertMethod = MethodUtils.getInsertMethod(methodList);
                GenerateJunitUpdate.INSTANCE.oneUpdateMethod(
                        sb, methodItem, insertMethod,
                        ImportCodePackageKey.API_RESPONSE.getPackageInfo().getClassName(),
                        SUCCESS_CODE,
                        GET_METHOD
                );
            }
            // 主键的删除方法
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
                        ImportCodePackageKey.API_RESPONSE.getPackageInfo().getClassName(),
                        SUCCESS_CODE,
                        GET_METHOD);
            }
            // 分页查询方法
            else if (MethodTypeEnum.QUERY.getType().equals(methodItem.getOperator())
                    && null != methodItem.getPageQueryFlag()
                    && methodItem.getPageQueryFlag()) {
                this.pageQueryMethod(
                        sb, methodItem, entityPackage, tableColumnMap, methodList, type, primaryKeyList);
            }
            // 数据查询
            else if (MethodTypeEnum.QUERY.getType().equals(methodItem.getOperator())) {
                this.queryMethod(
                        sb, methodItem, entityPackage, tableColumnMap, methodList, type, primaryKeyList);
            }
        }

        // 最后执执行after的清理方法，调用主键删除
        MethodInfo methodItem = MethodUtils.getPrimaryDeleteMethod(methodList);
        if (null != methodItem) {
            GenerateJunitUpdate.INSTANCE.deleteMethod(
                    sb, methodItem, entityPackage, ImportCodePackageKey.API_RESPONSE.getPackageInfo().getClassName(),
                    SUCCESS_CODE,
                    GET_METHOD);
        }

        // 结束
        sb.append(Symbol.BRACE_RIGHT);

        return sb;
    }


    private List<String> facadeImport() {
        List<String> importList = new ArrayList<>();

        importList.add(ImportCodePackageKey.API_RESPONSE.getPackageInfo().packageOut());
        importList.add(ImportCodePackageKey.API_RESPONSE_CODE.getPackageInfo().packageOut());
        importList.add(ImportCodePackageKey.PAGE_DTO.getPackageInfo().packageOut());
        importList.add(ImportCodePackageKey.API_PAGE_RESPONSE.getPackageInfo().packageOut());
        importList.add(ImportCodePackageKey.API_DATA_RESPONSE.getPackageInfo().packageOut());

        return importList;
    }

    /**
     * 数据的操作方法
     *
     * @param sb
     */
    public void insertMethod(StringBuilder sb, MethodInfo method, List<MethodInfo> methodList) {

        // 1,检查当前的添加方法是否为批量添加
        boolean batchFlag = MethodUtils.checkBatch(method.getParamType());

        if (batchFlag) {
            // 执行批量的添加
            GenerateJunitUpdate.INSTANCE.batchInsertMethod(
                    sb, method, methodList,
                    ImportCodePackageKey.API_RESPONSE.getPackageInfo().getClassName(),
                    SUCCESS_CODE,
                    GET_METHOD
            );
        } else {
            // 执行单个添加
            GenerateJunitUpdate.INSTANCE.oneInsertMethod(
                    sb, method, ImportCodePackageKey.API_RESPONSE.getPackageInfo().getClassName(),
                    SUCCESS_CODE, GET_METHOD);
        }
    }

    /**
     * 数据查询方法
     *
     * @param sb            添加的对象
     * @param queryMethod   方法
     * @param poPackageInfo 导入的实体
     * @param columnMap     列集合
     * @param methodList    方法
     * @param dbType        类型信息
     * @param primaryList   主键列
     */
    public void queryMethod(
            StringBuilder sb,
            MethodInfo queryMethod,
            ImportPackageInfo poPackageInfo,
            Map<String, TableColumnDTO> columnMap,
            List<MethodInfo> methodList,
            DatabaseTypeEnum dbType,
            List<TableColumnDTO> primaryList) {
        String methodName = NameProcess.INSTANCE.toJavaNameFirstUpper(queryMethod.getName());

        int tabIndex = 0;

        // 查询方法的定义
        GenerateJunitQuery.INSTANCE.queryMethodDefine(sb, queryMethod, methodName);

        // 查询方法的前的插入方法
        this.queryInsert(sb, queryMethod, tabIndex, poPackageInfo, columnMap, methodList);

        // 添加查询的代码
        this.junitQueryMethod(
                sb, queryMethod, poPackageInfo, columnMap, tabIndex, dbType, primaryList);

        // 方法结束
        JavaClassCodeUtils.methodEnd(sb);
    }


    /**
     * 单元测试中的查询部分
     *
     * @param sb
     * @param queryMethod
     * @param poPackageInfo
     * @param columnMap
     * @param tabIndex
     * @param dbType
     * @param primaryList
     */
    public void junitQueryMethod(
            StringBuilder sb,
            MethodInfo queryMethod,
            ImportPackageInfo poPackageInfo,
            Map<String, TableColumnDTO> columnMap,
            int tabIndex,
            DatabaseTypeEnum dbType,
            List<TableColumnDTO> primaryList) {

        sb.append(Symbol.ENTER_LINE);

        TypeInfo resultType = queryMethod.getReturnType();
        String className = resultType.getImportClassName();
        className =
                className.replaceAll(
                        GenerateDefineFlag.TABLE_NAME.getDefineFlag(), poPackageInfo.getClassName());

        // 进行条件的设置
        GenerateJunitQuery.INSTANCE.methodQueryConditionSet(sb, queryMethod, poPackageInfo, columnMap, dbType, primaryList);

        // 调用查询方法
        this.invokeQueryMethodData(sb, queryMethod, poPackageInfo);

        // 执行方法结果断言
        this.methodResponseAssert(sb, queryMethod, poPackageInfo);
    }


    /**
     * 调用查询方法
     *
     * @param sb
     */
    private void invokeQueryMethodData(
            StringBuilder sb, MethodInfo queryMethod, ImportPackageInfo dtoDataInfo) {

        // 方法调用
        sb.append(JavaFormat.appendTab(2));
        sb.append(ImportCodePackageKey.API_DATA_RESPONSE.getPackageInfo().getClassName());
        sb.append(Symbol.ANGLE_BRACKETS_LEFT).append(dtoDataInfo.getClassName()).append(Symbol.ANGLE_BRACKETS_RIGHT);
        sb.append(Symbol.SPACE);

        sb.append(JavaVarName.INVOKE_METHOD_QUERY_RSP).append(Symbol.SPACE);
        sb.append(Symbol.EQUAL);

        sb.append(Symbol.BRACKET_LEFT);
        sb.append(ImportCodePackageKey.API_DATA_RESPONSE.getPackageInfo().getClassName());
        sb.append(Symbol.ANGLE_BRACKETS_LEFT).append(dtoDataInfo.getClassName()).append(Symbol.ANGLE_BRACKETS_RIGHT);
        sb.append(Symbol.BRACKET_RIGHT);

        sb.append(Symbol.SPACE).append(JavaVarName.SPRING_INSTANCE_NAME);
        sb.append(Symbol.POINT).append(queryMethod.getName()).append(Symbol.BRACKET_LEFT);
        sb.append(JavaVarName.METHOD_PARAM_TEMP_NAME).append(Symbol.BRACKET_RIGHT);
        sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    }

    /**
     * 查询方法结果断言操作
     *
     * @param sb
     * @param methodInfo
     */
    private void methodResponseAssert(StringBuilder sb, MethodInfo methodInfo, ImportPackageInfo dtoInfo) {
        // 如果当前返回结果为结果集，则使用使用集合断言
        boolean checkList = ReturnUtils.checkReturnList(methodInfo.getReturns());
        if (checkList) {
            sb.append(JavaFormat.appendTab(2));
            sb.append(JavaKeyWord.THIS).append(Symbol.POINT);
            sb.append(JavaMethodName.ASSERT_DATA_LIST);
            sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.BATCH_LIST_NAME);
            sb.append(Symbol.COMMA).append(Symbol.SPACE);
            sb.append(Symbol.BRACKET_LEFT);
            sb.append(JavaKeyWord.LIST_TYPE);
            sb.append(dtoInfo.getClassName());
            sb.append(Symbol.ANGLE_BRACKETS_RIGHT);
            sb.append(Symbol.BRACKET_RIGHT);
            sb.append(JavaVarName.INVOKE_METHOD_QUERY_RSP);
            sb.append(Symbol.POINT);
            sb.append(JavaMethodName.GET);
            sb.append(NameProcess.INSTANCE.toJavaNameFirstUpper(JavaMethodName.PAGE_DTO_DATA));
            sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);

            sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
            sb.append(Symbol.ENTER_LINE);
        }
        // 当前为对象，则执行对象断言
        else if (ReturnUtils.checkReturnObject(methodInfo.getReturns())) {
            sb.append(JavaFormat.appendTab(2));
            sb.append(JavaKeyWord.THIS).append(Symbol.POINT);
            sb.append(JavaMethodName.ASSERT_DATA);
            sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.INSTANCE_NAME_ENTITY);
            sb.append(Symbol.COMMA).append(Symbol.SPACE).append(JavaVarName.INVOKE_METHOD_QUERY_RSP);
            sb.append(Symbol.POINT);
            sb.append(JavaMethodName.GET);
            sb.append(NameProcess.INSTANCE.toJavaNameFirstUpper(JavaMethodName.PAGE_DTO_DATA));
            sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
            sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
            sb.append(Symbol.ENTER_LINE);
        }
    }

    /**
     * 分页查询
     *
     * @param sb            添加的对象
     * @param queryMethod   方法
     * @param poPackageInfo 导入的实体
     * @param columnMap     列集合
     * @param methodList    方法
     * @param dbType        类型信息
     * @param primaryList   主键列
     */
    public void pageQueryMethod(
            StringBuilder sb,
            MethodInfo queryMethod,
            ImportPackageInfo poPackageInfo,
            Map<String, TableColumnDTO> columnMap,
            List<MethodInfo> methodList,
            DatabaseTypeEnum dbType,
            List<TableColumnDTO> primaryList) {
        String methodName = NameProcess.INSTANCE.toJavaNameFirstUpper(queryMethod.getName());

        int tabIndex = 0;

        // 查询方法的定义
        GenerateJunitQuery.INSTANCE.queryMethodDefine(sb, queryMethod, methodName);

        // 查询方法的前的插入方法
        this.queryInsert(sb, queryMethod, tabIndex, poPackageInfo, columnMap, methodList);

        // 分页查询的代码
        this.junitPageQueryMethod(
                sb, queryMethod, poPackageInfo, columnMap, dbType, primaryList);

        // 方法结束
        JavaClassCodeUtils.methodEnd(sb);
    }


    /**
     * 单元测试中的分页查询部分
     *
     * @param sb
     * @param queryMethod
     * @param dtoPackageInfo
     * @param columnMap
     * @param dbType
     * @param primaryList
     */
    public void junitPageQueryMethod(
            StringBuilder sb,
            MethodInfo queryMethod,
            ImportPackageInfo dtoPackageInfo,
            Map<String, TableColumnDTO> columnMap,
            DatabaseTypeEnum dbType,
            List<TableColumnDTO> primaryList) {

        sb.append(Symbol.ENTER_LINE);

        TypeInfo resultType = queryMethod.getReturnType();
        String className = resultType.getImportClassName();
        className =
                className.replaceAll(
                        GenerateDefineFlag.TABLE_NAME.getDefineFlag(), dtoPackageInfo.getClassName());

        // 进行条件的设置
        GenerateJunitQuery.INSTANCE.methodQueryConditionSet(sb, queryMethod, dtoPackageInfo, columnMap, dbType, primaryList);

        // 调用分页查询方法
        this.invokePageQueryMethod(sb, queryMethod, dtoPackageInfo);

        // 执行方法结果断言
        this.methodPageResponseAssert(sb, dtoPackageInfo);
    }


    /**
     * 调用分页的方法
     *
     * @param sb
     */
    private void invokePageQueryMethod(StringBuilder sb, MethodInfo queryMethod, ImportPackageInfo dtoPackage) {

        // 声明分页的对象信息
        sb.append(JavaFormat.appendTab(2));
        sb.append(ImportCodePackageKey.PAGE_DTO.getPackageInfo().getClassName());
        sb.append(Symbol.ANGLE_BRACKETS_LEFT).append(dtoPackage.getClassName()).append(Symbol.ANGLE_BRACKETS_RIGHT);
        sb.append(Symbol.SPACE).append(JavaVarName.QUERY_PAGE_PARAM_VAR).append(Symbol.SPACE);
        sb.append(Symbol.EQUAL).append(Symbol.SPACE);
        sb.append(JavaKeyWord.NEW).append(Symbol.SPACE);
        sb.append(ImportCodePackageKey.PAGE_DTO.getPackageInfo().getClassName());
        sb.append(Symbol.ANGLE_BRACKETS_LEFT).append(Symbol.ANGLE_BRACKETS_RIGHT);
        sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
        sb.append(Symbol.ENTER_LINE);

        //设置值,当前页大小
        sb.append(JavaFormat.appendTab(2));
        sb.append(JavaVarName.QUERY_PAGE_PARAM_VAR).append(Symbol.POINT);
        sb.append(JavaMethodName.SET);
        sb.append(NameProcess.INSTANCE.toJavaNameFirstUpper(JavaMethodName.PAGE_SIZE));
        sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.FINAL_BATCH_INSERT_NUM).append(Symbol.BRACKET_RIGHT);
        sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

        //当前第1页
        sb.append(JavaFormat.appendTab(2));
        sb.append(JavaVarName.QUERY_PAGE_PARAM_VAR).append(Symbol.POINT);
        sb.append(JavaMethodName.SET);
        sb.append(NameProcess.INSTANCE.toJavaNameFirstUpper(JavaMethodName.PAGE_NUM));
        sb.append(Symbol.BRACKET_LEFT).append(JavaVarValue.VALUE_ONE).append(Symbol.BRACKET_RIGHT);
        sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);

        //设置数据值
        sb.append(JavaFormat.appendTab(2));
        sb.append(JavaVarName.QUERY_PAGE_PARAM_VAR).append(Symbol.POINT);
        sb.append(JavaMethodName.SET);
        sb.append(NameProcess.INSTANCE.toJavaNameFirstUpper(JavaMethodName.PAGE_DTO_DATA));
        sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.METHOD_PARAM_TEMP_NAME).append(Symbol.BRACKET_RIGHT);
        sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);


        // 方法调用
        sb.append(JavaFormat.appendTab(2));
        sb.append(ImportCodePackageKey.API_PAGE_RESPONSE.getPackageInfo().getClassName());
        sb.append(Symbol.ANGLE_BRACKETS_LEFT).append(dtoPackage.getClassName()).append(Symbol.ANGLE_BRACKETS_RIGHT);
        sb.append(Symbol.SPACE).append(ImportCodePackageKey.API_PAGE_RESPONSE.getPackageInfo().getVarName());
        sb.append(Symbol.SPACE);
        sb.append(Symbol.EQUAL).append(Symbol.SPACE);
        sb.append(Symbol.BRACKET_LEFT);
        sb.append(ImportCodePackageKey.API_PAGE_RESPONSE.getPackageInfo().getClassName());
        sb.append(Symbol.ANGLE_BRACKETS_LEFT).append(dtoPackage.getClassName()).append(Symbol.ANGLE_BRACKETS_RIGHT);
        sb.append(Symbol.BRACKET_RIGHT);
        sb.append(JavaVarName.SPRING_INSTANCE_NAME);
        sb.append(Symbol.POINT).append(queryMethod.getName()).append(Symbol.BRACKET_LEFT);
        sb.append(JavaVarName.QUERY_PAGE_PARAM_VAR);
        sb.append(Symbol.BRACKET_RIGHT);
        sb.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    }

    /**
     * 查询方法结果断言操作
     *
     * @param sb
     * @param poPackage 实体信息
     */
    private void methodPageResponseAssert(StringBuilder sb, ImportPackageInfo poPackage) {
        int tabIndex = 0;
        // 如果当前返回结果为结果集，则使用使用集合断言
        sb.append(JavaFormat.appendTab(tabIndex + 2));
        sb.append(JavaKeyWord.THIS).append(Symbol.POINT);
        sb.append(JavaMethodName.ASSERT_DATA_LIST);
        sb.append(Symbol.BRACKET_LEFT).append(JavaVarName.BATCH_LIST_NAME);
        sb.append(Symbol.COMMA).append(Symbol.SPACE);
        sb.append(Symbol.BRACKET_LEFT).append(JavaKeyWord.LIST_TYPE);
        sb.append(poPackage.getClassName());
        sb.append(JavaKeyWord.LIST_TYPE_END).append(Symbol.BRACKET_RIGHT);
        sb.append(ImportCodePackageKey.API_PAGE_RESPONSE.getPackageInfo().getVarName());
        sb.append(Symbol.POINT).append(JavaMethodName.GET);
        sb.append(NameProcess.INSTANCE.toJavaNameFirstUpper(JavaMethodName.PAGE_DTO_DATA));
        sb.append(Symbol.BRACKET_LEFT).append(Symbol.BRACKET_RIGHT);
        sb.append(Symbol.BRACKET_RIGHT).append(Symbol.SEMICOLON);
        sb.append(Symbol.ENTER_LINE);
    }


    /**
     * 查询的数据插入操作
     *
     * @param sb
     * @param queryMethod   方法信息
     * @param tabIndex      索引号
     * @param poPackageInfo 实体
     * @param columnMap     列信息
     * @param methodList    列
     */
    public void queryInsert(
            StringBuilder sb,
            MethodInfo queryMethod,
            int tabIndex,
            ImportPackageInfo poPackageInfo,
            Map<String, TableColumnDTO> columnMap,
            List<MethodInfo> methodList) {
        // 给查询方法生成添加数据
        // 1,检查当前方法是否结果为结果集,如果为结果集需要执行批量插入
        // 或者当前的请求条件中带有in条件，也需要批量插入
        if (GenerateJunitQuery.INSTANCE.batchFlag(queryMethod)) {
            // 进行in的处理
            GenerateJunitQuery.INSTANCE.conditionIn(sb, queryMethod, tabIndex, poPackageInfo, columnMap);

            // 调用批量添加方法
            sb.append(
                    GenerateJunitUpdate.INSTANCE.invokeBatch(
                            methodList, ImportCodePackageKey.API_RESPONSE.getPackageInfo().getClassName(),
                            SUCCESS_CODE, GET_METHOD));

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
                            insertMethod, ImportCodePackageKey.API_RESPONSE.getPackageInfo().getClassName(),
                            SUCCESS_CODE, GET_METHOD));

            // 进行标识的设置操作
            sb.append(GenerateJunitDefine.INSTANCE.setBatchInsertFlag(JavaVarValue.INSERT_TYPE_ONE_KEY));
        }
    }
}
