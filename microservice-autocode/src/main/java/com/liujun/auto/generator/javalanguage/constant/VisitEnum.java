
package com.liujun.auto.generator.javalanguage.constant;

/**
 * java的访问就访问修饰符
 *
 * @author liujun
 * @since 2022/4/6
 */
public enum VisitEnum {


    /**
     * 公共的
     */
    PUBLIC("public"),

    /**
     * 默认的访问修饰符
     */
    DEFAULT(""),

    /**
     * 私有
     */
    PRIVATE("private"),
    ;

    /**
     * 修饰符信息
     */
    private String visit;

    VisitEnum(String visit) {
        this.visit = visit;
    }

    public String getVisit() {
        return visit;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JavaVisitFlag{");
        sb.append("visit='").append(visit).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
