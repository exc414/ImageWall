package io.bluephoenix.imagewall.features.favourite;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import io.bluephoenix.imagewall.R;
import io.bluephoenix.imagewall.features.base.FragmentPresenters;

import static io.bluephoenix.imagewall.common.PresenterFragmentDef.FAVOURITE;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class FavouriteFragment extends Fragment implements IFavouriteContract.PublishToView
{
    private FavouritePresenter favouritePresenter;

    public FavouriteFragment() { }

    public static FavouriteFragment newInstance() { return new FavouriteFragment(); }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Activity)} and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        favouritePresenter = (FavouritePresenter) FragmentPresenters.getPresenter(FAVOURITE);
        favouritePresenter.attachView(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy()
    {
        favouritePresenter.detachView();
        super.onDestroy();
    }
}
