package io.bluephoenix.imagewall.features.wall;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import io.bluephoenix.imagewall.R;
import io.bluephoenix.imagewall.core.common.PresenterActivityDef;
import io.bluephoenix.imagewall.features.base.BaseActivity;
import io.bluephoenix.imagewall.features.login.LoginActivity;
import io.bluephoenix.imagewall.features.post.PostActivity;
import io.bluephoenix.imagewall.features.profile.EditProfileActivity;
import io.bluephoenix.imagewall.util.Constant;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class WallActivity extends BaseActivity implements IWallContract.PublishToView,
        NavigationView.OnNavigationItemSelectedListener
{
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.navView)
    NavigationView navigationView;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    private int[] tabIcons = { R.drawable.ic_view_list_white_24dp,
            R.drawable.ic_group_white_24dp, R.drawable.ic_favorite_white_24dp,
            R.drawable.ic_person_white_24dp };

    private WallPresenter wallPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);
        setToolbar(toolbar, getString(R.string.wall_activity_name));
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        wallPresenter = attachPresenter(WallPresenter.class, PresenterActivityDef.WALL);
        wallPresenter.attachView(this);

        try
        {
            //Set up viewpager
            ViewPagerAdapter viewPagerAdapter =
                    new ViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons();
        }
        catch(NullPointerException ex)
        {
            ex.printStackTrace();
        }

        Log.d(Constant.TAG, "onCreate : " + getClass().getName());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        wallPresenter.isUserAlreadyLoggedIn();
    }

    //Close/release everything.
    @Override
    protected void onDestroy()
    {
        wallPresenter.detachView();
        super.onDestroy();
    }

    /**
     * Assign tab icons.
     *
     * @throws NullPointerException if there is no tab at the specified position.
     */
    @SuppressWarnings("ConstantConditions")
    private void setupTabIcons() throws NullPointerException
    {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    /**
     * Create the menu.
     *
     * @param menu A menu to inflate.
     * @return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.wall_menu, menu);
        return true;
    }

    /**
     * Handles what happens when a button in the toolbar gets clicked.
     *
     * @param item A MenuItem object with the id of the pressed button.
     * @return whether the item was clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.addImageMenu)
        {
            start(PostActivity.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item.
     * @return true so that this view will consume the touch event. False will
     * let the event propagate further up the view tree.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.editProfile : start(EditProfileActivity.class); break;

            case R.id.post: start(PostActivity.class); break;

            case R.id.settings: //do something
                break;

            case R.id.signOut:

                FirebaseAuth.getInstance().signOut();
                startClearPrevious(this, LoginActivity.class);
                break;

            default: break;
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Publish the result of checking if the user is already logged in.
     *
     * @param isUserLoggedIn A boolean with true if user already logged in.
     */
    @Override
    public void isUserLoggedInResponse(boolean isUserLoggedIn)
    {
        //if the user is not login take them to the login activity.
        if(!isUserLoggedIn) { startClearPrevious(this, LoginActivity.class); }
    }
}