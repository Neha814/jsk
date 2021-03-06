package transport.vendor.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;

import transport.com.transportationapp.R;
import transport.vendor.fragments.HomeFragment;
import transport.vendor.fragments.ProfileFragment;
import transport.vendor.fragments.VendorProfileFragment;
import transport.vendor.fragments.WorkListFragment;
import transport.vendor.fragments.WorkListVendorFragment;
import utils.Constants;
import utils.NetConnection;
import utils.StringUtils;

/**
 * Created by bharat on 12/22/15.
 */
public class HomeActivity extends FragmentActivity implements View.OnClickListener {
    static Typeface face;
    static TextView home_tv, worklist_tv, profile_tv, logout_tv;
    static LinearLayout back_layout,edit_layout, cancel_layout;
    static TextView title_tv;
    static ImageView back_img,edit_img, cancel_img;
    SharedPreferences sp;
    private AsyncHttpClient client;
    private ProgressDialog dialog;
    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        face = Typeface.createFromAsset(getAssets(), "Avenir-Book.otf");
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        isConnected = NetConnection.checkInternetConnectionn(getApplicationContext());
        Constants.USER_ID = sp.getString("user_id", "");
        Constants.ROLE_ID = sp.getString("role_id","");
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("inHome",true);
        e.commit();

