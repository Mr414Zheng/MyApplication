package com.example.administrator.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.owant.thinkmap.AppConstants;
import com.owant.thinkmap.base.BaseActivity;
import com.owant.thinkmap.model.NodeModel;
import com.owant.thinkmap.model.TreeModel;
import com.owant.thinkmap.ui.EditAlertDialog;
import com.owant.thinkmap.ui.editmap.EditMapContract;
import com.owant.thinkmap.ui.editmap.EditMapPresenter;
import com.owant.thinkmap.util.AndroidUtil;
import com.owant.thinkmap.util.DensityUtils;
import com.owant.thinkmap.util.LOG;
import com.owant.thinkmap.view.RightTreeLayoutManager;
import com.owant.thinkmap.view.TreeView;
import com.owant.thinkmap.view.TreeViewItemClick;
import com.owant.thinkmap.view.TreeViewItemLongClick;


import java.io.File;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends BaseActivity implements EditMapContract.View {

    private final static String TAG = "EditMapActivity";
    private final static String tree_model = "tree_model";
    private final static int SHOW_MESSAGE = 1;
    private static ArrayList tempList = new ArrayList();
    private HashSet hashSet = new HashSet(30);
    private ArrayList list = new ArrayList();
    private String saveDefaultFilePath;
    private String DeviceID;
//    private ConnectSocket connectSocket = new ConnectSocket();

    private int i;
    private Timer mTimer;
    private TimerTask mTimerTask;

    private EditMapContract.Presenter mEditMapPresenter;
    private TreeView editMapTreeView;
    private Button btnAddSub;
    private Button btnAddNode;
    private Button btnFocusMid;
    private Button btnInsertSub;
    //    private Button btnInsertMain;
    private Button movableBtn;
    //    private Button saveData;
    private TextView textMsg;

    private EditAlertDialog addSubNodeDialog = null;
    private EditAlertDialog addNodeDialog = null;
    private EditAlertDialog insertSubNodeDialog = null;
    private EditAlertDialog insertMainNodeDialog = null;
    private EditAlertDialog editNodeDialog = null;
    private EditAlertDialog saveFileDialog = null;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_MESSAGE:
//                    if (i >= 3){
//                        mTimerTask.cancel();
//                    }
//                    String DeviceID = connectSocket.getDeviceID();
//                    Log.e("设备编号：", "————————————" + DeviceID);
//                    String ldz = connectSocket.getLdz();
//                    Log.e(" 漏电值: ", "——————————————" + ldz);
//                    String fdz = connectSocket.getFdz();
//                    Log.e(" 漏电阈值: ", "——————————" + fdz);
//                    textMsg.setText("设备编号: " + DeviceID + " 漏电值: " + ldz + " 漏电阈值: " + fdz);
//                    Log.e("i = ", ""+i);
                    break;
                default:
            }
        }
    };

    @Override
    protected void onBaseIntent() {

    }

    @Override
    protected void onBasePreLayout() {

    }

    @Override
    protected int onBaseLayoutId(@Nullable Bundle savedInstanceState) {
        return com.owant.thinkmap.R.layout.activity_edit_think_map;
    }

    public void bindViews() {

        editMapTreeView = (TreeView) findViewById(R.id.edit_map_tree_view);
        btnAddSub = (Button) findViewById(R.id.btn_add_sub);
        btnAddNode = (Button) findViewById(R.id.btn_add_node);
        btnFocusMid = (Button) findViewById(R.id.btn_focus_mid);
        btnInsertSub = (Button) findViewById(R.id.btn_code_mode);
//        btnInsertMain = (Button) findViewById(R.id.btn_add_main);
        //解除警报按钮
        movableBtn = (Button) findViewById(R.id.movableBtn);
//        saveData = (Button) findViewById(R.id.saveData);
        textMsg = (TextView) findViewById(R.id.textMsg);

    }

    /**
     * @param deviceNumber 此为传入的设备编号，用于告知哪个设备报警
     */
    public void warning(String deviceNumber) {
        editMapTreeView.nodeViewWarning(editMapTreeView, deviceNumber);
    }

