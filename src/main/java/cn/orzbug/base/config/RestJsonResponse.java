package cn.orzbug.base.config;


import cn.orzbug.base.utils.Action;

import java.lang.annotation.*;

/**
 * @author: SQJ
 * @data: 2018/6/11 15:42
 * @version:
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestJsonResponse {
    String value() default "";
    Action auth() default Action.CHECKTOKENSKIP;
}
