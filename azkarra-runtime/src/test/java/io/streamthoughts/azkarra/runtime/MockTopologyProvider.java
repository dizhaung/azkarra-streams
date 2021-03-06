/*
 * Copyright 2019 StreamThoughts.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy with the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.streamthoughts.azkarra.runtime;

import io.streamthoughts.azkarra.api.streams.TopologyProvider;
import org.apache.kafka.streams.Topology;

public class MockTopologyProvider implements TopologyProvider {

    protected String version;
    protected Topology topology;

    public MockTopologyProvider(final String version, final Topology topology) {
        this.version = version;
        this.topology = topology;
    }

    @Override
    public String version() {
        return version;
    }

    @Override
    public Topology get() {
        return topology;
    }
}
