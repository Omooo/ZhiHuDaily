package top.omooo.zhihudaily.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import top.omooo.zhihudaily.R;
import top.omooo.zhihudaily.bean.RecycleItemInfo;

/**
 * Created by Omooo on 2018/1/19.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RecycleViewHolder> {
    private List<RecycleItemInfo> mItemInfos;
    private Context mContext;

    public RecycleAdapter(List<RecycleItemInfo> itemInfos, Context context) {
        mItemInfos = itemInfos;
        mContext = context;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycleview_item, parent, false);
        RecycleViewHolder holder = new RecycleViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position) {
        String contentTitle = mItemInfos.get(position).getTitle();
        String contentImage = mItemInfos.get(position).getImageUrl();
        holder.mViewTitle.setText(contentTitle);
        holder.mImageViewUrl.setImageURI(Uri.parse(contentImage));

    }

    @Override
    public int getItemCount() {
        return mItemInfos.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder {
        TextView mViewTitle;
        //        ImageView mImageViewUrl;
        SimpleDraweeView mImageViewUrl;
        public RecycleViewHolder(View itemView) {
            super(itemView);
            mViewTitle = itemView.findViewById(R.id.tv_contentTitle);
            mImageViewUrl = itemView.findViewById(R.id.iv_contentImage);
        }
    }
}
