package com.ifnoif.androidtestdemo.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by shen on 17/9/29.
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface ClassAnotation1 {
    String testValue();
}
