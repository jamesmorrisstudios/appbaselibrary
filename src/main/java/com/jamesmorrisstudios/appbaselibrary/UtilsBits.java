/*
 * Copyright (c) 2015.   James Morris Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jamesmorrisstudios.appbaselibrary;

import android.support.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Bit and byte management functions used by the binary save feature
 * <p/>
 * Created by James on 3/10/2015.
 */
public final class UtilsBits {

    /**
     * Copies the given byte int the byte array
     *
     * @param index Index to copy to
     * @param src   Byte to insert
     * @param dest  Byte array to copy into
     */
    public static void copyIntoByteArr(final int index, final byte src, @NonNull final byte[] dest) {
        dest[index] = src;
    }

    /**
     * Retrieves the given byte from the byte array
     *
     * @param index Index to retrieve
     * @param dest  Byte array to retrieve from
     * @return The desired byte
     */
    public static byte copyFromByteArr(final int index, @NonNull final byte[] dest) {
        return dest[index];
    }

    /**
     * Copies a shorter byte array into a longer one
     *
     * @param index Index of dest array to start the copy at
     * @param src   The source byte array
     * @param dest  The destination byte array
     */
    public static void copyIntoByteArr(final int index, @NonNull final byte[] src, @NonNull final byte[] dest) {
        int innerIndex = 0;
        for (int i = index; i < index + src.length; i++) {
            dest[i] = src[innerIndex];
            innerIndex++;
        }
    }

    /**
     * Copies a byte array out of another byte array
     *
     * @param index  Index to start the copy from
     * @param length Number of bytes to copy
     * @param src    Byte array to copy from
     * @return A byte array of size length
     */
    @NonNull
    public static byte[] copyFromByteArr(final int index, final int length, @NonNull final byte[] src) {
        int innerIndex = 0;
        byte[] dest = new byte[length];
        for (int i = index; i < index + dest.length; i++) {
            dest[innerIndex] = src[i];
            innerIndex++;
        }
        return dest;
    }

    /**
     * Converts a short into a byte array of length 2
     *
     * @param value Value to convert
     * @return 2 byte array
     */
    @NonNull
    public static byte[] shortToByteArr(final short value) {
        return ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort(value).array();
    }

    /**
     * @param value Value to convert
     * @param index Index in array
     * @param dest  Array to place the short into
     */
    public static void shortToByteArr(final short value, final int index, @NonNull final byte[] dest) {
        byte[] valueArr = shortToByteArr(value);
        copyIntoByteArr(index, valueArr, dest);
    }

