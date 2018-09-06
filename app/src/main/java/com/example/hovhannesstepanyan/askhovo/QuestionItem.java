package com.example.hovhannesstepanyan.askhovo;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import Core.Database.QuestionModel;

public class QuestionItem extends RecyclerView.Adapter<QuestionItem.ViewHolder> {

    private List<QuestionModel> data;

    public QuestionItem(List<QuestionModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        QuestionModel model = data.get(position);
        viewHolder.mTitle.setText(model.getTitle());
        viewHolder.mDescription.setText(model.getQuestion());
        int imageResId = R.mipmap.ic_circle;
        if (model.getCompleated()) {
            imageResId = R.mipmap.ic_checked;
            viewHolder.mTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.mTitle.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
        }
        viewHolder.mCheckImage.setOnClickListener(view -> {
            model.checkOrUncheck();
            notifyItemChanged(position);
        });
        viewHolder.mCheckImage.setImageResource(imageResId);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private TextView mDescription;
        private ImageView mCheckImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.tv_question_title);
            mDescription = itemView.findViewById(R.id.tv_question_desc);
            mCheckImage = itemView.findViewById(R.id.iv_checkmark);
        }
    }
}
