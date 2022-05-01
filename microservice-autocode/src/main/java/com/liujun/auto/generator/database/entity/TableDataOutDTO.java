
package com.liujun.auto.generator.database.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * 数据导出对象信息
 *
 * @author liujun
 * @since 2022/4/30
 */
@Getter
@Setter
@ToString
public class TableDataOutDTO {

  public static final int DEFAULT_SIZE = 10;

  public static final int DEFAULT_PAGE = 1;

  /** 每页导出的条数 */
  private Integer size;

  /** 第几页 */
  private Integer page;

  /** 导出开始的索引号 */
  private Long startIndex;

  /** 结束的索引位置 */
  private Long endIndex;

  /** 导出的表结构信息 */
  private Map.Entry<String, List<TableColumnDTO>> tableColumn;

  public void runMysqlCount() {
    if (null == size) {
      this.size = DEFAULT_SIZE;
    }
    if (null == page) {
      this.page = DEFAULT_PAGE;
    }

    if (null == startIndex) {
      this.startIndex = 1L;
    }

    // 计算开始页
    if (startIndex != 0) {
      startIndex = (page - 1) * (long) size;
    }

    endIndex = page * (long) size;
  }
}
