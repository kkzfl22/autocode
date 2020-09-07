package com.liujun.micro.autocode.generator.javalanguage.constant;

/**
 * 定义java的关键字
 *
 * @author liujun
 * @version 0.0.1
 */
public class JavaKeyWord {

  /** 文件后缀名 */
  public static final String FILE_SUFFIX = ".java";

  /** 定义包的关键字 */
  public static final String PACKAGE = "package";

  /** 导入包 */
  public static final String IMPORT = "import";

  /** 公共 */
  public static final String PUBLIC = "public";

  /** this */
  public static final String THIS = "this";

  /** new */
  public static final String NEW = "new";

  /** 返回 */
  public static final String RETURN = "return";

  /** int类型 */
  public static final String INT_TYPE = "int";

  /** 接口 */
  public static final String INTERFACE_KEY = "interface";

  /** 类定义的关键字 */
  public static final String CLASS_KEY = "class";

  /** private 关键字 */
  public static final String PRIVATE = "private";

  /** 静态 */
  public static final String STATIC = "static";

  /** 最终的关键字 */
  public static final String FINAL = "final";

  /** 无返回值对象定义 */
  public static final String VOID = "void";

  /** 继承关键字 */
  public static final String EXTEND = "extends";

  /** 实现接口关键字 */
  public static final String IMPLEMENTS = "implements";

  /** 判断 */
  public static final String IF = "if ";

  /** 判断 */
  public static final String ELSE_IF = "else if ";

  /** 空关键字 */
  public static final String NULL = "null ";

  /** 类注释开始 */
  public static final String ANNO_CLASS = "/**";

  /** 类注释第二行 */
  public static final String ANNO_CLASS_MID = " *";

  /** 注释结束 */
  public static final String ANNO_OVER = " */";

  /** 版本号 */
  public static final String DOC_VERSION = " * @version 0.0.1";

  /** 作者 */
  public static final String DOC_AUTH = " * @author ";

  /** 类开头 */
  public static final String CLASS_START = "public class ";

  /** 用来生成get和set方法 */
  public static final String BEAN_IMPORT_DATA = "lombok.Data";

  /** 用来生成toString方法 */
  public static final String BEAN_IMPORT_TOSTRING = "lombok.ToString";

  /** 用来注解data */
  public static final String BEAN_USE_DATA = "@Data";

  /** 用来生成toString */
  public static final String BEAN_USE_TOSTRING = "@ToString";

  /** 集合开始 */
  public static final String LIST_TYPE = "List<";

  /** 集合结束 */
  public static final String LIST_TYPE_END = ">";

  /** 数据集合 */
  public static final String LIST_TYPE_ARRAYLIST = "ArrayList<>";

  /** 导入集合的包 */
  public static final String IMPORT_ARRAYLIST = "java.util.ArrayList";

  /** 方法的参数注释 */
  public static final String METHOD_PARAM = " @param ";

  /** 方法的返回注释 */
  public static final String METHOD_RETURN_COMMENT = " @return ";

  /** 属性条件的后缀 */
  public static final String FIELD_SUFFIX_NAME = "List";

  /** 转换为map结构 */
  public static final String MAP_TYPE = "Map";

  /** 使用的hashmap操作 */
  public static final String MAP_TYPE_HASHMAP = "HashMap";

  /** 导入集合的包 */
  public static final String IMPORT_LIST = "java.util.List";

  /** 空集合包 */
  public static final String IMPORT_COLLECTIONS = "java.util.Collections";

  /** 导入map的包 */
  public static final String IMPORT_MAP = "java.util.Map";

  /** 数据库的boolean的类型 */
  public static final String TYPE_BOOLEAN = "boolean";

  /** for关键字 */
  public static final String FOR_KEY = "for";

  /** String类型 */
  public static final String TYPE_STRING = "String";

  /** java中的StringBuilder对象 */
  public static final String TYPE_STRING_BUILDER_NAME = "StringBuilder";

  /** 行注释 */
  public static final String LINE_ANNOTATION = "//";

  /** 逻辑and关系 */
  public static final String LOGIC_AND = "&&";

  /** 逻辑或关系 */
  public static final String LOGIC_OR = "||";

  /** 默认的空集合 */
  public static final String LIST_EMPTY_DEFAULT = "Collections.EMPTY_LIST";
}
