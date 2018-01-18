package io.bluephoenix.imagewall.features.base;

import java.util.ArrayList;
import java.util.List;

import io.bluephoenix.imagewall.common.PresenterFragmentDef.PresenterFragmentType;
import io.bluephoenix.imagewall.features.base.BasePresenter;
import io.bluephoenix.imagewall.features.favourite.FavouritePresenter;
import io.bluephoenix.imagewall.features.feed.FeedPresenter;
import io.bluephoenix.imagewall.features.friends.FriendsPresenter;
import io.bluephoenix.imagewall.features.profile.ProfilePresenter;
import io.bluephoenix.imagewall.util.Constant;

/**
 * @author Carlos A. Perez Zubizarreta Zubizarreta
 */
public class FragmentPresenters
{
    private static List<BasePresenter> presenters =
            new ArrayList<>(Constant.VIEW_PAGER_ITEMS);

    public static BasePresenter getPresenter(@PresenterFragmentType int position)
    {
        if(presenters.isEmpty())
        {
            presenters.add(new FeedPresenter());
            presenters.add(new FriendsPresenter());
            presenters.add(new FavouritePresenter());
            presenters.add(new ProfilePresenter());

            return presenters.get(position);
        }
        else { return presenters.get(position); }
    }
}
