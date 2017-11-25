package com.example.self;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 *
 */
public class TagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> list;
    Activity context;

    public TagAdapter(Activity context, List<String> list) {
        this.list = list;
        this.context = context;

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    View view;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_tag, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            ViewHolder vh = (ViewHolder) holder;
            String name=list.get(position);
            ((ViewHolder) holder).textView.setText(name);

            if (null != mOnItemClickListener) {
                vh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setDiscLists(List<String> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.textview);
        }
    }
}
