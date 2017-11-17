package com.albert.uitl;

/**
 * Created by Albert on 2017/9/28 0028.
 */

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.albert.activity.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zdd on 2016/10/18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<InfoVO> mInfoVOList = new ArrayList<>();


    public RecyclerViewAdapter(Context context) {


        layoutInflater = LayoutInflater.from(context);


    }

    public void addList(List<InfoVO> list) {
        mInfoVOList.addAll(list);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case InfoVO.TYPE_ONE:
                return new TypeOneHolder(layoutInflater.inflate(R.layout.rclview_item, parent, false));
            case InfoVO.TYPE_TWO:
                return new TypeTowHolder(layoutInflater.inflate(R.layout.rclview_two, parent, false));
            case InfoVO.TYPE_THREE:
                Log.e("Web===", "hahahahahahahahahahaha-------------------------");
                return new TypeThreeHolder(layoutInflater.inflate(R.layout.rclview_three, parent, false));
            case InfoVO.TYPE_FOR:
                Log.e("Web===", "hahahahahahahahahahaha-------------------------");
                return new TypeForHolder(layoutInflater.inflate(R.layout.rclview_three, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TypeAbstractViewHolder) holder).bindHodler(mInfoVOList.get(position));
//
    }

    @Override
    public int getItemViewType(int position) {
//        if (position == InfoVO.TYPE_ONE) {
//            return InfoVO.TYPE_TWO;
//        } else if (position == InfoVO.TYPE_TWO) {
//            return InfoVO.TYPE_ONE;
//
//        }

        return mInfoVOList.get(position).type;
//        return -1;
    }

    @Override
    public int getItemCount() {
        return mInfoVOList.size();
    }
}
