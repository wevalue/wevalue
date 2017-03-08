package com.wevalue.utils;

import android.app.Activity;
import android.content.Intent;

import com.wevalue.model.NoteBean;
import com.wevalue.ui.details.activity.NoteDetailActivity;

/**
 * Created by Administrator on 2017/2/14.
 */

public class ActivityUtils {
    /**
     *
     * @param activity
     * @param noteEntity  帖子详情
     * @param repostfrom  1 世界  2 影响力  3 全部  默认为3
     */
    public static  void gotoNoteDetails(Activity activity, NoteBean.NoteEntity noteEntity,String repostfrom){
        Intent intent = null;
        if (noteEntity.getRepostid().equals("0")) {
            intent = new Intent(activity, NoteDetailActivity.class);
            intent.putExtra("noteId", noteEntity.getNoteid());
            intent.putExtra("repostid", "0");
            intent.putExtra("repostfrom", repostfrom);
            activity.startActivity(intent);
        } else {
            intent = new Intent(activity, NoteDetailActivity.class);
            intent.putExtra("noteId", noteEntity.getNoteid());
            intent.putExtra("repostid", noteEntity.getRepostid());
            intent.putExtra("repostfrom", repostfrom);
            activity.startActivity(intent);
        }
    }

}
