package com.example.administrator.myapplication;

import com.owant.thinkmap.model.NodeModel;

import org.litepal.crud.DataSupport;

import java.util.LinkedList;

/**
 * Created by Administrator on 2017/11/27.
 */

public class Node<T> extends DataSupport {

    private String value;

    private float left;

    private float right;

    private float top;

    private float bottom;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }
}
