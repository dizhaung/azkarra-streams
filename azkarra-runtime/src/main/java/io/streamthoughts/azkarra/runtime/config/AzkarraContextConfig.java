/*
 * Copyright 2019 StreamThoughts.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.streamthoughts.azkarra.runtime.config;

import io.streamthoughts.azkarra.api.config.Conf;
import io.streamthoughts.azkarra.api.streams.errors.StreamThreadExceptionHandler;
import io.streamthoughts.azkarra.runtime.interceptors.AutoCreateTopicsInterceptorConfig;
import io.streamthoughts.azkarra.runtime.interceptors.WaitForSourceTopicsInterceptorConfig;
import io.streamthoughts.azkarra.runtime.streams.errors.CloseKafkaStreamsOnThreadException;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public class AzkarraContextConfig {

    /**
     * This static field will be removed in a future version.
     * @deprecated use {@link WaitForSourceTopicsInterceptorConfig#WAIT_FOR_TOPICS_ENABLE_CONFIG}
     */
    @Deprecated
    public static String WAIT_FOR_TOPICS_ENABLE_CONFIG    = "enable.wait.for.topics";

    /**
     * This static field will be removed in a future version.
     * @deprecated use {@link AutoCreateTopicsInterceptorConfig#AUTO_CREATE_TOPICS_ENABLE_CONFIG}
     */
    @Deprecated
    public static String AUTO_CREATE_TOPICS_ENABLE_CONFIG = "auto.create.topics.enable";

    /**
     * This static field will be removed in a future version.
     * @deprecated use {@link AutoCreateTopicsInterceptorConfig#AUTO_DELETE_TOPICS_ENABLE_CONFIG}
     */
    @Deprecated
    public static String AUTO_DELETE_TOPICS_ENABLE_CONFIG = "auto.delete.topics.enable";

    /**
     * This static field will be removed in a future version.
     * @deprecated use {@link AutoCreateTopicsInterceptorConfig#AUTO_CREATE_TOPICS_NUM_PARTITIONS_CONFIG}
     */
    @Deprecated
    public static String AUTO_CREATE_TOPICS_NUM_PARTITIONS_CONFIG = "auto.create.topics.num.partitions";

    /**
     * This static field will be removed in a future version.
     * @deprecated use {@link AutoCreateTopicsInterceptorConfig#AUTO_CREATE_TOPICS_REPLICATION_FACTOR_CONFIG}
     */
    @Deprecated
    public static String AUTO_CREATE_TOPICS_REPLICATION_FACTOR_CONFIG = "auto.create.topics.replication.factor";

    /**
     * This static field will be removed in a future version.
     * @deprecated use {@link AutoCreateTopicsInterceptorConfig#AUTO_CREATE_TOPICS_CONFIGS_CONFIG}
     */
    @Deprecated
    public static String AUTO_CREATE_TOPICS_CONFIGS_CONFIG = "auto.create.topics.configs";

    public static String DEFAULT_STREAM_THREAD_EXCEPTION_HANDLER = "default.stream.thread.exception.handler";

    private Conf configs;

    /**
     * Creates a new {@link AzkarraContextConfig} instance.
     *
     * @param configs    the {@link Conf} instance.
     */
    public AzkarraContextConfig(final Conf configs) {
        this.configs = Objects.requireNonNull(configs, "configs cannot be null");
    }

    public boolean isAutoDeleteTopicsEnable() {
        return configs.getOptionalBoolean(AUTO_DELETE_TOPICS_ENABLE_CONFIG).orElse(false);
    }

    public boolean isAutoCreateTopicsEnable() {
        return configs.getOptionalBoolean(AUTO_CREATE_TOPICS_ENABLE_CONFIG).orElse(false);
    }

    public StreamThreadExceptionHandler getDefaultStreamsThreadExceptionHandler() {
        return configs.hasPath(DEFAULT_STREAM_THREAD_EXCEPTION_HANDLER) ?
            configs.getClass(DEFAULT_STREAM_THREAD_EXCEPTION_HANDLER, StreamThreadExceptionHandler.class) :
            new CloseKafkaStreamsOnThreadException();
    }

    public int getAutoCreateTopicsNumPartition() {
        return configs.getOptionalInt(AUTO_CREATE_TOPICS_NUM_PARTITIONS_CONFIG).orElse(1);
    }

    public short getAutoCreateTopicsReplicationFactor() {
        return configs.getOptionalInt(AUTO_CREATE_TOPICS_REPLICATION_FACTOR_CONFIG).orElse(1).shortValue();
    }

    public Map<String, String> getAutoCreateTopicsConfigs() {
        if (!configs.hasPath(AUTO_CREATE_TOPICS_CONFIGS_CONFIG)) {
            return Collections.emptyMap();
        }
        Properties props = configs.getSubConf(AUTO_CREATE_TOPICS_CONFIGS_CONFIG).getConfAsProperties();
        return props.entrySet().stream().collect(
            Collectors.toMap(
                e -> e.getKey().toString(),
                e -> e.getValue().toString()
            )
        );
    }

    public boolean isWaitForTopicsEnable() {
        return configs.getOptionalBoolean(WAIT_FOR_TOPICS_ENABLE_CONFIG).orElse(false);
    }

    public AzkarraContextConfig addConfiguration(final Conf configs) {
        this.configs = this.configs.withFallback(configs);
        return this;
    }

    public Conf configs() {
        return configs;
    }
}
