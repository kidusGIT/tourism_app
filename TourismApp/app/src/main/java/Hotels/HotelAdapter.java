package Hotels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tourismapp.DetailActivity;
import com.example.tourismapp.HotelDetailActivity;
import com.example.tourismapp.ImageUrl;
import com.example.tourismapp.R;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {
    private List<HotelModel> hotelList;
    private Context context;
    private static final String imgUrl = ImageUrl.imageUrl;

    public HotelAdapter(Context context) {
        this.context = context;
    }

    public void setHotelList(List<HotelModel> hotelList) {
        this.hotelList = hotelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.toursim_card, parent, false);
       ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(hotelList.get(position).getImg().equals("")) {
            Glide.with(context)
                    .asBitmap()
                    .load("https://images.pexels.com/photos/327098/pexels-photo-327098.jpeg")
                    .into(holder.image);
        } else {
            String img = imgUrl + hotelList.get(position).getImg();
            Glide.with(context)
                    .asBitmap()
                    .load(img.replace("\\", "/"))
                    .into(holder.image);
        }

        holder.name.setText(hotelList.get(position).getName());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HotelDetailActivity.class);
                intent.putExtra("id", hotelList.get(position).getId());
                intent.putExtra("latitude", hotelList.get(position).getLatitude());
                intent.putExtra("longitude", hotelList.get(position).getLongtude());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.tourism_cover_image);
            name = itemView.findViewById(R.id.tourism_name);
            card = itemView.findViewById(R.id.toursim_card);

            image.setClipToOutline(true);

        }
    }
}
