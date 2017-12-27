package com.owant.thinkmap.model;

import com.owant.thinkmap.view.TreeView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by owant on 09/02/2017.
 */
public class TreeModel<T> implements Serializable {

    /**
     * the root for the tree
     */
    private NodeModel<T> rootNode;

    /**
     * 模型里的接口是不用序列号的
     */

    private transient ForTreeItem<NodeModel<T>> mForTreeItem;

    public TreeModel(NodeModel<T> rootNode) {
        this.rootNode = rootNode;
    }

    /**
     * add the node in some father node
     *
     * @param start
     * @param nodes
     */

    public void addSubNodeFirst(NodeModel<T> start, NodeModel<T>... nodes) {
        int index = 1;
        NodeModel<T> temp = start;
        if (temp.getParentNode() != null) {
            index = temp.getParentNode().floor;
        }

        LinkedList<NodeModel<T>> childNodes = temp.getChildNodes();

        for (NodeModel<T> t : nodes) {
            t.setParentNode(start);
            t.setFloor(index);

            //校验是否存在
            boolean exist = false;
            for (NodeModel<T> hash : childNodes) {
                if (hash == t) {
                    exist = true;
                    continue;
                }
            }
            if (!exist) start.getChildNodes().add(t);
        }
    }

    public void insertSubNodeFirst(NodeModel<T> start, NodeModel<T>... nodes) {
        int index = 1;
        NodeModel<T> temp = start;
        if (temp.getParentNode() != null) {
//            index = temp.getParentNode().floor;
            index = temp.floor + index;
        }

        LinkedList<NodeModel<T>> childNodes = temp.getChildNodes();

        for (NodeModel<T> t : nodes) {
            t.setParentNode(start);
            t.setFloor(index);
            //插入的是主干线设备

            //校验是否存在
            boolean exist = false;
            for (NodeModel<T> hash : childNodes) {
                if (hash == t) {
                    exist = true;
                    continue;
                }
            }
            if (!exist) {
                LinkedList<NodeModel<T>> oldChildNodes = new LinkedList<>(childNodes);
                start.childNodes.clear();
                start.childNodes.add(t);
                t.setChildNodes(oldChildNodes);
                for (NodeModel<T> child : oldChildNodes) {
                    child.setParentNode(t);
                }
            }
        }
    }

    public boolean removeNode(NodeModel<T> starNode, NodeModel<T> deleteNote) {
        boolean rm = false;
        int size = starNode.getChildNodes().size();
        if (size > 0) {
            //如果size>0，但是deleteNote参数又不是starNode参数的子节点呢？
            rm = starNode.getChildNodes().remove(deleteNote);
        }
        return rm;
    }

    public boolean inTree(NodeModel<T> starNode, NodeModel<T> deleteNote) {
        boolean in = false;
        Deque<NodeModel<T>> queue = new ArrayDeque<>();
        NodeModel<T> rootNode = starNode;
        queue.offer(rootNode);

        while (!queue.isEmpty()) {
//            rootNode = (NodeModel<T>) queue.poll();
            rootNode = queue.poll();
            if (rootNode == deleteNote) {
                in = true;
            }

            LinkedList<NodeModel<T>> childNodes = rootNode.getChildNodes();
            if (childNodes.size() > 0) {
                for (NodeModel<T> item : childNodes) {
                    queue.add(item);
                }
            }
        }
        return in;
    }

    public NodeModel<T> getRootNode() {
        return rootNode;
    }

    public void setRootNode(NodeModel<T> rootNode) {
        this.rootNode = rootNode;
    }

    /**
     * 同一个父节点的上下
     *
     * @param midPreNode
     * @return
     */
    private NodeModel<T> getLowNode(NodeModel<T> midPreNode) {
        NodeModel<T> find = null;
        NodeModel<T> parentNode = midPreNode.getParentNode();
    /*
    *    队列是一种特殊的线性表，它只允许在表的前端进行删除操作，而在表的后端进行插入操作。
    *    LinkedList类实现了Queue接口，因此我们可以把LinkedList当成Queue来用
    */
        if (parentNode != null && parentNode.getChildNodes().size() >= 2) {
            Deque<NodeModel<T>> queue = new ArrayDeque<>();
            NodeModel<T> rootNode = parentNode;
            //add()和remove()方法在失败的时候会抛出异常(不推荐)
            queue.offer(rootNode);
            boolean up = false;

            while (!queue.isEmpty()) {
                //返回第一个元素，并在队列中删除
                rootNode = queue.poll();
                if (up) {
                    if (rootNode.getFloor() == midPreNode.getFloor()) {
                        find = rootNode;
                    }
                    break;
                }

                //到了该元素
                if (rootNode == midPreNode) up = true;
                LinkedList<NodeModel<T>> childNodes = rootNode.getChildNodes();
                if (childNodes.size() > 0) {
                    for (NodeModel<T> item : childNodes) {
                        queue.offer(item);
                    }
                }
            }
        }
        return find;
    }

