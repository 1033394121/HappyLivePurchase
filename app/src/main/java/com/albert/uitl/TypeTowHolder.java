package com.albert.uitl;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.albert.activity.R;

/**
 * Created by Albert on 2017/9/28 0028.
 */

public class TypeTowHolder extends TypeAbstractViewHolder {
    public MixtureTextView msg2;
    public CircleView userPIC;
    public ImageView ding;

    public TypeTowHolder(View itemView) {
        super(itemView);
//        userPIC = (CircleView) itemView.findViewById(R.id.user_tou);
        ding = (ImageView) itemView.findViewById(R.id.user_inf1_2);
        msg2 = (MixtureTextView) itemView.findViewById(R.id.msg_text_2);
        msg2.setShadowLayer(10, 5, 5, R.color.liaotian);


    }

    @Override
    public void bindHodler(InfoVO infoVO) {
//        userPIC.setImageBitmap(infoVO.userPic);
        ding.setImageResource(infoVO.userIfo1);
        msg2.setText(infoVO.msg);

    }


}
