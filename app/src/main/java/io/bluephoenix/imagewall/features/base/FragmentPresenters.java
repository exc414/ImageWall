package io.bluephoenix.imagewall.features.base;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import io.bluephoenix.imagewall.app.App;
import io.bluephoenix.imagewall.core.common.PresenterFragmentDef.PresenterFragmentType;
import io.bluephoenix.imagewall.core.data.repo.Preferences;
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
            presenters.add(new ProfilePresenter(
                    FirebaseAuth.getInstance(),
                    FirebaseDatabase.getInstance().getReference(),
                    new Preferences(App.getPrefs())));

            return presenters.get(position);
        }
        else { return presenters.get(position); }
    }
}
