package com.liujun.micro.autocode.generator.builder.operator.ddd.full;

import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.constant.GenerateCodePackageKey;
import com.liujun.micro.autocode.generator.builder.constant.JavaVarName;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.code.GenerateJavaBean;
import com.liujun.micro.autocode.generator.builder.operator.utils.GenerateOutFileUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.ImportPackageUtils;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 创建数据库访问层的PO对象
 *
 * @author liujun
 * @version 0.0.1
 * @since 2020/04/08
 */
public class JavaCodeRepositoryObjectCreate implements GenerateCodeInf {

    /**
     * 注释中的描述
     */
    private static final String DOC_ANNOTATION = "的数据库存储实体信息";

    /**
     * 用来生成存储层的实体名称
     */
    public static final String REPOSITORY_PO = "PO";

    /**
     * 实例对象
     */
    public static final JavaCodeRepositoryObjectCreate INSTANCE = new JavaCodeRepositoryObjectCreate();

    @Override
    public void generateCode(GenerateCodeContext param) {

        Map<String, TableInfoDTO> tableMap = param.getTableMap();
        Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
        Iterator<Map.Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();

        while (tableNameEntry.hasNext()) {
            Map.Entry<String, List<TableColumnDTO>> tableEntry = tableNameEntry.next();
            // 表名
            String tableName = tableEntry.getKey();

            // 构建包的存储信息
            ImportPackageInfo packageInfo =
                    this.buildPackageData(param, tableMap.get(tableName), tableName);

            // 将po记录到公共的上下文对象中
            ImportPackageUtils.putPackageInfo(
                    tableName,
                    param.getPackageMap(),
                    GenerateCodePackageKey.PERSIST_PO.getKey(),
                    packageInfo,
                    tableMap.size());

            // 进行存储层的bean代码生成
            StringBuilder persistBean =
                    GenerateJavaBean.INSTANCE.generateJavaBean(
                            packageInfo,
                            tableEntry.getValue(),
                            param.getGenerateConfig().getGenerate().getCode(),
                            param.getGenerateConfig().getGenerate().getAuthor(),
                            param.getTypeEnum());

            // 定义项目内的完整目录结构
            String outPackagePath =
                    param.getProjectPath().getSrcJavaNode().outPath()
                            + Symbol.PATH
                            + packageInfo.getPackagePath();

            // 进行存储层的实体输出
            GenerateOutFileUtils.outJavaFile(
                    persistBean, GeneratePathUtils.outServicePath(param), outPackagePath, packageInfo.getClassName());
        }
    }

    /**
     * 构建包信息
     *
     * @param param     路径信息
     * @param tableInfo 表信息
     * @param tableName 表名
     * @return 导入的包信息
     */
    private ImportPackageInfo buildPackageData(
            GenerateCodeContext param, TableInfoDTO tableInfo, String tableName) {

        // 得到类名
        String tableClassName = NameProcess.INSTANCE.toJavaClassName(tableName);
        String className = tableClassName + REPOSITORY_PO;

        // 获取以java定义的package路径
        String javaPackageStr = param.getJavaCodePackage().getRepositoryObjectNode().outJavaPackage();

        // 注释
        String docComment =
                tableInfo.getTableComment()
                        + Symbol.BRACKET_LEFT
                        + tableInfo.getTableName()
                        + Symbol.BRACKET_RIGHT
                        + DOC_ANNOTATION;

        // 将当前包信息存入到上下文对象信息中
        // 构建类路径及名称记录下
        ImportPackageInfo packageInfo =
                new ImportPackageInfo(
                        javaPackageStr, className, docComment, JavaVarName.INSTANCE_NAME_ENTITY);

        return packageInfo;
    }
}
