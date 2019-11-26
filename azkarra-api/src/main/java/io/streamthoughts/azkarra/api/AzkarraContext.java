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
package io.streamthoughts.azkarra.api;

import io.streamthoughts.azkarra.api.components.ComponentClassReader;
import io.streamthoughts.azkarra.api.components.ComponentFactory;
import io.streamthoughts.azkarra.api.components.ComponentRegistry;
import io.streamthoughts.azkarra.api.config.Conf;
import io.streamthoughts.azkarra.api.errors.AlreadyExistsException;
import io.streamthoughts.azkarra.api.providers.TopologyDescriptor;
import io.streamthoughts.azkarra.api.streams.ApplicationId;
import io.streamthoughts.azkarra.api.streams.TopologyProvider;
import org.apache.kafka.streams.KafkaStreams;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * The AzkarraContext.
 */
public interface AzkarraContext {

    /**
     * Sets the internal the {@link ComponentRegistry} which is used for registering components to this context.
     *
     * @param registry  the {@link ComponentRegistry} instance to be used.
     * @return          this {@link AzkarraContext} instance.
     */
    AzkarraContext setComponentRegistry(final ComponentRegistry registry);

    /**
     * Gets the internal {@link ComponentRegistry}.
     *
     * @return  the {@link ComponentRegistry} instance to be used.
     */
    ComponentRegistry getComponentRegistry();

    /**
     * Gets the internal {@link ComponentClassReader}.
     *
     * @return  the {@link ComponentClassReader} instance to be used.
     */
    ComponentClassReader getComponentClassReader();

    /**
     * Sets the internal the {@link ComponentClassReader} which is used for registering components to this context.
     *
     * @param reader  the {@link ComponentClassReader} instance to be used.
     * @return        this {@link AzkarraContext} instance.
     */
    AzkarraContext setComponentClassReader(final ComponentClassReader reader);

    /**
     * Registers a new listener instance to this context.
     *
     * @param listener  the {@link AzkarraContextListener} instance to register.
     * @return          this {@link AzkarraContext} instance.
     */
    AzkarraContext addListener(final AzkarraContextListener listener);

    /**
     * Sets if the created {@link AzkarraContext} should have a shutdown hook registered.
     *
     * Defaults to {@code true} to ensure that JVM shutdowns are handled gracefully.
     * @param registerShutdownHook if the shutdown hook should be registered
     *
     * @return          this {@link AzkarraContext} instance.
     */
    AzkarraContext setRegisterShutdownHook(final boolean registerShutdownHook);

    /**
     * Returns the global context streamsConfig of this {@link AzkarraContext} instance.
     *
     * @return a {@link Conf} instance.
     */
    Conf getConfiguration();

    /**
     * Sets the default configuration to be used for this {@link AzkarraContext}.
     *
     * @param configuration the {@link Conf} instance.
     *
     * @return          this {@link AzkarraContext} instance.
     */
    AzkarraContext setConfiguration(final Conf configuration);

    /**
     * Adds the specified {@link Conf} to the configuration of this {@link AzkarraContext}.
     *
     * @param configuration the {@link Conf} instance to be used.
     * @return              this {@link AzkarraContext} instance.
     */
    AzkarraContext addConfiguration(final Conf configuration);

    /**
     * Adds the {@link StreamsExecutionEnvironment} to this context.
     *
     * @param environment   the {@link StreamsExecutionEnvironment} instance.
     * @return              this {@link AzkarraContext} instance.
     *
     * @throws AlreadyExistsException   if a {@link StreamsExecutionEnvironment}
     *                                  is already registered for the given name.
     */
    AzkarraContext addExecutionEnvironment(final StreamsExecutionEnvironment environment)
            throws AlreadyExistsException;

    /**
     * Adds a topology to the default environment of this context.
     *
     * @param type          the fully qualified class name or alias of the target {@link TopologyProvider}.
     * @param executed      the {@link Executed} instance.
     *
     * @return              the {@link ApplicationId} instance if the environment is already started,
     *                      otherwise {@code null}.
     */
    ApplicationId addTopology(final String type, final Executed executed);

    /**
     * Adds a topology to the default environment of this context.
     *
     * @param type          the {@link TopologyProvider} class to add.
     * @param executed      the {@link Executed} instance.
     *
     * @return              the {@link ApplicationId} instance if the environment is already started,
     *                      otherwise {@code null}.
     */
    ApplicationId addTopology(final Class<? extends TopologyProvider> type, final Executed executed);

