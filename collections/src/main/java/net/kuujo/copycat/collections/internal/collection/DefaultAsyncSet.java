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
package net.kuujo.copycat.collections.internal.collection;

import net.kuujo.copycat.ResourceContext;
import net.kuujo.copycat.collections.AsyncSet;
import net.kuujo.copycat.collections.AsyncSetProxy;
import net.kuujo.copycat.internal.DefaultStateMachine;

/**
 * Default asynchronous set.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public class DefaultAsyncSet<T> extends AbstractAsyncCollection<AsyncSet<T>, SetState<T>, AsyncSetProxy<T>, T> implements AsyncSet<T> {

  @SuppressWarnings("unchecked")
  public DefaultAsyncSet(ResourceContext context) {
    super(context, new DefaultStateMachine(context, SetState.class, DefaultSetState.class), AsyncSetProxy.class);
  }

}
