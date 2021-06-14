package com.general.DialogOrPopUpList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.general.R;
import com.general.databinding.PopupCustomViewCellBinding;


public class MyListViewHolder extends RecyclerView.ViewHolder {
    Context context;
    String selectedItemText;
    int  selectedItemTxtColor;
    boolean hasHelp ;
    int  helpTextColor;
    PopupCustomViewCellBinding binding ;

    public MyListViewHolder(Context context, View itemView, String selectedItemText ,int selectedItemTxtColor , boolean hasHelp
    ,int  helpTextColor) {
        super(itemView);
        this.context = context;
        this.selectedItemText = selectedItemText;
        this.selectedItemTxtColor = selectedItemTxtColor;
        this.hasHelp = hasHelp;
        this.helpTextColor = helpTextColor;
        binding  = PopupCustomViewCellBinding.bind(itemView);
     }

    public void bindData(final Object item, final int position,
                         final MyListItemClickListener myListItemClickListener) {
        final MyListModel myListModel
                = (MyListModel) item;

        if (myListModel.getText().isEmpty()) {
            binding.popupCustomViewCellTxt.setVisibility(View.GONE);
        } else {
            binding.popupCustomViewCellTxt.setVisibility(View.VISIBLE);
            binding.popupCustomViewCellTxt.setText(myListModel.getText());

        }
        if (myListModel.getIcon() == 0) {
            binding.popupCustomViewCellImg.setVisibility(View.GONE);
        } else {
            binding.popupCustomViewCellImg.setVisibility(View.VISIBLE);
            binding.popupCustomViewCellImg.setImageDrawable(ContextCompat.getDrawable(context,myListModel.getIcon()));
         }

        if(hasHelp && position == 0)
        {
            binding.popupCustomViewCellTxt.setTextColor(helpTextColor);
        }
        else
        {
            if(myListModel.getText().equals(selectedItemText))
            {
                binding.popupCustomViewCellTxt.setTextColor(selectedItemTxtColor);
            }
            else
            {
                binding.popupCustomViewCellTxt.setTextColor(context.getResources().getColor(R.color.popup_cell_txt_not_selected));
            }
        }

        binding.popupCustomViewCellLlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListItemClickListener.onPopUpItemClicked(myListModel, position);
            }
        });

    }

    public static View getView(Context context, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return layoutInflater.inflate(R.layout.popup_custom_view_cell, viewGroup, false);
    }

}
