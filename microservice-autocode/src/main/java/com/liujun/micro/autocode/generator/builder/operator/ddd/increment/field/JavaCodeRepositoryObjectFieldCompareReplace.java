package com.liujun.micro.autocode.generator.builder.operator.ddd.increment.field;

import com.liujun.micro.autocode.algorithm.booyermoore.CharMatcherBmBadChars;
import com.liujun.micro.autocode.constant.Symbol;
import com.liujun.micro.autocode.generator.builder.entity.GenerateCodeContext;
import com.liujun.micro.autocode.generator.builder.operator.GenerateCodeInf;
import com.liujun.micro.autocode.generator.builder.operator.ddd.full.JavaCodeRepositoryObjectCreate;
import com.liujun.micro.autocode.generator.builder.operator.ddd.increment.field.matcher.MatcherFieldContext;
import com.liujun.micro.autocode.generator.builder.operator.ddd.increment.field.matcher.MultMatcherProcess;
import com.liujun.micro.autocode.generator.builder.operator.utils.FileReaderUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.GeneratePathUtils;
import com.liujun.micro.autocode.generator.builder.operator.utils.TableColumnUtils;
import com.liujun.micro.autocode.generator.database.entity.TableColumnDTO;
import com.liujun.micro.autocode.generator.database.entity.TableInfoDTO;
import com.liujun.micro.autocode.generator.javalanguage.constant.JavaKeyWord;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 当数据库增加修改后，进行当前字体字段的影射处理
 *
 * <p>添加的情况：1，将数据库中增加的字段添加到实体中
 *
 * <p>修改的情况修改类型：将数据库中修改的类型在字段中做类型的修改。
 *
 * <p>修改的情况-表重命名:数据库字段与表字段无法一一对应，只能做添加字段，删除的字段无法删除，需提供重命名的另外处理
 *
 * <p>删除的情况：数据库中做了删除字段处理，
 *
 * <p>基于比较的实现逻辑中无法对比出数据库中删除的
 *
 * <p>why:当数据库操作字段后，手动需要在实体中添加字段，较为麻烦，而且也容易出错。这类功能可通过程序来实现，节省开发资源
 *
 * <p>how:读取数据库中字段，与现有实体中的字段进行对比。找出当前实体中缺少的字段，进行添加操作
 *
 * @author liujun
 * @version 0.0.1
 * @since 2020/04/08
 */
public class JavaCodeRepositoryObjectFieldCompareReplace implements GenerateCodeInf {

  /** 实例对象 */
  public static final JavaCodeRepositoryObjectFieldCompareReplace INSTANCE =
      new JavaCodeRepositoryObjectFieldCompareReplace();

  /** { 位置匹配 */
  private static final CharMatcherBmBadChars BRACE_LEFT_MATCHER =
      CharMatcherBmBadChars.getBadInstance(Symbol.BRACE_LEFT);

  /** 注释结束位置匹配 */
  private static final CharMatcherBmBadChars COMMENT_OVER_MATCHER =
      CharMatcherBmBadChars.getBadInstance(JavaKeyWord.ANNOTATION_KEY);

  /** 注解符匹配 */
  private static final CharMatcherBmBadChars NOTE_MATCHER =
      CharMatcherBmBadChars.getBadInstance(Symbol.NOTE);

  /** 左括号匹配 */
  private static final CharMatcherBmBadChars BRACKET_LEFT_MATCHER =
      CharMatcherBmBadChars.getBadInstance(Symbol.BRACKET_LEFT);

  /** 多模式式串的匹配处理 */
  private static final MultMatcherProcess matcherProcess;

  /** 操作示例 */
  public static final Map<String, Consumer<MatcherFieldContext>> RUN_REPLACE_FIELD;

  /** 存储层的实体对象 */
  private static final String REPOSITORY_MATCHER_KEY = "repository_persist_entity";

  static {
    // 初始化容器对象
    RUN_REPLACE_FIELD = new HashMap<>(2);
    // 以注释开始的情况
    RUN_REPLACE_FIELD.put(JavaKeyWord.ANNO_CLASS, INSTANCE::annotationStartProcess);
    // 直接以代码开始的情况
    RUN_REPLACE_FIELD.put(JavaKeyWord.PRIVATE, INSTANCE::codeStartProcess);
    // 进行多模式串的处理
    matcherProcess = MultMatcherProcess.getInstance(RUN_REPLACE_FIELD);
  }

