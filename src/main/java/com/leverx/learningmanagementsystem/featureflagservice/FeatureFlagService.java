package com.leverx.learningmanagementsystem.featureflagservice;

import java.io.IOException;

public interface FeatureFlagService {
  boolean isFeatureEnabled(String feature) throws IOException;
}
