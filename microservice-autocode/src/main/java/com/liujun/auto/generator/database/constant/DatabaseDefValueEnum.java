package com.liujun.auto.generator.database.constant;

/**
 * database type
 *
 * @author liujun
 * @version 0.0.1
 * @date 2018/09/13
 */
public enum DatabaseDefValueEnum {

  /** value zone */
  VALUE_ZONE("0", 0),

  /** NULL value */
  VALUE_NULL("NULL", null),

  /** empty value */
  VALUE_EMPTY("EMPTY", "");

  private final String strVal;

  private final Object setVal;

  DatabaseDefValueEnum(String strVal, Object setVal) {
    this.strVal = strVal;
    this.setVal = setVal;
  }

  public String getStrVal() {
    return strVal;
  }

  public Object getSetVal() {
    return setVal;
  }

  /**
   * get proerties the type
   *
   * @param proValue proties defvalue
   * @return type info
   */
  public static Object getDefValue(String proValue) {
    if (proValue != null) {

      for (DatabaseDefValueEnum types : values()) {
        if (types.strVal.equals(proValue)) {
          return types.getSetVal();
        }
      }
    }
    return null;
  }
}
