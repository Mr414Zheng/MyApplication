package com.example.administrator.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.owant.thinkmap.AppConstants;
import com.owant.thinkmap.base.BaseActivity;
import com.owant.thinkmap.file.Conf;
import com.owant.thinkmap.file.OwantFileCreate;
import com.owant.thinkmap.model.NodeModel;
import com.owant.thinkmap.model.TreeModel;
import com.owant.thinkmap.ui.EditAlertDialog;
import com.owant.thinkmap.ui.SplashActivity;
import com.owant.thinkmap.ui.editmap.EditMapActivity;
import com.owant.thinkmap.ui.editmap.EditMapContract;
import com.owant.thinkmap.ui.editmap.EditMapPresenter;
import com.owant.thinkmap.util.AndroidUtil;
import com.owant.thinkmap.util.DensityUtils;
import com.owant.thinkmap.util.LOG;
import com.owant.thinkmap.view.RightTreeLayoutManager;
import com.owant.thinkmap.view.TreeView;
import com.owant.thinkmap.view.TreeViewItemClick;
import com.owant.thinkmap.view.TreeViewItemLongClick;
//import com.squareup.haha.perflib.Main;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
//
//
//        final NodeModel<String> nodeA = new NodeModel<>("剑灵");
//        final NodeModel<String> nodeB = new NodeModel<>("穿越火线");
//        final NodeModel<String> nodeC = new NodeModel<>("QQ飞车");
//        final NodeModel<String> nodeD = new NodeModel<>("QQ炫舞");
//        final NodeModel<String> nodeE = new NodeModel<>("QQ三国");
//        final NodeModel<String> nodeF = new NodeModel<>("地下城与勇士");
//        final NodeModel<String> nodeG = new NodeModel<>("英雄联盟");
//        final NodeModel<String> nodeH = new NodeModel<>("剑灵PC版");
//        final NodeModel<String> nodeI = new NodeModel<>("剑灵手游版");
//        final NodeModel<String> nodeJ = new NodeModel<>("冒险岛");
//        final NodeModel<String> nodeK = new NodeModel<>("龙之谷");
//        final NodeModel<String> nodeL = new NodeModel<>("传奇世界");
//        final NodeModel<String> nodeM = new NodeModel<>("热血传奇");
//        final NodeModel<String> nodeN = new NodeModel<>("永恒之塔");
//        final NodeModel<String> nodeO = new NodeModel<>("彩虹岛");
//        final NodeModel<String> nodeP = new NodeModel<>("我的世界");
//        final NodeModel<String> nodeQ = new NodeModel<>("守望先锋");
//        final NodeModel<String> nodeR = new NodeModel<>("炉石传说");
//        final NodeModel<String> nodeS = new NodeModel<>("天谕");
//        final NodeModel<String> nodeT = new NodeModel<>("风暴英雄");
//        final NodeModel<String> nodeU = new NodeModel<>("无尽战区");
//        final NodeModel<String> nodeV = new NodeModel<>("龙魂时刻");
//        final NodeModel<String> nodeW = new NodeModel<>("梦幻西游PC版");
//        final NodeModel<String> nodeX = new NodeModel<>("梦幻西游手游版");
//        final NodeModel<String> nodeY = new NodeModel<>("魔兽世界");
//        final NodeModel<String> nodeZ = new NodeModel<>("梦幻西游");
//        final NodeModel<String> node1 = new NodeModel<>("游戏运营商");
//        final NodeModel<String> node2 = new NodeModel<>("腾讯");
//        final NodeModel<String> node3 = new NodeModel<>("盛大");
//        final NodeModel<String> node4 = new NodeModel<>("网易");
//
//
//        final TreeModel<String> tree = new TreeModel<>(node1);
//        tree.addNode(node1, node2, node3, node4);
//        tree.addNode(node2, nodeA, nodeB, nodeC, nodeD, nodeE, nodeF, nodeG);
//        tree.addNode(node3, nodeJ, nodeK, nodeL, nodeM, nodeN, nodeO);
//        tree.addNode(node4, nodeP, nodeQ, nodeR, nodeS, nodeT, nodeU, nodeV, nodeY, nodeZ);
//        tree.addNode(nodeA, nodeH, nodeI);
//        tree.addNode(nodeZ, nodeW, nodeX);
//
//        int dx = DensityUtils.dp2px(this, 60);
//        int dy = DensityUtils.dp2px(this, 30);
//        int mHeight = DensityUtils.dp2px(this, 720);
//
//        TreeView testTreeView = new TreeView(this);
//        testTreeView.setTreeLayoutManager(new RightTreeLayoutManager(dx, dy, mHeight));
//        testTreeView.setTreeModel(tree);
//
//        setContentView(testTreeView);
//
//    }
//}


