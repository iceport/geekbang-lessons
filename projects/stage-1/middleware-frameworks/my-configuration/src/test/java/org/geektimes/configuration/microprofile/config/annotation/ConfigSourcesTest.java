package org.geektimes.configuration.microprofile.config.annotation;


import org.junit.Before;
import org.junit.Test;

import java.net.URL;

@ConfigSources(value = {@ConfigSource(ordinal = 200, resource = "classpath:/META-INF/default.properties")
        , @ConfigSource(ordinal = 300, resource = "ttl:/META-INF/default.properties")
})
public class ConfigSourcesTest {

    @Before
    public void initConfigSourceFactory() throws Exception {
        ConfigSources configSources = ConfigSourcesTest.class.getAnnotation(ConfigSources.class);
        ConfigSource[] sources = configSources.value();
        for (ConfigSource item : sources) {
            int ordinal = item.ordinal();
            String resource = item.resource();
            URL resourceURL = new URL(resource);

            String encoding = item.encoding();
            String name = item.name();
            Class<? extends ConfigSourceFactory> factory = item.factory();
            if (ConfigSourceFactory.class.equals(factory)) {
                factory = DefaultConfigSourceFactory.class;

            }
            ConfigSourceFactory configSourceFactory = factory.newInstance();
            org.eclipse.microprofile.config.spi.ConfigSource target = configSourceFactory.createConfigSource(name, ordinal, resourceURL, encoding);

            System.out.println(target.getProperties());
        }
    }


    @Test
    public void test() {

    }

}
