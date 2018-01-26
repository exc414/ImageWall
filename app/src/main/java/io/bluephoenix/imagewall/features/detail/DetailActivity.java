package io.bluephoenix.imagewall.features.detail;

import android.os.Bundle;
import android.util.Log;

import io.bluephoenix.imagewall.R;
import io.bluephoenix.imagewall.core.common.PresenterActivityDef;
import io.bluephoenix.imagewall.features.base.BaseActivity;
import io.bluephoenix.imagewall.util.Constant;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class DetailActivity extends BaseActivity implements IDetailContract.PublishToView
{
    private DetailPresenter detailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        detailPresenter = attachPresenter(DetailPresenter.class, PresenterActivityDef.DETAIL);
        detailPresenter.attachView(this);
        Log.d(Constant.TAG, "onCreate : " + getClass().getName());
    }
}
