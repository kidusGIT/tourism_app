package Images;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.toursimadmin.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context context;
    private List<ImageModel> imageList;
    private String imgUrl = "http://192.168.43.202:8800/";

    public void setImageList(List<ImageModel> imageList) {
        this.imageList = imageList;
    }

    public ImageAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.images_list, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String img = imgUrl + imageList.get(position).getImgUrl();
        Glide.with(context)
                .asBitmap()
                .load(img.replace("\\", "/"))
                .into(holder.image);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageCrud imageCrud = new ImageCrud(context);
                imageCrud.deleteImage(imageList.get(position).getId());
                imageList.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ImageButton delete;
        ConstraintLayout card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image_cover_list);
            delete = itemView.findViewById(R.id.delete_image_btn);
            card = itemView.findViewById(R.id.image_list_card);

            image.setClipToOutline(true);

        }
    }
}
