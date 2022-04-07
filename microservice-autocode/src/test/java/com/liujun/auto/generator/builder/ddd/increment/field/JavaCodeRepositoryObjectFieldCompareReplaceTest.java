package com.liujun.auto.generator.builder.ddd.increment.field;

import com.liujun.auto.generator.builder.ddd.increment.field.entity.EntityField;
import com.liujun.auto.generator.builder.ddd.increment.field.entity.EntityInfo;
import com.liujun.auto.generator.builder.ddd.increment.field.entity.EntityMethod;
import com.liujun.auto.generator.builder.ddd.increment.field.matcher.MatcherFieldContext;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.operator.code.CodeBaseUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 测试属性的生成
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaCodeRepositoryObjectFieldCompareReplaceTest {

  @Test
  public void testAnnotation() {
    GenerateCodeContext context = CodeBaseUtils.getBase();

    String value =
        "@EnableFeignClients\n"
            + "@SpringBootApplication\n"
            + "@MapperScan(value = {\"com.paraview.security.pap.microservice.**.repository.mapper\"})\n"
            + "@ImportResource(locations = {\"classpath*:/spring/**/Spring-*.xml\"})";

    MatcherFieldContext matchContext =
        JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE.builderContext(
            context, "table", value);

    List<String> dataAnnotationList =
        runAnnotation(JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE, matchContext);

    Assert.assertThat(dataAnnotationList.size(), Matchers.greaterThanOrEqualTo(1));
    Assert.assertThat(dataAnnotationList, Matchers.hasItem("@EnableFeignClients"));
    Assert.assertThat(dataAnnotationList, Matchers.hasItem("@SpringBootApplication"));
    Assert.assertThat(
        dataAnnotationList,
        Matchers.hasItem(
            "@MapperScan(value = {\"com.paraview.security.pap.microservice.**.repository.mapper\"})"));
    Assert.assertThat(
        dataAnnotationList,
        Matchers.hasItem("@ImportResource(locations = {\"classpath*:/spring/**/Spring-*.xml\"})"));
  }

  @Test
  public void testAnnotationSpring() {
    GenerateCodeContext context = CodeBaseUtils.getBase();

    String value = "@Autowired private PapActionDomainService actionDomainService;";

    MatcherFieldContext matchContext =
        JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE.builderContext(
            context, "table", value);

    List<String> dataAnnotationList =
        runAnnotation(JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE, matchContext);

    Assert.assertThat(dataAnnotationList.size(), Matchers.greaterThanOrEqualTo(1));
    Assert.assertThat(dataAnnotationList, Matchers.hasItem("@Autowired"));
  }

  @Test
  public void testAnnotationSpringData() {
    GenerateCodeContext context = CodeBaseUtils.getBase();

    String value = "@Autowired PapActionDomainService actionDomainService;";

    MatcherFieldContext matchContext =
        JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE.builderContext(
            context, "table", value);

    List<String> dataAnnotationList =
        runAnnotation(JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE, matchContext);

    Assert.assertThat(dataAnnotationList.size(), Matchers.greaterThanOrEqualTo(1));
    Assert.assertThat(dataAnnotationList, Matchers.hasItem("@Autowired"));
  }

  /**
   * 执行注解的方法
   *
   * @param instance
   * @param matchContext
   * @return
   */
  private List<String> runAnnotation(
      JavaCodeRepositoryObjectFieldCompareReplace instance, MatcherFieldContext matchContext) {
    List<String> result = null;
    // 获取方法信息
    Method methodInfo = this.getMethod("annotation", MatcherFieldContext.class);
    try {
      result = (List<String>) methodInfo.invoke(instance, matchContext);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }

    return result;
  }

  /**
   * 获取方法
   *
   * @param methodName 方法名
   * @param paramClass 参数类型信息
   * @return 方法名
   */
  private Method getMethod(String methodName, Class<?>... paramClass) {
    try {
      Class cls =
          Class.forName(
                  "com.liujun.auto.generator.builder.ddd.increment.field.JavaCodeRepositoryObjectFieldCompareReplace");
      Method methodInfo = cls.getDeclaredMethod(methodName, paramClass);
      methodInfo.setAccessible(true);
      return methodInfo;
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Test
  public void testAnnotationValues() {
    GenerateCodeContext context = CodeBaseUtils.getBase();

    String value =
        "@EnableFeignClients\n"
            + "@SpringBootApplication\n"
            + "@MapperScan(value = \n"
            + "{\"com.paraview.security.pap.microservice.**.repository.mapper\"})\n"
            + "@ImportResource(locations = {\"classpath*:/spring/**/Spring-*.xml\"})";

    MatcherFieldContext matchContext =
        JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE.builderContext(
            context, "table", value);

    List<String> dataAnnotationList =
        runAnnotation(JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE, matchContext);

    Assert.assertThat(dataAnnotationList.size(), Matchers.greaterThanOrEqualTo(1));
    Assert.assertThat(dataAnnotationList.size(), Matchers.greaterThanOrEqualTo(1));
    Assert.assertThat(dataAnnotationList, Matchers.hasItem("@EnableFeignClients"));
    Assert.assertThat(dataAnnotationList, Matchers.hasItem("@SpringBootApplication"));
    Assert.assertThat(
        dataAnnotationList,
        Matchers.hasItem(
            "@MapperScan(value = \n{\"com.paraview.security.pap.microservice.**.repository.mapper\"})"));
    Assert.assertThat(
        dataAnnotationList,
        Matchers.hasItem("@ImportResource(locations = {\"classpath*:/spring/**/Spring-*.xml\"})"));
  }

  /** 获取访问修饰符 */
  @Test
  public void testGetVisit() {
    GenerateCodeContext context = CodeBaseUtils.getBase();
    String value = "@Autowired private PapActionDomainService actionDomainService;";
    MatcherFieldContext matchContext =
        JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE.builderContext(
            context, "table", value);
    matchContext.setLastIndex("@Autowired".length());
    String dataAnnotationList =
        runVisit(JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE, matchContext);

    Assert.assertEquals(dataAnnotationList, "private");
  }

  /** 获取访问修饰符 */
  @Test
  public void testGetVisitDefault() {
    GenerateCodeContext context = CodeBaseUtils.getBase();
    String value = "@Autowired PapActionDomainService actionDomainService;";
    MatcherFieldContext matchContext =
        JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE.builderContext(
            context, "table", value);
    matchContext.setLastIndex("@Autowired".length());
    String dataAnnotationList =
        runVisit(JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE, matchContext);

    Assert.assertEquals(dataAnnotationList, Symbol.EMPTY);
  }

  /**
   * 执行注解的方法
   *
   * @param instance
   * @param matchContext
   * @return
   */
  private String runVisit(
      JavaCodeRepositoryObjectFieldCompareReplace instance, MatcherFieldContext matchContext) {
    String result = null;
    // 获取方法信息
    Method methodInfo = this.getMethod("visit", MatcherFieldContext.class);
    try {
      result = (String) methodInfo.invoke(instance, matchContext);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }

    return result;
  }

  @Test
  public void testGetStaticFainlDefault() {
    GenerateCodeContext context = CodeBaseUtils.getBase();
    String value = "@Autowired PapActionDomainService actionDomainService;";
    MatcherFieldContext matchContext =
        JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE.builderContext(
            context, "table", value);
    matchContext.setLastIndex("@Autowired".length());
    EntityInfo entityInfo = new EntityInfo();
    staticFainalGet(JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE, matchContext, entityInfo);
    Assert.assertEquals(entityInfo.getStaticKey(), Symbol.EMPTY);
    Assert.assertEquals(entityInfo.getFinalKey(), Symbol.EMPTY);
  }

  @Test
  public void testGetStatic() {
    GenerateCodeContext context = CodeBaseUtils.getBase();
    String value = "@Autowired private static PapActionDomainService actionDomainService;";
    MatcherFieldContext matchContext =
        JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE.builderContext(
            context, "table", value);
    matchContext.setLastIndex("@Autowired".length());
    EntityInfo entityInfo = new EntityInfo();
    staticFainalGet(JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE, matchContext, entityInfo);
    Assert.assertEquals(entityInfo.getStaticKey(), JavaKeyWord.STATIC);
    Assert.assertEquals(entityInfo.getFinalKey(), Symbol.EMPTY);
  }

  @Test
  public void testGetFinalStatic() {
    GenerateCodeContext context = CodeBaseUtils.getBase();
    String value = "@Autowired private final static PapActionDomainService actionDomainService;";
    MatcherFieldContext matchContext =
        JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE.builderContext(
            context, "table", value);
    matchContext.setLastIndex("@Autowired".length());
    EntityInfo entityInfo = new EntityInfo();
    staticFainalGet(JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE, matchContext, entityInfo);
    Assert.assertEquals(entityInfo.getStaticKey(), JavaKeyWord.STATIC);
    Assert.assertEquals(entityInfo.getFinalKey(), JavaKeyWord.FINAL);
  }

  @Test
  public void testGetStaticFinalInfo() {
    GenerateCodeContext context = CodeBaseUtils.getBase();
    String value = "@Autowired private static final PapActionDomainService actionDomainService;";
    MatcherFieldContext matchContext =
        JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE.builderContext(
            context, "table", value);
    matchContext.setLastIndex("@Autowired".length());
    EntityInfo entityInfo = new EntityInfo();
    staticFainalGet(JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE, matchContext, entityInfo);
    Assert.assertEquals(entityInfo.getStaticKey(), JavaKeyWord.STATIC);
    Assert.assertEquals(entityInfo.getFinalKey(), JavaKeyWord.FINAL);
  }

  @Test
  public void testGetFinalInfo() {
    GenerateCodeContext context = CodeBaseUtils.getBase();
    String value = "@Autowired private final PapActionDomainService actionDomainService;";
    MatcherFieldContext matchContext =
        JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE.builderContext(
            context, "table", value);
    matchContext.setLastIndex("@Autowired".length());
    EntityInfo entityInfo = new EntityInfo();
    staticFainalGet(JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE, matchContext, entityInfo);
    Assert.assertEquals(entityInfo.getStaticKey(), Symbol.EMPTY);
    Assert.assertEquals(entityInfo.getFinalKey(), JavaKeyWord.FINAL);
  }

  /**
   * 执行注解的方法
   *
   * @param instance
   * @param matchContext
   * @return
   */
  private void staticFainalGet(
      JavaCodeRepositoryObjectFieldCompareReplace instance,
      MatcherFieldContext matchContext,
      EntityInfo entity) {
    String result = null;
    // 获取方法信息
    Method methodInfo =
        this.getMethod("staticAndFinalFlag", MatcherFieldContext.class, EntityInfo.class);
    try {
      methodInfo.invoke(instance, matchContext, entity);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void typeObject() {
    GenerateCodeContext context = CodeBaseUtils.getBase();
    String value = "@Autowired private final PapActionDomainService actionDomainService;";
    MatcherFieldContext matchContext =
        JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE.builderContext(
            context, "table", value);
    matchContext.setLastIndex("@@Autowired private final".length());
    String classType = typeGet(JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE, matchContext);
    Assert.assertEquals(classType, "PapActionDomainService");
  }

  @Test
  public void typeListObject() {
    GenerateCodeContext context = CodeBaseUtils.getBase();
    String value = "@Autowired private final List<PapActionDomainService> actionDomainService;";
    MatcherFieldContext matchContext =
        JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE.builderContext(
            context, "table", value);
    matchContext.setLastIndex("@@Autowired private final".length());
    String classType = typeGet(JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE, matchContext);
    Assert.assertEquals(classType, "List<PapActionDomainService>");
  }

  /**
   * 读取类型信息
   *
   * @param instance
   * @param matchContext
   * @return
   */
  private String typeGet(
      JavaCodeRepositoryObjectFieldCompareReplace instance, MatcherFieldContext matchContext) {
    String result = null;
    // 获取方法信息
    Method methodInfo = this.getMethod("typeInfo", MatcherFieldContext.class);
    try {
      result = (String) methodInfo.invoke(instance, matchContext);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }

    return result;
  }

  @Test
  public void varName() {
    GenerateCodeContext context = CodeBaseUtils.getBase();
    String value = "@Autowired private final PapActionDomainService actionDomainService;";
    MatcherFieldContext matchContext =
        JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE.builderContext(
            context, "table", value);
    matchContext.setLastIndex("@Autowired private final PapActionDomainService".length());
    EntityInfo entityInfo = new EntityInfo();
    EntityField field =
        varNameAndValue(
            JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE, matchContext, entityInfo);
    Assert.assertEquals(field.getFieldName(), "actionDomainService");
    Assert.assertEquals(field.getValue(), "");
  }

  @Test
  public void varListName() {
    GenerateCodeContext context = CodeBaseUtils.getBase();
    String value =
        "@Autowired private final List<PapActionDomainService> actionDomainList = new ArrayList();";
    MatcherFieldContext matchContext =
        JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE.builderContext(
            context, "table", value);
    matchContext.setLastIndex("@Autowired private final List<PapActionDomainService>".length());
    EntityInfo entityInfo = new EntityInfo();
    EntityField field =
        varNameAndValue(
            JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE, matchContext, entityInfo);
    Assert.assertEquals(field.getFieldName(), "actionDomainList");
    Assert.assertEquals(field.getValue(), "new ArrayList()");
  }

  /**
   * 读取变量名信息
   *
   * @param instance
   * @param matchContext
   * @return
   */
  private EntityField varNameAndValue(
      JavaCodeRepositoryObjectFieldCompareReplace instance,
      MatcherFieldContext matchContext,
      EntityInfo entity) {
    // 获取方法信息
    Method methodInfo =
        this.getMethod("varNameAndValue", MatcherFieldContext.class, EntityInfo.class);
    try {
      return (EntityField) methodInfo.invoke(instance, matchContext, entity);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }

    throw new IllegalArgumentException("oper error");
  }

  @Test
  public void methodGet() {
    GenerateCodeContext context = CodeBaseUtils.getBase();
    String targetValue =
        "\n"
            + "    final StringBuilder sb = new StringBuilder(\"EntityField{\");\n"
            + "    sb.append(\"value='\").append(value).append('\\'');\n"
            + "    sb.append('}');\n"
            + "    sb.append(super.toString());\n"
            + "    return sb.toString();\n";

    String value = "public String toString(String name) {" + targetValue + "}";

    MatcherFieldContext matchContext =
        JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE.builderContext(
            context, "table", value);
    matchContext.setLastIndex("public String".length());
    EntityInfo entityInfo = new EntityInfo();
    EntityMethod field =
        MethodGet(JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE, matchContext, entityInfo);
    Assert.assertEquals(field.getFieldName(), "toString");
    Assert.assertEquals(field.getMethodParam(), "String name");
    Assert.assertEquals(targetValue, field.getMethodContext());
  }

  @Test
  public void methodGetTwo() {
    GenerateCodeContext context = CodeBaseUtils.getBase();

    String targetValue =
        "\n"
            + "    final StringBuilder sb = new StringBuilder(\"EntityField{\");\n"
            + "    sb.append(\"value='\").append(value).append('\\'');\n"
            + "    sb.append('}');\n"
            + "    sb.append(super.toString());\n"
            + "    return sb.toString();\n";

    String value = "public String toString(String name,String outName) {" + targetValue + "}";

    MatcherFieldContext matchContext =
        JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE.builderContext(
            context, "table", value);
    matchContext.setLastIndex("public String".length());
    EntityInfo entityInfo = new EntityInfo();
    EntityMethod field =
        MethodGet(JavaCodeRepositoryObjectFieldCompareReplace.INSTANCE, matchContext, entityInfo);
    Assert.assertEquals(field.getFieldName(), "toString");
    Assert.assertEquals(field.getMethodParam(), "String name,String outName");
    Assert.assertEquals(targetValue, field.getMethodContext());
  }

  /**
   * 读取变量名信息
   *
   * @param instance
   * @param matchContext
   * @return
   */
  private EntityMethod MethodGet(
      JavaCodeRepositoryObjectFieldCompareReplace instance,
      MatcherFieldContext matchContext,
      EntityInfo entity) {
    // 获取方法信息
    Method methodInfo =
        this.getMethod("varNameAndValue", MatcherFieldContext.class, EntityInfo.class);
    try {
      return (EntityMethod) methodInfo.invoke(instance, matchContext, entity);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }

    throw new IllegalArgumentException("oper error");
  }
}
