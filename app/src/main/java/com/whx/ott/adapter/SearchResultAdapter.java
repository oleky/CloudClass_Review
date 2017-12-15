package com.whx.ott.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whx.ott.R;
import com.whx.ott.bean.CoursesBean;
import com.whx.ott.beanfeature.SoulcoursesBean;
import com.whx.ott.conn.NumToString;


/**
 * Created by oleky on 2016/9/5.
 */
public class SearchResultAdapter extends BaseRecyclerViewAdapter<CoursesBean> {
    private Context mContext;
    public SearchResultAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    protected BaseRecyclerViewHolder createItem(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_gridsearch, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    protected void bindData(BaseRecyclerViewHolder holder, int position) {
        int yearid              = getItemData(position).getYear_id();
        int termid              = getItemData(position).getTerm_id();
        int gradeid             = getItemData(position).getGrade_id();
        int subjectid           = getItemData(position).getSubject_id();
        String strcoursename    = getItemData(position).getCourse_name();
        int teacherid           = getItemData(position).getTeacher_id();


        /**
         * 教师头像目前却
         * */
        if (yearid == 1) {
            Glide.with(mContext)
                    .load("http://60.205.148.237/cloudapi/Public/jy/" + teacherid + ".png")
                    .centerCrop()
                    .placeholder(R.drawable.teacher_bg)
                    .into(((MyViewHolder) holder).teacherbg);
        } else {
            Glide.with(mContext)
                    .load("http://60.205.148.237/cloudapi/Public/yc/"+teacherid+".png")
                    .centerCrop()
                    .placeholder(R.drawable.teacher_bg)
                    .into(((MyViewHolder) holder).teacherbg);
        }


        //测试
        String stryear      = NumToString.searchYears(yearid)+"系列";
        String strterm      = NumToString.searchTerms(termid);
        String strgrade     = NumToString.searchGrades(gradeid);
        String strsubject   = NumToString.searchSubject(subjectid);

        ((MyViewHolder) holder).yearname.setText(stryear);
        ((MyViewHolder) holder).termname.setText(strterm);
        ((MyViewHolder)holder).gradename.setText(strgrade);
        ((MyViewHolder) holder).subjectname.setText(strsubject);
        ((MyViewHolder) holder).coursename.setText(strcoursename);
    }


    class MyViewHolder extends BaseRecyclerViewHolder {

        TextView yearname,termname,subjectname,coursename,gradename;
        ImageView teacherbg;
        public MyViewHolder(View itemView) {
            super(itemView);
            teacherbg   = itemView.findViewById(R.id.teacherpic_search);
            yearname    = itemView.findViewById(R.id.id_yearname_search);
            termname    = itemView.findViewById(R.id.id_termname_search);
            subjectname = itemView.findViewById(R.id.id_subjectname_search);
            coursename = itemView.findViewById(R.id.id_teachername_search);
            gradename   = itemView.findViewById(R.id.id_gradename_search);
        }

        @Override
        protected View getView() {
            return null;
        }
    }
}
