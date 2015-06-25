package me.anhvannguyen.android.asimplelisttodo.data;

import android.provider.BaseColumns;

public class TodoContract {

    public static final class TodoEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";

        public static final String COLUMN_TEXT = "note_text";

        public static final String COLUMN_CREATED = "note_created";


    }
}
