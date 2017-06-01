package it.namron.sweeping.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.StatFs;
import android.text.format.Formatter;

import it.namron.sweeping.sweeping.R;

import static it.namron.sweeping.utils.Constant.ILLEGAL_ARGUMENT_FOLDER_SIZE;
import static it.namron.sweeping.utils.Constant.NOT_INITIALIZED_FOLDER_SIZE;

/**
 * Created by norman on 29/05/17.
 */

public class WrappedDirectorySize {

    public static String size(long bytes) {
        String unita;

        if (bytes == NOT_INITIALIZED_FOLDER_SIZE) {
            return ResourceHashCode.getContext().getString(R.string.calcolo);
        }

        if (bytes == ILLEGAL_ARGUMENT_FOLDER_SIZE) {
            return ResourceHashCode.getContext().getString(R.string.illegal_folder);
        }

        //todo vedere StatFs
//        StatFs stat = new StatFs(path.getPath());
//        long blockSize = stat.getBlockSizeLong();
//        long availableBlocks = stat.getAvailableBlocksLong();
        String s = Formatter.formatFileSize(ResourceHashCode.getContext(), bytes);
        return s;
    }
}
