package com.kora.android.presentation.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kora.android.R;
import com.kora.android.presentation.model.UserEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SectionViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.section_name)
    TextView sectionName;

    public SectionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(UserEntity.Section section) {
        sectionName.setText(section.getName());
    }
}
