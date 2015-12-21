package com.jamesmorrisstudios.appbaselibrary;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Compression and decompression functions.
 * <p/>
 * Created by James on 11/30/2015.
 */
public final class UtilsCompression {

    /**
     * Must use the matching decompress function.
     *
     * @param data Byte array to compress
     * @return Compressed byte array
     */
    @Nullable
    public static byte[] compress(@NonNull final byte[] data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        Deflater deflater = new Deflater();
        deflater.setLevel(2);
        deflater.setInput(data);
        deflater.finish();
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            return null;
        }
        return outputStream.toByteArray();
    }

    /**
     * Must use the matching compress function.
     *
     * @param data Data to decompress
     * @return Decompressed data
     */
    @Nullable
    public static byte[] decompress(@NonNull final byte[] data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        Inflater inflater = new Inflater();
        int count;
        inflater.setInput(data);
        try {
            while (!inflater.finished()) {
                count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
        } catch (DataFormatException e) {
            return null;
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            return null;
        }
        return outputStream.toByteArray();
    }

}
