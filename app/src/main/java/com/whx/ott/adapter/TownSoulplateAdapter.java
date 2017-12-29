package com.whx.ott.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whx.ott.R;
import com.whx.ott.bean.Soulplates;
import com.whx.ott.bean.TermsBean;

/**
 * Created by oleky on 2016/9/1.
 */
public class TownSoulplateAdapter extends BaseRecyclerViewAdapter<Soulplates> {

    private Context mContext;

    public TownSoulplateAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected BaseRecyclerViewHolder createItem(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bacisbutton, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    protected void bindData(BaseRecyclerViewHolder holder, int position) {
        ((MyViewHolder)holder).textView.setText(getItemData(position).getSoulplate_name());
    }

    class MyViewHolder extends BaseRecyclerViewHolder {

        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView  = itemView.findViewById(R.id.id_content_new);
        }

        @Override
        protected View getView() {
            return null;
        }
    }
}
