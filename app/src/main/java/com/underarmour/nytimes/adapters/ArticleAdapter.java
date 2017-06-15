package com.underarmour.nytimes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.underarmour.nytimes.R;
import com.underarmour.nytimes.models.Article;
import com.underarmour.nytimes.models.Multimedia;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>{

    private static final String IMAGE_TEXT = "image";
    private static final String IMG_SUBTYPE = "thumbnail";
    private static final String TAG = ArticleAdapter.class.getSimpleName();
    private static final String MEDIA_BASE_URL = "https://www.nytimes.com/";
    private final Context context;
    private final ArrayList<Article> listArticles;
    private CustomOnClickListener customClickListener;

    public ArticleAdapter(Context context, ArrayList<Article> listArticles){

        this.context = context;
        this.listArticles = listArticles;
    }

    public interface CustomOnClickListener{

        public void onItemClick(View view,int position);
    }

    public void setClickListener(CustomOnClickListener customClickListener){

        this.customClickListener = customClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View userView = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item,parent,false);
        return new ViewHolder(userView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Article article = listArticles.get(position);
        holder.tvHeadline.setText(article.getHeadline().getHeadlineText());
        holder.tvSnippet.setText(article.getSnippet());
        ArrayList<Multimedia> mediaList = article.getMediaList();
        int mediaListSize = mediaList.size();
        if(mediaList!=null && mediaListSize>0){

            // if thumbnail, then set an image
            Multimedia thumbnailObj = mediaList.get(0);
            if(thumbnailObj.getType().equalsIgnoreCase(IMAGE_TEXT)
                    && thumbnailObj.getSubType().equalsIgnoreCase(IMG_SUBTYPE)){

                String imgUrl = MEDIA_BASE_URL + thumbnailObj.getImageUrl();
                int height = thumbnailObj.getHeight();
                int width = thumbnailObj.getWidth();
                Glide.with(context).load(imgUrl).override(width,height).into(holder.ivThumbnail);
            }
        }else
            holder.ivThumbnail.setVisibility(View.GONE);

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvHeadline;
        ImageView ivThumbnail;
        TextView tvSnippet;

        public ViewHolder(View itemView) {
            super(itemView);;
            tvHeadline = (TextView) itemView.findViewById(R.id.headline);
            ivThumbnail = (ImageView) itemView.findViewById(R.id.newsThumbnail);
            tvSnippet= (TextView) itemView.findViewById(R.id.tvSnippet);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        customClickListener.onItemClick(view,position);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {

        if(listArticles!=null)
            return listArticles.size();
        else
            return 0;
    }

}
