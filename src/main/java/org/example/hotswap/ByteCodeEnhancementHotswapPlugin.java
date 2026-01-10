package org.example.hotswap;

import org.hotswap.agent.annotation.*;
import org.hotswap.agent.javassist.CannotCompileException;
import org.hotswap.agent.javassist.CtClass;
import org.hotswap.agent.javassist.NotFoundException;

import java.io.IOException;

@Plugin(name = "ByteCodeEnhancement", testedVersions = {"1.0"})
public class ByteCodeEnhancementHotswapPlugin {

    @OnClassLoadEvent(classNameRegexp = ".*", events = LoadEvent.REDEFINE)
    public static void patch(final CtClass ctClass, final ClassLoader classLoader, final Class<?> originalClass) throws IOException, CannotCompileException, NotFoundException {
        System.out.println("ByteCodeEnhancementHotswapPlugin: patch class " + originalClass.getName());

        if("org.example.enhanced.EnhancedPOJO".equals(originalClass.getName())) {
            System.out.println("original methods: " + originalClass.getMethods().length);
            try {
                Class<?> alreadyEnhancedClass = classLoader.loadClass("org.example.enhanced.EnhancedPOJO");
                System.out.println("org.example.enhanced.EnhancedPOJO enhanced found? " + (alreadyEnhancedClass != null));

                System.out.println("alreadyEnhancedClass methods: " + alreadyEnhancedClass.getMethods().length);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @OnClassFileEvent(classNameRegexp = ".*")
    public void onClassFile() {
        System.out.println("onClassFile");
    }

    @OnResourceFileEvent(path = "/")
    public void onResourceChanged() {
        System.out.println("onResourceChanged");
    }
}