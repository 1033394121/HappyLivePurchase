package com.albert.uitl;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.albert.activity.R;

/**
 * @name HappyLivePurchase
 * @class name：com.albert.uitl
 * @class describe
 * @anthor Albert QQ:1032006226
 * @time 2017/9/5 9:20
 * @change
 * @chang time
 * @class describe*/



public class MyDialog extends Dialog {
    public MyDialog(@NonNull Context context) {
        super(context);
    }

    public MyDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected MyDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public static class Builder {
        private Context context;
        private String title;
        private EditText roomID;
        private CheckBox roomA;
        private CheckBox roomB;
        private CheckBox roomC;
        private View contentView;

        private String positiveButtonText;
        private String negativeButtonText;

        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

//        private String room;

        public Builder(Context context) {
            this.context = context;
        }


       /*  * Set the Dialog title from resource
         *
         * @param title
         * @return*/


        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

/*
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */


        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

//        public Builder setroomID(int roomID) {
//            this.roomID = (EditText) context.getText(roomID);
//            return this;
//        }
//
//        public Builder setroomID(EditText roomID) {
//            this.roomID = roomID;
//            return this;
//
//        }

//

//         * @author Albert
//         * set Edit
//         *
//         *
//         * @time 2017/9/5  9:33
//         * @describe
//

//        public Builder EditText(int roomID) {
//            this.title = (String) context.getText(roomID);
//            return this;
//        }


        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }


       /*  * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */

        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }


        public MyDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final MyDialog dialog = new MyDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_chose, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title_dialog)).setText(title);
            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.intentBtn_dialog))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.intentBtn_dialog))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.intentBtn_dialog).setVisibility(
                        View.GONE);
            }
//             set the cancel button
            if (negativeButtonText != null) {
//                ((ImageButton) layout.findViewById(R.id.closeBtn_dialog))
//                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((ImageButton) layout.findViewById(R.id.closeBtn_dialog))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.closeBtn_dialog).setVisibility(
                        View.GONE);
            }
//             set the content message
            if (roomID != null) {
                roomID = (EditText) layout.findViewById(R.id.roomID_dialog);
//                room = roomID.getText().toString();
                SharedPreferences.Editor editor = context.getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                editor.putString("roomid",String.valueOf(roomID.getText()));
                editor.apply();

            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            }
            dialog.setContentView(layout);
            return dialog;
        }
//
//
    }


}
