package com.albert.uitl;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.albert.activity.R;

/**
 * Created by Albert on 2017/9/28 0028.
 */

public class TypeThreeHolder extends TypeAbstractViewHolder {
    public CircleView userPIC;
    public TextView userName;
    public ImageView userIfo1;
    public ImageView userIfo2;
    public TextView duoshouNum;
    public MixtureTextView msg;

    public TypeThreeHolder(View itemView) {
        super(itemView);
//        userPIC = (CircleView) itemView.findViewById(R.id.user_pic);
        userName = (TextView) itemView.findViewById(R.id.user_name);
        userIfo1 = (ImageView) itemView.findViewById(R.id.user_inf1);
        userIfo2 = (ImageView) itemView.findViewById(R.id.user_inf2);
        duoshouNum = (TextView) itemView.findViewById(R.id.duoshou_num);
        msg = (MixtureTextView) itemView.findViewById(R.id.msg_text);
        userName.setShadowLayer(10, 5, 5, R.color.liaotian);
        msg.setShadowLayer(10, 5, 5, R.color.liaotian);

    }

    @Override
    public void bindHodler(InfoVO infoVO) {
//        userPIC.setImageBitmap(infoVO.userPic);
        userName.setText(infoVO.userName);
        userIfo1.setImageResource(infoVO.userIfo1);
        userIfo2.setImageResource(infoVO.userIfo2);
        duoshouNum.setText(infoVO.dunshou);
        msg.setText(infoVO.msg);

    }


}
