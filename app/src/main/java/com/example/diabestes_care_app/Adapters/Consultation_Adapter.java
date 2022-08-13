package com.example.diabestes_care_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diabestes_care_app.Models.Consolation_Model;
import com.example.diabestes_care_app.R;

import java.util.ArrayList;

public class Consultation_Adapter extends RecyclerView.Adapter<Consultation_Adapter.MyViewHolder> {
    Context context;
    ArrayList<Consolation_Model> list;

    public Consultation_Adapter(Context context, ArrayList<Consolation_Model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Consultation_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consu_recycler_layout, parent, false);
        return new Consultation_Adapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Consultation_Adapter.MyViewHolder holder, int position) {

        holder.title.setText(list.get(position).getTitle());
        holder.question.setText(list.get(position).getQuestion());
        holder.doctorName.setText(list.get(position).getDoctorName());
//        holder.answer.setText(list.get(position).getAnswer());
        Glide.with(context).load(list.get(position).getImageUrl()).placeholder(R.drawable.ic_user).error(R.drawable.notifications).into(holder.doctorImage);

        holder.showAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.AnswerLayout.getVisibility() == View.GONE) {
                    holder.AnswerLayout.setVisibility(View.VISIBLE);
                } else {
                    holder.AnswerLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateUsersList(ArrayList<Consolation_Model> Consolation_Model) {
        this.list = Consolation_Model;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, question, doctorName, answer;
        ImageView doctorImage;
        // Container
        RelativeLayout AnswerLayout;
        ImageButton showAnswer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Consu_title);
            question = itemView.findViewById(R.id.Consu_Que);
            doctorName = itemView.findViewById(R.id.Consu_name);
            answer = itemView.findViewById(R.id.Consu_answer);
            doctorImage = itemView.findViewById(R.id.Consu_Doc_Image);
            AnswerLayout = itemView.findViewById(R.id.Consu_cont_show_answer);
            showAnswer = itemView.findViewById(R.id.Consu_btn_show_answer);
        }
    }
}
