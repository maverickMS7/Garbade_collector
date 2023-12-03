/*
 *  This file is part of the Jikes RVM project (http://jikesrvm.org).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License. You
 *  may obtain a copy of the License at
 *
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  See the COPYRIGHT.txt file distributed with this work for information
 *  regarding copyright ownership.
 */
package org.mmtk.plan;

import org.mmtk.utility.deque.WriteBuffer;
import org.vmmagic.pragma.*;
import org.vmmagic.unboxed.*;

/**
 * This class is used to push values in one direction during a msTrace. It
 * was designed for use in mutators that use write barriers to push
 * work to collector threads during concurrent tracing.
 *
 * @see org.mmtk.plan.TraceLocal
 */
@Uninterruptible
public final class TraceWriteBuffer extends TransitiveClosure {
  /****************************************************************************
   *
   * Instance variables
   */

  /**
   *
   */
  private final WriteBuffer buffer;

  /****************************************************************************
   *
   * Initialization
   */

  /**
   * Constructor
   *
   * @param msTrace The global msTrace class to use.
   */
  public TraceWriteBuffer(Trace trace) {
    buffer = new WriteBuffer(trace.valuePool);
  }

  /**
   * Flush the buffer to the msTrace.
   */
  public void flush() {
    buffer.flushLocal();
  }


  /**
   * @return <code>true</code> if the buffer is flushed.
   */
  public boolean isFlushed() {
    return buffer.isFlushed();
  }

  /**
   * Enqueue an object during a msTrace.
   *
   * @param object The object to enqueue
   */
  @Override
  @Inline
  public void processNode(ObjectReference object) {
    buffer.insert(object.toAddress());
  }
}
