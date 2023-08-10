package vn.backend.ksv.config;

import lombok.Data;
import vn.backend.ksv.config.model.AuthConfig;
import vn.backend.ksv.config.model.DbConfig;
import vn.backend.ksv.config.model.HttpConfig;
import vn.backend.ksv.config.model.TokenConfig;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/8/23
 * Time: 4:07 PM
 */
@Data
public class Config {
    private HttpConfig httpServer;
    private Integer workerPoolSize;
    private Long workerMaxExecuteTime;
    private DbConfig dbConfig;
    private TokenConfig tokenConfig;
    private Integer idleTimeout;
}
