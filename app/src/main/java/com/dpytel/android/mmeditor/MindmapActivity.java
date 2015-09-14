package com.dpytel.android.mmeditor;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import com.dpytel.android.mmeditor.model.Mindmap;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MindmapActivity extends ExpandableListActivity {

    private MindmapExpandableAdapter mindmapExpandableAdapter;

    private MindmapParser mindmapParser = new MindmapParser();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mindmap);

        final ExpandableListView expandableList = getExpandableListView();

        expandableList.setDividerHeight(2);
        expandableList.setClickable(true);

        InputStream inputStream = getInputStream();
        setUpAdapter(inputStream);
        expandActiveGroup();
        expandableList.setOnChildClickListener(this);

        findViewById(R.id.go_level_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLevelUp();
            }
        });
        findViewById(R.id.go_to_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRoot();
            }
        });
    }

    private void setUpAdapter(@NonNull InputStream inputStream) {
        Mindmap mindmap = mindmapParser.parseMindmap(inputStream);
        mindmapExpandableAdapter = new MindmapExpandableAdapter(mindmap);
        mindmapExpandableAdapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        mindmapExpandableAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                expandActiveGroup();
            }

            @Override
            public void onInvalidated() {
                expandActiveGroup();
            }
        });
        this.setListAdapter(mindmapExpandableAdapter);
    }

    private void expandActiveGroup() {
        getExpandableListView().expandGroup(mindmapExpandableAdapter.getActiveGroupPosition());
    }

    @NonNull
    private InputStream getInputStream() {
        Intent intent = getIntent();
        InputStream inputStream;
        String action = intent.getAction();
        if (Intent.ACTION_MAIN.equals(action)) {
            inputStream = getResources().openRawResource(R.raw.mmeditorhelp);
        } else if (Intent.ACTION_VIEW.equals(action)) {
            inputStream = getInputStreamToOpen(intent);
        } else {
            throw new IllegalStateException("Unhandled action: " + action);
        }
        return inputStream;
    }

    @NonNull
    private InputStream getInputStreamToOpen(Intent intent) {
        InputStream inputStream;
        try {
            inputStream = getContentResolver().openInputStream(intent.getData());
        } catch (FileNotFoundException e) {
            // TODO handle gracefully
            throw new RuntimeException(e);
        }
        return inputStream;
    }

    @Override
    public void onBackPressed() {
        if (mindmapExpandableAdapter.isRootLevel()) {
            super.onBackPressed();
        } else {
            goLevelUp();
        }

    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        mindmapExpandableAdapter.goToChild(groupPosition, childPosition);
        return true;
    }

    private void goLevelUp() {
        if (!mindmapExpandableAdapter.isRootLevel()) {
            mindmapExpandableAdapter.goLevelUp();
        }
    }

    private void goToRoot() {
        mindmapExpandableAdapter.goToRoot();
    }
}
