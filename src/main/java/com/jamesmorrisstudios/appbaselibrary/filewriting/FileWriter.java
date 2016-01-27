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

package com.jamesmorrisstudios.appbaselibrary.filewriting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jamesmorrisstudios.appbaselibrary.Utils;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * File writer class that can take images or strings and read/write them to app hidden directories
 * or to the app cache dir on the sd card.
 * <p/>
 * Created by James on 9/19/2014.
 */
public final class FileWriter {

    /**
     * Creates a directory or directory tree
     *
     * @param directory Directory path
     * @param location  File location
     * @return False if failed to create. True if created or already exists.
     */
    public synchronized static boolean mkDirs(@NonNull final String directory, @NonNull final FileLocation location) {
        File dir = getFile(directory, location);
        if (dir == null) {
            return false;
        }
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the given file exists
     *
     * @param fileName The name of the file
     * @param location If internal or external storage
     * @return True if the file exists, false otherwise
     */
    public synchronized static boolean doesFileExist(@NonNull final String fileName, @NonNull final FileLocation location) {
        File file = getFile(fileName, location);
        return file != null && file.exists();
    }

    /**
     * Gets the Uri of the file. Useful mostly for external files
     *
     * @param fileName The name of the file
     * @param location If internal or external storage
     * @return The URI pointing to the file
     */
    @Nullable
    public static Uri getFileUri(@NonNull final String fileName, @NonNull final FileLocation location) {
        File file = getFile(fileName, location);
        if (file == null) {
            return null;
        }
        return Uri.fromFile(file);
    }

    public static Uri writeToZip(@NonNull final Uri[] files, @NonNull final String zipFileName, @NonNull final FileLocation zipLocation) {
        int BUFFER = 2048;
        BufferedInputStream origin = null;
        File zipFile = getFile(zipFileName, zipLocation);
        if(zipFile == null) {
            return null;
        }
        try {
            FileOutputStream dest = new FileOutputStream(zipFile);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER];
            for(int i=0; i < files.length; i++) {
                Log.v("Compress", "Adding: " + files[i].getPath());
                File file = getFile(files[i].getPath(), FileLocation.PATH);
                if(file == null || !file.canRead()) {
                    continue;
                }
                FileInputStream fi = new FileInputStream(file);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(files[i].getPath().substring(files[i].getPath().lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
            return Uri.fromFile(zipFile);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void readFromZip(@NonNull final Uri zipFile, @NonNull final FileLocation destLocation) {
        try {
            FileInputStream fin = new FileInputStream(zipFile.getPath());
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                Log.v("Decompress", "Unzipping " + ze.getName());
                if(!ze.isDirectory()) {
                    File file = getFile(ze.getName(), destLocation);
                    if(file != null) {
                        Log.v("Decompress", "Unzipping " + file.getPath());
                        FileOutputStream fout = new FileOutputStream(file);
                        for (int c = zin.read(); c != -1; c = zin.read()) {
                            fout.write(c);
                        }
                        fout.close();
                        Log.v("Decompress", "Unzipping File Written " + ze.getName());
                    }
                    zin.closeEntry();
                }
            }
            zin.close();
        } catch(Exception e) {
            Log.e("Decompress", "unzip", e);
        }
    }

    /**
     * Writes a bitmap as a png encoded file
     *
     * @param fileName The name of the file
     * @param bitmap   The bitmap to write
     * @param location If internal or external storage
     * @return True if successful
     */
    @Nullable
    public synchronized static Uri writeImage(@NonNull final String fileName, @NonNull final Bitmap bitmap, @NonNull final FileLocation location) {
        File file = getFile(fileName, location);
        if (file == null) {
            return null;
        }
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    return null;
                }
            }
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
        } catch (IOException e) {
            return null;
        }
        return getFileUri(fileName, location);
    }

    /**
     * Reads a png or jpg encoded bitmap from a file
     *
     * @param fileName The name of the file
     * @param location If internal or external storage
     * @return The bitmap that was read
     */
    @Nullable
    public synchronized static Bitmap readImage(@NonNull final String fileName, @NonNull final FileLocation location) {
        return readImageSub(getFile(fileName, location));
    }

    /**
     * Reads a png or jpg encoded bitmap from a file
     *
     * @param uri      The name of the file
     * @param location If internal or external storage
     * @return The bitmap that was read
     */
    @Nullable
    public synchronized static Bitmap readImage(@NonNull final Uri uri, @NonNull final FileLocation location) {
        if (isContentUri(uri, location)) {
            return readImageContent(uri);
        }
        return readImageSub(getFile(uri.getPath(), location));
    }

    /**
     * Reads a png or jpg encoded bitmap from a file
     *
     * @return The bitmap that was read
     */
    @Nullable
    private synchronized static Bitmap readImageSub(@Nullable final File file) {
        if (file == null) {
            return null;
        }
        Bitmap bitmap;
        try {
            if (!file.exists()) {
                return null;
            }
            InputStream inputStream = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            return null;
        }
        return bitmap;
    }

    /**
     * Writes a generic byte array to a file
     *
     * @param fileName The name of the file
     * @param bytes    The byte array to write
     * @param location If internal or external storage
     * @return Uri of file or null if failed
     */
    @Nullable
    public synchronized static Uri writeFile(@NonNull final String fileName, @NonNull final byte[] bytes, @NonNull final FileLocation location) {
        return writeFile(fileName, bytes, location, false);
    }

    /**
     * Writes a generic byte array to a file
     *
     * @param fileName The name of the file
     * @param bytes    The byte array to write
     * @param location If internal or external storage
     * @return Uri of file or null if failed
     */
    @Nullable
    public synchronized static Uri appendFile(@NonNull final String fileName, @NonNull final byte[] bytes, @NonNull final FileLocation location) {
        return writeFile(fileName, bytes, location, true);
    }

    /**
     * Writes a generic byte array to a file
     *
     * @param fileName The name of the file
     * @param bytes    The byte array to write
     * @param location If internal or external storage
     * @param append True to append to the file, false to overwrite
     * @return Uri of file or null if failed
     */
    @Nullable
    private synchronized static Uri writeFile(@NonNull final String fileName, @NonNull final byte[] bytes, @NonNull final FileLocation location, boolean append) {
        File file = getFile(fileName, location);
        if (file == null) {
            return null;
        }
        FileOutputStream outputStream;
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    return null;
                }
            }
            outputStream = new FileOutputStream(file, append);
            outputStream.write(bytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return getFileUri(fileName, location);
    }

