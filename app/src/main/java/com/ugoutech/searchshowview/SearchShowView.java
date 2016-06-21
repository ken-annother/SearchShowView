package com.ugoutech.searchshowview;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.HashMap;


/**
 * @描述: 搜索框带头像展示
 * @项目名: SearchShowView
 * @包名: com.ugoutech.searchshowview
 * @类名:
 * @作者: soongkun
 * @创建时间: 2016/4/29 13:16
 */

public class SearchShowView extends LinearLayout {

    private HashMap<String, View> views = new HashMap<>();

    private View mRootView;
    private LinearLayout mLlContainer;
    private HorizontalScrollView mHs;
    private EditText mEt;
    private ImageView mLeftEditIv;
    private IconRemoveListener mListener;

    //标记待删除的child
    private View markedChild;
    private boolean mChangedNull;

    private TextWatcher mTextWatcher;


    //屏幕的宽度
    private int disPlayWidth;





    public SearchShowView(Context context) {
        this(context, null);
    }


    public SearchShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public SearchShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    /**
     * 初始化视图
     *
     * @param context
     */
    private void initView(Context context) {

        disPlayWidth = getResources().getDisplayMetrics().widthPixels;

        mRootView = View.inflate(context, R.layout.search_show_view, this);
        mLlContainer = (LinearLayout) mRootView.findViewById(R.id.search_show_view_container);
        mHs = (HorizontalScrollView) mRootView.findViewById(R.id.search_show_view_hs);
        mEt = (EditText) mRootView.findViewById(R.id.search_show_view_et);
        mLeftEditIv = (ImageView) mRootView.findViewById(R.id.search_show_view_edit_iv);

        initListener();
    }


    private int num = 0;


    private void initListener() {
        mEt.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                num++;
                if (num == 2) {
                    num = 0;

                    Log.d(SearchShowView.this.getClass().getSimpleName(), "按键为" + keyCode);

                    if (keyCode == KeyEvent.KEYCODE_DEL && TextUtils.isEmpty(mEt.getText().toString()) && views.size
                            () !=0 && !mChangedNull) {    //删除按钮

                        Log.d(SearchShowView.this.getClass().getSimpleName(), "setOnKeyListener事件　删除键被触发了");

                        //最后一个孩子view
                        View lastChild = mLlContainer.getChildAt(mLlContainer.getChildCount() - 1);

                        if (markedChild == lastChild) {
                            removeViewEvent((String) markedChild.getTag());
                        } else {  //markedChild == null
                            markedChild = lastChild;
                            lastChild.setAlpha(0.5f);
                        }

                        return true;
                    }
                }

                mChangedNull = false;
                return false;
            }
        });


        mEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(mTextWatcher !=null){
                    mTextWatcher.beforeTextChanged(s,start,count,after);
                }
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty() && markedChild !=null){
                    markedChild.setAlpha(1.0f);
                    markedChild = null;
                }

                mChangedNull = true;

                if(mTextWatcher !=null){
                    mTextWatcher.onTextChanged(s,start,before,count);
                }
            }


            @Override
            public void afterTextChanged(Editable s) {
                if(mTextWatcher !=null){
                    mTextWatcher.afterTextChanged(s);
                }
            }
        });
    }


    /**
     * 添加某个view
     *
     * @param view
     * @param tag
     * @return 如果添加成功则返回true 　如果已经存在了　返回　false
     */
    public boolean addIconView(View view, final String tag) {

        if(markedChild !=null){
            markedChild.setAlpha(1.0f);
            markedChild = null;
        }

        if (views.containsKey(tag)) {
            return false;
        }


        //将要添加的view绑定tag
        view.setTag(tag);
        //加入到map中
        views.put(tag, view);
        //添加到布局容器中展示
        mLlContainer.addView(view);

        mLlContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                //最后一个显示最新的一个
                mHs.smoothScrollTo(mLlContainer.getMeasuredWidth(), 0);

                mLlContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            }
        });


        if (views.size() > 0) {
            mLeftEditIv.setVisibility(View.GONE);
        }

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                removeViewEvent(tag);

                if (mListener != null) {
                    mListener.onIconRemoved(SearchShowView.this, tag);
                }
            }
        });

        return true;
    }


    /**
     * 删除某个view
     *
     * @param tag
     * @return 如果删除成功则返回true 　如果已经存在了　返回　false
     */
    public boolean removeIconView(String tag) {
        View view = views.get(tag);

        if (view == null) {
            return false;
        }

        removeViewEvent(tag);

        return true;
    }


    /**
     * 删除view的动作
     *
     * @param tag view的标记
     */
    private void removeViewEvent(String tag) {

        Log.d(this.getClass().getSimpleName(), "removeViewEvent删除执行之前");

        View view = views.get(tag);

        if (markedChild == view) {        //将标记的孩子置空
            markedChild = null;
        }

        mLlContainer.removeView(view);

        views.remove(tag);

        Log.d(this.getClass().getSimpleName(), "removeViewEvent删除执行之后");

        Log.d(this.getClass().getSimpleName(), "removeViewEvent删除执行之后，Map的大小" + views.size());
        if (views.size() == 0) {
            mLeftEditIv.setVisibility(View.VISIBLE);
        }
    }


    public interface IconRemoveListener {
        void onIconRemoved(View v, String tag);
    }


    /**
     * 设置时间监听
     *
     * @param listener
     */
    public void setOnIconRemoveListener(IconRemoveListener listener) {
        this.mListener = listener;
    }


    public void addTextChangedListener(TextWatcher watcher){
        this.mTextWatcher = watcher;
    }


    /**
     * 获得当前孩子的个数
     * @return
     */
    public int getChildViewCount(){
       return mLlContainer.getChildCount();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        Log.d(getClass().getSimpleName(),"onLayout");
    }
}
