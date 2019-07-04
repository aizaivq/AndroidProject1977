package com.nick.baseapp.base.camera;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nick.baseapp.R;

import java.util.ArrayList;
import java.util.List;

public class CameraParamList extends RecyclerView implements View.OnClickListener {
    private List<MyData> mDatas = new ArrayList<>();
    private OnClickListener mItemClickListener;
    private static final String TAG = CameraParamList.class.getSimpleName();

    public CameraParamList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setAdapter(new MyAdapter());
        setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

    }

    public void init(List<MyData> datas, OnClickListener onClickListener) {
        mDatas.clear();
        mDatas.addAll(datas);
        mItemClickListener = onClickListener;
        getAdapter().notifyDataSetChanged();

    }

    private class MyAdapter extends Adapter<MyViewHolder> {
        public MyAdapter() {
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_camera_param, null));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.mFLItem.setTag(position);
            holder.mFLItem.setOnClickListener(CameraParamList.this);
            holder.mTVTitle.setText(mDatas.get(position).mTitle);
            if (mDatas.get(position).mIsCheck) {
                holder.mTVTitle.setTextColor(Color.BLACK);
            } else {
                holder.mTVTitle.setTextColor(Color.WHITE);
            }
        }

        @Override
        public int getItemCount() {
            return mDatas != null ? mDatas.size() : 0;
        }
    }


    private class MyViewHolder extends ViewHolder {
        private FrameLayout mFLItem;
        private TextView mTVTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            mFLItem = itemView.findViewById(R.id.mFLItem);
            mTVTitle = itemView.findViewById(R.id.mTVTitle);
        }
    }

    public static class MyData {
        public String mTitle;
        public boolean mIsCheck;

        public MyData(String mTitle, boolean mIsCheck) {
            this.mTitle = mTitle;
            this.mIsCheck = mIsCheck;
        }
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        for (int i = 0; i < mDatas.size(); i++) {
            mDatas.get(i).mIsCheck = false;
        }
        mDatas.get(position).mIsCheck = true;

        getAdapter().notifyDataSetChanged();
        mItemClickListener.onClick(v);
    }
}