    /**
     * Reads a file and returns the byte array of its contents
     *
     * @param fileName The name of the file
     * @param location If internal or external storage
     * @return Byte array of the file contents
     */
    @Nullable
    public synchronized static byte[] readFile(@NonNull final String fileName, @NonNull final FileLocation location) {
        return readFileSub(getFile(fileName, location));
    }

    /**
     * Reads a file and returns the byte array of its contents
     *
     * @param uri      The Uri path to the file
     * @param location The storage location
     * @return Byte array of the file contents
     */
    @Nullable
    public synchronized static byte[] readFile(@NonNull final Uri uri, @NonNull final FileLocation location) {
        if (isContentUri(uri, location)) {
            return readFileContent(uri);
        }
        return readFileSub(getFile(uri.getPath(), location));
    }

    /**
     * Reads a file and returns the byte array of its contents
     *
     * @param file File object to read
     * @return Byte array of the file contents
     */
    @Nullable
    private synchronized static byte[] readFileSub(@Nullable final File file) {
        if (file == null) {
            return null;
        }
        if (!file.exists()) {
            return null;
        }
        byte[] bytes;
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            bytes = readBytes(inputStream);
            inputStream.close();
            return bytes;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Writes a string to a file
     *
     * @param fileName The name of the file
     * @param string    The string to write
     * @param location If internal or external storage
     * @return Uri of file or null if failed
     */
    @Nullable
    public synchronized static Uri writeString(@NonNull final String fileName, @NonNull final String string, @NonNull final FileLocation location) {
        return writeString(fileName, string, location, false);
    }

    /**
     * Writes a string to a file
     *
     * @param fileName The name of the file
     * @param string    The string to write
     * @param location If internal or external storage
     * @return Uri of file or null if failed
     */
    @Nullable
    public synchronized static Uri appendString(@NonNull final String fileName, @NonNull final String string, @NonNull final FileLocation location) {
        return writeString(fileName, string, location, true);
    }

    /**
     * Writes a String to a file
     *
     * @param fileName The name of the file
     * @param string    The string to write
     * @param location If internal or external storage
     * @param append True to append to the file, false to overwrite
     * @return Uri of file or null if failed
     */
    @Nullable
    private synchronized static Uri writeString(@NonNull final String fileName, @NonNull final String string, @NonNull final FileLocation location, boolean append) {
        File file = getFile(fileName, location);
        if (file == null) {
            return null;
        }
        FileOutputStream outputStream;
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    return null;
                }
            }
            outputStream = new FileOutputStream(file, append);
            outputStream.write(string.getBytes(Utils.stringType));
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return getFileUri(fileName, location);
    }

    /**
     * Reads a file and returns the String of its contents
     *
     * @param fileName The name of the file
     * @param location If internal or external storage
     * @return String of the file contents
     */
    @Nullable
    public synchronized static String readString(@NonNull final String fileName, @NonNull final FileLocation location) {
        return readStringSub(getFile(fileName, location));
    }

    /**
     * Reads a file and returns the String of its contents
     *
     * @param uri      The Uri path to the file
     * @param location The storage location
     * @return String of the file contents
     */
    @Nullable
    public synchronized static String readString(@NonNull final Uri uri, @NonNull final FileLocation location) {
        if (isContentUri(uri, location)) {
            return readStringContent(uri);
        }
        return readStringSub(getFile(uri.getPath(), location));
    }

    /**
     * Reads a file and returns the string of its contents
     *
     * @param file File object to read
     * @return String of the file contents
     */
    @Nullable
    private synchronized static String readStringSub(@Nullable final File file) {
        if (file == null) {
            return null;
        }
        if (!file.exists()) {
            return null;
        }
        String string;
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            string = convertStreamToString(inputStream);
            inputStream.close();
            return string;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * True if this file is from a content provider
     *
     * @param uri      Uri path to the file
     * @param location The storage location
     * @return True if this file is from a content provider
     */
    private static boolean isContentUri(@NonNull final Uri uri, @NonNull final FileLocation location) {
        if (location == FileLocation.PATH) {
            if (uri.getScheme().contains("content")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Read a file from a content provider
     *
     * @param uri Uri path to the file
     * @return Byte array of the file contents
     */
    @Nullable
    private static byte[] readFileContent(@NonNull final Uri uri) {
        ParcelFileDescriptor mInputPFD;
        try {
            mInputPFD = AppBase.getContext().getContentResolver().openFileDescriptor(uri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        if (mInputPFD == null) {
            return null;
        }
        FileDescriptor fd = mInputPFD.getFileDescriptor();
        FileInputStream inputStream;
        byte[] bytes;
        try {
            inputStream = new FileInputStream(fd);
            bytes = readBytes(inputStream);
            inputStream.close();
            return bytes;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Read a file from a content provider
     *
     * @param uri Uri path to the file
     * @return String of the file contents
     */
    @Nullable
    private static String readStringContent(@NonNull final Uri uri) {
        ParcelFileDescriptor mInputPFD;
        try {
            mInputPFD = AppBase.getContext().getContentResolver().openFileDescriptor(uri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        if (mInputPFD == null) {
            return null;
        }
        FileDescriptor fd = mInputPFD.getFileDescriptor();
        FileInputStream inputStream;
        String string;
        try {
            inputStream = new FileInputStream(fd);
            string = convertStreamToString(inputStream);
            inputStream.close();
            return string;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Read an image from a content provider
     *
     * @param uri Uri path to the file
     * @return Bitmap that was read. Null if failed.
     */
    @Nullable
    private static Bitmap readImageContent(@NonNull final Uri uri) {
        ParcelFileDescriptor mInputPFD;
        try {
            mInputPFD = AppBase.getContext().getContentResolver().openFileDescriptor(uri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        if (mInputPFD == null) {
            return null;
        }
        FileDescriptor fd = mInputPFD.getFileDescriptor();
        Bitmap bitmap;
        try {
            InputStream inputStream = new FileInputStream(fd);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            return null;
        }
        return bitmap;
    }

    /**
     * Deletes the given file
     *
     * @param fileName The name of the file
     * @param location If internal or external storage
     * @return True if successful
     */
    public synchronized static boolean deleteFile(@NonNull final String fileName, @NonNull final FileLocation location) {
        File file = getFile(fileName, location);
        return file != null && file.delete();
    }

    public synchronized static void deleteCache() {
        try {
            File dir = AppBase.getContext().getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {

        }
    }

    public static boolean deleteDir(@NonNull final File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * Gets a file handle for manipulation
     *
     * @param fileName The name of the file
     * @param location If internal or external storage
     * @return The File handle
     */
    @Nullable
    public static File getFile(@NonNull final String fileName, @NonNull final FileLocation location) {
        switch (location) {
            case INTERNAL:
                return new File(AppBase.getContext().getFilesDir(), fileName);
            case CACHE_EXTERNAL:
                return new File(AppBase.getContext().getExternalCacheDir(), fileName);
            case CACHE_INTERNAL:
                return new File(AppBase.getContext().getCacheDir(), fileName);
            case PATH:
                return new File(fileName);
            case SDCARD:
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    return new File(Environment.getExternalStorageDirectory(), fileName);
                } else {
                    return null;
                }
            default:
                return new File(AppBase.getContext().getFilesDir(), fileName);
        }
    }

    /**
     * Takes an inputStream and reads it into a byte array
     *
     * @param inputStream Byte input stream
     * @return Byte array of the input stream
     * @throws IOException
     */
    @NonNull
    private static byte[] readBytes(@NonNull final InputStream inputStream) throws IOException {
        // this dynamically extends to take the bytes you read
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        // we need to know how may bytes were read to write them to the byteBuffer
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }

    @Nullable
    private static String convertStreamToString(@NonNull InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    /**
     * File save location
     */
    public enum FileLocation {

        /**
         * Internal protected storage.
         */
        INTERNAL,

        /**
         * Cache location internal
         */
        CACHE_INTERNAL,

        /**
         * Cache location external
         */
        CACHE_EXTERNAL,

        /**
         * External storage. Must acquire permission before calling this.
         * May not always be available.
         */
        SDCARD,

        /**
         * Fully qualified path
         */
        PATH
    }

}
