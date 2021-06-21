package com.apowersoft.mirrorsender.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;


import com.apowersoft.mirrorsender.R;

import java.util.List;

public class ChoiceDialog extends Dialog {
    private List<String> datas;
    private RecyclerView reChoice;
    private ChoiceAdapter choiceAdapter;
    private OnItemClickListener onItemClickListener;

    public ChoiceDialog(Context context, List<String> datas) {
        this(context, 0, datas);
        this.datas = datas;
    }

    public ChoiceDialog(Context context, int themeResId, List<String> datas) {
        super(context, themeResId);
        this.datas = datas;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_choice_dialog);
        reChoice = findViewById(R.id.re_choice);
        reChoice.setLayoutManager(new LinearLayoutManager(getContext()));
        choiceAdapter = new ChoiceAdapter( datas);
        reChoice.setAdapter(choiceAdapter);

        choiceAdapter.setOnItemClickListener(new ChoiceAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View v, int position) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(choiceAdapter, v, position);
                }
            }
        });
    }


    public interface OnItemClickListener {
        void onItemClick(ChoiceAdapter adapter, View view, int position);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
