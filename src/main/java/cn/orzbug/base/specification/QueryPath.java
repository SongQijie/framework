package cn.orzbug.base.specification;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: SQJ
 * @data: 2018/4/7 15:39
 * @version:
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryPath {
    /**
     * 字段名称
     */
    String filedName() default "";

    /**
     * 取值字段名称
     */
    String filedValueName() default "";

    Operator operator() default Operator.EQ;

    String relevance() default "";

    boolean relevancePk() default true;
}
