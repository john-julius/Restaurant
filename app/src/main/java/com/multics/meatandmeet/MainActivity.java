package com.multics.meatandmeet;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.multics.meatandmeet.adapters.AlcoholAdapter;
import com.multics.meatandmeet.adapters.FoodAdapter;
import com.multics.meatandmeet.adapters.ItemAdapter;
import com.multics.meatandmeet.adapters.OrderAdapter;
import com.multics.meatandmeet.fragments.AlcoholFragment;
import com.multics.meatandmeet.fragments.FoodFragment;
import com.multics.meatandmeet.fragments.HelpFragment;
import com.multics.meatandmeet.fragments.HomeFragment;
import com.multics.meatandmeet.models.Alcohol;
import com.multics.meatandmeet.models.Food;
import com.multics.meatandmeet.models.Item;
import com.multics.meatandmeet.models.Order;
import com.multics.meatandmeet.posts.OrderRequest;
import com.multics.meatandmeet.utilities.CircleAnimationUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        OrderAdapter.IOrderAdapterCallback, ItemAdapter.IItemAdapterCallback,FoodAdapter.IItemAdapterCallback,AlcoholAdapter.IItemAdapterCallback{

    private DrawerLayout drawer;
    private RelativeLayout rlCart;
    private TextView txtCount;
    private TextView txtTotal;
    private RecyclerView rvOrder;
    private TextView txtClearAll;
    private Button btnCompleteOrder;
    private ProgressDialog dialog;
    private OrderAdapter orderAdapter;
    private ArrayList<Order> orderList;

    String tables[] = {"1","2"};

    @Override
    public void onIncreaseDecreaseCallback() {
        updateOrderTotal();
        updateBadge();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //loading the default fragment
        loadFragment(new HomeFragment());

        // Find views
        drawer = findViewById(R.id.dlMain);
        txtTotal = findViewById(R.id.txtTotal);
        rvOrder = findViewById(R.id.rvOrder);
        txtClearAll = findViewById(R.id.txtClearAll);
        btnCompleteOrder = findViewById(R.id.btnCompleteOrder);

        orderList = new ArrayList<Order>();

        // set
        rvOrder.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        orderAdapter = new OrderAdapter(MainActivity.this, orderList);
        rvOrder.setAdapter(orderAdapter);

        btnCompleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderList.size() > 0) {
                    dialogCompleteOrder();
                }
            }
        });

        txtClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderList.size() > 0) {
                    dialogClearAll();
                }
            }
        });


        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cart, menu);

        final View actionCart = menu.findItem(R.id.actionCart).getActionView();

        txtCount = (TextView) actionCart.findViewById(R.id.txtCount);
        rlCart = (RelativeLayout) actionCart.findViewById(R.id.rlCart);

        rlCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOrderDrawer();
            }
        });

        return true;
    }

    /*
     * Updates the value of the badge
     */
    private void updateBadge() {
        if (orderList.size() == 0) {
            txtCount.setVisibility(View.INVISIBLE);
        } else {
            txtCount.setVisibility(View.VISIBLE);
            txtCount.setText(String.valueOf(orderList.size()));
        }
    }

    /*
     * Gets the total price of all products added to the cart
     */
    private double getOrderTotal() {
        double total = 0.0;
        for (Order order : orderList) {
            total += order.extendedPrice;
        }

        return total;
    }

    /*
     * Updates the total price of all products added to the cart
     */
    private void updateOrderTotal() {
        double total = getOrderTotal();
        txtTotal.setText(String.format("%.2f", total));
    }

    /*
     * Makes the cart empty
     */
    private void dialogClearAll() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(MainActivity.this);
        }
        builder.setTitle(R.string.clear_all)
                .setMessage(R.string.delete_all_orders)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clearAll();
                        showMessage(true, getString(R.string.cart_clean));
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }



    /*
     * Closes or opens the drawer
     */
    private void handleOrderDrawer() {
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else {
                drawer.openDrawer(GravityCompat.END);
            }
        }
    }

    /*
     * Completes the order
     */
    private void dialogCompleteOrder() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(MainActivity.this);
        }
        builder.setTitle(getString(R.string.complete_order))
                .setMessage(getString(R.string.complete_order_question))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress(true);
                        new CompleteOrderTask().execute();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    @Override
    public void onItemCallback(Item item) {

    }

    @Override
    public void onAddItemCallback(ImageView imageView, Item item) {

        addItemToCartAnimation(imageView, item, 1);

    }


    private void addItemToCartAnimation(ImageView targetView, final Item item, final int quantity) {
        RelativeLayout destView = (RelativeLayout) findViewById(R.id.rlCart);

        new CircleAnimationUtil().attachActivity(this).setTargetView(targetView).setMoveDuration(300).setDestView(destView).setAnimationListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                addItemToCart(item, quantity);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        }).startAnimation();
    }

    /*
     * Adds the item to order list.
     */
    private void addItemToCart(Item item, int quantity) {
        boolean isAdded = false;

        for (Order order : orderList) {
            if (order.item.getId() == item.getId()) {
                //if item already added to cart, dont add new order
                //just add the quantity
                isAdded = true;
                order.quantity += quantity;
               // order.extendedPrice += item.getPrice();
                break;
            }
        }

        //if item's not added yet
        if (!isAdded) {
            orderList.add(new Order(item, quantity));
        }

        orderAdapter.notifyDataSetChanged();
        rvOrder.smoothScrollToPosition(orderList.size() - 1);
        updateOrderTotal();
        updateBadge();
    }

    @Override
    public void onItemCallback(Food food) {

    }

    @Override
    public void onAddItemCallback(ImageView imageView, Food food) {
        addItemToCartAnimation(imageView, food, 1);

    }

    @Override
    public void onItemCallback(Alcohol alcohol) {

    }

    @Override
    public void onAddItemCallback(ImageView imageView, Alcohol alcohol) {
        addItemToCartAnimation(imageView, alcohol, 1);

    }


    /**
     * Represents an asynchronous task used to complete
     * the order.
     */
    public class CompleteOrderTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
           //sample of id from seat and table
            final  String table_id ="1";
            final String table_seat_id ="1";



            final JSONArray ord = new JSONArray();//array of both seat and itenms
            final JSONObject tseat = new JSONObject(); // object of seat and table
            try {

                tseat.put("table_seat_id",table_seat_id);
                tseat.put("table_id",table_id);
            //looping in order to get all items
            for (int t= 0; t< orderList.size(); t++){
                JSONObject objc = new JSONObject();
                    objc.put("item_id",orderList.get(t).item.getId());
                    objc.put("name",orderList.get(t).item.getName());
                    objc.put("quantity",orderList.get(t).quantity);
                    ord.put(objc);
            }



            } catch (JSONException e) {
                e.printStackTrace();
            }

            Response.Listener<String> responserlistener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success){
                            //move to the new class
                        }

                        else{
                            //show alert
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            };

            OrderRequest orderRequest = new OrderRequest(tseat,ord,responserlistener);  //send to order requests
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            queue.add(orderRequest);


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);
            clearAll();
            showMessage(true, getString(R.string.sent_order));
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
            showMessage(false, getString(R.string.failed_order));
        }

    }





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;

            case R.id.navigation_dashboard:
                fragment = new AlcoholFragment();
                break;

            case R.id.navigation_notifications:
                fragment = new FoodFragment();
                break;

            case R.id.help:
                fragment = new HelpFragment();
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    /**
     * Shows the progress
     */
    private void showProgress(boolean show) {
        if (dialog == null) {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage(getString(R.string.sending_order));
        }

        if (show) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

    /*
     * Clears all orders from the cart
     */
    private void clearAll() {
        orderList.clear();
        orderAdapter.notifyDataSetChanged();

        updateBadge();
        updateOrderTotal();
        handleOrderDrawer();
    }

    /*
     * Shows a message by using Snackbar
     */
    private void showMessage(Boolean isSuccessful, String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

        if (isSuccessful) {
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
        } else {
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
        }

        snackbar.show();
    }

}


