package app.com.moneytap.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.com.moneytap.R;
import app.com.moneytap.activities.HomeActivity;
import app.com.moneytap.activities.WebActivity;
import app.com.moneytap.models.Page;

/**
 * Created by pawansingh on 08/09/18.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List dataList = new ArrayList();
    private HomeActivity homeActivity;

    public List getDataList() {
        return dataList;
    }

    public DataAdapter(List dataList, HomeActivity homeActivity) {
        this.dataList = dataList;
        this.homeActivity = homeActivity;
    }

    public void addData(List dataList) {
        this.dataList = new ArrayList(dataList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.data_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        if(dataList.get(position) instanceof Page) {
            final Page page = (Page) dataList.get(position);
            if(page.getThumbnail() != null) {
                Picasso
                        .get()
                        .load(page.getThumbnail().getSource())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(holder.imageView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(((page.getThumbnail())).getSource()).into(holder.imageView);
                            }
                        });
            }

            if(page.getTerms() != null) {
                holder.textViewDescription.setText(TextUtils.join(", ", page.getTerms().getDescription()));
            }

            holder.textViewTitle.setText(page.getTitle());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(homeActivity, WebActivity.class);
                    intent.putExtra("KEY", String.format("https://en.wikipedia.org/wiki/%s", page.getTitle()));
                    intent.putExtra("TITLE", page.getTitle());
                    homeActivity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (dataList.isEmpty()) {
            homeActivity.relativeLayoutEmpty.setVisibility(View.VISIBLE);
        } else {
            homeActivity.relativeLayoutEmpty.setVisibility(View.GONE);
        }
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        Context context;
        TextView textViewTitle, textViewDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }
}
