/*
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

package org.apache.kylin.metadata.datatype;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 */
public class BigDecimalSerializerTest {

    private static BigDecimalSerializer bigDecimalSerializer;

    @BeforeClass
    public static void beforeClass() {
        bigDecimalSerializer = new BigDecimalSerializer(DataType.getType("decimal"));
    }

    @Test
    public void testNormal() {
        BigDecimal input = new BigDecimal("1234.1234");
        ByteBuffer buffer = ByteBuffer.allocate(256);
        buffer.mark();
        bigDecimalSerializer.serialize(input, buffer);
        buffer.reset();
        BigDecimal output = bigDecimalSerializer.deserialize(buffer);
        assertEquals(input, output);
    }

    @Test
    public void testScaleOutOfRange() {
        BigDecimal input = new BigDecimal("1234.1234567890");
        ByteBuffer buffer = ByteBuffer.allocate(256);
        buffer.mark();
        bigDecimalSerializer.serialize(input, buffer);
        buffer.reset();
        BigDecimal output = bigDecimalSerializer.deserialize(buffer);
        assertEquals(input.setScale(bigDecimalSerializer.type.getScale(), BigDecimal.ROUND_HALF_EVEN), output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOutOfPrecision() {
        BigDecimal input = new BigDecimal("66855344214907231736.4924");
        ByteBuffer buffer = ByteBuffer.allocate(256);
        bigDecimalSerializer.serialize(input, buffer);
    }

}
