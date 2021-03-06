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
package net.kuujo.copycat.internal;

import net.kuujo.copycat.CopycatState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Start state.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
class StartState extends AbstractState {
  private static final Logger LOGGER = LoggerFactory.getLogger(StartState.class);

  StartState(CopycatStateContext context) {
    super(context);
  }

  @Override
  public CopycatState state() {
    return CopycatState.START;
  }

  @Override
  protected Logger logger() {
    return LOGGER;
  }

}
