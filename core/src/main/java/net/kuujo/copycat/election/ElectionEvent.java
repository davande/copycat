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
package net.kuujo.copycat.election;

import net.kuujo.copycat.Event;
import net.kuujo.copycat.cluster.Member;

/**
 * Election event.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public class ElectionEvent implements ElectionResult, Event<ElectionEvent.Type> {

  /**
   * Election event type.
   */
  public static enum Type {
    COMPLETE
  }

  private final Type type;
  private final long term;
  private final Member winner;

  public ElectionEvent(Type type, long term, Member winner) {
    this.type = type;
    this.term = term;
    this.winner = winner;
  }

  @Override
  public Type type() {
    return type;
  }

  @Override
  public long term() {
    return term;
  }

  @Override
  public Member winner() {
    return winner;
  }

}
