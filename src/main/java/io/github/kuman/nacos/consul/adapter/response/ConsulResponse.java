package io.github.kuman.nacos.consul.adapter.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * consul service 结果封装类
 *
 * @author kuman
 * @since 1.0, 2023/04/08 23:37
 */
@Getter
@AllArgsConstructor
public class ConsulResponse<T> {
    private T data;
    private long index;
}