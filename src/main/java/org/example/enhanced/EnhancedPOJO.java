package org.example.enhanced;

import org.example.changing.ChangingPOJO;

public class EnhancedPOJO {

    public int getVersion() {
        return new ChangingPOJO().getVersion();
    }

    public boolean isEnhanced() {
        return false;
    }
}