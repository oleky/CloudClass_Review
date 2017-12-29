package com.whx.ott.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whx.ott.R;
import com.whx.ott.bean.CoursesBean;
import com.whx.ott.beanfeature.SoulcoursesBean;
import com.whx.ott.conn.TownNumToString;
import com.whx.ott.util.ConvertUtils;


/**
 * Created by oleky on 2016/9/5.
 */
public class TownFeatureResultAdapter extends BaseRecyclerViewAdapter<SoulcoursesBean> {
    private Context mContext;
    public TownFeatureResultAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    protected BaseRecyclerViewHolder createItem(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_towngrid, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    protected void bindData(BaseRecyclerViewHolder holder, int position) {
        int yearid              = getItemData(position).getYear_id();
        String soulplateid      = getItemData(position).getSoulplate_id();
        String gradeid             = getItemData(position).getGrade_id();
        String subject           = getItemData(position).getSubject_id();
        int subjectid = Integer.parseInt(subject);
        String strcoursename    = getItemData(position).getSoulcourse_name();
        int teacherid           = getItemData(position).getTeacher_id();

        /**
         * 教师头像目前却
         * */

            ConvertUtils.showCircleIcon(((MyViewHolder) holder).teacherbg
                    , "http://60.205.148.237/cloudapi/Public/town/" + teacherid + ".png", mContext);

        switch (subjectid) {
            case 1:
                ((MyViewHolder) holder).itembg.setImageResource(R.mipmap.chinese);
                break;
            case 2:
                ((MyViewHolder) holder).itembg.setImageResource(R.mipmap.math);

                 break;

            case 3:
                ((MyViewHolder) holder).itembg.setImageResource(R.mipmap.english);

                break;
            case 4:
                ((MyViewHolder) holder).itembg.setImageResource(R.mipmap.physics);

                break;
            case 5:
                ((MyViewHolder) holder).itembg.setImageResource(R.mipmap.chemistry);

                break;
            case 6:
                ((MyViewHolder) holder).itembg.setImageResource(R.mipmap.biology);

                break;
            default:
                ((MyViewHolder) holder).itembg.setImageResource(R.mipmap.chinese);

                break;
        }

        //测试
        String stryear      = TownNumToString.searchYears(yearid)+"系列";
        String strterm      = TownNumToString.searchSoulplate(Integer.parseInt(soulplateid));
        String strgrade     = TownNumToString.searchGrades(Integer.parseInt(gradeid));
        String strsubject   = TownNumToString.searchSubject(subjectid);
        String teachername = TownNumToString.searchTeacher(teacherid);

        ((MyViewHolder) holder).yearname.setText(stryear);
        ((MyViewHolder) holder).termname.setText(strterm+" "+strgrade+" "+strsubject);
//        ((MyViewHolder)holder).gradename.setText(strgrade);
//        ((MyViewHolder) holder).subjectname.setText(strsubject);
        ((MyViewHolder) holder).coursename.setText(strcoursename);
        ((MyViewHolder) holder).teachername.setText(teachername);
    }


    class MyViewHolder extends BaseRecyclerViewHolder {

        TextView yearname,termname,subjectname,coursename,gradename,teachername;
        ImageView teacherbg,itembg;
        public MyViewHolder(View itemView) {
            super(itemView);
            teacherbg   = itemView.findViewById(R.id.teacherpic_search);
            yearname    = itemView.findViewById(R.id.id_yearname_search);
            termname    = itemView.findViewById(R.id.id_termname_search);
//            subjectname = (TextView) itemView.findViewById(R.id.id_subjectname_search);
            coursename = itemView.findViewById(R.id.id_teachername_search);
//            gradename   = (TextView) itemView.findViewById(R.id.id_gradename_search);
            teachername = itemView.findViewById(R.id.id_teacher_name);
            itembg = itemView.findViewById(R.id.item_bg);

        }

        @Override
        protected View getView() {
            return null;
        }
    }
}
