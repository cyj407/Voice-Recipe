package fragmentPage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        CuisineItem cuisineItem = cuisineItems.get(position);

        holder.textViewHead.setText(cuisineItem.getHead());
        holder.textViewDesc.setText(cuisineItem.getDesc());

        Picasso.get()
                .load(cuisineItem.getImageUrl())
                .into(holder.imageViewImage);
    }

    @Override
    public int getItemCount() {
        return this.cuisineItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewHead;
        public TextView textViewDesc;
        public ImageView imageViewImage;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textView_name);
            textViewDesc = (TextView) itemView.findViewById(R.id.textView_desc);
            imageViewImage = (ImageView) itemView.findViewById(R.id.imageView_cuisine);
        }
    }
}
