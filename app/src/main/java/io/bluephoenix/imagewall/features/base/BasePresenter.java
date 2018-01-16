package io.bluephoenix.imagewall.features.base;

import android.support.annotation.NonNull;

/**
 * @author Carlos A. Perez Zubizarreta
 */

public abstract class BasePresenter<V>
{
    protected V publishToView;

    public final void attachView(@NonNull V view)
    {
        this.publishToView = view;
    }

    final void detachView()
    {
        this.publishToView = null;
    }
}