        initialize();
    }

    private void initialize() {

        client = new AsyncHttpClient();
        client.setTimeout(Constants.connection_timeout);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //  Func.set_title_to_actionbar("Forgot Password", this, mToolbar, false);
        back_layout = (LinearLayout) mToolbar.findViewById(R.id.back_layout);
        title_tv = (TextView) mToolbar.findViewById(R.id.title_tv);
        back_img = (ImageView) mToolbar.findViewById(R.id.back_img);
        edit_img = (ImageView) mToolbar.findViewById(R.id.edit_img);
        edit_layout = (LinearLayout) mToolbar.findViewById(R.id.edit_layout);
        cancel_img = (ImageView) mToolbar.findViewById(R.id.cancel_img);
        cancel_layout = (LinearLayout) mToolbar.findViewById(R.id.cancel_layout);


        home_tv = (TextView) findViewById(R.id.home_tv);
        worklist_tv = (TextView) findViewById(R.id.worklist_tv);
        profile_tv = (TextView) findViewById(R.id.profile_tv);
        logout_tv = (TextView) findViewById(R.id.logout_tv);


        home_tv.setBackgroundColor(Color.parseColor("#d2d4d4"));
        worklist_tv.setBackgroundColor(Color.parseColor("#00ffffff"));
        profile_tv.setBackgroundColor(Color.parseColor("#00ffffff"));
        logout_tv.setBackgroundColor(Color.parseColor("#00ffffff"));


        title_tv.setText("Home");
        title_tv.setTypeface(face);
        home_tv.setTypeface(face);
        worklist_tv.setTypeface(face);
        profile_tv.setTypeface(face);
        logout_tv.setTypeface(face);

        back_layout.setVisibility(View.GONE);
        edit_layout.setVisibility(View.GONE);
        cancel_layout.setVisibility(View.GONE);

        home_tv.setOnClickListener(this);
        worklist_tv.setOnClickListener(this);
        profile_tv.setOnClickListener(this);
        logout_tv.setOnClickListener(this);
        back_img.setOnClickListener(this);
        back_layout.setOnClickListener(this);
        edit_img.setOnClickListener(this);
        edit_layout.setOnClickListener(this);
        cancel_img.setOnClickListener(this);
        cancel_layout.setOnClickListener(this);

        home_tv.setEnabled(false);

        // Add initial fragment
        addInitialFragment();
    }

    private void addInitialFragment() {
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = null;
        fragment = new HomeFragment();
        if (fragment != null) {
            ft.replace(R.id.frame_layout, fragment);
        } else {
            ft.add(R.id.frame_layout, fragment);
        }
        // ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        if (v == home_tv) {
            home_tv.setBackgroundColor(Color.parseColor("#d2d4d4"));
            worklist_tv.setBackgroundColor(Color.parseColor("#00ffffff"));
            profile_tv.setBackgroundColor(Color.parseColor("#00ffffff"));
            logout_tv.setBackgroundColor(Color.parseColor("#00ffffff"));

            Fragment fragment = new HomeFragment();
            CallFragment(fragment);

            home_tv.setEnabled(false);
            worklist_tv.setEnabled(true);
            profile_tv.setEnabled(true);
            logout_tv.setEnabled(true);

            edit_layout.setVisibility(View.GONE);
            cancel_layout.setVisibility(View.GONE);
        }
        if (v == worklist_tv) {
            home_tv.setBackgroundColor(Color.parseColor("#00ffffff"));
            worklist_tv.setBackgroundColor(Color.parseColor("#d2d4d4"));
            profile_tv.setBackgroundColor(Color.parseColor("#00ffffff"));
            logout_tv.setBackgroundColor(Color.parseColor("#00ffffff"));

            if(Constants.ROLE_ID.equals("2")) {
                Fragment fragment = new WorkListFragment();
                CallFragment(fragment);
            } else {
                Fragment fragment = new WorkListVendorFragment();
                CallFragment(fragment);
            }

            home_tv.setEnabled(true);
            worklist_tv.setEnabled(false);
            profile_tv.setEnabled(true);
            logout_tv.setEnabled(true);

            edit_layout.setVisibility(View.GONE);
            cancel_layout.setVisibility(View.GONE);
        }
        if (v == profile_tv) {
            home_tv.setBackgroundColor(Color.parseColor("#00ffffff"));
            worklist_tv.setBackgroundColor(Color.parseColor("#00ffffff"));
            profile_tv.setBackgroundColor(Color.parseColor("#d2d4d4"));
            logout_tv.setBackgroundColor(Color.parseColor("#00ffffff"));

            if(Constants.ROLE_ID.equals("2")){
                Fragment fragment = new ProfileFragment();
                CallFragment(fragment);
            } else {
                Fragment fragment = new VendorProfileFragment();
                CallFragment(fragment);
            }



            home_tv.setEnabled(true);
            worklist_tv.setEnabled(true);
            profile_tv.setEnabled(false);
            logout_tv.setEnabled(true);

            edit_layout.setVisibility(View.GONE);
            cancel_layout.setVisibility(View.GONE);
        }
        if (v == logout_tv) {
            home_tv.setBackgroundColor(Color.parseColor("#00ffffff"));
            worklist_tv.setBackgroundColor(Color.parseColor("#00ffffff"));
            profile_tv.setBackgroundColor(Color.parseColor("#00ffffff"));
            logout_tv.setBackgroundColor(Color.parseColor("#d2d4d4"));

            home_tv.setEnabled(true);
            worklist_tv.setEnabled(true);
            profile_tv.setEnabled(true);
            logout_tv.setEnabled(false);

            showLayoutDailog("Are you sure you want to logout ?");

            edit_layout.setVisibility(View.GONE);
            cancel_layout.setVisibility(View.GONE);
        }

        if (v == back_img || v == back_layout) {
            Log.e("back click", "back click");
            getSupportFragmentManager().popBackStack();
        }

        if(v==edit_img || v==edit_layout){
            ProfileFragment.enableEditing();
            cancel_layout.setVisibility(View.VISIBLE);
            edit_layout.setVisibility(View.GONE);
        }
        if(v==cancel_img || v==cancel_layout){
            ProfileFragment.stopEditing();
            cancel_layout.setVisibility(View.GONE);
            edit_layout.setVisibility(View.VISIBLE);
        }
    }


    private void CallFragment(Fragment fragment) {
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fragment != null) {
            ft.replace(R.id.frame_layout, fragment);
        } else {
            ft.add(R.id.frame_layout, fragment);
        }
        //ft.addToBackStack(null);
        ft.commit();
    }


    public static void ChangeProfileColor() {
        home_tv.setBackgroundColor(Color.parseColor("#00ffffff"));
        worklist_tv.setBackgroundColor(Color.parseColor("#00ffffff"));
        profile_tv.setBackgroundColor(Color.parseColor("#d2d4d4"));
        logout_tv.setBackgroundColor(Color.parseColor("#00ffffff"));

        home_tv.setEnabled(true);
        worklist_tv.setEnabled(true);
        profile_tv.setEnabled(false);
        logout_tv.setEnabled(true);
    }

    public static void ChangeManageWorkOrderColor() {
        home_tv.setBackgroundColor(Color.parseColor("#00ffffff"));
        worklist_tv.setBackgroundColor(Color.parseColor("#d2d4d4"));
        profile_tv.setBackgroundColor(Color.parseColor("#00ffffff"));
        logout_tv.setBackgroundColor(Color.parseColor("#00ffffff"));

        home_tv.setEnabled(true);
        worklist_tv.setEnabled(false);
        profile_tv.setEnabled(true);
        logout_tv.setEnabled(true);
    }

    public static void changeTitle(String title, boolean isShown,boolean isEditShown) {
        title_tv.setText(title);
        title_tv.setTypeface(face);
        if (isShown) {
            back_layout.setVisibility(View.VISIBLE);
            back_img.setVisibility(View.VISIBLE);
        } else if (!isShown) {
            back_layout.setVisibility(View.GONE);
            back_img.setVisibility(View.GONE);
        }
        if(isEditShown){
            edit_layout.setVisibility(View.VISIBLE);
            edit_img.setVisibility(View.VISIBLE);
        }
        else if(!isEditShown){
            edit_layout.setVisibility(View.GONE);
            edit_img.setVisibility(View.GONE);
        }
    }


    protected void showLayoutDailog(String msg) {
        final Dialog dialog;
        dialog = new Dialog(HomeActivity.this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);

        Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(0);
        dialog.getWindow().setBackgroundDrawable(d);
        TextView static_tv, message;
        Button yes_bt, no_bt;

        dialog.setContentView(R.layout.logout_dialog);
        static_tv = (TextView) dialog.findViewById(R.id.static_tv);
        message = (TextView) dialog.findViewById(R.id.message);
        yes_bt = (Button) dialog.findViewById(R.id.yes_bt);
        no_bt = (Button) dialog.findViewById(R.id.no_bt);

        static_tv.setTypeface(face);
        message.setTypeface(face);
        yes_bt.setTypeface(face);
        no_bt.setTypeface(face);

        message.setText(msg);

        dialog.show();

        no_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                logout_tv.setEnabled(true);
            }
        });
        yes_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                logout_tv.setEnabled(true);
                CallLogoutAPI();
            }
        });

    }

    private void CallLogoutAPI() {
        if (isConnected) {
            sp.edit().clear().commit();
            finish();
            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(i);
        } else {
            StringUtils.showDialog(Constants.NO_INTERNET, HomeActivity.this);
        }
    }

    public static void SetEditCancelToDefault() {
        cancel_layout.setVisibility(View.GONE);
        edit_layout.setVisibility(View.VISIBLE);
    }


}
