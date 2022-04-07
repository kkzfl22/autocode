package com.liujun.auto.generator.builder.ddd.increment.field;

import com.liujun.auto.generator.builder.ddd.full.repositorymybatis.JavaCodeRepositoryMyBatisObjectCreate;
import com.liujun.auto.generator.builder.ddd.increment.field.constant.ClassFieldTypeEnum;
import com.liujun.auto.generator.builder.ddd.increment.field.entity.EntityInfo;
import com.liujun.auto.generator.builder.ddd.increment.field.entity.EntityMethod;
import com.liujun.auto.generator.builder.ddd.increment.field.matcher.MatcherFieldContext;
import com.liujun.auto.algorithm.ahocorsick.AhoCorasickChar;
import com.liujun.auto.algorithm.ahocorsick.BaseAhoCorasick;
import com.liujun.auto.algorithm.ahocorsick.MatcherBusi;
import com.liujun.auto.algorithm.booyermoore.CharMatcherBmBadChars;
import com.liujun.auto.constant.Symbol;
import com.liujun.auto.constant.SymbolChar;
import com.liujun.auto.generator.builder.ddd.entity.GenerateCodeContext;
import com.liujun.auto.generator.builder.ddd.entity.JavaClassFieldEntity;
import com.liujun.auto.generator.builder.GenerateCodeInf;
import com.liujun.auto.generator.builder.ddd.code.GenerateJavaBean;
import com.liujun.auto.generator.builder.ddd.increment.field.entity.EntityField;
import com.liujun.auto.generator.builder.ddd.increment.field.matcher.MultMatcherProcess;
import com.liujun.auto.generator.builder.utils.FileReaderUtils;
import com.liujun.auto.generator.builder.utils.GeneratePathUtils;
import com.liujun.auto.generator.builder.utils.TableColumnUtils;
import com.liujun.auto.generator.database.entity.TableColumnDTO;
import com.liujun.auto.generator.database.entity.TableInfoDTO;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * 当数据库增加修改后，进行当前字体字段的影射处理
 *
 * <p>目前情况在在默认访问修饰符情况下可能存在问题
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

  /** 进行数据)匹配的操作 */
  private static final CharMatcherBmBadChars MATCHER_RIGHT_MATCHER =
      CharMatcherBmBadChars.getBadInstance(Symbol.BRACKET_RIGHT);

  /** 多模式式串的匹配处理 */
  private static final MultMatcherProcess matcherProcess;

  /** 操作示例 */
  public static final Map<String, Consumer<MatcherFieldContext>> RUN_REPLACE_FIELD;

  /** 存储层的实体对象 */
  private static final String REPOSITORY_MATCHER_KEY = "repository_persist_entity";

  /** 多字符的匹配操作 */
  private static final BaseAhoCorasick AHO_ANNOTATION_FLAG = new AhoCorasickChar();

  /**
   * 当这些字符被匹配说明annotation结束
   *
   * <p>空格匹配，类似spring的@Autowired private
   *
   * <p>左括号匹配,类似@ImportResource(locations = {xxx.xml})
   *
   * <p>换行符匹配，类似@SpringBootApplication
   */
  private static final List<String> FINISH_ANNOTATION =
      Arrays.asList(Symbol.SPACE, Symbol.BRACKET_LEFT, Symbol.ENTER_LINE);

  /** 注解的结束处理 */
  private static final List<String> ANNOTATION_NEXT =
      Arrays.asList(
          Symbol.NOTE,
          JavaKeyWord.PRIVATE,
          JavaKeyWord.PUBLIC,
          JavaKeyWord.PROTECTED,
          Symbol.SPACE);

  /** 多字符的匹配操作 */
  private static final BaseAhoCorasick AHO_ANNOTATION_NEXT_FLAG = new AhoCorasickChar();

  /** 注解的结束处理 */
  private static final List<String> VISIT_NEXT =
      Arrays.asList(JavaKeyWord.PRIVATE, JavaKeyWord.PUBLIC, JavaKeyWord.PROTECTED);

  /** 多字符的匹配操作 */
  private static final BaseAhoCorasick VISIT_NEXT_FLAG = new AhoCorasickChar();

  /** static和final的结果 */
  private static final List<String> STATIC_FINAL_NEXT =
      Arrays.asList(JavaKeyWord.STATIC, JavaKeyWord.FINAL);

  /** 多匹配的结果 */
  private static final BaseAhoCorasick STATIC_FINAL_FLAG = new AhoCorasickChar();

  /** 静态关键字与final关键字匹配的次数 */
  private static final int STATIC_FINAL_MAX_NUM = 3;

  /** 空参数的标识 */
  private static final int EMPTY_PARAM_FLAG = 1;

  /** 相对于括号的位置，起始位置需要加1操作 */
  private static final int CONTEXT_LEFT_STAT = 1;

  /** 注解的结束处理 */
  private static final List<String> VAR_NAME_END =
      Arrays.asList(Symbol.SEMICOLON, Symbol.EQUAL, Symbol.BRACKET_LEFT);

  /** 多字符的匹配操作 */
  private static final BaseAhoCorasick VAR_NAME_END_FLAG = new AhoCorasickChar();

  static {
    // 初始化容器对象
    RUN_REPLACE_FIELD = new HashMap<>(2);
    // 以注释开始的情况
    RUN_REPLACE_FIELD.put(JavaKeyWord.ANNO_CLASS, INSTANCE::entityProcess);
    // 直接以代码开始的情况
    RUN_REPLACE_FIELD.put(JavaKeyWord.PRIVATE, INSTANCE::codeStartProcess);
    // 进行多模式串的处理
    matcherProcess = MultMatcherProcess.getInstance(RUN_REPLACE_FIELD);
    // 构建模式串
    AHO_ANNOTATION_FLAG.buildFailure(FINISH_ANNOTATION);
    // 进行构建操作
    AHO_ANNOTATION_FLAG.builderFailurePointer();

    // 构建结束注解的结束匹配
    AHO_ANNOTATION_NEXT_FLAG.buildFailure(ANNOTATION_NEXT);
    // 构建指针
    AHO_ANNOTATION_NEXT_FLAG.builderFailurePointer();

    // 访问修饰符
    VISIT_NEXT_FLAG.buildFailure(VISIT_NEXT);
    // 构建指针
    VISIT_NEXT_FLAG.builderFailurePointer();

    // static和final的匹配操作
    STATIC_FINAL_FLAG.buildFailure(STATIC_FINAL_NEXT);
    // 进行失败指针的构建
    STATIC_FINAL_FLAG.builderFailurePointer();

    // 变量结束的匹配
    VAR_NAME_END_FLAG.buildFailure(VAR_NAME_END);
    VAR_NAME_END_FLAG.builderFailurePointer();
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

      // 属性内容信息
      MatcherFieldContext fieldContext = this.builderContext(param, tableName, repositoryObejct);

      // 从文件中读取的属性处理
      List<EntityInfo> readerEntityList = entityProcess(fieldContext);

      // 数据库相关的字段的生成
      List<JavaClassFieldEntity> databaseEntityList =
          GenerateJavaBean.INSTANCE.propertiesToJavaEntity(
              tableEntry.getValue(), param.getTypeEnum());

      // 对比并执行数据设置操作
      compareAndSet(readerEntityList, databaseEntityList);

      // 将数据输出到文件中

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
   * 执行本地实体与数据库的实体的对象操作
   *
   * @param readEntityList 读取的文件的列表
   * @param databaseEntityList 数据库中的列表
   * @return 对比后生成的结果
   */
  private List<EntityInfo> compareAndSet(
      List<EntityInfo> readEntityList, List<JavaClassFieldEntity> databaseEntityList) {
    List<EntityInfo> resultList = new ArrayList<>(readEntityList);

    // 类型设置操作
    typeSet(resultList, databaseEntityList);

    // 检查当前数据库中存在，但实体中不存在的字段
    fieldAdd(resultList, databaseEntityList);

    return null;
  }

  /**
   * 进行属性的添加操作
   *
   * @param resultList
   * @param databaseEntityList
   */
  private void fieldAdd(
      List<EntityInfo> resultList, List<JavaClassFieldEntity> databaseEntityList) {

    for (JavaClassFieldEntity entityInfo : databaseEntityList) {

      // 如果当前不存在，执行添加操作
      if (!exists(resultList, entityInfo)) {
        resultList.add(toEntityField(entityInfo));
      }
    }
  }

  /**
   * 将属性转换为java中的属性对象
   *
   * @param entityInfo
   * @return
   */
  private EntityInfo toEntityField(JavaClassFieldEntity entityInfo) {
    EntityField field = new EntityField();
    field.setComment(entityInfo.getComment());
    field.setAnnotation(entityInfo.getAnnotationList());
    field.setVisit(entityInfo.getVisit());
    field.setStaticKey(entityInfo.getStaticFlag());
    field.setFinalKey(entityInfo.getFinalFlag());
    field.setFieldType(entityInfo.getType());
    field.setFieldName(entityInfo.getName());
    field.setValue(entityInfo.getValue());

    return field;
  }

  /**
   * 检查当前字段在当前类型中是否存在
   *
   * @param resultList 当前文件中属性
   * @param databaseEntity 当前数据库中的属性
   * @return true 存在，false 不存在
   */
  private boolean exists(List<EntityInfo> resultList, JavaClassFieldEntity databaseEntity) {
    for (EntityInfo entityInfo : resultList) {

      // 如果非属性，则跳过
      if (!ClassFieldTypeEnum.FIELD.equals(entityInfo.getClassType())) {
        continue;
      }

      if (entityInfo.getFieldName().equals(databaseEntity.getName())) {
        return true;
      }
    }
    return false;
  }

  /**
   * 数据类型不一致的设置操作
   *
   * @param resultList
   * @param databaseEntityList
   */
  private void typeSet(List<EntityInfo> resultList, List<JavaClassFieldEntity> databaseEntityList) {
    // 将数据转换为map
    Map<String, JavaClassFieldEntity> dataMap = this.parseMap(databaseEntityList);

    for (EntityInfo entityInfo : resultList) {
      // 如果非属性，则跳过
      if (!ClassFieldTypeEnum.FIELD.equals(entityInfo.getClassType())) {
        continue;
      }

      JavaClassFieldEntity classFieldInfo = dataMap.get(entityInfo.getFieldName());

      // 当前字段不存在，则跳过
      if (null == classFieldInfo) {
        continue;
      }
      // 当前字段存在，则检查类型是否一致
      else {
        // 如果当前的类型一致，则跳过
        if (classFieldInfo.getType().equals(entityInfo.getFieldType())) {
          continue;
        }
        // 如果当前的类型不一致，则进行类型的覆盖操作
        else {
          entityInfo.setFieldType(classFieldInfo.getType());
        }
      }
    }
  }

  private Map<String, JavaClassFieldEntity> parseMap(
      List<JavaClassFieldEntity> databaseEntityList) {
    Map<String, JavaClassFieldEntity> dataMap = new HashMap<>(databaseEntityList.size());

    for (JavaClassFieldEntity entityInfo : databaseEntityList) {
      dataMap.put(entityInfo.getName(), entityInfo);
    }

    return dataMap;
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
        readerPath + Symbol.PATH + JavaCodeRepositoryMyBatisObjectCreate.INSTANCE.getClassName(tableName);

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
   * 属性与实体相互处理
   *
   * @param context
   */
  private List<EntityInfo> entityProcess(MatcherFieldContext context) {

    EntityInfo entity = new EntityInfo();

    // 读取注释
    entity.setComment(comment(context));
    // 注解处理
    entity.setAnnotation(annotation(context));
    // 读取访问修饰符
    entity.setVisit(visit(context));
    // 静态标识及final标识的设置
    this.staticAndFinalFlag(context, entity);
    // 读取类型信息
    entity.setFieldType(typeInfo(context));
    // 变量名与变量值
    varNameAndValue(context, entity);

    List<EntityInfo> dataList = context.getValue(REPOSITORY_MATCHER_KEY);
    dataList.add(entity);

    return dataList;
  }

  /**
   * 读取变量名
   *
   * @param context
   * @return
   */
  private EntityInfo varNameAndValue(MatcherFieldContext context, EntityInfo entity) {

    // 1,跳过之前的空格
    this.skipSpace(context);
    // 设置最后的位置在空格之后
    context.setLastIndex(context.getLastIndex() + 1);
    // 查找结束符
    MatcherBusi varNameIndex =
        VAR_NAME_END_FLAG.matcherOne(context.getSrcDataArray(), context.getLastIndex());

    if (varNameIndex == null || varNameIndex.getMatcherIndex() == -1) {
      throw new IllegalArgumentException("java code Error: not found var End");
    }

    // 如果当前结束符为;号，说明结束
    if (Symbol.SEMICOLON.equals(varNameIndex.getMatcherKey())) {

      String varName =
          new String(
              context.getSrcDataArray(),
              context.getLastIndex(),
              varNameIndex.getMatcherIndex() - context.getLastIndex());

      entity.setFieldName(varName);
      context.setLastIndex(varNameIndex.getMatcherIndex() + Symbol.SEMICOLON.length());
      // 设置当前为属性
      entity.setClassType(ClassFieldTypeEnum.FIELD);

      return getFieldData(entity, Symbol.EMPTY);
    }
    // 如果当前为=号
    else if (Symbol.EQUAL.equals(varNameIndex.getMatcherKey())) {
      // 设置变量的值
      String value = setVarValue(varNameIndex, context, entity);
      // 设置当前为属性
      entity.setClassType(ClassFieldTypeEnum.FIELD);
      return getFieldData(entity, value);
    }
    // 当前发现左括号说明当前为方法
    else if (Symbol.BRACKET_LEFT.equals(varNameIndex.getMatcherKey())) {

      String varName =
          new String(
              context.getSrcDataArray(),
              context.getLastIndex(),
              varNameIndex.getMatcherIndex() - context.getLastIndex());

      entity.setFieldName(varName.trim());

      // 设置当前的操作方法为开始 方法
      context.setLastIndex(varNameIndex.getMatcherIndex());
      // 设置当前为方法
      entity.setClassType(ClassFieldTypeEnum.METHOD);
      // 找到括号的结束位置
      return this.getMethod(context, entity);
    }

    // 匹配错误
    throw new IllegalArgumentException("code match error");
  }

  /**
   * 获取方法信息
   *
   * @param context 上下文信息
   * @param entityInfo 实体信息
   * @return 返回前前的实体
   */
  private EntityInfo getMethod(MatcherFieldContext context, EntityInfo entityInfo) {

    EntityMethod result = new EntityMethod();
    result.setComment(entityInfo.getComment());
    result.setAnnotation(entityInfo.getAnnotation());
    result.setVisit(entityInfo.getVisit());
    result.setStaticKey(entityInfo.getStaticKey());
    result.setFinalKey(entityInfo.getFinalKey());
    result.setFieldType(entityInfo.getFieldType());
    result.setFieldName(entityInfo.getFieldName());
    result.setClassType(entityInfo.getClassType());
    // 设置方法参数
    result.setMethodParam(bracketMatch(context, SymbolChar.BRACKET_LEFT, SymbolChar.BRACKET_RIGHT));
    // 设置方法体信息获取
    result.setMethodContext(bracketMatch(context, SymbolChar.BRACE_LEFT, SymbolChar.BRACE_RIGHT));

    return result;
  }

  /**
   * 进行括号的匹配操作,仅提取内容，去除括号
   *
   * @param context 上下文对象
   * @param left 左括号
   * @param right 右括号
   * @return 内容信息
   */
  private String bracketMatch(MatcherFieldContext context, char left, char right) {
    int leftIndex = 0;
    int rightIndex = 0;

    boolean first = true;

    Stack<Character> LEFT = new Stack<>();
    for (int i = context.getLastIndex(); i < context.getSrcDataArray().length; i++) {
      if (context.getSrcDataArray()[i] == left) {
        LEFT.push(left);

        // 记录下首个左括号的位置
        if (first) {
          leftIndex = i;
          first = false;
        }

      } else if (context.getSrcDataArray()[i] == right) {
        LEFT.pop();

        // 当栈为空，说明当前括号匹配结束
        if (LEFT.isEmpty()) {
          rightIndex = i;
          break;
        }
      }
    }

    // 设置最后的位置为结束的点
    context.setLastIndex(rightIndex);

    // 如果当前差值为1，说明无参数
    if ((rightIndex - leftIndex) == EMPTY_PARAM_FLAG) {
      return Symbol.EMPTY;
    }

    // 内容左括号位置
    int leftContextStart = leftIndex + CONTEXT_LEFT_STAT;
    // 得到括号之间的参数字符信息
    return new String(context.getSrcDataArray(), leftContextStart, rightIndex - leftContextStart);
  }

  /**
   * 获取属性的信息
   *
   * @param entityInfo 公共的信息
   * @param value 当前的属性值
   * @return 实体信息
   */
  private EntityInfo getFieldData(EntityInfo entityInfo, String value) {
    EntityField result = new EntityField();
    result.setComment(entityInfo.getComment());
    result.setAnnotation(entityInfo.getAnnotation());
    result.setVisit(entityInfo.getVisit());
    result.setStaticKey(entityInfo.getStaticKey());
    result.setFinalKey(entityInfo.getFinalKey());
    result.setFieldType(entityInfo.getFieldType());
    result.setFieldName(entityInfo.getFieldName());
    result.setClassType(entityInfo.getClassType());
    result.setValue(value);

    return result;
  }

  /**
   * 设置变量的值
   *
   * @param varNameIndex 变量的索引
   * @param context 上下文对象
   * @param entity 实体
   */
  private String setVarValue(
      MatcherBusi varNameIndex, MatcherFieldContext context, EntityInfo entity) {
    // 读取名称
    // 查找空格
    int endNameIndex = this.findNextSpaceIndex(context);
    String varName =
        new String(
            context.getSrcDataArray(),
            context.getLastIndex(),
            endNameIndex - context.getLastIndex());
    entity.setFieldName(varName);
    context.setLastIndex(endNameIndex);

    // 需要路过等号之后的空格
    context.setLastIndex(varNameIndex.getMatcherIndex() + 1);
    this.skipSpace(context);
    // 设置最后的位置在空格之后
    context.setLastIndex(context.getLastIndex() + 1);

    int endIndex = findDataIndex(context, SymbolChar.SEMICOLON);
    String value =
        new String(
            context.getSrcDataArray(), context.getLastIndex(), endIndex - context.getLastIndex());
    // entity.setValue(value);
    // 设置最后的结束符的位置
    context.setLastIndex(endIndex + Symbol.SEMICOLON.length());

    return value;
  }

  /**
   * 读取类型信息
   *
   * @param context
   * @return
   */
  private String typeInfo(MatcherFieldContext context) {

    // 1,跳过空格
    this.skipSpace(context);
    // 2,找到下一个空格的位置
    int spaceIndex = findNextSpaceIndex(context);
    // 获取类型
    String type =
        new String(
            context.getSrcDataArray(), context.getLastIndex(), spaceIndex - context.getLastIndex());
    // 设置最后操作的位置
    context.setLastIndex(spaceIndex);

    return type;
  }

  /**
   * 查找下一个空格的位置
   *
   * @param context 上下文对象
   * @param dataChar 查找指定的字符
   * @return 索引
   */
  private int findDataIndex(MatcherFieldContext context, char dataChar) {
    for (int i = context.getLastIndex(); i < context.getSrcDataArray().length; i++) {
      // 当找到指定字符时，进行返回操作
      if (context.getSrcDataArray()[i] == dataChar) {
        return i;
      }
    }

    return -1;
  }

  /**
   * 查找下一个空格的位置
   *
   * @param context
   * @return
   */
  private int findNextSpaceIndex(MatcherFieldContext context) {
    for (int i = context.getLastIndex(); i < context.getSrcDataArray().length; i++) {
      // 当找到首个空格时，进行返回操作
      if (context.getSrcDataArray()[i] == SymbolChar.SPACE) {
        return i;
      }
    }

    throw new IllegalArgumentException("code error not find space");
  }

  /**
   * 进行字符中的客格跳过操作
   *
   * @param context
   */
  private void skipSpace(MatcherFieldContext context) {
    for (int i = context.getLastIndex(); i < context.getSrcDataArray().length; i++) {
      // 进行空格字符的跳过操作
      if (context.getSrcDataArray()[i] == SymbolChar.SPACE) {
        context.setLastIndex(i);
      }
      // 当数据不为空格，则返回
      else {
        break;
      }
    }
  }

  /**
   * 获取访问修饰符
   *
   * @param context 上下文对象信息
   * @return 当前访问修饰符
   */
  private String visit(MatcherFieldContext context) {
    MatcherBusi matcherBusi =
        VISIT_NEXT_FLAG.matcherOne(context.getSrcDataArray(), context.getLastIndex());

    if (matcherBusi.getMatcherIndex() != -1) {
      if (!Symbol.SPACE.equals(matcherBusi.getMatcherKey())) {
        // 移动最后查询到的位置
        context.setLastIndex(matcherBusi.getMatcherIndex() + matcherBusi.getMatcherKey().length());
        return matcherBusi.getMatcherKey();
      }
    }

    return Symbol.EMPTY;
  }

  /**
   * 获取static以及final标识符
   *
   * <p>由于static和final可以无先后顺序，匹配时只能谁先匹配到就设置谁
   *
   * @param context 上下文对象信息
   * @return 当前访问修饰符
   */
  private void staticAndFinalFlag(MatcherFieldContext context, EntityInfo entity) {
    int index = 0;

    while (index < STATIC_FINAL_MAX_NUM) {
      MatcherBusi matcherBusi =
          STATIC_FINAL_FLAG.matcherOne(context.getSrcDataArray(), context.getLastIndex());

      if (matcherBusi.getMatcherIndex() != -1) {
        // 静态标识
        if (JavaKeyWord.STATIC.equals(matcherBusi.getMatcherKey())) {
          // 移动最后查询到的位置
          context.setLastIndex(
              matcherBusi.getMatcherIndex() + matcherBusi.getMatcherKey().length());
          entity.setStaticKey(JavaKeyWord.STATIC);
        }
        // final标识
        else if (JavaKeyWord.FINAL.equals(matcherBusi.getMatcherKey())) {
          // 移动最后查询到的位置
          context.setLastIndex(
              matcherBusi.getMatcherIndex() + matcherBusi.getMatcherKey().length());
          entity.setFinalKey(JavaKeyWord.FINAL);
        }
      }
      // 当任务关键守都没有查询到，则返回
      else {
        break;
      }

      index++;
    }

    // 当没有进行设置时，则进行默认的值设置操作
    if (StringUtils.isEmpty(entity.getStaticKey())) {
      entity.setStaticKey(Symbol.EMPTY);
    }

    // 当没有设置final标识时，默认为空
    if (StringUtils.isEmpty(entity.getFinalKey())) {
      entity.setFinalKey(Symbol.EMPTY);
    }
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
   * 注解处理
   *
   * @param context 上下文对象
   * @return 注解信息
   */
  private List<String> annotation(MatcherFieldContext context) {
    // 最后的操作位置
    int noteIndex = NOTE_MATCHER.matcherIndex(context.getSrcDataArray(), context.getLastIndex());

    // 如果当前不存在注解符
    if (noteIndex == -1) {
      return Collections.emptyList();
    }

    List<String> dataList = new ArrayList<>();

    int index = 0;
    while (index < context.getSrcDataArray().length) {
      int matchIndex = noteIndex;
      // 进行结束情况的匹配操作
      MatcherBusi matcherBusi =
          AHO_ANNOTATION_FLAG.matcherOne(context.getSrcDataArray(), noteIndex);
      // 如果当前为空格，说明当前结束
      if (Symbol.SPACE.equals(matcherBusi.getMatcherKey())) {
        int length = matcherBusi.getMatcherIndex() - noteIndex;
        matchIndex = matcherBusi.getMatcherIndex();
        dataList.add(new String(context.getSrcDataArray(), noteIndex, length));
        // 设置最后操作的位置为当前
        context.setLastIndex(matchIndex);
      }
      // 如果当前为左括号，则需要进行右括号的匹配
      else if (Symbol.BRACKET_LEFT.equals(matcherBusi.getMatcherKey())) {
        int rightIndex =
            MATCHER_RIGHT_MATCHER.matcherIndex(
                    context.getSrcDataArray(), matcherBusi.getMatcherIndex())
                + Symbol.BRACKET_RIGHT.length();
        matchIndex = rightIndex;
        dataList.add(new String(context.getSrcDataArray(), noteIndex, rightIndex - noteIndex));
        // 设置最后操作的位置为当前
        context.setLastIndex(rightIndex);
      }
      // 如果当前直接匹配换行符，说明结束
      else if (Symbol.ENTER_LINE.equals(matcherBusi.getMatcherKey())) {
        int length = matcherBusi.getMatcherIndex() - noteIndex;
        matchIndex = matcherBusi.getMatcherIndex();
        dataList.add(new String(context.getSrcDataArray(), noteIndex, length));
        // 设置最后操作的位置为当前
        context.setLastIndex(matchIndex);
      }

      // 进行其他情况的匹配操作
      MatcherBusi rightMatchIndex =
          AHO_ANNOTATION_NEXT_FLAG.matcherOne(context.getSrcDataArray(), matchIndex);
      if (rightMatchIndex.getMatcherIndex() != -1) {
        // 当以上三种都匹配完成后，可能会存在后面多种情况的匹配
        // 如果是@注解的情况，则继续
        if (Symbol.NOTE.equals(rightMatchIndex.getMatcherKey())) {
          noteIndex = rightMatchIndex.getMatcherIndex();
        }
        // 访问修饰符的情况，继续后面的操作
        else {
          break;
        }
      }
      // 当不能被找到，则退出遍历
      else {
        break;
      }
      index++;
    }

    return dataList;
  }

  /**
   * 当前属性无注释的情况
   *
   * @param context
   */
  private void codeStartProcess(MatcherFieldContext context) {}
}
