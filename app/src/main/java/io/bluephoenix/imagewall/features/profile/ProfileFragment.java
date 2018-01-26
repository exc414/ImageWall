package io.bluephoenix.imagewall.features.profile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bluephoenix.imagewall.R;
import io.bluephoenix.imagewall.core.data.model.User;
import io.bluephoenix.imagewall.features.base.FragmentPresenters;

import static io.bluephoenix.imagewall.core.common.PresenterFragmentDef.PROFILE;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class ProfileFragment extends Fragment implements IProfileContract.PublishToView
{
    @BindView(R.id.profilePostNum)
    TextView profilePostNum;

    @BindView(R.id.profileFollowersNum)
    TextView profileFollowersNum;

    @BindView(R.id.profileFollowingNum)
    TextView profileFollowingNum;

    @BindView(R.id.profilePhoto)
    ImageView profilePhoto;

    @BindView(R.id.profileName)
    TextView profileName;

    @BindView(R.id.profileCountry)
    TextView profileCountry;

    @BindView(R.id.profileWebsite)
    TextView profileWebsite;

    private Unbinder unbinder;
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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        profilePresenter.populateProfile();
        return view;
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy()
    {
        unbinder.unbind();
        profilePresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void populateProfile(User user)
    {
        profileName.setText(user.getName());
        profileCountry.setText(user.getCountry());
        profileWebsite.setText(user.getWebsite());
        profilePostNum.setText(String.valueOf(user.getPosts()));
        profileFollowersNum.setText(String.valueOf(user.getFollowers()));
        profileFollowingNum.setText(String.valueOf(user.getFollowing()));
    }

    @Override
    public void passwordUpdateResponse(int responseType) { }

    @Override
    public void isUnique(int uniqueType) { }
}