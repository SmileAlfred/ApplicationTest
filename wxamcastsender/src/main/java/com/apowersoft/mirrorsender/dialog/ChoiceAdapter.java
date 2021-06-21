package com.apowersoft.mirrorsender.dialog;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apowersoft.mirrorsender.R;

import java.util.List;

public class ChoiceAdapter extends RecyclerView.Adapter<ChoiceAdapter.ChoiceViewHolder> {
    private List<String> data;
    private OnItemClickListener onItemClickListener;

    public ChoiceAdapter(List<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ChoiceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_choice, viewGroup, false);
        return new ChoiceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChoiceViewHolder choiceViewHolder, final int i) {
        String item = data.get(i);
        choiceViewHolder.tvItem.setText(item);
        if (i == data.size() - 1) {
            choiceViewHolder.ivLine.setVisibility(View.GONE);
        } else {
            choiceViewHolder.ivLine.setVisibility(View.VISIBLE);
        }
        choiceViewHolder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.itemClick(v, i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ChoiceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItem;
        private LinearLayout llContainer;
        private ImageView ivLine;

        public ChoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tv_item);
            llContainer = itemView.findViewById(R.id.ll_container);
            ivLine = itemView.findViewById(R.id.iv_line);
        }
    }

    public interface OnItemClickListener {
        void itemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
