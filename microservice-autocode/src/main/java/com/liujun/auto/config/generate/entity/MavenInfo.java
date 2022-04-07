package com.liujun.auto.config.generate.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 相关maven文件的信息
 *
 * @author liujun
 * @version 0.0.1
 */
public class MavenInfo {

  /** maven中的group的id */
  private String groupId;

  /** 版本信息 */
  private String version;

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("MavenInfo{");
    sb.append("groupId='").append(groupId).append('\'');
    sb.append(", version='").append(version).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
