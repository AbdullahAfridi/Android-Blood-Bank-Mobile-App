package com.rishabh.bloodbank.Adaptar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rishabh.bloodbank.DataModels.RequestDataModel;
import com.rishabh.bloodbank.R;

import java.util.List;

import static android.Manifest.permission.CALL_PHONE;

public class RequestAdaptar extends RecyclerView.Adapter<RequestAdaptar.ViewHolder> {

    private List<RequestDataModel> dataSet;
    private Context context;

    public RequestAdaptar(
            List<RequestDataModel> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_item_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,
                                 final int position) {

        holder.messageR.setText(dataSet.get(position).getMessage());

        Glide.with(context).load(dataSet.get(position).getUrl()).into(holder.image1);


        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionChecker.checkSelfPermission(context, CALL_PHONE) == PermissionChecker.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + dataSet.get(position).getNum1()));
                    context.startActivity(intent);
                } else {
                    ((Activity) context).requestPermissions(new String[]{CALL_PHONE}, 401);
                }
            }


    });
        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return dataSet.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView messageR;
        ImageView  callBtn, shareBtn;
        ImageView image1;
         ViewHolder( final View itemView) {
            super(itemView);
            messageR=itemView.findViewById(R.id.messageR);
            image1= itemView.findViewById(R.id.image1);
            callBtn=itemView.findViewById(R.id.call_btn);
           shareBtn=itemView.findViewById(R.id.share_btn);

        }

    }

}