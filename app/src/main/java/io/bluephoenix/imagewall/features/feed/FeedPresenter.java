package io.bluephoenix.imagewall.features.feed;

import java.util.ArrayList;
import java.util.List;

import io.bluephoenix.imagewall.features.base.BasePresenter;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class FeedPresenter extends BasePresenter<IFeedContract.PublishToView>
    implements IFeedContract.Presenter
{
    @Override
    public void init()
    {
        List<String> data = new ArrayList<>();
        data.add("String 0");
        data.add("String 1");
        data.add("String 2");
        data.add("String 3");
        data.add("String 4");
        data.add("String 5");
        data.add("String 6");
        data.add("String 7");
        data.add("String 8");
        data.add("String 9");
        data.add("String 10");
        publishToView.init(data);
    }
}
