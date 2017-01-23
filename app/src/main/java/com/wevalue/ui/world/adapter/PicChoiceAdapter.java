
package com.wevalue.ui.world.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.wevalue.MainActivity;
import com.wevalue.R;
import com.wevalue.ui.world.activity.ImgShowActivity;
import com.wevalue.ui.world.activity.PicChoiceActivity;
import com.wevalue.ui.world.util.CommonAdapter;
import com.wevalue.ui.world.util.ViewHolder;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.ShowUtil;

import java.util.List;

public class PicChoiceAdapter extends CommonAdapter<String> {

    PicChoiceActivity mActivity;
    List<String> mDatas;
    private int choiceLimit = 9;//选择图片的限制

    /**
     * 文件夹路径
     */
    private String mDirPath;

    public PicChoiceAdapter(PicChoiceActivity context, List<String> mDatas, int itemLayoutId, String dirPath, int choiceLimit) {
        super(context, mDatas, itemLayoutId);
        mActivity = context;
        this.mDirPath = dirPath;
        this.mDatas = mDatas;
        this.choiceLimit = choiceLimit;
    }

    @Override
    public void convert(final ViewHolder helper, final String item, final int position) {

//		if (item.equals("拍照")) {
//			helper.setImageResource(R.id.id_item_image, R.mipmap.default_head);
//			final ImageView mImageView = helper.getView(R.id.id_item_image);
//			final ImageView mSelect = helper.getView(R.id.id_item_select);
//			mSelect.setVisibility(View.GONE);
//			mImageView.setOnClickListener(news OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					mActivity.showCamera();
//				}
//			});
//		}else{
        // 设置no_pic
        helper.setImageResource(R.id.id_item_image, R.mipmap.pictures_no);
        // 设置no_selected
        helper.setImageResource(R.id.id_item_select, R.mipmap.picture_unselected);
        LogUtils.e("item  = " + item);
        // 设置图片
        helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);

        final ImageView mImageView = helper.getView(R.id.id_item_image);
        final ImageView mSelect = helper.getView(R.id.id_item_select);

        mImageView.setColorFilter(null);
        // 设置ImageView的点击事件
        mSelect.setOnClickListener(new OnClickListener() {
            // 选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v) {
                if (MainActivity.mSelectedImage.size() < choiceLimit) {
                    // 已经选择过该图片
                    if (MainActivity.mSelectedImage.contains(mDirPath + "/" + item)) {
                        MainActivity.mSelectedImage.remove(mDirPath + "/" + item);
                        mSelect.setImageResource(R.mipmap.picture_unselected);
                        mImageView.setColorFilter(null);
                    } else
                    // 未选择该图片
                    {
                        MainActivity.mSelectedImage.add(mDirPath + "/" + item);
                        mSelect.setImageResource(R.mipmap.pictures_selected);
                        mImageView.setColorFilter(Color.parseColor("#77000000"));
                    }

                } else {
                    // 已经选择过该图片
                    if (MainActivity.mSelectedImage.contains(mDirPath + "/" + item)) {
                        MainActivity.mSelectedImage.remove(mDirPath + "/" + item);
                        mSelect.setImageResource(R.mipmap.picture_unselected);
                        mImageView.setColorFilter(null);
                    }
                    ShowUtil.showToast(mContext, "最多选择" + choiceLimit + "张图片");
                }
            }
        });
        /**
         * 已经选择过的图片，显示出选择过的效果
         */
        if (MainActivity.mSelectedImage != null) {
            if (MainActivity.mSelectedImage.contains(mDirPath + "/" + item)) {
                mSelect.setImageResource(R.mipmap.pictures_selected);
                mImageView.setColorFilter(Color.parseColor("#77000000"));
            }
        }
//		}
        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] imgurl = new String[mDatas.size()];
                for (int i = 0; i < mDatas.size(); i++) {
                    imgurl[i] = mDirPath + "/" + mDatas.get(i);
                }
                Intent intent = new Intent(mContext, ImgShowActivity.class);
                intent.putExtra("imgUrl", imgurl);
                intent.putExtra("index", position);
                intent.putExtra("imPreview", "yes");//选择图片时候的预览
                mContext.startActivity(intent);
            }
        });
    }
}