    /**
     * Adds a topology to a specified environment.
     *
     * @param type          the {@link TopologyProvider} class to add.
     * @param environment   the environment name.
     * @param executed      the {@link Executed} instance.
     *
     * @return              the {@link ApplicationId} instance if the environment is already started,
     *                      otherwise {@code null}..
     */
    ApplicationId addTopology(final Class<? extends TopologyProvider> type,
                               final String environment,
                               final Executed executed);

    /**
     * Adds a topology to a specified environment.
     *
     * @param type          the fully qualified class name or alias of the target {@link TopologyProvider}.
     * @param environment   the environment name.
     * @param executed      the {@link Executed} instance.
     *
     * @return              the {@link ApplicationId} instance if the environment is already started,
     *                      otherwise {@code null}.
     */
    ApplicationId addTopology(final String type, final String environment, final Executed executed);

    /**
     * Adds a topology to a specified environment.
     *
     * @param type          the fully qualified class name or alias of the target {@link TopologyProvider}.
     * @param version       the topology version.
     * @param environment   the environment name.
     * @param executed      the {@link Executed} instance.
     *
     * @return              the {@link ApplicationId} instance if the environment is already started,
     *                      otherwise {@code null}.
     */
    ApplicationId addTopology(final String type,
                              final String version,
                              final String environment,
                              final Executed executed);

    /**
     * Gets all topologies registered into this {@link AzkarraContext}.
     * Note, if provider scan is enable then topologies will be scan when this {@link AzkarraContext} will be started.
     *
     * @return a set of {@link TopologyDescriptor}.
     */
    Set<TopologyDescriptor> topologyProviders();

    /**
     * Gets all {@link StreamsExecutionEnvironment} registered to this context.
     *
     * @return  a list of {@link StreamsExecutionEnvironment} instance.
     */
    List<StreamsExecutionEnvironment> environments();

    /**
     * Gets the {@link StreamsExecutionEnvironment} for the specified name or create a new one.
     *
     * @param envName   the environment name.
     * @return          a {@link StreamsExecutionEnvironment} instance with the specified name.
     */
    StreamsExecutionEnvironment getEnvironmentForNameOrCreate(final String envName);

    /**
     * Gets the default {@link StreamsExecutionEnvironment}.
     *
     * @return a {@link StreamsExecutionEnvironment} instance.
     */
    StreamsExecutionEnvironment defaultExecutionEnvironment();

    /**
     * Registers a components into this context.
     *
     * @param cls the component class.
     * @return    this {@link AzkarraContext} instance.
     */
    <T> AzkarraContext addComponent(final Class<T> cls);

    /**
     * Registers a components into this context.
     *
     * @param factory   the {@link ComponentFactory} instance.
     * @return          this {@link AzkarraContext} instance.
     */
    <T> AzkarraContext addComponent(final ComponentFactory<T> factory);

    /**
     * Registers a components into this context.
     *
     * @param className      the component class name.
     * @return               this {@link AzkarraContext} instance.
     */
    AzkarraContext addComponent(final String className);

    /**
     * Gets a component instance for the specified class.
     *
     * @param cls   the component class.
     * @param <T>   the component type.
     * @return      the component instance of type {@link T}.
     */
    <T> T getComponentForType(final Class<T> cls);

    /**
     * Get all component instances for the specified class.
     *
     * @param cls   the component class.
     * @param <T>   the component type.
     * @return      the component instance of type {@link T}.
     */
    <T> Collection<T> getAllComponentForType(final Class<T> cls);

    /**
     * Gets the topology for the specified class name or alias.
     *
     * @param type  the topology type.
     * @return      the {@link TopologyDescriptor}.
     */
    TopologyDescriptor getTopology(final String type);

    /**
     * Starts this {@link AzkarraContext} instance.
     */
    void start();

    /**
     * Stops this {@link AzkarraContext} instance.
     *
     * @param cleanUp if local states of each {@link KafkaStreams} instance must be cleanup.
     * @see KafkaStreams#cleanUp() .
     */
    void stop(boolean cleanUp);

    /**
     * Stops this {@link AzkarraContext} instance.
     */
    void stop();
}