package com.liujun.micro.autocode.generator.builder.constant;

import com.liujun.micro.autocode.generator.builder.entity.ImportPackageInfo;

/**
 * 用于单元测试的类的导入
 *
 * @author liujun
 * @version 0.0.1
 */
public enum ImportJunitPkgKey {

  /** spring与junit结合的测试框架 */
  SPRING_RUNNER(
      PkgBuildMethod.getAnnotationPkg(
          "org.springframework.test.context.junit4", "SpringRunner", "@RunWith")),

  /** 指定加载类 */
  SPRING_BOOT_TEST(
      PkgBuildMethod.getAnnotationPkg(
          "org.springframework.boot.test.context", "SpringBootTest", "@SpringBootTest")),

  /** 指定加载类 */
  SPRING_BOOT_TEST_MAPPER_SCAN(
      PkgBuildMethod.getAnnotationPkg(
          "org.mybatis.spring.annotation", "MapperScan", "@MapperScan")),

  /** config的注解 */
  SPRING_CONFIG(
      PkgBuildMethod.getAnnotationPkg(
          "org.springframework.context.annotation", "Configuration", "@Configuration")),

  /** 测试的事务操作 */
  SPRING_BOOT_TEST_TRANSACTIONAL(
      PkgBuildMethod.getAnnotationPkg(
          "org.springframework.transaction.annotation", "Transactional", "@Transactional")),

  /** 测试的事务操作 */
  SPRING_BOOT_TEST_PROPERTY_SOURCE(
      PkgBuildMethod.getAnnotationPkg(
          "org.springframework.test.context", "TestPropertySource", "@TestPropertySource")),

  /** 测试包的导入 */
  SPRING_BOOT_TEST_IMPORT(
      PkgBuildMethod.getAnnotationPkg(
          "org.springframework.context.annotation", "Import", "@Import")),

  /** 数据源注入的包 */
  SPRING_BOOT_TEST_DATA_SOURCE(
      PkgBuildMethod.classInfo(
          "com.alibaba.druid.spring.boot.autoconfigure", "DruidDataSourceAutoConfigure")),

  /** mybatis的自动配制 */
  SPRING_BOOT_TEST_MYBATIS_AUTO_CONFIG(
      PkgBuildMethod.classInfo(
          "org.mybatis.spring.boot.autoconfigure", "MybatisAutoConfiguration")),

  /** 时间处理 */
  SPRING_BOOT_TEST_DATETIME_UTILS(
      PkgBuildMethod.classInfo("com.ddd.common.infrastructure.utils", "DataBaseUtils"));

  /** 包定义信息 */
  private final ImportPackageInfo packageInfo;

  ImportJunitPkgKey(ImportPackageInfo packageInfo) {
    this.packageInfo = packageInfo;
  }

  public ImportPackageInfo getPackageInfo() {
    return packageInfo;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("CreateCommKey{");
    sb.append('}');
    return sb.toString();
  }
}
