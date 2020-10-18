package com.liujun.micro.autocode.config.generate.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 相关maven文件的信息
 *
 * @author liujun
 * @version 0.0.1
 */
@Getter
@Setter
@ToString
public class MavenInfo {

    /**
     * maven中的group的id
     */
    private String groupId;

    /**
     * 版本信息
     */
    private String version;
}
