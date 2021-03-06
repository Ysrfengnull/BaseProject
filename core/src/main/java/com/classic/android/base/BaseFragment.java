package com.classic.android.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.classic.android.event.FragmentEvent;
import com.classic.android.interfaces.IFragment;
import com.classic.android.interfaces.IRegister;
import com.classic.android.permissions.EasyPermissions;
import com.classic.android.utils.SharedPreferencesUtil;

import java.util.List;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.base
 *
 * 文件描述: Fragment父类
 * 创 建 人: 续写经典
 * 创建时间: 2015/12/16 18:34
 */
@SuppressWarnings("unused") public abstract class BaseFragment extends Fragment
        implements IFragment, IRegister, View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private static final String SP_NAME = "firstConfig";
    private static final String STATE_IS_HIDDEN = "isHidden";

    private int mFragmentState;
    protected Activity mActivity;
    protected Context mAppContext;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        stateChange(FragmentEvent.ATTACH);
        mAppContext = context.getApplicationContext();
        mActivity = (Activity)context;
    }

    @Override public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_IS_HIDDEN, isHidden());
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateChange(FragmentEvent.CREATE);
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater,
                                       @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {
        stateChange(FragmentEvent.CREATE_VIEW);
        View parentView = inflater.inflate(getLayoutResId(), container, false);
        SharedPreferencesUtil spUtil = new SharedPreferencesUtil(mActivity, SP_NAME);
        final String simpleName = this.getClass().getSimpleName();
        if (spUtil.getBooleanValue(simpleName, true)) {
            onFirst();
            spUtil.putBooleanValue(simpleName, false);
        }
        initData();
        initView(parentView, savedInstanceState);
        return parentView;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stateChange(FragmentEvent.VIEW_CREATE);
        if (savedInstanceState != null) {
            boolean isHidden = savedInstanceState.getBoolean(STATE_IS_HIDDEN);
            FragmentManager fm = getFragmentManager();
            if (null == fm) {
                return;
            }
            FragmentTransaction transaction = fm.beginTransaction();
            if (isHidden) {
                transaction.hide(this);
                onFragmentHide();
            } else {
                transaction.show(this);
                onFragmentShow();
            }
            transaction.commitAllowingStateLoss();
        }
    }

    @Override public void onStart() {
        super.onStart();
        stateChange(FragmentEvent.START);
    }

    @Override public void onResume() {
        super.onResume();
        stateChange(FragmentEvent.RESUME);
        register();
    }

    @Override public void onPause() {
        stateChange(FragmentEvent.PAUSE);
        super.onPause();
        unRegister();
    }

    @Override public void onStop() {
        stateChange(FragmentEvent.STOP);
        super.onStop();
    }

    @Override public void onDestroyView() {
        stateChange(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        stateChange(FragmentEvent.DESTROY);
        super.onDestroy();
    }

    @Override public void onDetach() {
        stateChange(FragmentEvent.DETACH);
        super.onDetach();
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override public void onFirst() { }

    @Override public void initData() { }

    @Override public void initView(@NonNull View parentView, @Nullable Bundle savedInstanceState) { }

    @Override public void register() { }

    @Override public void unRegister() { }

    @Override public void onFragmentShow() { }

    @Override public void onFragmentHide() { }

    @Override public void viewClick(@NonNull View v) { }

    @Override public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) { }

    @Override public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) { }

    @Override public void onClick(View v) {
        viewClick(v);
    }

    /**
     * 获取当前Fragment状态
     * {@link FragmentEvent#ATTACH},
     * {@link FragmentEvent#CREATE},
     * {@link FragmentEvent#CREATE_VIEW},
     * {@link FragmentEvent#VIEW_CREATE},
     * {@link FragmentEvent#START},
     * {@link FragmentEvent#RESUME},
     * {@link FragmentEvent#PAUSE},
     * {@link FragmentEvent#STOP},
     * {@link FragmentEvent#DESTROY_VIEW},
     * {@link FragmentEvent#DESTROY},
     * {@link FragmentEvent#DETACH}.
     */
    public int getFragmentState() {
        return mFragmentState;
    }

    void stateChange(@FragmentEvent int event) {
        mFragmentState = event;
    }
}
