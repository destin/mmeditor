package com.dpytel.android.mmeditor;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dpytel.android.mmeditor.model.Mindmap;
import com.dpytel.android.mmeditor.model.MindmapNode;
import com.dpytel.android.mmeditor.model.NodeId;

import java.util.Collections;
import java.util.List;

public class MindmapExpandableAdapter extends BaseExpandableListAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private Mindmap mindmap;
    private MindmapNode mindmapNode;
    private NodeId activeNodeId;

    private static final int[] EMPTY_STATE_SET = {};
    private static final int[] GROUP_EXPANDED_STATE_SET = { android.R.attr.state_expanded };
    private static final int[][] GROUP_STATE_SETS = { EMPTY_STATE_SET, // 0
            GROUP_EXPANDED_STATE_SET // 1
    };

    public MindmapExpandableAdapter(Mindmap mindmap) {
        this.mindmap = mindmap;
        this.mindmapNode = new ParentOfRootNode(mindmap);
        this.activeNodeId = mindmap.getRootNode().getNodeId();
    }

    public void setInflater(LayoutInflater inflater, Activity activity) {
        this.inflater = inflater;
        this.activity = activity;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.mindmap_child_item, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.selected_element_text);
        final MindmapNode groupNode = getGroupNode(groupPosition);
        final MindmapNode childNode = groupNode.getChildren().get(childPosition);
        textView.setText(childNode.getText());

        return convertView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.mindmap_group_item, null);
        }

        MindmapNode groupNode = getGroupNode(groupPosition);
        CheckedTextView checkedTextView = (CheckedTextView) convertView.findViewById(R.id.group_item_text);
        checkedTextView.setText(groupNode.getText());
        checkedTextView.setChecked(isExpanded);

        ImageView indicator = (ImageView) convertView.findViewById(R.id.group_indicator);
        if (groupNode.getChildren().size() == 0) {
            indicator.setVisibility(View.INVISIBLE);
        } else {
            indicator.setVisibility(View.VISIBLE);
            int state = isExpanded ? 1 : 0;
            indicator.getDrawable().setState(GROUP_STATE_SETS[state]);
        }

        if (groupNode.getNodeId().equals(activeNodeId)) {
            ExpandableListView expandableListView = (ExpandableListView) parent;
            expandableListView.expandGroup(groupPosition);
        }

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getChildNode(groupPosition, childPosition).getNodeId().toLong() + Integer.MAX_VALUE;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return getGroupNode(groupPosition).getChildren().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return mindmapNode.getChildren().size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return getGroupNode(groupPosition).getNodeId().toLong();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean isRootLevel() {
        return mindmapNode instanceof ParentOfRootNode;
    }

    public void goLevelUp() {
        if (isRootLevel()) {
            throw new IllegalStateException("Please first check level with isRootLevel");
        }
        MindmapNode parentNode;
        if (mindmapNode.isRoot()) {
            parentNode = new ParentOfRootNode(mindmap);
        } else {
            parentNode = mindmapNode.getParentNode();
        }
        switchToNode(parentNode, mindmapNode.getNodeId());
    }

    public void goToChild(int groupPosition, int childPosition) {
        switchToNode(getGroupNode(groupPosition), getChildNode(groupPosition, childPosition).getNodeId());
    }

    public void goToRoot() {
        switchToNode(new ParentOfRootNode(mindmap), mindmap.getRootNode().getNodeId());
    }

    private void switchToNode(@NonNull MindmapNode groupNode, NodeId newActiveNodeId) {
        MindmapExpandableAdapter.this.mindmapNode = groupNode;
        activeNodeId = newActiveNodeId;
        notifyDataSetChanged();
    }

    private MindmapNode getGroupNode(int groupPosition) {
        List<MindmapNode> groupNodes = mindmapNode.getChildren();
        return groupNodes.get(groupPosition);
    }

    private MindmapNode getChildNode(int groupPosition, int childPosition) {
        final MindmapNode groupNode = getGroupNode(groupPosition);
        return groupNode.getChildren().get(childPosition);
    }

    public static class ParentOfRootNode implements MindmapNode {

        public static final NodeId PARENT_OF_ROOT_NODE_ID = new NodeId("ID_0");
        private Mindmap mindmap;

        public ParentOfRootNode(Mindmap mindmap) {
            this.mindmap = mindmap;
        }

        @Override
        public String getText() {
            throw new UnsupportedOperationException();
        }

        @Override
        @NonNull
        public List<MindmapNode> getChildren() {
            return Collections.singletonList(mindmap.getRootNode());
        }

        @Override
        @NonNull
        public NodeId getNodeId() {
            return PARENT_OF_ROOT_NODE_ID;
        }

        @Override
        @NonNull
        public MindmapNode getParentNode() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isRoot() {
            return true;
        }
    }
}
