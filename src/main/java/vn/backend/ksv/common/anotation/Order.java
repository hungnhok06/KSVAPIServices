package vn.backend.ksv.common.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:42 AM
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {
    int value();
    boolean isParent() default false;
}
