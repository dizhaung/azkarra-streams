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
package io.streamthoughts.azkarra.api.streams.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class LogOffsetsFetcher {

    private static Logger LOG = LoggerFactory.getLogger(LogOffsetsFetcher.class);

    public static <K, V> Map<TopicPartition, Long> fetchLogEndOffsetsFor(final Consumer<K, V> consumer,
                                                                         final Collection<TopicPartition> partitions) {

        try {
            return consumer.endOffsets(partitions);
        } catch (final KafkaException e) {
            LOG.warn("Unexpected error while fetching end offsets for topic/partitions", e);
            return Collections.emptyMap();
        }
    }

    public static <K, V> Map<TopicPartition, Long> fetchLogStartOffsetsFor(final Consumer<K, V> consumer,
                                                                           final Collection<TopicPartition> partitions) {
        try {
            return consumer.beginningOffsets(partitions);
        } catch (final KafkaException e) {
            LOG.warn("Unexpected error while fetching start offsets for topic/partitions", e);
            return Collections.emptyMap();
        }
    }
}