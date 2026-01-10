package org.example.enhanced;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.pool.TypePool;

import java.lang.reflect.Modifier;
import java.util.function.Function;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.returns;

public class POJOEnhancer {

    public static void enhanceAndLoad(String className) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        enhanceInternal(className, t -> {
            t.load(classLoader, ClassLoadingStrategy.Default.INJECTION);
            return new byte[0];
        });
    }

    public static byte[] enhance(String className) {
        return enhanceInternal(className, DynamicType::getBytes);
    }

    private static byte[] enhanceInternal(String className, Function<DynamicType.Unloaded<Object>, byte[]> action) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        ClassFileLocator locator = ClassFileLocator.ForClassLoader.of(classLoader);

        TypePool typePool = TypePool.Default.of(locator);

        TypeDescription type = typePool.describe(className).resolve();

        try(DynamicType.Unloaded<Object> enhancedType = new ByteBuddy()
                .redefine(type, locator)
                .method(named("isEnhanced").and(returns(boolean.class)))
                .intercept(FixedValue.value(true))
                .defineMethod("addedMethod", boolean.class, Modifier.PUBLIC)
                .intercept(FixedValue.value(true))
                .make()) {
            return action.apply(enhancedType);
        }
    }
}
