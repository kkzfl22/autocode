package com.liujun.auto.generator.builder.ddd.entity;

import com.liujun.auto.constant.Symbol;

/**
 * 导入的文件包信息
 *
 * @author liujun
 * @version 0.0.1
 */
public class ImportPackageInfo {

  /** java的包路径，以点分隔开 */
  private String packagePath;

  /** 类文件名 */
  private String className;

  /** 类的注释 */
  private String classComment;

  /** 声明的变量的名称 */
  private String varName;

  /** 注解标识 */
  private String annotation;

  /** 泛型标识 */
  private String generic;

  private ImportPackageInfo(Builder builder) {
    this.packagePath = builder.packagePath;
    this.className = builder.className;
    this.classComment = builder.classComment;
    this.varName = builder.varName;
    this.annotation = builder.annotation;
    this.generic = builder.generic;
  }

  /** 创建一个构建器 */
  public static class Builder {

    /** java的包路径，以点分隔开 */
    private String packagePath;

    /** 类文件名 */
    private String className;

    /** 类的注释 */
    private String classComment;

    /** 声明的变量的名称 */
    private String varName;

    /** 注解标识 */
    private String annotation;

    /** 泛型标识 */
    private String generic;

    public ImportPackageInfo.Builder pkgPath(String pkgPath) {
      this.packagePath = pkgPath;
      return this;
    }

    public Builder className(String className) {
      this.className = className;
      return this;
    }

    public Builder classComment(String classComment) {
      this.classComment = classComment;
      return this;
    }

    public Builder varName(String varName) {
      this.varName = varName;
      return this;
    }

    public Builder annotation(String annotation) {
      this.annotation = annotation;
      return this;
    }

    public Builder generic(String generic) {
      this.generic = generic;
      return this;
    }

    /**
     * 构建对象
     *
     * @return
     */
    public ImportPackageInfo build() {
      return new ImportPackageInfo(this);
    }
  }

  /**
   * 创建构建器对象
   *
   * @return
   */
  public static Builder builder() {
    return new Builder();
  }

  public String getPackagePath() {
    return packagePath;
  }

  /**
   * 类名信息
   *
   * @return
   */
  public String getClassName() {
    return className;
  }

  /**
   * 获取类名加泛型信息
   *
   * @return
   */
  public String getClassNameAndGeneric() {
    return className + this.generic;
  }

  public String getClassComment() {
    return classComment;
  }

  public String getVarName() {
    return varName;
  }

  public String getAnnotation() {
    return annotation;
  }

  public String getGeneric() {
    return generic;
  }

  /**
   * 进行package信息的输出操作
   *
   * @return 包路径信息，以点分隔
   */
  public String packageOut() {
    return this.getPackagePath() + Symbol.POINT + this.getClassName();
  }

  /**
   * 执行泛型输出
   *
   * @param className
   * @return
   */
  public String genericOut(String className) {
    StringBuilder outData = new StringBuilder();

    outData.append(this.getClassName());
    outData.append(Symbol.ANGLE_BRACKETS_LEFT);
    outData.append(className);
    outData.append(Symbol.ANGLE_BRACKETS_RIGHT);

    return outData.toString();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ImportPackageInfo{");
    sb.append("packagePath='").append(packagePath).append('\'');
    sb.append(", className='").append(className).append('\'');
    sb.append(", classComment='").append(classComment).append('\'');
    sb.append(", varName='").append(varName).append('\'');
    sb.append(", annotation='").append(annotation).append('\'');
    sb.append(", generic='").append(generic).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
