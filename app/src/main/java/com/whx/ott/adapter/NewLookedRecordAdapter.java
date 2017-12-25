package com.whx.ott.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whx.ott.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HelloWorld on 2017/11/13.
 */

public class NewLookedRecordAdapter extends RecyclerView.Adapter<NewLookedRecordAdapter.MyViewHolder> {
    private List<String> datatimeLists;
    private List<String> timeLists;
    private List<String> nameLists;
    private List<String> gradeLists;
    private Context context;
    private List<Boolean> isClicks;
    private int type;
    private OnItemListener mOnItemListener;

    public NewLookedRecordAdapter(List<String> datatimeLists, List<String> timeLists, List<String> nameLists, List<String> gradeLists, Context context) {
        this.datatimeLists = datatimeLists;
        this.timeLists = timeLists;
        this.nameLists = nameLists;
        this.gradeLists = gradeLists;
        this.context = context;
    }

    public NewLookedRecordAdapter(List<String> datatimeLists, List<String> nameLists, List<String> gradeLists, Context context) {
        this.datatimeLists = datatimeLists;
        this.nameLists = nameLists;
        this.gradeLists = gradeLists;
        this.context = context;
        isClicks = new ArrayList<>();
        for (int i = 0; i < nameLists.size(); i++) {
            isClicks.add(false);
        }
    }

    public void setmOnItemListener(OnItemListener mOnItemListener) {
        this.mOnItemListener = mOnItemListener;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_record_looked, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_datatime.setText(datatimeLists.get(position));
        holder.tv_grade.setText(gradeLists.get(position));
        holder.tv_name.setText(nameLists.get(position));

    }

    @Override
    public int getItemCount() {
        return nameLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_datatime;
        private final TextView tv_grade;
        private final TextView tv_name;
//        private final TextView tv_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_datatime = itemView.findViewById(R.id.tv_datatime_looked);
            tv_grade = itemView.findViewById(R.id.tv_grade_looked);
            tv_name = itemView.findViewById(R.id.tv_name_looked);
//            tv_time = (TextView) itemView.findViewById(R.id.tv_time_looked);
        }
    }

    public interface OnItemListener {
        void onItemChange(TextView time, TextView names, TextView grade, int position);
    }
}
