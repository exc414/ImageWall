package io.bluephoenix.imagewall.app;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import io.bluephoenix.imagewall.common.PresenterDef.*;
import io.bluephoenix.imagewall.data.repo.Storage;
import io.bluephoenix.imagewall.data.repo.IRepository;
import io.bluephoenix.imagewall.features.base.BasePresenter;
import io.bluephoenix.imagewall.features.favourite.FavouritePresenter;
import io.bluephoenix.imagewall.features.friends.FriendsPresenter;
import io.bluephoenix.imagewall.features.feed.FeedPresenter;
import io.bluephoenix.imagewall.features.profile.ProfilePresenter;
import io.bluephoenix.imagewall.util.Constant;

/**
 * @author Carlos A. Perez Zubizarreta Zubizarreta
 */
public class Components
{
    private static List<BasePresenter> presenters =
            new ArrayList<>(Constant.NUMBER_OF_VIEW_PAGER_ITEMS);
    private static Storage storage;

    public static BasePresenter getPresenter(@PresenterType int position)
    {
        if(presenters.isEmpty())
        {
            presenters.add(new FeedPresenter());
            presenters.add(new FriendsPresenter());
            presenters.add(new FavouritePresenter());
            presenters.add(new ProfilePresenter());

            return presenters.get(position);
        }
        else
        {
            return presenters.get(position);
        }
    }

    public static IRepository.Storage getStorage()
    {
        if(storage == null) { storage = new Storage(); return storage; }
        return storage;
    }

    public static DatabaseReference getFDBReference()
    {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference getFDBReference(String type)
    {
        return FirebaseDatabase.getInstance().getReference().child(type);
    }
}
