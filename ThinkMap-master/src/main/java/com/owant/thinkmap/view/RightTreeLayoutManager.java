package com.owant.thinkmap.view;

import android.util.Log;
import android.view.View;

import com.owant.thinkmap.model.ForTreeItem;
import com.owant.thinkmap.model.NodeModel;
import com.owant.thinkmap.model.TreeModel;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by owant on 08/03/2017.
 */
public class RightTreeLayoutManager implements TreeLayoutManager {

    final int msg_standard_layout = 1;
    final int msg_correct_layout = 2;
    final int msg_box_call_back = 3;

    private ViewBox mViewBox;
    private int mDy;
    private int mDx;
    private int mHeight;

    public RightTreeLayoutManager(int dx, int dy, int height) {
        mViewBox = new ViewBox();

        this.mDx = dx;
        this.mDy = dy;
        this.mHeight = height;
    }

    @Override
    public void onTreeLayout(final TreeView treeView) {

        final TreeModel<String> mTreeModel = treeView.getTreeModel();
        if (mTreeModel != null) {

            View rootView = treeView.findNodeViewFromNodeModel(mTreeModel.getRootNode());
            if (rootView != null) {

                //根节点位置
                rootTreeViewLayout((NodeView) rootView);
            }

            mTreeModel.addForTreeItem(new ForTreeItem<NodeModel<String>>() {
                @Override
                public void next(int msg, NodeModel<String> next) {
                    doNext(msg, next, treeView);
                }
            });

            //基本布局
            mTreeModel.ergodicTreeInWith(msg_standard_layout);

            //纠正
            mTreeModel.ergodicTreeInWith(msg_correct_layout);

            mViewBox.clear();
            mTreeModel.ergodicTreeInDeep(msg_box_call_back);

        }
    }

    @Override
    public ViewBox onTreeLayoutCallBack() {
        if (mViewBox != null) {
            return mViewBox;
        } else {
            return null;
        }
    }

    private void doNext(int msg, NodeModel<String> next, TreeView treeView) {
        View view = treeView.findNodeViewFromNodeModel(next);

        if (msg == msg_standard_layout) {
            //标准分布
            standardLayout(treeView, (NodeView) view);
        } else if (msg == msg_correct_layout) {
            //纠正
            correctLayout(treeView, (NodeView) view);
        } else if (msg == msg_box_call_back) {

            //View的大小变化
            int left = view.getLeft();
            int top = view.getTop();
            int bottom = view.getBottom();
            int right = view.getRight();

            //     *******
            //     *     *
            //     *     *
            //     *******

            if (left < mViewBox.left) {
                mViewBox.left = left;
            }
            if (top < mViewBox.top) {
                mViewBox.top = top;
            }
            if (bottom > mViewBox.bottom) {
                mViewBox.bottom = bottom;
            }
            if (right > mViewBox.right) {
                mViewBox.right = right;
            }
        }
    }

    /**
     * 布局纠正
     *
     * @param treeView
     * @param next
     */
    public void correctLayout(TreeView treeView, NodeView next) {

        TreeModel mTree = treeView.getTreeModel();
        int count = next.getTreeNode().getChildNodes().size();

        if (next.getParent() != null && count >= 2) {
            NodeModel<String> tn = next.getTreeNode().getChildNodes().get(0);
            NodeModel<String> bn = next.getTreeNode().getChildNodes().get(count - 1);
            Log.i("see fc", next.getTreeNode().getValue() + ":" + tn.getValue() + "," + bn.getValue());

            int topDr = next.getTop() - treeView.findNodeViewFromNodeModel(tn).getBottom() + mDy;
            int bnDr = treeView.findNodeViewFromNodeModel(bn).getTop() - next.getBottom() + mDy;

            //上移动
            ArrayList<NodeModel<String>> allLowNodes = mTree.getAllLowNodes(bn);
            ArrayList<NodeModel<String>> allPreNodes = mTree.getAllPreNodes(tn);

            for (NodeModel<String> low : allLowNodes) {
                NodeView view = (NodeView) treeView.findNodeViewFromNodeModel(low);
                moveNodeLayout(treeView, view, bnDr);
            }

//            for (NodeModel<String> pre : allPreNodes) {
//                NodeView view = (NodeView) treeView.findNodeViewFromNodeModel(pre);
//                moveNodeLayout(treeView, view, -topDr);
//            }
        }
    }

