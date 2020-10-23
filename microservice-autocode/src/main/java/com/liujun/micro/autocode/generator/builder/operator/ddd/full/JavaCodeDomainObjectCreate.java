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
import com.liujun.micro.autocode.generator.builder.operator.utils.JavaCommentUtil;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.serivce.NameProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 创建领域层的实体对象
 *
 * @author liujun
 * @version 0.0.1
 * @since 2020/04/08
 */
public class JavaCodeDomainObjectCreate implements GenerateCodeInf {

  /** 注释中的描述 */
  private static final String DOC_ANNOTATION = "的领域实体信息";

  /** 领域层的后缀名 */
  private static final String DOMAIN_PO = "DO";

  public static final JavaCodeDomainObjectCreate INSTANCE = new JavaCodeDomainObjectCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Map.Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();

    while (tableNameEntry.hasNext()) {
      Map.Entry<String, List<TableColumnDTO>> tableEntry = tableNameEntry.next();
      // 表名
      String tableName = tableEntry.getKey();

      // 获取类名
      String className = this.getClassName(tableName);

      // 表信息
      TableInfoDTO tableInfo = tableMap.get(tableName);

      // 领域对象依赖存储信息的构建
      domainObjectPkg(param, tableInfo, tableMap.size());

      // 存储层的实体
      ImportPackageInfo domainObject =
          param.getPackageMap().get(tableName).get(GenerateCodePackageKey.DOMAIN_DO.getKey());

      // 进行存储层的bean代码生成
      StringBuilder persistBean =
          GenerateJavaBean.INSTANCE.generateJavaBean(domainObject, tableEntry.getValue(), param);

      // 获取以java定义的package路径
      String javaPackageStr = param.getJavaCodePackage().getDomainObjectNode().outJavaPackage();
      // 定义项目内的完整目录结构
      String outJavaPackageStr =
          param.getProjectPath().getSrcJavaNode().outPath() + Symbol.PATH + javaPackageStr;

      // 进行领域层的实体输出
      GenerateOutFileUtils.outJavaFile(
          persistBean, GeneratePathUtils.outServicePath(param), outJavaPackageStr, className);
    }
  }

  /**
   * 领域的依赖
   *
   * @param param 参数
   * @param tableInfo 表信息
   * @param tableMapSize 表大小
   */
  public void domainObjectPkg(GenerateCodeContext param, TableInfoDTO tableInfo, int tableMapSize) {
    // 获取以java定义的package路径
    String javaPackageStr = param.getJavaCodePackage().getDomainObjectNode().outJavaPackage();
    // 类名
    String className = JavaCodeDomainObjectCreate.INSTANCE.getClassName(tableInfo.getTableName());
    // 注释
    String docComment =
        JavaCommentUtil.tableCommentProc(tableInfo.getTableComment())
            + JavaCodeDomainObjectCreate.DOC_ANNOTATION;
    // 将当前包信息存入到上下文对象信息中
    // 构建类路径及名称记录下
    ImportPackageInfo packageInfo =
        new ImportPackageInfo(
            javaPackageStr, className, docComment, JavaVarName.INSTANCE_NAME_ENTITY);
    // 将领域对象记录到公共的上下文对象中，领域层的实体对象
    ImportPackageUtils.putPackageInfo(
        tableInfo.getTableName(),
        param.getPackageMap(),
        GenerateCodePackageKey.DOMAIN_DO.getKey(),
        packageInfo,
        tableMapSize);
  }

  /**
   * 得到类名
   *
   * @param tableName 表名
   * @return 当前的类名
   */
  private String getClassName(String tableName) {
    // 得到类名
    return NameProcess.INSTANCE.toJavaClassName(tableName) + DOMAIN_PO;
  }
}
