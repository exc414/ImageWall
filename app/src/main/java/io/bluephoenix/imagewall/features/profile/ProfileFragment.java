package io.bluephoenix.imagewall.features.profile;

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

import static io.bluephoenix.imagewall.common.PresenterFragmentDef.PROFILE;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class ProfileFragment extends Fragment implements IProfileContract.PublishToView
{
    private ProfilePresenter profilePresenter;

    public static ProfileFragment newInstance()
    {
        return new ProfileFragment();
    }

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
        profilePresenter = (ProfilePresenter) FragmentPresenters.getPresenter(PROFILE);
        profilePresenter.attachView(this);
        profilePresenter.populateProfile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
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
        profilePresenter.detachView();
        super.onDestroy();
    }
}