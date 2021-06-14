package com.general.DialogOrPopUpList;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.general.R;
import com.general.ui.adapters.GenericRecyclerViewAdapter;

import java.util.ArrayList;


public class MyDialogOrPopUpList {

    PopupWindow popupWindow;
    AppCompatDialog dialog;
    GenericRecyclerViewAdapter popupAdapter;
    RecyclerView popupCustomViewRv;
    Context context;
    MyListItemClickListener myListItemClickListener;
    int offsetX = 0;
    int offsetY = 2;
    boolean anchorIsRight;
    String selectedItemText;
    int  selectedItemTxtColor;
    boolean hasHelp ;
    int  helpTextColor;
    public boolean isAnchorIsRight() {
        return anchorIsRight;
    }

    public void setAnchorIsRight(boolean anchorIsRight) {
        this.anchorIsRight = anchorIsRight;
    }

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    public AppCompatDialog getDialog() {
        return dialog;
    }

    public Context getContext() {
        return context;
    }


    public void setContext(Context context) {

        this.context = context;
    }


    public MyListItemClickListener getMyListItemClickListener() {
        return myListItemClickListener;
    }

    public void setMyListItemClickListener(MyListItemClickListener myListItemClickListener) {
        this.myListItemClickListener = myListItemClickListener;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public MyDialogOrPopUpList(Context context ) {
        setContext(context);
    }

    public void setSelectedItemText(String selectedItemText) {
        this.selectedItemText = selectedItemText;
    }

    public void setselectedItemTxtColor(int selectedItemTxtColor) {
        this.selectedItemTxtColor = selectedItemTxtColor;
    }

    public void setHasHelp(boolean hasHelp) {
        this.hasHelp = hasHelp;
    }

    public void setHelpTextColor(int helpTextColor) {
        this.helpTextColor = helpTextColor;
    }

    public void showPopup(View anchorView, ArrayList<MyListModel> list, boolean isDialog , CancelCallBack cancelCallBack ) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.popup_custom_view, null);
        if (isDialog) {
             dialog = new AppCompatDialog(getContext(),  R.style.MyDialogList);
             dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancelCallBack.onCancelCallBack();
                }
            });
            dialog.setContentView(contentView);

        } else {
            popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        popupCustomViewRv = contentView.findViewById(R.id.popup_custom_view_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                RecyclerView.VERTICAL, false);
        popupCustomViewRv.setLayoutManager(layoutManager);
        popupAdapter = new GenericRecyclerViewAdapter(context, new GenericRecyclerViewAdapter.AdapterDrawData() {

            @Override
            public RecyclerView.ViewHolder getView(ViewGroup parent, int viewType) {
                return new MyListViewHolder(context,
                        MyListViewHolder.getView(context, parent),selectedItemText,selectedItemTxtColor,hasHelp,helpTextColor);
            }

            @Override
            public void bindView(GenericRecyclerViewAdapter genericRecyclerViewAdapter,
                                 RecyclerView.ViewHolder holder, Object item, int position) {
                ((MyListViewHolder) holder).bindData(
                        genericRecyclerViewAdapter.getItem(position), position, myListItemClickListener);
            }
        });

        popupCustomViewRv.setAdapter(popupAdapter);
        popupCustomViewRv.scrollToPosition(getSelectedPos(selectedItemText));
         setItems(list);
        showPopup(anchorView);
    }

    private int getSelectedPos(String selectedItemText) {
        for (int i = 0; i <popupAdapter.getList().size() ; i++) {
            if(((MyListModel)(popupAdapter.getList().get(i))).getText().equals(selectedItemText))
            {
                return i ;
            }
        }
        return 0 ;
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        } else if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        } else if (popupWindow != null) {
            return popupWindow.isShowing();
        }
        return false ;
    }

    private void setItems(ArrayList<MyListModel> list) {
        popupAdapter.setAll(list);
    }

    public void showPopup(View anchorView) {
        if(dialog!=null)
        {
            dialog.show();
           // dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        }
        else  if(popupWindow!=null)

        {
            if (isAnchorIsRight()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    popupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    int xOffset = -(popupWindow.getContentView().getMeasuredWidth() - anchorView.getWidth());
                    popupWindow.showAsDropDown(anchorView, xOffset, 0);
                } else {
                    popupWindow.showAsDropDown(anchorView, anchorView.getWidth() - popupWindow.getWidth(), 0);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    popupWindow.showAsDropDown(anchorView, 0, 0, Gravity.LEFT);
                } else {
                    popupWindow.showAsDropDown(anchorView, 20, 0);
                }
            }

            dimBehind(popupWindow);
        }
    }


    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.5f;
        wm.updateViewLayout(container, p);
    }

    public interface CancelCallBack
    {
        void onCancelCallBack();
    }
}
