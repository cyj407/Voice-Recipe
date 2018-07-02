package cuisineList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.linyunchen.voicerecipe.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CuisineAdapter extends RecyclerView.Adapter<CuisineAdapter.ViewHolder>{

    private List<CuisineItem> cuisineItems;

    public CuisineAdapter(List<CuisineItem> cuisineItems, Context context) {
        this.cuisineItems = cuisineItems;
        this.context = context;
    }

    private Context context;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cuisine_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CuisineItem cuisineItem = cuisineItems.get(position);

        holder.textViewHead.setText(cuisineItem.getHead());
        holder.ratingBar.setRating(Integer.valueOf(cuisineItem.getRatingStar()));
        Log.i("showRating",Integer.valueOf(cuisineItem.getRatingStar()).toString());

        Picasso.get()
                .load(cuisineItem.getImageUrl())
                .into(holder.imageViewImage);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(v.getContext());
                ab.setMessage("確定要查看這個食譜嗎？將會連結到網頁。")
                        .setCancelable(false)
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(cuisineItem.getRecipeUrl()));
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                ab.create();
                ab.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.cuisineItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewHead;
        public RatingBar ratingBar;
        public ImageView imageViewImage;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textView_name);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            imageViewImage = (ImageView) itemView.findViewById(R.id.imageView_cuisine);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layout_item);
        }
    }
}