//    public void startTimer() {
//        if (mTimer != null) {
//            if (mTimerTask != null) {
//                mTimerTask.cancel();
//            }
//        }
//        mTimer = new Timer();
//        mTimerTask = new TimerTask() {
//            @Override
//            public void run() {
//                Message message = new Message();
//                message.what = SHOW_MESSAGE;
//                handler.sendMessage(message);
//                i++;
//            }
//        };
//        mTimer.schedule(mTimerTask, 1000, 1000);
//    }

    @Override
    protected void onBaseBindView() {
        bindViews();

//        connectSocket.Connect_socket();

        btnAddNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditMapPresenter.addNote();
            }
        });

        btnAddSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditMapPresenter.addSubNote();
            }
        });

        btnFocusMid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditMapPresenter.focusMid();
            }
        });

        btnInsertSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditMapPresenter.insertSubNote();
            }
        });

//        btnInsertMain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditMapPresenter.insertMainNote();
//            }
//        });

        int dx = DensityUtils.dp2px(getApplicationContext(), 40);
        int dy = DensityUtils.dp2px(getApplicationContext(), 40);
        int screenHeight = DensityUtils.dp2px(getApplicationContext(), 720);
        editMapTreeView.setTreeLayoutManager(new RightTreeLayoutManager(dx, dy, screenHeight));

//        Bundle extras = getIntent().getExtras();
//        hashSet =(HashSet) extras.getSerializable("id");
//        if (hashSet != null) {
//            for (Object hashEle : hashSet) {
//                String element = hashEle.toString();
//                element = element.replaceAll("\\D","");
//                list.add(element);
//            }
//        }

        editMapTreeView.setTreeViewItemLongClick(new TreeViewItemLongClick() {
            @Override
            public void onLongClick(View view) {
                mEditMapPresenter.editNote();
            }
        });

