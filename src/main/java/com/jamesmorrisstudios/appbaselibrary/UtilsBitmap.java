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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.support.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Some common bitmap manipulation functions
 * <p/>
 * Created by James on 12/7/2014.
 */
public final class UtilsBitmap {

    /**
     * Resizes a bitmap to a square with the given side length in pixels
     * If the source is not square the center portion will be kept
     *
     * @param bitmap      Source image
     * @param sideLength  Max side length of resized image
     * @param forceSquare True to take the center square of the image. False to preserve aspect ratio
     * @return Resized image
     */
    @NonNull
    public static Bitmap resizeBitmap(@NonNull final Bitmap bitmap, final int sideLength, final boolean forceSquare) {
        if (forceSquare) {
            //Return the square center of the image as sized.
            return ThumbnailUtils.extractThumbnail(bitmap, sideLength, sideLength, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        } else if (bitmap.getWidth() < sideLength && bitmap.getHeight() < sideLength) {
            //The image is smaller then our side length so just return it
            return bitmap;
        } else {
            //Need to find the scale ratio and scale the image based on that
            if (bitmap.getWidth() > bitmap.getHeight()) {
                float ratio = 1.0f * sideLength / bitmap.getWidth();
                int height = Math.round(bitmap.getHeight() * ratio);
                return ThumbnailUtils.extractThumbnail(bitmap, sideLength, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            } else {
                float ratio = 1.0f * sideLength / bitmap.getHeight();
                int width = Math.round(bitmap.getWidth() * ratio);
                return ThumbnailUtils.extractThumbnail(bitmap, width, sideLength, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            }
        }
    }

    /**
     * The Bitmap must be square!
     * Turns a square bitmap into a round one
     * On failure it returns the source bitmap
     *
     * @param bitmap Input bitmap (Must be square!)
     * @return A round bitmap
     */
    @NonNull
    public static Bitmap getRoundedRectBitmap(@NonNull final Bitmap bitmap) {
        Bitmap result;
        try {
            result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            int color = 0xff424242;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(bitmap.getWidth() / 2.0f, bitmap.getHeight() / 2.0f, bitmap.getWidth() / 2.0f, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return result;
        } catch (NullPointerException | OutOfMemoryError e) {
            //Not a critical error so do nothing
        }
        return bitmap;
    }

    /**
     * Layers two bitmap on top of each other
     * The under bitmap must be equal or larger
     *
     * @param over  Bitmap to place on top
     * @param under Bitmap to place on bottom
     * @return Composite bitmap
     */
    @NonNull
    public static Bitmap composite(@NonNull final Bitmap over, @NonNull final Bitmap under) {
        Bitmap bmOverlay = Bitmap.createBitmap(under.getWidth(), under.getHeight(), under.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(under, new Matrix(), null);
        canvas.drawBitmap(over, new Matrix(), null);
        return bmOverlay;
    }

    /**
     * Shrinks a bitmap by the given percentage of its width
     *
     * @param srcImage      Bitmap to shrink
     * @param shrinkPercent Percent of images width to shrink bitmap by
     * @return Resulting bitmap
     */
    @NonNull
    public static Bitmap shrinkInto(@NonNull final Bitmap srcImage, final float shrinkPercent) {
        //Shrink the bitmap so it fits in the original sized bitmap properly
        int overSize = Math.round(srcImage.getWidth() * shrinkPercent);
        int width = srcImage.getWidth() + overSize;
        int height = srcImage.getWidth() + overSize;
        Bitmap endImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(endImage);
        Rect src = new Rect(
                0, 0,
                srcImage.getWidth(), srcImage.getHeight()
        );
        Rect dest = new Rect(
                overSize, overSize,
                endImage.getWidth() - overSize, endImage.getHeight() - overSize
        );
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(srcImage, src, dest, paint);
        return endImage;
    }

    /**
     * Converts a bitmap into a byte array with format lossless png
     *
     * @param src Bitmap to convert
     * @return byte array
     */
    @NonNull
    public static byte[] bitmapToByteArr(@NonNull final Bitmap src) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    /**
     * Converts a byte array compressed bitmap into bitmap again.
     * Only works if compressed with matching bitmapToByteArr function.
     *
     * @param src byte array
     * @return bitmap
     */
    @NonNull
    public static Bitmap byteArrToBitmap(@NonNull final byte[] src) {
        ByteArrayInputStream stream = new ByteArrayInputStream(src);
        return BitmapFactory.decodeStream(stream);
    }

}