  @Override
  public void generateCode(GenerateCodeContext param) {

    Map<String, TableInfoDTO> tableMap = param.getTableMap();
    Map<String, List<TableColumnDTO>> map = param.getColumnMapList();
    Iterator<Map.Entry<String, List<TableColumnDTO>>> tableNameEntry = map.entrySet().iterator();

    while (tableNameEntry.hasNext()) {
      Map.Entry<String, List<TableColumnDTO>> tableEntry = tableNameEntry.next();
      // 表名
      String tableName = tableEntry.getKey();

      // 1,读取当前文件
      String repositoryObejct = readerRepositoryObject(param, tableName);

      // 1,进行当前文件的解析操作
      // RepositoryEntity repositoryEntity = new RepositoryEntity("", "");

      //// 进行存储层的bean代码生成
      // StringBuilder persistBean =
      //    GenerateJavaBean.INSTANCE.generateJavaBean(
      //        packageInfo,
      //        tableEntry.getValue(),
      //        param.getGenerateConfig().getGenerate().getCode(),
      //        param.getGenerateConfig().getGenerate().getAuthor(),
      //        param.getTypeEnum());
      //
      //// 定义项目内的完整目录结构
      // String outPackagePath =
      //    param.getProjectPath().getSrcJavaNode().outPath()
      //        + Symbol.PATH
      //        + packageInfo.getPackagePath();
      //
      //// 进行存储层的实体输出
      // GenerateOutFileUtils.outJavaFile(
      //    persistBean,
      //    GeneratePathUtils.outServicePath(param),
      //    outPackagePath,
      //    packageInfo.getClassName());
    }
  }

  /**
   * 读取存储实体的内容信息
   *
   * @param param 上下文参数信息
   * @param tableName 表名
   * @return 读取的文件内容
   */
  private String readerRepositoryObject(GenerateCodeContext param, String tableName) {
    String repositoryPath = param.getProjectPath().getSrcJavaNode().outPath();
    String readerPath = GeneratePathUtils.outServicePath(param) + Symbol.PATH + repositoryPath;
    String mapperFileName =
        readerPath + Symbol.PATH + JavaCodeRepositoryObjectCreate.INSTANCE.getClassName(tableName);

    return FileReaderUtils.readFile(mapperFileName);
  }

  /**
   * 构建上下文对象信息
   *
   * @param param 上下文参数
   * @param tableName 表
   * @param dataContext mapper内容信息
   * @return 构建的操作对象
   */
  public MatcherFieldContext builderContext(
      GenerateCodeContext param, String tableName, String dataContext) {

    MatcherFieldContext context = new MatcherFieldContext(dataContext);

    context.setLastIndex(0);
    context.setTableInfo(param.getTableMap().get(tableName));
    context.setColumnList(param.getColumnMapList().get(tableName));
    context.setColumnMap(param.getColumnMapMap().get(tableName));
    context.putValue(REPOSITORY_MATCHER_KEY, new ArrayList<>(16));
    // 获取当前主键列表
    context.setPrimaryKeyList(TableColumnUtils.getPrimaryKey(context.getColumnList()));
    context.setTypeEnum(param.getTypeEnum());

    return context;
  }

  /**
   * 读取当前的内容，转化为实体对象信息
   *
   * @param data
   * @return
   */
  private Map<String, RepositoryEntity> toRepositoryMap(String data) {

    char[] matchData = data.toCharArray();

    int startIndex = 0;

    // 1,找到类中内容开始的位置
    int index = BRACE_LEFT_MATCHER.matcherIndex(matchData, startIndex);

    return Collections.emptyMap();
  }

  /**
   * 以注释开始的处理
   *
   * @param context
   */
  private void annotationStartProcess(MatcherFieldContext context) {

    RepositoryEntity entity = new RepositoryEntity();
    entity.setComment(comment(context));

    // 注解处理

    List<RepositoryEntity> dataList = context.getValue(REPOSITORY_MATCHER_KEY);
    dataList.add(entity);
  }

  /**
   * 注释处理
   *
   * @param context 内容信息
   * @return 注释信息
   */
  private String comment(MatcherFieldContext context) {
    int matStartIndex = context.getMatcherBusi().getMatcherIndex();
    // 注释信息处理
    int commentIndex = COMMENT_OVER_MATCHER.matcherIndex(context.getSrcDataArray(), matStartIndex);
    int length = commentIndex + JavaKeyWord.ANNOTATION_KEY.length() - matStartIndex;
    String comment = new String(context.getSrcDataArray(), matStartIndex, length);
    // 更新最后操作位置
    context.setLastIndex(commentIndex + JavaKeyWord.ANNOTATION_KEY.length());

    return comment;
  }

  /**
   * 注释处理
   *
   * @param context 上下文对象
   * @return 注解信息
   */
  private String annotation(MatcherFieldContext context) {
    // 最后的操作位置
    int noteIndex = NOTE_MATCHER.matcherIndex(context.getSrcDataArray(), context.getLastIndex());

    // 如果当前不存在注解符
    if (noteIndex == -1) {
      return Symbol.EMPTY;
    }

    // 当前存在注解符，检查左括号
    int matchIndex = BRACKET_LEFT_MATCHER.matcherIndex(context.getSrcDataArray(), noteIndex);
    String value = new String("");
    value.indexOf("12", 2);

    return Symbol.EMPTY;
  }

  /**
   * 当前属性无注释的情况
   *
   * @param context
   */
  private void codeStartProcess(MatcherFieldContext context) {}

  /** 内部类，存储层实体信息 */
  @Getter
  @Setter
  @ToString
  private class RepositoryEntity {

    /** 注释 */
    private String comment;

    /** 注解信息 */
    private String annotation;

    /** 访问修饰符 */
    private String visit;

    /** 静态标识 */
    private String staticKey;

    /** final标识 */
    private String finalKey;

    /** 实体类型 */
    private String fieldType;

    /** 实体名称 */
    private String fieldName;

    /** 值信息 */
    private String value;
  }
}
