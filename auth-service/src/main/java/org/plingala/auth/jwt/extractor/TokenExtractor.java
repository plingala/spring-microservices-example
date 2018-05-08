package org.plingala.auth.jwt.extractor;

public interface TokenExtractor {
    public String extract(String payload);
}
