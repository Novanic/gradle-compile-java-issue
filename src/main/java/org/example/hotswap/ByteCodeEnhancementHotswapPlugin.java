package org.example.hotswap;

import org.hotswap.agent.annotation.*;
import org.hotswap.agent.javassist.CannotCompileException;
import org.hotswap.agent.javassist.CtClass;
import org.hotswap.agent.javassist.NotFoundException;

import java.io.IOException;

@Plugin(name = "ByteCodeEnhancement", testedVersions = {"DCEVM"})
public class ByteCodeEnhancementHotswapPlugin {

    @OnClassLoadEvent(classNameRegexp = ".*", events = LoadEvent.REDEFINE)
    public static void patch(final CtClass ctClass, final ClassLoader classLoader, final Class<?> originalClass) throws IOException, CannotCompileException, NotFoundException {
        System.out.println("patch ByteCodeEnhancementHotswapPlugin");
    }

    @OnClassLoadEvent(classNameRegexp = ".*", events = LoadEvent.REDEFINE)
    public void onClassLoad() {
        System.out.println("onClassLoad");
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