public class MainActivity extends BaseActivity implements EditMapContract.View {

    private final static String TAG = "EditMapActivity";
    private final static String tree_model = "tree_model";


    private String saveDefaultFilePath;
    private EditMapContract.Presenter mEditMapPresenter;

    private TreeView editMapTreeView;
    private Button btnAddSub;
    private Button btnAddNode;
    private Button btnFocusMid;
    private Button btnCodeMode;
    private Button movableBtn;

    private EditAlertDialog addSubNodeDialog = null;
    private EditAlertDialog addNodeDialog = null;
    private EditAlertDialog editNodeDialog = null;
    private EditAlertDialog saveFileDialog = null;

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

        editMapTreeView = (TreeView) findViewById(com.owant.thinkmap.R.id.edit_map_tree_view);
        btnAddSub = (Button) findViewById(com.owant.thinkmap.R.id.btn_add_sub);
        btnAddNode = (Button) findViewById(com.owant.thinkmap.R.id.btn_add_node);
        btnFocusMid = (Button) findViewById(com.owant.thinkmap.R.id.btn_focus_mid);
        btnCodeMode = (Button) findViewById(com.owant.thinkmap.R.id.btn_code_mode);
        movableBtn = (Button) findViewById(R.id.movableBtn);

    }

    @Override
    protected void onBaseBindView() {
        bindViews();

        btnAddNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditMapPresenter.addSubNoteSecond();
            }
        });

        btnAddSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditMapPresenter.addSubNoteFirst();
            }
        });

        btnFocusMid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditMapPresenter.focusMid();
            }
        });

        btnCodeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO 添加命令模式指令
                Toast.makeText(MainActivity.this,
                        "This feature is in development...",
                        Toast.LENGTH_SHORT).show();
            }
        });


        int dx = DensityUtils.dp2px(getApplicationContext(), 80);
        int dy = DensityUtils.dp2px(getApplicationContext(), 80);
        int screenHeight = DensityUtils.dp2px(getApplicationContext(), 720);
        editMapTreeView.setTreeLayoutManager(new RightTreeLayoutManager(dx, dy, screenHeight));

        editMapTreeView.setTreeViewItemLongClick(new TreeViewItemLongClick() {
            @Override
            public void onLongClick(View view) {
                mEditMapPresenter.editNote();
            }
        });

        editMapTreeView.setTreeViewItemClick(new TreeViewItemClick() {
            @Override
            public void onItemClick(View item) {

            }
        });

        DisplayMetrics dm = getResources().getDisplayMetrics();
        final int screenWidth = dm.widthPixels;
        final int screenHeight1 = dm.heightPixels - 50;
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

        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
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
    public void showAddSubNoteDialogSecond() {
        if (editMapTreeView.getCurrentFocusNode().getParentNode() == null) {
            Toast.makeText(this, getString(com.owant.thinkmap.R.string.cannot_add_second_node), Toast.LENGTH_SHORT).show();
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
                    if (addNodeDialog != null && addNodeDialog.isShowing())
                        addNodeDialog.dismiss();
                }
            });
            addNodeDialog.show();
        } else {
            addNodeDialog.clearInput();
            addNodeDialog.show();
        }
    }

    @Override
    public void showAddSubNoteDialogFirst() {
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
                    value = getString(com.owant.thinkmap.R.string.transformer);
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

                mEditMapPresenter.doSaveFile(value);

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
        mEditMapPresenter.onRecycle();
        super.onDestroy();
    }
}