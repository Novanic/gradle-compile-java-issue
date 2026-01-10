package org.example;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.pool.TypePool;
import org.example.enhanced.EnhancedPOJO;
import org.example.changing.ChangingPOJO;

import java.lang.reflect.Modifier;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.returns;

public class Main {

    public static void main(String[] args) throws Exception {

        enhance();

        do {
            EnhancedPOJO enhancedPojo = new EnhancedPOJO();

            System.out.println("EnhancedPOJO version: " + enhancedPojo.getVersion());
            System.out.println("EnhancedPOJO enhanced: " + enhancedPojo.isEnhanced());
            System.out.println("EnhancedPOJO method number: " + EnhancedPOJO.class.getMethods().length);

            Thread.sleep(2000L);
        } while(true);
    }

    private static void enhance() {
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
            enhancedType.load(classLoader, ClassLoadingStrategy.Default.INJECTION);
        }
    }
}