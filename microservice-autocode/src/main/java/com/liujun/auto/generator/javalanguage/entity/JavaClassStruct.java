package com.liujun.auto.generator.javalanguage.entity;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.javalanguage.constant.ClassKeyWordEnum;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.constant.PrefixSpaceEnum;
import com.liujun.auto.generator.javalanguage.constant.VisitEnum;
import com.liujun.auto.generator.javalanguage.utils.JavaCodeOutUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 类文件的结构信息
 *
 * @author liujun
 * @since 2022/4/6
 */
@Setter
@Getter
@ToString
public class JavaClassStruct implements OutCodeInf {

  /** 文件的路径 */
  private String filePath;

  /** 文件最开始定义的版本信息 */
  private String copyRight;

  /** 定义的包路径信息 */
  private String pkgPath;

  /** 引用的导入 */
  private List<JavaClassImportClass> referenceImport;

  /** 导入与注释之间存在的空行数 */
  private int spaceLine;

  /** 类注释信息 */
  private JavaClassDocument classDocument;

  /** 类的注解信息 */
  private List<ContextAnnotation> classAnnotation;

  /** 类的访问修饰符 */
  private VisitEnum classVisit;

  /** 类的关键字标识符 */
  private ClassKeyWordEnum classKeyWord;

  /** 类名 */
  private String className;

  /** 所有的类的内容中的信息（包括属性、方法、静态代码块等） */
  private List<ContentBase> content;

  @Override
  public String outCode() {
    StringBuilder classInfo = new StringBuilder();

    // 输出版本信息
    classInfo.append(outCopyRight(PrefixSpaceEnum.NONE, copyRight));
    // 输出包定义信息
    classInfo.append(JavaKeyWord.PACKAGE).append(Symbol.SPACE).append(pkgPath);
    classInfo.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    // 输出导包信息
    classInfo.append(importPkg(this.getReferenceImport()));
    // 空行输出
    classInfo.append(JavaCodeOutUtils.emptyLine(spaceLine));
    // 注释输出
    classInfo.append(classDocument.outDocument());
    // 注解输出
    classInfo.append(JavaCodeOutUtils.outAnnotation(classAnnotation));
    // 类定义输出
    classInfo.append(classVisit.getVisit()).append(Symbol.SPACE).append(classKeyWord.getKeyWord());
    classInfo.append(Symbol.SPACE).append(className).append(Symbol.SPACE).append(Symbol.BRACE_LEFT);
    classInfo.append(Symbol.ENTER_LINE);

    // 执行代码的属性与方法的输出操作
    for (ContentBase contentItem : content) {
      classInfo.append(contentItem.outCode());
    }

    // 类结束
    classInfo.append(Symbol.BRACE_RIGHT).append(Symbol.ENTER_LINE);

    return classInfo.toString();
  }

  /**
   * 导包信息的输出
   *
   * @param referenceImportList
   * @return
   */
  private String importPkg(List<JavaClassImportClass> referenceImportList) {
    if (null == referenceImportList || referenceImportList.isEmpty()) {
      return Symbol.EMPTY;
    }

    StringBuilder outImport = new StringBuilder();

    for (JavaClassImportClass importPkg : referenceImportList) {
      outImport.append(JavaKeyWord.IMPORT).append(Symbol.SPACE);
      if (importPkg.isStaticImport()) {
        outImport.append(JavaKeyWord.STATIC).append(Symbol.SPACE);
      }
      outImport.append(importPkg.getReferencePath());
      outImport.append(Symbol.SEMICOLON).append(Symbol.ENTER_LINE);
    }

    return outImport.toString();
  }

  /**
   * 执行版权注释的输出操作
   *
   * @param data
   * @return
   */
  public static String outCopyRight(PrefixSpaceEnum leftSpace, String data) {
    StringBuilder comment = new StringBuilder();

    comment.append(JavaCodeOutUtils.outSpace(leftSpace));
    comment.append(Symbol.PATH).append(Symbol.STAR).append(Symbol.STAR).append(Symbol.ENTER_LINE);
    comment.append(JavaCodeOutUtils.outSpace(leftSpace));
    comment.append(Symbol.SPACE).append(Symbol.STAR).append(Symbol.SPACE);
    comment.append(data).append(Symbol.ENTER_LINE);
    comment.append(JavaCodeOutUtils.outSpace(leftSpace));
    comment.append(Symbol.SPACE).append(Symbol.STAR).append(Symbol.PATH).append(Symbol.ENTER_LINE);

    return comment.toString();
  }
}
