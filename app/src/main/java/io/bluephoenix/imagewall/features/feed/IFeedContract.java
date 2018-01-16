package io.bluephoenix.imagewall.features.feed;

import java.util.List;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public interface IFeedContract
{
    interface PublishToView
    {
        void init(List<String> data);
    }

    interface Presenter
    {
        void init();
    }
}
