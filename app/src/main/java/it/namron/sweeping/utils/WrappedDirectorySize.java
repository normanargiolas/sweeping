package it.namron.sweeping.utils;

import android.content.Context;
import android.content.res.Resources;

import it.namron.sweeping.sweeping.R;

import static it.namron.sweeping.utils.Constant.ILLEGAL_ARGUMENT_FOLDER_SIZE;
import static it.namron.sweeping.utils.Constant.NOT_INITIALIZED_FOLDER_SIZE;

/**
 * Created by norman on 29/05/17.
 */

public class WrappedDirectorySize {

    public static String size(long size) {
        if (size == NOT_INITIALIZED_FOLDER_SIZE) {
            return ResourceHashCode.getContext().getString(R.string.calcolo);
        }

        if (size == ILLEGAL_ARGUMENT_FOLDER_SIZE) {
            return ResourceHashCode.getContext().getString(R.string.illegal_folder);
        }

        return String.valueOf(size);
    }
}
