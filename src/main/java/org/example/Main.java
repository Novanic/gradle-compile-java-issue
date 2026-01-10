package org.example;

import org.example.enhanced.EnhancedPOJO;
import org.example.enhanced.POJOEnhancer;

/**
 * Moeglichkeiten zur Integration:
 * 1.) hotswap-agent-core.jar in JBR ablegen (.../jbr/lib/hotswap/hotswap-agent-core.jar) und die VM-Optionen setzen: -XX:+AllowEnhancedClassRedefinition -XX:HotswapAgent=core
 * 2.) oder diese VM-Optionen setzen und den Pfad zu hotswap-agent-core.jar als javaagent angeben: -XX:+AllowEnhancedClassRedefinition -XX:HotswapAgent=core -javaagent:C:\Users\novan\.jdks\jbr-21.0.9\lib\hotswapx\hotswap-agent-core.jar
 */
public class Main {

    public static void main(String[] args) throws Exception {

        POJOEnhancer.enhanceAndLoad("org.example.enhanced.EnhancedPOJO");

        do {
            EnhancedPOJO enhancedPojo = new EnhancedPOJO();

            System.out.println("EnhancedPOJO version: " + enhancedPojo.getVersion());
            System.out.println("EnhancedPOJO enhanced: " + enhancedPojo.isEnhanced());
            System.out.println("EnhancedPOJO method number: " + EnhancedPOJO.class.getMethods().length);

            Thread.sleep(2000L);
        } while(true);
    }
}