    /**
     * 标准分布
     *
     * @param treeView
     * @param rootView
     */
    private void standardLayout(TreeView treeView, NodeView rootView) {
        NodeModel<String> treeNode = rootView.getTreeNode();
        if (treeNode != null) {
            //所有的子节点
            LinkedList<NodeModel<String>> childNodes = treeNode.getChildNodes();
            int size = childNodes.size();

            //基线
            //        b
            //    a-------
            //        c
            //
            int left = rootView.getRight() + mDx;
            int top = rootView.getTop();
            int right;
            int bottom;

            if (size == 0) {
                return;
            } else if (size == 1) {
                NodeView midChildNodeView = (NodeView) treeView.findNodeViewFromNodeModel(childNodes.get(0));

                right = left + midChildNodeView.getMeasuredWidth();
                bottom = top + midChildNodeView.getMeasuredHeight();
                midChildNodeView.layout(left, top, right, bottom);
            } else {

                int bottomTop;
                int bottomRight;
                int bottomBottom;

                NodeView midView = (NodeView) treeView.findNodeViewFromNodeModel(childNodes.get(0));
                midView.layout(left, top, left + midView.getMeasuredWidth(), top + midView.getMeasuredHeight());

                bottomTop = midView.getBottom();

                for (int i = 1; i <= size - 1; i++) {
                    NodeView bottomView = (NodeView) treeView.findNodeViewFromNodeModel(childNodes.get(i));

                    bottomTop = bottomTop + mDy;
                    bottomRight = left + bottomView.getMeasuredWidth();
                    bottomBottom = bottomTop + bottomView.getMeasuredHeight();

                    bottomView.layout(left, bottomTop, bottomRight, bottomBottom);
                    bottomTop = bottomView.getBottom();
                }
            }
        }
    }

    /**
     * 移动
     *
     * @param rootView
     * @param dy
     */
    private void moveNodeLayout(TreeView superTreeView, NodeView rootView, int dy) {

        Deque<NodeModel<String>> queue = new ArrayDeque<>();
        NodeModel<String> rootNode = rootView.getTreeNode();
        queue.add(rootNode);
        while (!queue.isEmpty()) {
            rootNode = queue.poll();
            rootView = (NodeView) superTreeView.findNodeViewFromNodeModel(rootNode);
            int l = rootView.getLeft();
            int t = rootView.getTop() + dy;
            rootView.layout(l, t, l + rootView.getMeasuredWidth(), t + rootView.getMeasuredHeight());

            LinkedList<NodeModel<String>> childNodes = rootNode.getChildNodes();
            for (NodeModel<String> item : childNodes) {
                queue.add(item);
            }
        }
    }

//    private void moveNodeLayout_2(TreeView superTreeView, NodeView rootView) {
//
//        Deque<NodeModel<String>> queue = new ArrayDeque<>();
//        NodeModel<String> rootNode = rootView.getTreeNode();
//        = superTreeView.findNodeViewFromNodeModel(rootNode.getParentNode());
//        queue.add(rootNode);
//        while (!queue.isEmpty()) {
//            rootNode = queue.poll();
//            rootView = (NodeView) superTreeView.findNodeViewFromNodeModel(rootNode);
//            int l = rootView.getLeft();
//            int t = rootView.getTop();
//            rootView.layout(l, t, l + rootView.getMeasuredWidth(), t + rootView.getMeasuredHeight());
//
//            LinkedList<NodeModel<String>> childNodes = rootNode.getChildNodes();
//            for (NodeModel<String> item : childNodes) {
//                queue.add(item);
//            }
//        }
//    }

    /**
     * root节点的定位
     *
     * @param rootView
     */
    private void rootTreeViewLayout(NodeView rootView) {
        int lr = mDy;
        int tr = mHeight / 2 - rootView.getMeasuredHeight() / 2;
        int rr = lr + rootView.getMeasuredWidth();
        int br = tr + rootView.getMeasuredHeight();
        rootView.layout(lr, tr, rr, br);
    }
}
