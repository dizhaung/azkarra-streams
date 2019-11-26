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
package io.streamthoughts.azkarra.api.query;

import io.streamthoughts.azkarra.api.monad.CheckedSupplier;
import io.streamthoughts.azkarra.api.monad.Retry;
import io.streamthoughts.azkarra.api.monad.Try;
import org.apache.kafka.streams.errors.InvalidStateStoreException;

import java.util.function.Supplier;

/**
 * Default class to wrap access to local state store.
 *
 * @param <T>   the read-only store type.
 */
public class LocalStoreAccessor<T> {

    private final StoreSupplier<T> supplier;

    private volatile Try<T> store;

    /**
     * Creates a new {@link LocalStoreAccessor} instance.
     *
     * @param supplier the read-only store {@link Supplier} instance.
     */
    public LocalStoreAccessor(final StoreSupplier<T> supplier) {
        this.supplier = supplier;
    }

    public Try<T> get(final Queried options) {
        if (store != null && store.isSuccess()) {
            return store;
        }
        store = Try.retriable(supplier, Retry
             .withMaxAttempts(options.retries())
             .withFixedWaitDuration(options.retryBackoff())
             .stopAfterDuration(options.queryTimeout())
             .ifExceptionOfType(InvalidStateStoreException.class));

        return store;
    }

    @FunctionalInterface
    public interface StoreSupplier<T> extends CheckedSupplier<T, InvalidStateStoreException> {
        T get() throws InvalidStateStoreException;
    }
}