    private NodeModel<T> getPreNode(NodeModel<T> midPreNode) {

        NodeModel<T> parentNode = midPreNode.getParentNode();
        NodeModel<T> find = null;

        if (parentNode != null && parentNode.getChildNodes().size() > 0) {

            Deque<NodeModel<T>> queue = new ArrayDeque<>();
            NodeModel<T> rootNode = parentNode;
            queue.offer(rootNode);

            while (!queue.isEmpty()) {
                rootNode = (NodeModel<T>) queue.poll();
                //到了该元素
                if (rootNode == midPreNode) {
                    //返回之前的值
                    break;
                }

                find = rootNode;
                LinkedList<NodeModel<T>> childNodes = rootNode.getChildNodes();
                if (childNodes.size() > 0) {
                    for (NodeModel<T> item : childNodes) {
                        queue.offer(item);
                    }
                }
            }

            if (find != null && find.getFloor() != midPreNode.getFloor()) {
                find = null;
            }
        }
        return find;
    }

    public ArrayList<NodeModel<T>> getAllLowNodes(NodeModel<T> addNode) {
        ArrayList<NodeModel<T>> array = new ArrayList<>();
        NodeModel<T> parentNode = addNode.getParentNode();
        while (parentNode != null) {
            NodeModel<T> lowNode = getLowNode(parentNode);
            while (lowNode != null) {
                array.add(lowNode);
                lowNode = getLowNode(lowNode);
            }
            parentNode = parentNode.getParentNode();
        }
        return array;
    }

    public ArrayList<NodeModel<T>> getAllPreNodes(NodeModel<T> addNode) {
        ArrayList<NodeModel<T>> array = new ArrayList<>();
        NodeModel<T> parentNode = addNode.getParentNode();
        while (parentNode != null) {
            NodeModel<T> preNode = getPreNode(parentNode);
            while (preNode != null) {
                array.add(preNode);
                preNode = getPreNode(preNode);
            }
            parentNode = parentNode.getParentNode();
        }
        return array;
    }

    public LinkedList<NodeModel<T>> getNodeChildNodes(NodeModel<T> node) {
        return node.getChildNodes();
    }

    public void ergodicTreeInDeep(int msg) {
        Stack<NodeModel<T>> stack = new Stack<>();
        NodeModel<T> rootNode = getRootNode();
        stack.add(rootNode);
        while (!stack.isEmpty()) {
            NodeModel<T> pop = stack.pop();
            if (mForTreeItem != null) {
                mForTreeItem.next(msg, pop);
            }
            LinkedList<NodeModel<T>> childNodes = pop.getChildNodes();
            for (NodeModel<T> item : childNodes) {
                stack.add(item);
            }
        }
    }

    public void ergodicTreeInWith(int msg) {
        Deque<NodeModel<T>> queue = new ArrayDeque<>();
        NodeModel<T> rootNode = getRootNode();
        queue.add(rootNode);
        while (!queue.isEmpty()) {
            rootNode = (NodeModel<T>) queue.poll();
            if (mForTreeItem != null) {
                mForTreeItem.next(msg, rootNode);
            }
            LinkedList<NodeModel<T>> childNodes = rootNode.getChildNodes();
            if (childNodes.size() > 0) {
                for (NodeModel<T> item : childNodes) {
                    queue.add(item);
                }
            }
        }
    }

    public void addForTreeItem(ForTreeItem<NodeModel<T>> forTreeItem) {
        this.mForTreeItem = forTreeItem;
    }

    public Object deepClone() throws IOException, ClassNotFoundException {
        //实现输出流中的数据被写入一个字节数组
        ByteArrayOutputStream bo=new ByteArrayOutputStream();
        //写入基本数据类型和Java对象的图形到OutputStream.(这里是绑定了OutputStream bo)
        ObjectOutputStream oo=new ObjectOutputStream(bo);
        //将指定对象写入ObjectOutputStream
        oo.writeObject(this);

        //允许在内存中的缓冲区被作为一个InputStream。输入源是一个字节数组
        ByteArrayInputStream bi=new ByteArrayInputStream(bo.toByteArray());
        //恢复先前序列化的对象
        ObjectInputStream oi=new ObjectInputStream(bi);
        //从ObjectInputStream中读取对象
        return(oi.readObject());
    }



}
