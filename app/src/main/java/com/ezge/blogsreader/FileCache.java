package com.ezge.blogsreader;

/**
 * Created by ezge on 2/25/2018.
 */

// FileCache class saves temporary images into the device internal storage to prevent the images to be downloaded repeatedly.

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileCache {

    private File cacheDir;

    public FileCache(Context context) {
        // Find the dir to save cached images
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            cacheDir = new File(Environment.getExternalStorageDirectory(),"BlogsReader/images");
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public File getFile(String url) {
        String filename = String.valueOf(url.hashCode());
        // String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        return f;
    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }
}
