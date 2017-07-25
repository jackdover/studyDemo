package com.dover.newapp.base;

/**
 * Created by d on 2017/7/24.
 */
public interface IBaseView {

    /**
     * 显示加载中页面
     */
    void showLoadingView();

    /**
     * 显示错误页面
     */
    void showErrorView();

    /**
     * 显示空页面
     */
    void showEmptyView();


    /**
     * 显示进度条
     *
     * @param flag    是否可取消
     * @param message 要显示的信息
     */
    void showProgress(boolean flag, String message);

    /**
     * 隐藏进度条
     */
    void hideProgress();

    /**
     * 根据字符串弹出toast
     *
     * @param msg 提示内容
     */
    void showToast(String msg);

    /**
     * 关联RxLifecycle
     *
     * @param <T>
     * @return
     */
//    <T> LifecycleTransformer<T> bind();

}
