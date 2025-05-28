package com.leverx.learningmanagementsystem.featureflagservice.service;

import java.io.IOException;

public interface FeatureFlagService {
  boolean isFeatureEnabled(String feature) throws IOException;
}