    /**
     * Converts a 2 byte array into a short
     *
     * @param value Byte array of length 2
     * @return The short value
     */
    public static short byteArrToShort(@NonNull final byte[] value) {
        return ByteBuffer.wrap(value).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    /**
     * Convert a byte array of length 2 or more into an short
     *
     * @param value Byte array of length 2
     * @param index start point in the array
     * @return The short value
     */
    public static short byteArrToShort(@NonNull final byte[] value, final int index) {
        return ByteBuffer.wrap(value, index, 2).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    /**
     * Converts an int into a byte array of length 4
     *
     * @param value Int value to convert
     * @return Byte array of length 4
     */
    @NonNull
    public static byte[] intToByteArr(final int value) {
        return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(value).array();
    }

    /**
     * Copies a int into a byte array
     *
     * @param value Value to convert
     */
    public static void intToByteArr(final int value, final int index, @NonNull final byte[] dest) {
        byte[] valueArr = intToByteArr(value);
        copyIntoByteArr(index, valueArr, dest);
    }

    /**
     * Convert a byte array of length 4 into an int
     *
     * @param value Byte array
     * @return Int value
     */
    public static int byteArrToInt(@NonNull final byte[] value) {
        return ByteBuffer.wrap(value).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    /**
     * Convert a byte array of length 4 or more into an int
     *
     * @param value Byte array
     * @param index start point in the array
     * @return Int value
     */
    public static int byteArrToInt(@NonNull final byte[] value, final int index) {
        return ByteBuffer.wrap(value, index, 4).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    /**
     * Converts a long into a byte array of length 8
     *
     * @param value long value
     * @return Byte array of length 8
     */
    @NonNull
    public static byte[] longToByteArr(final long value) {
        return ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN).putLong(value).array();
    }

    /**
     * Copies a long into a byte array
     *
     * @param value Value to convert
     */
    public static void longToByteArr(final long value, final int index, @NonNull final byte[] dest) {
        byte[] valueArr = longToByteArr(value);
        copyIntoByteArr(index, valueArr, dest);
    }

    /**
     * Converts a byte array of length 8 into a long
     *
     * @param value Byte array
     * @return Long value
     */
    public static long byteArrToLong(@NonNull final byte[] value) {
        return ByteBuffer.wrap(value).order(ByteOrder.BIG_ENDIAN).getLong();
    }

    /**
     * Converts a byte array of length 8 or more into a long
     *
     * @param value Byte array
     * @param index start point in the array
     * @return Long value
     */
    public static long byteArrToLong(@NonNull final byte[] value, final int index) {
        return ByteBuffer.wrap(value, index, 8).order(ByteOrder.BIG_ENDIAN).getLong();
    }

    /**
     * Sets the specific bit in the byte
     *
     * @param pos     index in the byte right aligned
     * @param value   true for 1, false for 0
     * @param srcByte Byte to set
     * @return The resulting byte
     */
    public static byte setBitInByte(final byte pos, final boolean value, final byte srcByte) {
        if (value) {
            return (byte) (srcByte | (1 << pos));
        }
        return (byte) (srcByte & ~(1 << pos));
    }

    /**
     * Retrieves the specified bit in the byte
     *
     * @param pos     index in the byte right aligned
     * @param srcByte Byte to retrieve from
     * @return true if bit was 1, false if bit was 0
     */
    public static boolean getBitInByte(final byte pos, final byte srcByte) {
        return ((srcByte >> pos) & 1) == 1;
    }

    /**
     * Sets the specific bit in the int
     *
     * @param pos    index in the int right aligned
     * @param value  true for 1, false for 0
     * @param srcInt Int to set
     * @return The resulting int
     */
    public static int setBitInInt(final byte pos, final boolean value, final int srcInt) {
        if (value) {
            return (srcInt | (1 << pos));
        }
        return (srcInt & ~(1 << pos));
    }

    /**
     * Retrieves the specified bit in the int
     *
     * @param pos    index in the int right aligned
     * @param srcInt int to retrieve from
     * @return true if bit was 1, false if bit was 0
     */
    public static boolean getBitInInt(final byte pos, final int srcInt) {
        return ((srcInt >> pos) & 1) == 1;
    }

    /**
     * Sets the given byte into the given int at the location specified.
     * Only copies the number of bits listed.
     *
     * @param pos     Starting position in the int
     * @param numBits Number of bits to copy (max of 8)
     * @param value   Value to copy from
     * @param src     Value to copy into
     * @return The resulting int
     */
    public static int setByteInInt(final byte pos, final byte numBits, final byte value, final int src) {
        byte bitVal = 0;
        for (byte i = pos; i < pos + numBits; i++) {
            setBitInInt(i, getBitInByte(bitVal, value), src);
            bitVal++;
        }
        return src;
    }

    /**
     * Retrieves a byte from the given int given a starting position and number of bits
     *
     * @param pos     Position to start in the int
     * @param numBits Number of bits to retrieve
     * @param srcInt  Int to retrieve from
     * @return Value of the given bits
     */
    public static byte getByteInInt(final byte pos, final byte numBits, final int srcInt) {
        byte result = 0;
        byte bitVal = 0;
        for (byte i = pos; i < pos + numBits; i++) {
            setBitInByte(bitVal, getBitInInt(i, srcInt), result);
            bitVal++;
        }
        return result;
    }

    /**
     * TRUNCATES an int to fit within a byte
     *
     * @param value int value
     * @return byte value of least significant byte out of int
     */
    public static byte intToByte(final int value) {
        return (byte) value;
    }

    /**
     * Extends a byte to fit into an int
     *
     * @param value Byte value
     * @return Int value
     */
    public static int byteToInt(final byte value) {
        return value;
    }

    /**
     * TRUNCATES an int to fit in a short
     *
     * @param value int value
     * @return short value of 2 least significant bytes out of int
     */
    public static short intToShort(final int value) {
        return (short) value;
    }

    /**
     * Extends a byte to fit in a short
     *
     * @param value Byte value
     * @return Short value
     */
    public static short byteToShort(final byte value) {
        return value;
    }

}
