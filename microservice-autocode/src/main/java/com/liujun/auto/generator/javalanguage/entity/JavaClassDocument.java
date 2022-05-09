package com.liujun.auto.generator.javalanguage.entity;

import com.liujun.auto.constant.Symbol;
import com.liujun.auto.generator.builder.ddd.entity.ImportPackageInfo;
import com.liujun.auto.generator.builder.utils.DateTimeUtils;
import com.liujun.auto.generator.javalanguage.constant.JavaKeyWord;
import com.liujun.auto.generator.javalanguage.constant.PrefixSpaceEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 类文件的结构信息
 *
 * @author liujun
 * @since 2022/4/6
 */
@Getter
@Setter
@ToString
public class JavaClassDocument {

  /** 类注释信息 */
  private String comment;

  /** 作者信息 */
  private String author;

  /** 类创建的时间 */
  private String since;

  /**
   * 构建注释
   *
   * @return
   */
  public static JavaClassDocument buildDoc(ImportPackageInfo pkgInfo, String author) {

    JavaClassDocument doc = new JavaClassDocument();

    doc.setComment(pkgInfo.getClassComment());
    doc.setAuthor(author);
    doc.setSince(DateTimeUtils.localDataTimeOutYMD(DateTimeUtils.getCurrDateTime()));

    return doc;
  }

  /**
   * 执行注释的输出操作
   *
   * @return
   */
  public String outDocument() {
    StringBuilder commentOut = new StringBuilder();

    commentOut.append(Symbol.PATH).append(Symbol.STAR).append(Symbol.STAR);
    commentOut.append(Symbol.ENTER_LINE);
    commentOut.append(Symbol.SPACE).append(Symbol.STAR).append(Symbol.SPACE).append(comment);
    commentOut.append(Symbol.ENTER_LINE);
    commentOut.append(Symbol.SPACE).append(Symbol.STAR).append(Symbol.ENTER_LINE);
    commentOut.append(Symbol.SPACE).append(Symbol.STAR).append(Symbol.SPACE);
    commentOut.append(JavaKeyWord.DOC_AUTH).append(Symbol.SPACE).append(author);
    commentOut.append(Symbol.ENTER_LINE);
    commentOut.append(Symbol.SPACE).append(Symbol.STAR).append(Symbol.SPACE);
    commentOut.append(JavaKeyWord.DOC_SINCE);
    commentOut.append(Symbol.SPACE).append(since).append(Symbol.ENTER_LINE);
    commentOut.append(Symbol.SPACE).append(Symbol.STAR).append(Symbol.PATH);
    commentOut.append(Symbol.ENTER_LINE);

    return commentOut.toString();
  }
}
