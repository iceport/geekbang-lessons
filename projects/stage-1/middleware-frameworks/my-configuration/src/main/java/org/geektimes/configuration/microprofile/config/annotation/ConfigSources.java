package org.geektimes.configuration.microprofile.config.annotation;


import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigSources {

    ConfigSource[] value();
}
