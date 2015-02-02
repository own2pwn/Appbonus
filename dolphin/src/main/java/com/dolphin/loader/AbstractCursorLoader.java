package com.dolphin.loader;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

import com.dolphin.db.AbstractDataBaseHelper;


public abstract class AbstractCursorLoader extends CursorLoader {
    protected String baseName;

    public AbstractCursorLoader(Context context, String baseName) {
        super(context);
        this.baseName = baseName;
    }

    public AbstractCursorLoader(Context context, String baseName, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
        this.baseName = baseName;
    }

    public AbstractCursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public Cursor loadInBackground() {
        return createHelper().select(getProjection(), getSelection(), getSelectionArgs(), getSortOrder());
    }

    protected abstract AbstractDataBaseHelper createHelper();
}
