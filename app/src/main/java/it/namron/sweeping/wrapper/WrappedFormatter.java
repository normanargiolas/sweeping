package it.namron.sweeping.wrapper;

import android.support.annotation.NonNull;
import android.text.format.Formatter;

import it.namron.sweeping.sweeping.R;
import it.namron.sweeping.utils.ResourceHashCode;

import static it.namron.sweeping.constant.Constant.ILLEGAL_ARGUMENT_FOLDER_SIZE;
import static it.namron.sweeping.constant.Constant.NOT_INITIALIZED_FOLDER_SIZE;

/**
 * Created by norman on 29/05/17.
 */

public class WrappedFormatter {

    public static String byteSize(long bytes) {
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

    public static String filesNumber(@NonNull int numberOfFiles) {
        String res = String.valueOf(numberOfFiles);

        return res + " files.";
    }
}