//        editMapTreeView.setTreeViewItemClick(new TreeViewItemClick() {
//            @Override
//            public void onItemClick(View item) {
//                i = 0;
//                DeviceID = getCurrentFocusNode().value;
//                if (DeviceID.matches("[0-9]{8}")) {
//                    connectSocket.setDeviceID(DeviceID);
//                    try {
//                        connectSocket.getSocket().sendUrgentData(0xFF);
//                        connectSocket.leakquery();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        connectSocket.leakquery();
//                    }
////                    startTimer();
//                } else {
//                    textMsg.setText("设备编号: " + getCurrentFocusNode().getValue());
//                    Toast.makeText(MainActivity.this, "该设备编号不合法", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        DisplayMetrics dm = getResources().getDisplayMetrics();
        final int screenWidth = dm.widthPixels;
        final int screenHeight1 = dm.heightPixels - 50;

        /**
         * warning测试按钮的点击事件
         */
        movableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NodeModel<String> rootNode = editMapTreeView.getTreeModel().getRootNode();
                Deque<NodeModel<String>> deque = new ArrayDeque<>();
                deque.offer(rootNode);
                while (!deque.isEmpty()) {
                    NodeModel<String> poll = deque.poll();
                    editMapTreeView.findNodeViewFromNodeModel(poll).setBackgroundResource(R.drawable.node_view_bg);
                    LinkedList<NodeModel<String>> childNodes = poll.getChildNodes();
                    for (NodeModel<String> childNode : childNodes) {
                        deque.offer(childNode);
                    }
                }
                tempList.clear();
            }
        });
        movableBtn.setOnTouchListener(new View.OnTouchListener() {
            int lastX, lastY; //记录移动的最后的位置

            public boolean onTouch(View v, MotionEvent event) {
                //获取Action
                int ea = event.getAction();
                Log.i("TAG", "Touch:" + ea);
                switch (ea) {
                    case MotionEvent.ACTION_DOWN:   //按下
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:  //移动
                        //移动中动态设置位置
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        int left = v.getLeft() + dx;
                        int top = v.getTop() + dy;
                        int right = v.getRight() + dx;
                        int bottom = v.getBottom() + dy;
                        if (left < 0) {
                            left = 0;
                            right = left + v.getWidth();
                        }
                        if (right > screenWidth) {
                            right = screenWidth;
                            left = right - v.getWidth();
                        }
                        if (top < 0) {
                            top = 0;
                            bottom = top + v.getHeight();
                        }
                        if (bottom > screenHeight1) {
                            bottom = screenHeight1;
                            top = bottom - v.getHeight();
                        }
                        v.layout(left, top, right, bottom);
                        //将当前的位置再次设置
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:   //脱离
                        break;
                }
                return false;
            }
        });

        initPresenter();
        //TODO 需要进入文件时对焦中心
        tempList.addAll(list);
        for (int i = 0; i < tempList.size(); i++) {
            NodeModel<String> rootNode = editMapTreeView.getTreeModel().getRootNode();
            Deque<NodeModel<String>> deque = new ArrayDeque<>();
            deque.offer(rootNode);
            while (!deque.isEmpty()) {
                NodeModel<String> poll = deque.poll();
                if (poll.getValue().equals(tempList.get(i))) {
                    editMapTreeView.findNodeViewFromNodeModel(poll).setBackgroundResource(R.drawable.node_view_br);
                } else {
                    LinkedList<NodeModel<String>> childNodes = poll.getChildNodes();
                    for (NodeModel<String> childNode : childNodes) {
                        deque.offer(childNode);
                    }
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(tree_model, mEditMapPresenter.getTreeModel());
        Log.i(TAG, "onSaveInstanceState: 保持数据");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Serializable saveZable = savedInstanceState.getSerializable(tree_model);
        mEditMapPresenter.setTreeModel((TreeModel<String>) saveZable);
    }

    private void initPresenter() {
        //presenter层关联的View
        mEditMapPresenter = new EditMapPresenter(this);
        mEditMapPresenter.start();

        Intent intent = new Intent();
        intent.setData(Uri.parse(Environment.getExternalStorageDirectory().getPath() +
                AppConstants.owant_maps + "保存成功.owant"));
        Uri data = intent.getData();
        File file = new File(data.toString());
        if (file.exists()) {
            final String path = data.getPath();
            //加载owant的文件路径
            presenterSetLoadMapPath(path);
            //解析owant文件
            mEditMapPresenter.readOwantFile();
        } else {
            mEditMapPresenter.createDefaultTreeModel();
        }
    }

    private void presenterSetLoadMapPath(String path) {
        mEditMapPresenter.setLoadMapPath(path);
    }

    @Override
    protected void onLoadData() {
    }

    @Override
    public void setPresenter(EditMapContract.Presenter presenter) {
        if (mEditMapPresenter == null) {
            mEditMapPresenter = presenter;
        }
    }

    @Override
    public void showLoadingFile() {

    }

    @Override
    public void setTreeViewData(TreeModel<String> treeModel) {
        editMapTreeView.setTreeModel(treeModel);
    }

    @Override
    public void hideLoadingFile() {

    }

    @Override
    public void showInsertMainNoteDialog() {
        if (insertMainNodeDialog == null) {
            LayoutInflater factory = LayoutInflater.from(this);
            View inflate = factory.inflate(com.owant.thinkmap.R.layout.dialog_edit_input, null);
            insertMainNodeDialog = new EditAlertDialog(MainActivity.this);
            insertMainNodeDialog.setView(inflate);
            insertMainNodeDialog.setDivTitle(getString(com.owant.thinkmap.R.string.tab_add_main));
            insertMainNodeDialog.addEnterCallBack(new EditAlertDialog.EnterCallBack() {
                @Override
                public void onEdit(String value) {
                    if (TextUtils.isEmpty(value)) {
                        value = getString(com.owant.thinkmap.R.string.tab_add_main);
                    }
                    editMapTreeView.insertMainNode(value);
                    clearDialog(insertMainNodeDialog);
                    if (insertMainNodeDialog != null && insertMainNodeDialog.isShowing())
                        insertMainNodeDialog.dismiss();
                }
            });
            insertMainNodeDialog.show();
        } else {
            insertMainNodeDialog.clearInput();
            insertMainNodeDialog.show();
        }
    }

    @Override
    public void showInsertSubNoteDialog() {
        if (insertSubNodeDialog == null) {
            LayoutInflater factory = LayoutInflater.from(this);
            View inflate = factory.inflate(com.owant.thinkmap.R.layout.dialog_edit_input, null);
            insertSubNodeDialog = new EditAlertDialog(MainActivity.this);
            insertSubNodeDialog.setView(inflate);
            insertSubNodeDialog.setDivTitle(getString(com.owant.thinkmap.R.string.add_third_node));
            insertSubNodeDialog.addEnterCallBack(new EditAlertDialog.EnterCallBack() {
                @Override
                public void onEdit(String value) {
                    if (TextUtils.isEmpty(value)) {
                        value = getString(com.owant.thinkmap.R.string.tab_code_mode);
                    }
                    editMapTreeView.insertSubNode(value);
                    clearDialog(insertSubNodeDialog);
                    if (insertSubNodeDialog != null && insertSubNodeDialog.isShowing()) insertSubNodeDialog.dismiss();
                }
            });
            insertSubNodeDialog.show();
        } else {
            insertSubNodeDialog.clearInput();
            insertSubNodeDialog.show();
        }
    }

    @Override
    public void showAddNoteDialog() {
        if (editMapTreeView.getCurrentFocusNode().getParentNode() == null) {
            Toast.makeText(this, getString(com.owant.thinkmap.R.string.cannot_add_second_node), Toast.LENGTH_SHORT)
                    .show();
        } else if (addNodeDialog == null) {
            LayoutInflater factory = LayoutInflater.from(this);
            View inflate = factory.inflate(com.owant.thinkmap.R.layout.dialog_edit_input, null);
            addNodeDialog = new EditAlertDialog(MainActivity.this);
            addNodeDialog.setView(inflate);
            addNodeDialog.setDivTitle(getString(com.owant.thinkmap.R.string.add_second_node));
            addNodeDialog.addEnterCallBack(new EditAlertDialog.EnterCallBack() {
                @Override
                public void onEdit(String value) {
                    if (TextUtils.isEmpty(value)) {
                        value = getString(com.owant.thinkmap.R.string.second_node);
                    }
                    editMapTreeView.addNode(value);
                    clearDialog(addNodeDialog);
                    if (addNodeDialog != null && addNodeDialog.isShowing()) addNodeDialog.dismiss();
                }
            });
            addNodeDialog.show();
        } else {
            addNodeDialog.clearInput();
            addNodeDialog.show();
        }
    }

    @Override
    public void showAddSubNoteDialog() {
        if (addSubNodeDialog == null) {
            LayoutInflater factory = LayoutInflater.from(this);
            View inflate = factory.inflate(com.owant.thinkmap.R.layout.dialog_edit_input, null);
            addSubNodeDialog = new EditAlertDialog(this);
            addSubNodeDialog.setView(inflate);
            addSubNodeDialog.setDivTitle(getString(com.owant.thinkmap.R.string.add_first_node));
            addSubNodeDialog.addEnterCallBack(new EditAlertDialog.EnterCallBack() {
                @Override
                public void onEdit(String value) {
                    if (TextUtils.isEmpty(value)) {
                        value = getString(com.owant.thinkmap.R.string.first_node);
                    }
                    editMapTreeView.addSubNode(value);
                    clearDialog(addSubNodeDialog);
                }
            });
            addSubNodeDialog.show();
        } else {
            addSubNodeDialog.clearInput();
            addSubNodeDialog.show();
        }
    }

    @Override
    public void showEditNoteDialog() {
        if (editNodeDialog == null) {
            LayoutInflater factory = LayoutInflater.from(this);
            View view = factory.inflate(com.owant.thinkmap.R.layout.dialog_edit_input, null);
            editNodeDialog = new EditAlertDialog(this);
            editNodeDialog.setView(view);
            editNodeDialog.setDivTitle(getString(com.owant.thinkmap.R.string.edit_node));
        }

        editNodeDialog.setNodeModel(getCurrentFocusNode());
        editNodeDialog.setInput(getCurrentFocusNode().getValue());
        editNodeDialog.addDeleteCallBack(new EditAlertDialog.DeleteCallBack() {
            @Override
            public void onDeleteModel(NodeModel<String> model) {
                try {
                    editMapTreeView.deleteNode(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDelete() {
            }
        });

        editNodeDialog.addEnterCallBack(new EditAlertDialog.EnterCallBack() {
            @Override
            public void onEdit(String value) {
                if (TextUtils.isEmpty(value)) {
                    value = getString(R.string.save_success);
                }
                editMapTreeView.changeNodeValue(getCurrentFocusNode(), value);
                clearDialog(editNodeDialog);
            }
        });
        editNodeDialog.show();
    }

    @Override
    public void showSaveFileDialog(String fileName) {
        if (saveFileDialog == null) {
            LayoutInflater factory = LayoutInflater.from(this);
            View view = factory.inflate(com.owant.thinkmap.R.layout.dialog_edit_input, null);
            saveFileDialog = new EditAlertDialog(this);
            saveFileDialog.setView(view);
            saveFileDialog.setDivTitle(getString(com.owant.thinkmap.R.string.save_file));
        }
        //如果是编辑文本时可能已经有文件名了，需要进行读取文件的名字
        saveFileDialog.setInput(mEditMapPresenter.getSaveInput());
        saveFileDialog.setConditionDeleteTextValue(getString(com.owant.thinkmap.R.string.exit_edit));

        //获取文件目录下的已经存在的文件集合
        saveFileDialog.setCheckLists(mEditMapPresenter.getOwantLists());

        saveFileDialog.addEnterCallBack(new EditAlertDialog.EnterCallBack() {
            @Override
            public void onEdit(String value) {

                mEditMapPresenter.doSaveFile("保存成功");


                //退出文件
                clearDialog(saveFileDialog);
//                Intent intent=new Intent(EditMapActivity.this,WorkSpaceActivity.class);
//                startActivityForResult(intent,WorkSpaceActivity.result_msg);

                MainActivity.this.finish();
            }
        });

        saveFileDialog.addDeleteCallBack(new EditAlertDialog.DeleteCallBack() {
            @Override
            public void onDeleteModel(NodeModel<String> nodeModel) {

            }

            @Override
            public void onDelete() {
                MainActivity.this.finish();
            }
        });
        saveFileDialog.show();
    }

    @Override
    public void focusingMid() {
        editMapTreeView.focusMidLocation();
    }

    @Override
    public String getDefaultPlanStr() {
        return getString(com.owant.thinkmap.R.string.transformer);
    }

    @Override
    public NodeModel<String> getCurrentFocusNode() {
        return editMapTreeView.getCurrentFocusNode();
    }

    @Override
    public String getDefaultSaveFilePath() {
        saveDefaultFilePath = Environment.getExternalStorageDirectory().getPath() + AppConstants.owant_maps;
        LOG.jLogi("saveDefaultFilePath=%s", saveDefaultFilePath);
        return saveDefaultFilePath;
    }

    @Override
    public String getAppVersion() {
        return AndroidUtil.getAppVersion(getApplicationContext());
    }

    @Override
    public void finishActivity() {
        Log.e("finishActivity", "");
        MainActivity.this.finish();
    }

    private void clearDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //TODO 判断一下文本是否改变了
            mEditMapPresenter.saveFile();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        Log.e("onDestroy", "");
        mEditMapPresenter.onRecycle();
        super.onDestroy();
    }
}