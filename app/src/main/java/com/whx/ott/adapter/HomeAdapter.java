package com.whx.ott.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.whx.ott.R;

import java.util.List;

/**
 * Created by HelloWorld on 2016/9/4.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    private Context context;
    private List<String> tvs;
    private List<Integer> ivs;
    private OnItemClickListener listener;
    private boolean animationsLocked = false;
    private int lastAnimatedPosition = -1;
    private boolean delayEnterAnimation = true;

    public HomeAdapter(Context context, List<String> tvs, List<Integer> ivs) {
        this.context = context;
        this.tvs = tvs;
        this.ivs = ivs;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        public void onItemClick(View itemView, int position);

        public void onItemLongClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_view, parent,
                false);
        MyViewHolder holder = new MyViewHolder(item);

        return holder;
    }

    private void runEnterAnimation(View view, int position) {

        if (animationsLocked) return;//animationsLocked是布尔类型变量，一开始为false，确保仅屏幕一开始能够显示的item项才开启动画

        if (position > lastAnimatedPosition) {//lastAnimatedPosition是int类型变量，一开始为-1，这两行代码确保了recycleview滚动式回收利用视图时不会出现不连续的效果
            lastAnimatedPosition = position;
            view.setTranslationX(500);//相对于原始位置下方400
            view.setAlpha(0.f);//完全透明
            //每个item项两个动画，从透明到不透明，从下方移动到原来的位置
            //并且根据item的位置设置延迟的时间，达到一个接着一个的效果
            view.animate()
                    .translationX(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)//根据item的位置设置延迟时间，达到依次动画一个接一个进行的效果
                    .setInterpolator(new DecelerateInterpolator(0.5f))//设置动画效果为在动画开始的地方快然后慢
                    .setDuration(700)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;//确保仅屏幕一开始能够显示的item项才开启动画，也就是说屏幕下方还没有显示的item项滑动时是没有动画效果
                        }
                    })
                    .start();
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        holder.tv.setText(tvs.get(position));
        holder.iv.setImageResource(ivs.get(position % 10));
        holder.itemView.setTag(position);
        setUpEvent(holder);
    }

    @Override
    public int getItemCount() {
        return tvs.size();
    }

    protected void setUpEvent(final MyViewHolder holder) {
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int location = holder.getLayoutPosition();
                    listener.onItemClick(holder.itemView, location);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //获取点击的是holder的第几条
                    int location = holder.getLayoutPosition();
                    listener.onItemLongClick(holder.itemView, location);
                    return false;
                }
            });
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView tv;
        ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById(R.id.item_rv_iv);
            tv = (TextView) view.findViewById(R.id.item_rv_tv);
        }

    }


}

