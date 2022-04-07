package com.liujun.auto.generator.builder.ddd.full.repositorymybatis;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.constant.GenerateCodePackageKey;
import com.liujun.auto.generator.builder.ddd.constant.GenerateCommConstant;
import com.liujun.auto.generator.builder.ddd.constant.ImportJunitPkgKey;
import com.liujun.auto.generator.builder.ddd.constant.JavaVarName;
import com.liujun.auto.generator.builder.ddd.constant.PkgBuildMethod;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.utils.GenerateOutFileUtils;
import com.liujun.auto.generator.builder.utils.GeneratePathUtils;
import com.liujun.auto.generator.builder.utils.JavaClassCodeUtils;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.javalanguage.serivce.JavaFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 生成单元测试的配制
 *
 * @author liujun
 * @version 0.0.1
 * @since 2018年4月15日 下午4:11:42
 */
public class JavaCodeRepositoryMyBatisJunitScanConfigCreate implements GenerateCodeInf {

  /** 注释 */
  private static final String DATABASE_DOC = "单元测试的mybatis的配制";

  /** 导入类文件 */
  private static final List<String> IMPORT_CLASS =
      Arrays.asList(
          "org.mybatis.spring.annotation.MapperScan",
          "org.springframework.context.annotation.Bean",
          ImportJunitPkgKey.SPRING_CONFIG.getPackageInfo().packageOut(),
          "org.springframework.jdbc.datasource.DataSourceTransactionManager",
          "javax.sql.DataSource",
          "com.github.pagehelper.PageInterceptor",
          "java.util.Properties");

  public static final JavaCodeRepositoryMyBatisJunitScanConfigCreate INSTANCE =
      new JavaCodeRepositoryMyBatisJunitScanConfigCreate();

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();
    if (tableNameEntry.hasNext()) {

      // 获取以java定义的package路径
      String javaDaoPackageStr =
          param.getJavaCodePackage().getRepositoryDaoNodeConfig().outJavaPackage();

      // mybatis依赖的生成
      this.mybatisDependency(param);

      // 获取dao的完整路径
      // 获取dao的完整路径
      ImportPackageInfo mybatisScanConfig =
          param.getPkg(
              GenerateCommConstant.GENERATE_JUNIT_CONFIG,
              GenerateCodePackageKey.REPOSITORY_MAPPER_CONFIG);

      // 生成接口的定义
      StringBuilder outClass =
          JavaClassCodeUtils.classDefine(
              mybatisScanConfig,
              IMPORT_CLASS,
              annotationList(param.getJavaCodePackage().getRepositoryDaoNode().outJavaPackage()),
              param.getGenerateConfig().getGenerate().getAuthor());

      // 事务的获取逻辑
      outClass.append(transManagerClass());

      // 换行
      outClass.append(Symbol.ENTER_LINE);

      // 分页插件
      outClass.append(pageInterceptor());

      // 将数据库存储的文件输出
      JavaClassCodeUtils.classEnd(outClass);

      // 定义项目内的完整目录结构
      String baseJavaPath = param.getProjectPath().getTestJavaNode().outPath();
      javaDaoPackageStr = baseJavaPath + Symbol.PATH + javaDaoPackageStr;

      // 定义的类名
      GenerateOutFileUtils.outJavaFile(
          outClass,
          GeneratePathUtils.outServicePath(param),
          javaDaoPackageStr,
          JavaVarName.JUNIT_TRANSACTION_CLASS_NAME);
    }
  }

  /**
   * 注解的列表
   *
   * @param mapperPath mapper的路径
   * @return 返回路径信息
   */
  private List<String> annotationList(String mapperPath) {
    List<String> annotationList = new ArrayList<>();

    annotationList.add(ImportJunitPkgKey.SPRING_CONFIG.getPackageInfo().getAnnotation());
    annotationList.add(
        ImportJunitPkgKey.SPRING_BOOT_TEST_MAPPER_SCAN.getPackageInfo().getAnnotation()
            + Symbol.BRACKET_LEFT
            + Symbol.QUOTE
            + mapperPath
            + Symbol.QUOTE
            + Symbol.BRACKET_RIGHT);

    return annotationList;
  }

  /**
   * 事务方法的类定义
   *
   * @return 构建的代码
   */
  private String transManagerClass() {
    StringBuilder outMethod = new StringBuilder();

    outMethod.append(JavaFormat.appendTab(1));
    outMethod.append("@Bean(name = \"").append(JavaVarName.JUNIT_TRANSACTION_MANAGER_NAME);
    outMethod.append("\")");
    outMethod.append(Symbol.ENTER_LINE);
    outMethod.append(JavaFormat.appendTab(1));
    outMethod.append(
        "public DataSourceTransactionManager transactionManager(DataSource dataSource) {");
    outMethod.append(Symbol.ENTER_LINE);
    outMethod.append(JavaFormat.appendTab(2));
    outMethod.append("return new DataSourceTransactionManager(dataSource);");
    outMethod.append(Symbol.ENTER_LINE);
    outMethod.append(JavaFormat.appendTab(1));
    outMethod.append(Symbol.BRACE_RIGHT);
    outMethod.append(Symbol.ENTER_LINE);

    return outMethod.toString();
  }

  /**
   * 分页插件拦截
   *
   * @return 构建的代码
   */
  private String pageInterceptor() {
    StringBuilder outMethod = new StringBuilder();

    outMethod.append(JavaFormat.appendTab(1));
    outMethod.append("@Bean");
    outMethod.append(Symbol.ENTER_LINE);
    outMethod.append(JavaFormat.appendTab(1));
    outMethod.append("public PageInterceptor pageCfg() {");
    outMethod.append(Symbol.ENTER_LINE);
    outMethod.append(JavaFormat.appendTab(2));
    outMethod.append("PageInterceptor cfg = new PageInterceptor();");
    outMethod.append(Symbol.ENTER_LINE);

    outMethod.append(JavaFormat.appendTab(2));
    outMethod.append("Properties properties = new Properties();");
    outMethod.append(Symbol.ENTER_LINE);

    outMethod.append(JavaFormat.appendTab(2));
    outMethod.append("properties.setProperty(\"helperDialect\", \"mysql\");");
    outMethod.append(Symbol.ENTER_LINE);

    outMethod.append(JavaFormat.appendTab(2));
    outMethod.append("cfg.setProperties(properties);");
    outMethod.append(Symbol.ENTER_LINE);

    outMethod.append(JavaFormat.appendTab(2));
    outMethod.append("return cfg;");
    outMethod.append(Symbol.ENTER_LINE);

    outMethod.append(JavaFormat.appendTab(1));
    outMethod.append(Symbol.BRACE_RIGHT);
    outMethod.append(Symbol.ENTER_LINE);

    return outMethod.toString();
  }

  /**
   * 类的注释
   *
   * @return
   */
  public String getClassComment() {
    return DATABASE_DOC;
  }

  /**
   * mybatis的依赖
   *
   * @param param
   */
  public void mybatisDependency(GenerateCodeContext param) {

    // 单元测试的路径
    ImportPackageInfo importPkg =
        PkgBuildMethod.classInfoComment(
            param.getJavaCodePackage().getRepositoryDaoNodeConfig().outJavaPackage(),
            JavaVarName.JUNIT_TRANSACTION_CLASS_NAME,
            JavaCodeRepositoryMyBatisJunitScanConfigCreate.INSTANCE.getClassComment());

    param.putPkg(
        GenerateCommConstant.GENERATE_JUNIT_CONFIG,
        GenerateCodePackageKey.REPOSITORY_MAPPER_CONFIG,
        importPkg);
  }
}
