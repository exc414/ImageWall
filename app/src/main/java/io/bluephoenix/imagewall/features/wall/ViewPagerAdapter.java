package io.bluephoenix.imagewall.features.wall;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.bluephoenix.imagewall.features.favourite.FavouriteFragment;
import io.bluephoenix.imagewall.features.feed.FeedFragment;
import io.bluephoenix.imagewall.features.friends.FriendsFragment;
import io.bluephoenix.imagewall.features.profile.ProfileFragment;
import io.bluephoenix.imagewall.util.Constant;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class ViewPagerAdapter extends FragmentPagerAdapter
{
    ViewPagerAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position An int which detonates the position of each fragment.
     */
    @Override
    public Fragment getItem(int position)
    {
        switch(position)
        {
            case 0: return FeedFragment.newInstance();
            case 1: return FriendsFragment.newInstance();
            case 2: return FavouriteFragment.newInstance();
            case 3: return ProfileFragment.newInstance();
            default: return FeedFragment.newInstance();
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() { return Constant.VIEW_PAGER_ITEMS; }
}
