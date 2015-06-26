package me.anhvannguyen.android.asimplelisttodo;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import me.anhvannguyen.android.asimplelisttodo.data.TodoContract;


public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final int LIST_TODO_LOADER = 0;

    private ListView mTodoListView;
    private TodoCursorAdapter mCursorAdapter;

    public MainActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(LIST_TODO_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(LIST_TODO_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mCursorAdapter = new TodoCursorAdapter(
                getActivity(),
                null,
                0
        );
        mTodoListView = (ListView) rootView.findViewById(R.id.todo_listview);
        mTodoListView.setAdapter(mCursorAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

        }
        return super.onOptionsItemSelected(item);
    }

    // TODO: Move off of UI thread
    public void insertTodo(String todoText) {
        ContentValues newValue = new ContentValues();

        newValue.put(TodoContract.TodoEntry.COLUMN_TEXT, todoText);
        Uri insertUri = getActivity().getContentResolver().insert(
                TodoContract.TodoEntry.CONTENT_URI,
                newValue
        );
        Toast.makeText(getActivity(), "Inserting to " + insertUri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = TodoContract.TodoEntry.COLUMN_CREATED + " DESC";
        return new CursorLoader(
                getActivity(),
                TodoContract.TodoEntry.CONTENT_URI,
                null,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
