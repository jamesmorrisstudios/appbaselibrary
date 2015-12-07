package com.jamesmorrisstudios.appbaselibrary;

import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Created by James on 11/30/2015.
 */
public class UtilsCompression {

    @Nullable
    public static byte[] compress(byte[] data) {
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
        byte[] output = outputStream.toByteArray();
        //Log.d("UtilsCompression", "Original: " + data.length + " B");
        //Log.d("UtilsCompression", "Compressed: " + output.length + " B");
        return output;
    }

    @Nullable
    public static byte[] decompress(byte[] data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        Inflater inflater = new Inflater();
        int count = 0;

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
        byte[] output = outputStream.toByteArray();
        //Log.d("UtilsCompression", "Original: " + data.length + " B");
        //Log.d("UtilsCompression", "UnCompressed: " + output.length + " B");
        return output;
    }

}
