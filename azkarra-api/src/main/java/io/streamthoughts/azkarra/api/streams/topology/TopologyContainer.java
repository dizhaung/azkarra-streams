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
package io.streamthoughts.azkarra.api.streams.topology;

import io.streamthoughts.azkarra.api.config.Conf;
import io.streamthoughts.azkarra.api.monad.Tuple;
import io.streamthoughts.azkarra.api.streams.ApplicationId;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;


/**
 * Default class to encapsulate a {@link Topology} instance.
 */
public class TopologyContainer {

    private final Topology topology;

    private final TopologyMetadata metadata;

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private Topology topology;
        private String version;
        private String name;
        private String description;
        private Conf conf;

        public Builder withConf(final Conf conf) {
            this.conf = conf;
            return this;
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withDescription(final String description) {
            this.description = description;
            return this;
        }

        public Builder withVersion(final String version) {
            this.version = version;
            return this;
        }

        public Builder withTopology(final Topology topology) {
            this.topology = topology;
            return this;
        }

        public TopologyContainer build() {
            return new TopologyContainer(
                topology,
                new TopologyMetadata(
                    name,
                    version,
                    description,
                    topology.describe(),
                    conf != null ? conf : Conf.empty())
            );
        }
    }

    /**
     * Creates a new {@link TopologyContainer} instance.
     *
     * @param topology  the {@link Topology} instance.
     * @param metadata  the {@link TopologyMetadata} instance.
     */
    private TopologyContainer(final Topology topology,
                              final TopologyMetadata metadata) {
        this.topology = topology;
        this.metadata = metadata;
    }

    public Topology getTopology() {
        return topology;
    }

    public TopologyMetadata getMetadata() {
        return metadata;
    }

    /**
     * Builds a new {@link KafkaStreams} for this {@link Topology}.
     *
     * @param applicationId           the application id to be used.
     * @param defaultStreamsConfig    the default streamsConfig to be used.
     *
     * @return                        a new {@link Tuple} instance of a streams {@link Conf}
     *                                instance and a {@link KafkaStreams} instance.
     */
    public Tuple<Conf, KafkaStreams> buildKafkaStreams(final ApplicationId applicationId,
                                                       final Conf defaultStreamsConfig) {

        Conf streamsConfig = Conf
                .with(StreamsConfig.APPLICATION_ID_CONFIG, applicationId.toString())
                .withFallback(metadata.streamsConfig())
                .withFallback(defaultStreamsConfig);

        return new Tuple<>(streamsConfig, new KafkaStreams(topology, streamsConfig.getConfAsProperties()));
    }
}