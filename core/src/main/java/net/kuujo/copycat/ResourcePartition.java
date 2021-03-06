/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kuujo.copycat;

import net.kuujo.copycat.cluster.Cluster;

/**
 * Copycat resource.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public interface ResourcePartition<T extends ResourcePartition<T>> extends Managed<T> {

  /**
   * Returns the resource name.
   *
   * @return The resource name.
   */
  String name();

  /**
   * Returns the resource partition number.
   *
   * @return The resource partition number.
   */
  int partition();

  /**
   * Returns the resource cluster.
   *
   * @return The resource cluster.
   */
  Cluster cluster();

  /**
   * Returns the current resource state.
   *
   * @return The current resource state.
   */
  CopycatState state();

}
