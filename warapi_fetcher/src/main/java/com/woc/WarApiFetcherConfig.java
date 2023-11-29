package com.woc;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "warapi_fetcher")
public interface WarApiFetcherConfig {
/* 
    @WithName("shards")
    String message();
*/
}