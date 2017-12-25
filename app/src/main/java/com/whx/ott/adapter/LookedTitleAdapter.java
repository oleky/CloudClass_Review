package com.whx.ott.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whx.ott.R;

import java.util.List;

/**
 * Created by HelloWorld on 2017/11/14.
 */

public class LookedTitleAdapter extends RecyclerView.Adapter<LookedTitleAdapter.ViewHolder> {
    private Context context;
    private List<String> names;
//    private OnTextViewColorChangeListener listener;

    public LookedTitleAdapter(Context context, List<String> names) {
        this.context = context;
        this.names = names;
    }


//    public void setListener(OnTextViewColorChangeListener listener) {
//        this.listener = listener;
//    }

    @Override
    public LookedTitleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_title_looked, parent, false);
        ViewHolder viewHolder = new LookedTitleAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_title.setText(names.get(position));
        holder.tv_title.setTextSize(16);
//        if (listener != null) {
//            listener.setViewColor(holder.tv_title, position);
//        }
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        //        private TextView tv_line;
        private TextView tv_title;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title_mine);
//            tv_line = (TextView) itemView.findViewById(R.id.tv_title_line);
        }
    }

//    public interface OnTextViewColorChangeListener {
//        void setViewColor(TextView textView, int position);
//    }
}