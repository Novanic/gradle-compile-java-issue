package org.example.hotswap;

import org.example.enhanced.POJOEnhancer;
import org.hotswap.agent.annotation.*;

@Plugin(name = "ByteCodeEnhancement", testedVersions = {"1.0"})
public class ByteCodeEnhancementHotswapPlugin {

    @OnClassLoadEvent(classNameRegexp = ".*", events = LoadEvent.REDEFINE)
    public static byte[] onClassLoad(final Class<?> originalClass, byte[] bytes) {
        final String className = originalClass.getName();
        System.out.println("ByteCodeEnhancementHotswapPlugin - onClassLoad: " + className);

        if("org.example.enhanced.EnhancedPOJO".equals(className)) {
            return POJOEnhancer.enhance(className);
        }
        return bytes;
    }
}