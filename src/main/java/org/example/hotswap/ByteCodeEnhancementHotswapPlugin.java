package org.example.hotswap;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.pool.TypePool;
import org.hotswap.agent.annotation.*;

import java.lang.reflect.Modifier;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.returns;

@Plugin(name = "ByteCodeEnhancement", testedVersions = {"1.0"})
public class ByteCodeEnhancementHotswapPlugin {

    @OnClassLoadEvent(classNameRegexp = ".*", events = LoadEvent.REDEFINE)
    public static byte[] onClassLoad(final Class<?> originalClass, byte[] bytes) {
        System.out.println("ByteCodeEnhancementHotswapPlugin - onClassLoad: " + originalClass.getName());

        if("org.example.enhanced.EnhancedPOJO".equals(originalClass.getName())) {
            return enhance();
        }
        return bytes;
    }

    //TODO reuse code of Main.enhance()
    private static byte[] enhance() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        ClassFileLocator locator =
                ClassFileLocator.ForClassLoader.of(classLoader);

        TypePool typePool =
                TypePool.Default.of(locator);

        TypeDescription type =
                typePool.describe( "org.example.enhanced.EnhancedPOJO").resolve();

        try(DynamicType.Unloaded<Object> enhancedType = new ByteBuddy()
                .redefine(type, locator)
                .method(named("isEnhanced").and(returns(boolean.class)))
                .intercept(FixedValue.value(true))
//                // optional: when this is activated, IntelliJ throws an error at 'Apply HotSwap'
//                // because the JRE (jbr / dcevm) doesn't support reloading classes when number of methods gets changed.
                .defineMethod("addedMethod", boolean.class, Modifier.PUBLIC)
                .intercept(FixedValue.value(true))
                .make()) {
            return enhancedType.getBytes();
        }
    }
}