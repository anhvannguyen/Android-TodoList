package me.anhvannguyen.android.asimplelisttodo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import me.anhvannguyen.android.asimplelisttodo.data.TodoContract;


public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final int LIST_TODO_LOADER = 0;
    private static final int EDITOR_REQUEST_CODE = 100;

//    private ListView mTodoListView;
    private RecyclerView mTodoRecyclerView;
//    private TodoCursorAdapter mCursorAdapter;
    private TodoRecycleAdapter mRecycleAdapter;

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
        restartLoader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

//        mCursorAdapter = new TodoCursorAdapter(
//                getActivity(),
//                null,
//                0
//        );
        mRecycleAdapter = new TodoRecycleAdapter(getActivity(), new TodoRecycleAdapter.TodoAdapterOnClickHandler() {
            @Override
            public void onClick(TodoRecycleAdapter.ViewHolder viewHolder) {
                if (mRecycleAdapter.getCursor() != null) {
                    int idIndex = mRecycleAdapter.getCursor().getColumnIndex(TodoContract.TodoEntry._ID);
                    long id = mRecycleAdapter.getCursor().getLong(idIndex);
                    Intent intent = new Intent(getActivity(), EditorActivity.class);
                    Uri uri = TodoContract.TodoEntry.buildTodoUri(id);
                    intent.putExtra(EditorActivityFragment.TODO_ITEM, uri);
                    startActivityForResult(intent, EDITOR_REQUEST_CODE);
                }
            }
        });

//        mTodoListView = (ListView) rootView.findViewById(R.id.todo_listview);
//        mTodoListView.setAdapter(mCursorAdapter);
//        mTodoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), EditorActivity.class);
//                Uri uri = TodoContract.TodoEntry.buildTodoUri(id);
//                intent.putExtra(EditorActivityFragment.TODO_ITEM, uri);
//                startActivityForResult(intent, EDITOR_REQUEST_CODE);
//
//            }
//        });

        mTodoRecyclerView = (RecyclerView) rootView.findViewById(R.id.todo_recycleview);
        mTodoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTodoRecyclerView.setAdapter(mRecycleAdapter);

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
            case R.id.action_create_item:
                openEditor();
                break;
//            case R.id.action_create_sample:
//                generateTodoSample();
//                break;
            case R.id.action_delete_all:
                deleteAllTodo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openEditor() {
        Intent intent = new Intent(getActivity(), EditorActivity.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    private void deleteAllTodo() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            // Delete all items
                            getActivity().getContentResolver().delete(
                                    TodoContract.TodoEntry.CONTENT_URI,
                                    null,
                                    null
                            );
                            restartLoader();
                            Toast.makeText(getActivity(),
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }

//    private void generateTodoSample() {
//        insertTodo("Something something");
//        insertTodo("Another junk \nbelow");
//        insertTodo("Something something blah blah junk junk, stuff do do do do lalalalalalala");
//        restartLoader();
//    }

    private void restartLoader() {
        getLoaderManager().restartLoader(LIST_TODO_LOADER, null, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            restartLoader();
        }
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
//        mCursorAdapter.swapCursor(data);
        mRecycleAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        mCursorAdapter.swapCursor(null);
        mRecycleAdapter.swapCursor(null);
    }
}
