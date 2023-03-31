package io.github.kuman.nacos.consul.adapter.common;

/**
 * 常量定义
 *
 * @author manco
 * @since 1.0, 2023/03/25 20:20
 */
public final class ConsulAdapterConstant {

    private ConsulAdapterConstant() {}
    /** 运行模式 */
    public static final String NACOS_MODE = "nacos.consul.adapter.mode";
    public static final String LONG_POLLING_MODE = "polling";
    public static final String DIRECT_MODE = "direct";

    public static final String DATA_CENTER = "acct";

    /** 默认缓存管理器 */
    public static final String CACHE_MANAGER_DEFAULT = "nacosCacheManagerDefault";
}
