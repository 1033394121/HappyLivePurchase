package com.albert.uitl;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Albert on 2017/9/28 0028.
 */

public abstract class TypeAbstractViewHolder extends RecyclerView.ViewHolder {


    public TypeAbstractViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindHodler(InfoVO infoVO);


}
