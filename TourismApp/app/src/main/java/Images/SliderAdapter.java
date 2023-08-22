package Images;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tourismapp.ImageUrl;
import com.example.tourismapp.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderViewHolder>{

    private Context context;
    private List<SliderData> sliderList;

    private static final String imgUrl = ImageUrl.imageUrl;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public void setSliderList(List<SliderData> sliderList) {
        this.sliderList = sliderList;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_slider, null);

        return new SliderViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {
        String img = imgUrl + sliderList.get(position).getImgUrl();
        Glide.with(context)
                .asBitmap()
                .load(img.replace("\\", "/"))
                .into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return sliderList.size();
    }

    static class SliderViewHolder extends SliderViewAdapter.ViewHolder {
        ImageView imageView;

        public SliderViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.slider_image);
            imageView.setClipToOutline(true);
        }
    }
}
