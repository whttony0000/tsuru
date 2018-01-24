package com.aikon.fin.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author haitao.wang
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,ElementType.TYPE})
public @interface EnDeCodePar {

    Class<? extends Encoder> encoder() default UesEncoder.class;

    Class<? extends Decoder> decoder() default UesDecoder.class;


}


