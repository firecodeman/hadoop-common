/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.ipc;

import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import javax.net.SocketFactory;

import junit.framework.Assert;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.net.StandardSocketFactory;
import org.junit.Test;

public class TestSocketFactory {

  @Test
  public void testSocketFactoryAsKeyInMap() throws Exception {
    Map<SocketFactory, Integer> dummyCache = new HashMap<SocketFactory, Integer>();
    int toBeCached1 = 1;
    int toBeCached2 = 2;
    Configuration conf = new Configuration();
    conf.set("hadoop.rpc.socket.factory.class.default",
        "org.apache.hadoop.ipc.TestSocketFactory$DummySocketFactory");
    final SocketFactory dummySocketFactory = NetUtils
        .getDefaultSocketFactory(conf);
    dummyCache.put(dummySocketFactory, toBeCached1);

    conf.set("hadoop.rpc.socket.factory.class.default",
        "org.apache.hadoop.net.StandardSocketFactory");
    final SocketFactory defaultSocketFactory = NetUtils
        .getDefaultSocketFactory(conf);
    dummyCache.put(defaultSocketFactory, toBeCached2);

    Assert
        .assertEquals("The cache contains two elements", 2, dummyCache.size());
    Assert.assertEquals("Equals of both socket factory shouldn't be same",
        defaultSocketFactory.equals(dummySocketFactory), false);

    assertSame(toBeCached2, dummyCache.remove(defaultSocketFactory));
    dummyCache.put(defaultSocketFactory, toBeCached2);
    assertSame(toBeCached1, dummyCache.remove(dummySocketFactory));

  }

  /**
   * A dummy socket factory class that extends the StandardSocketFactory. 
   */
  static class DummySocketFactory extends StandardSocketFactory {
    
  }
}
