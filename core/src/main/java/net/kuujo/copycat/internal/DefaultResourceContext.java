/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kuujo.copycat.internal;

import net.kuujo.copycat.CopycatState;
import net.kuujo.copycat.ResourceContext;
import net.kuujo.copycat.cluster.coordinator.CoordinatedResourceConfig;
import net.kuujo.copycat.cluster.manager.ClusterManager;
import net.kuujo.copycat.internal.cluster.coordinator.DefaultClusterCoordinator;
import net.kuujo.copycat.internal.util.Assert;
import net.kuujo.copycat.internal.util.concurrent.Futures;
import net.kuujo.copycat.log.LogManager;
import net.kuujo.copycat.protocol.CommitRequest;
import net.kuujo.copycat.protocol.Consistency;
import net.kuujo.copycat.protocol.QueryRequest;
import net.kuujo.copycat.protocol.Response;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

/**
 * Default resource context.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public class DefaultResourceContext implements ResourceContext {
  private final String name;
  private final CoordinatedResourceConfig config;
  private final ClusterManager cluster;
  private final CopycatStateContext context;
  private final DefaultClusterCoordinator coordinator;
  private boolean open;

  public DefaultResourceContext(String name, CoordinatedResourceConfig config, ClusterManager cluster, CopycatStateContext context, DefaultClusterCoordinator coordinator) {
    this.name = Assert.isNotNull(name, "name");
    this.config = Assert.isNotNull(config, "config");
    this.cluster = Assert.isNotNull(cluster, "cluster");
    this.context = Assert.isNotNull(context, "context");
    this.coordinator = Assert.isNotNull(coordinator, "coordinator");
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  @SuppressWarnings("unchecked")
  public CoordinatedResourceConfig config() {
    return config;
  }

  @Override
  public CopycatState state() {
    return context.state();
  }

  @Override
  public ClusterManager cluster() {
    return cluster;
  }

  @Override
  public LogManager log() {
    return context.log();
  }

  @Override
  public void execute(Runnable command) {
    context.executor().execute(command);
  }

  @Override
  public synchronized ResourceContext consumer(BiFunction<Long, ByteBuffer, ByteBuffer> consumer) {
    context.consumer(consumer);
    return this;
  }

  @Override
  public synchronized CompletableFuture<ByteBuffer> query(ByteBuffer entry) {
    return query(entry, Consistency.DEFAULT);
  }

  @Override
  public synchronized CompletableFuture<ByteBuffer> query(ByteBuffer entry, Consistency consistency) {
    if (!open) {
      return Futures.exceptionalFuture(new IllegalStateException("Context not open"));
    }

    CompletableFuture<ByteBuffer> future = new CompletableFuture<>();
    QueryRequest request = QueryRequest.builder()
      .withId(UUID.randomUUID().toString())
      .withUri(context.getLocalMember())
      .withEntry(entry)
      .withConsistency(consistency)
      .build();
    context.query(request).whenComplete((response, error) -> {
      if (error == null) {
        if (response.status() == Response.Status.OK) {
          future.complete(response.result());
        } else {
          future.completeExceptionally(response.error());
        }
      } else {
        future.completeExceptionally(error);
      }
    });
    return future;
  }

  @Override
  public synchronized CompletableFuture<ByteBuffer> commit(ByteBuffer entry) {
    if (!open) {
      return Futures.exceptionalFuture(new IllegalStateException("Context not open"));
    }

    CompletableFuture<ByteBuffer> future = new CompletableFuture<>();
    CommitRequest request = CommitRequest.builder()
      .withId(UUID.randomUUID().toString())
      .withUri(context.getLocalMember())
      .withEntry(entry)
      .build();
    context.commit(request).whenComplete((response, error) -> {
      if (error == null) {
        if (response.status() == Response.Status.OK) {
          future.complete(response.result());
        } else {
          future.completeExceptionally(response.error());
        }
      } else {
        future.completeExceptionally(error);
      }
    });
    return future;
  }

  @Override
  public synchronized CompletableFuture<ResourceContext> open() {
    return coordinator.acquireResource(name)
      .thenRun(() -> {
        open = true;
      }).thenApply(v -> this);
  }

  @Override
  public boolean isOpen() {
    return open;
  }

  @Override
  public synchronized CompletableFuture<Void> close() {
    return coordinator.releaseResource(name)
      .thenRun(() -> {
        open = false;
      });
  }

  @Override
  public boolean isClosed() {
    return !open;
  }

}
