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
package io.streamthoughts.azkarra.api.streams.internal;

import io.streamthoughts.azkarra.api.StreamsLifecycleContext;
import io.streamthoughts.azkarra.api.config.Conf;
import io.streamthoughts.azkarra.api.streams.KafkaStreamsContainer;
import io.streamthoughts.azkarra.api.streams.State;
import io.streamthoughts.azkarra.api.util.Version;
import org.apache.kafka.streams.TopologyDescription;

import java.util.Objects;

/**
 * Internal {@link StreamsLifecycleContext} implementation.
 */
public class InternalStreamsLifecycleContext implements StreamsLifecycleContext {

    private final KafkaStreamsContainer container;

    /**
     * Creates a new {@link InternalStreamsLifecycleContext} instance.
     *
     * @param container the {@link KafkaStreamsContainer} instance
     */
    public InternalStreamsLifecycleContext(final KafkaStreamsContainer container) {
        this.container = Objects.requireNonNull(container, "container cannot be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String topologyName() {
        return container.topologyMetadata().name();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Version topologyVersion() {
        return Version.parse(container.topologyMetadata().version());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String applicationId() {
        return container.applicationId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TopologyDescription topologyDescription() {
        return container.topologyDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Conf streamsConfig() {
        return container.streamsConfig();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State streamsState() {
        return container.state().value();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setState(final State state) {
       container.setState(state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addStateChangeWatcher(final KafkaStreamsContainer.StateChangeWatcher watcher) {
        container.addStateChangeWatcher(watcher);
    }

    /**
     * Return the container that is running the current {@link org.apache.kafka.streams.KafkaStreams} instance.
     * @return  the {@link KafkaStreamsContainer}; cannot be {@code null}.
     */
    public KafkaStreamsContainer container() {
        return container;
    }
}