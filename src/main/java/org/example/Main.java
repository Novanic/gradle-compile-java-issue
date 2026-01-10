package org.example;

import org.example.enhanced.EnhancedPOJO;
import org.example.enhanced.POJOEnhancer;

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