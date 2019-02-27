package com.phaseII.placepoint;

import java.util.Map;

public interface AppsFlyerConversionListener {
    void onInstallConversionDataLoaded(Map<String,String> conversionData);
    void onInstallConversionFailure(String errorMessage);
}