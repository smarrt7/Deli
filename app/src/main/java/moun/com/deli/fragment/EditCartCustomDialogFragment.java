package moun.com.deli.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import moun.com.deli.MyCartActivity;
import moun.com.deli.R;
import moun.com.deli.database.ItemsDAO;
import moun.com.deli.model.MenuItems;
import moun.com.deli.util.AppUtils;

/**
 * Created by Mounzer on 12/7/2015.
 */
public class EditCartCustomDialogFragment extends DialogFragment {

    public static final String ARG_ITEM_ID = "custom_dialog_fragment";
    private TextView itemTitle;
    private TextView itemDescription;
    private TextView totalPrice;
    private MenuItems menuItems;
    private Spinner qtySpinner;
    private ItemsDAO itemDAO;
    private UpdateItemTask task;


    /*
	 * Callback used to communicate with MyCartFragment to notify the list adapter.
	 * MyCartActivity implements this interface and communicates with MyCartFragment.
	 */
    public interface EditCartDialogFragmentListener {
        void onFinishDialog();
    }

    public EditCartCustomDialogFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemDAO = new ItemsDAO(getActivity());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        menuItems = bundle.getParcelable("selectedItem");

        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(true);

        itemTitle = (TextView) dialog.findViewById(R.id.item_title);
        itemDescription = (TextView) dialog.findViewById(R.id.item_description);
        qtySpinner = (Spinner) dialog.findViewById(R.id.spinner_qty);
        totalPrice = (TextView) dialog.findViewById(R.id.total_price);
        setItemsData();
        qtySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                int qty = (int) qtySpinner.getSelectedItem();
                totalPrice.setText(String.valueOf("$" + qty * menuItems.getItemPrice()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Add to cart
        dialog.findViewById(R.id.order_button).setVisibility(View.GONE);

        // Update
        dialog.findViewById(R.id.update_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getItemsData();
                task = new UpdateItemTask(getActivity());
                task.execute((Void) null);
                MyCartActivity myCartActivity = (MyCartActivity) getActivity();
                myCartActivity.onFinishDialog();
                dismiss();
            }

        });

        // Close
        dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }

        });
        return dialog;

    }

    private void setItemsData(){
        Integer[] quantity = new Integer[] { 1, 2, 3, 4, 5, 6 };
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(),
                android.R.layout.simple_spinner_item, quantity);
        qtySpinner.setAdapter(adapter);
        int position = adapter.getPosition(menuItems.getItemQuantity());
        if(menuItems != null){
            itemTitle.setText(menuItems.getItemName());
            itemTitle.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOLD));
            itemDescription.setText(menuItems.getItemDescription());
            itemDescription.setTypeface(AppUtils.getTypeface(getActivity(), AppUtils.FONT_BOOK));
            qtySpinner.setSelection(position);
        }
    }

    private void getItemsData(){
        menuItems.setItemName(menuItems.getItemName());
        menuItems.setItemDescription(menuItems.getItemDescription());
        menuItems.setItemImage(menuItems.getItemImage());
        menuItems.setItemPrice(menuItems.getItemPrice());
        menuItems.setItemQuantity(Integer.parseInt(qtySpinner.getSelectedItem().toString()));


    }

    public class UpdateItemTask extends AsyncTask<Void, Void, Long> {

        private final WeakReference<Activity> activityWeakRef;

        public UpdateItemTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected Long doInBackground(Void... arg0) {
            long result = itemDAO.updateCartTable(menuItems);
            return result;
        }

        @Override
        protected void onPostExecute(Long result) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                if (result != -1) {
                    AppUtils.CustomToast(activityWeakRef.get(), "Item Updated");
                    Log.d("ITEM: ", menuItems.toString());
                }
            }
        }
    }
}
