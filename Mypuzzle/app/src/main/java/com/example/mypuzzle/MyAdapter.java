package com.example.mypuzzle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Gamedatas> personList;

    public MyAdapter(List<Gamedatas> personList) {
        this.personList = personList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Gamedatas gamedatas = personList.get(position);
        holder.numText.setText(gamedatas.getNum());
        holder.timeText.setText(gamedatas.getGametime());
        holder.stepText.setText(gamedatas.getGamestep());
        holder.dateText.setText(gamedatas.getGamedate());
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView numText;
        TextView timeText;
        TextView stepText;
        TextView dateText;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.numText = itemView.findViewById(R.id.textView13);
            this.timeText = itemView.findViewById(R.id.textView14);
            this.stepText = itemView.findViewById(R.id.textView15);
            this.dateText = itemView.findViewById(R.id.textView16);
        }
    }
}


