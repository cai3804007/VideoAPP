package com.example.videoapp.fragenmt;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.videoapp.R;
import com.example.videoapp.avtivity.MyCollectionActivity;

import butterknife.BindView;
import butterknife.OnClick;
import skin.support.SkinCompatManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends BaseFragenmt {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.mineheader)
    ImageView mineheader;
    @BindView(R.id.namelabel)
    TextView namelabel;
    @BindView(R.id.niblabel)
    TextView niblabel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_my;
//        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.mineheader)
    public void onClick() {
    }

    @OnClick({R.id.mineheader, R.id.yuedu, R.id.dianzan, R.id.pinglun, R.id.fenxiang, R.id.shoucang, R.id.huanfu, R.id.tuichu, R.id.recLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mineheader:
                showToast("点击头像");
                break;
            case R.id.yuedu:
                break;
            case R.id.dianzan:
                break;
            case R.id.pinglun:
                break;
            case R.id.fenxiang:
                break;
            case R.id.shoucang:
                navigateTo(MyCollectionActivity.class);
                break;
            case R.id.huanfu:
                String skin = findByKey("skin");
                if (skin.equals("night")) {
                    // 恢复应用默认皮肤
                    SkinCompatManager.getInstance().restoreDefaultTheme();
                    insertVal("skin", "default");
                } else {
                    SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // 后缀加载
                    insertVal("skin", "night");
                }

                break;
            case R.id.tuichu:
                break;
            case R.id.recLayout:
                break;
        }
    }
}