package moun.com.deli.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import moun.com.deli.R;
import moun.com.deli.model.MenuItems;
import moun.com.deli.util.AppUtils;

/**
 * Created by Mounzer on 12/1/2015.
 */
public class HomeMenuCustomAdapter extends RecyclerView.Adapter<HomeMenuCustomAdapter.ViewHolder> {

    private static final String LOG_TAG = HomeMenuCustomAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    private int mResourceId;
    private List<MenuItems> itemList;
    private Context context;

    /**
     * Create a new instance of {@link HomeMenuCustomAdapter}.
     *
     * @param context    Context.
     * @param itemList   List of data.
     * @param inflater   The layout inflater.
     * @param resourceId The resource ID for the layout to be used. The layout should contain an
     *                   ImageView with ID of "meat_image" and a TextView with ID of "meat_title".
     */
    public HomeMenuCustomAdapter(Context context, List<MenuItems> itemList, LayoutInflater inflater, int resourceId) {
        this.itemList = itemList;
        this.context = context;
        mLayoutInflater = inflater;
        mResourceId = resourceId;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;


        public ViewHolder(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.menu_title);
            this.title.setTypeface(AppUtils.getTypeface(v.getContext(), AppUtils.FONT_BOLD));
            image = (ImageView) v.findViewById(R.id.menu_image);

        }


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View view = mLayoutInflater.inflate(mResourceId, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
    //    Log.d(LOG_TAG, "Element " + position + " set.");

        MenuItems menuItems = itemList.get(position);
        viewHolder.image.setImageResource(menuItems.getItemImage());
        viewHolder.title.setText(menuItems.getItemName());

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
