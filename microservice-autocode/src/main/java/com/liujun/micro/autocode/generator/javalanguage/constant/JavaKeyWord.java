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
  public static final String INTERFACE = "interface";

  /** private 关键字 */
  public static final String PRIVATE = "private";

  /** 静态 */
  public static final String STATIC = "static";

  /** 无返回值对象定义 */
  public static final String VOID = "void";

  /** 判断 */
  public static final String IF = "if ";

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
  public static final String DOC_AUTH = " * @author liujun";

  /** 类开头 */
  public static final String ClASS_START = "public class ";

  /** 用来生成get和set方法 */
  public static final String BEAN_IMPORT_DATA = "import lombok.Data;";

  /** 用来生成toString方法 */
  public static final String BEAN_IMPORT_TOSTRING = "import lombok.ToString;";

  /** 用来注解data */
  public static final String BEAN_USE_DATA = "@Data";

  /** 用来生成toString */
  public static final String BEAN_USE_TOSTRING = "@ToString";

  /** 集合开始 */
  public static final String LIST_TYPE = "List<";

  /** 集合结束 */
  public static final String LIST_TYPE_END = ">";

  /** 方法的参数注释 */
  public static final String METHOD_PARAM = " @param param ";

  /** 方法的返回注释 */
  public static final String METHOD_RETURN_COMMENT = " @return ";

  /** 用于定义参数的变量名称 */
  public static final String METHOD_PARAM_NAME = "param";

  /** 属性条件的后缀 */
  public static final String FIELD_SUFFIX_NAME = "List";

  /** 导入集合的包 */
  public static final String IMPORT_LIST = "java.util.List";
}
