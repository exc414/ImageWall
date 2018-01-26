package io.bluephoenix.imagewall.features.feed;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.bluephoenix.imagewall.R;
import io.bluephoenix.imagewall.features.base.FragmentPresenters;
import io.bluephoenix.imagewall.util.CardDecorator;

import static io.bluephoenix.imagewall.core.common.PresenterFragmentDef.FEED;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class FeedFragment extends Fragment implements IFeedContract.PublishToView
{
    @BindView(R.id.personalRecyclerView)
    RecyclerView personalRecyclerView;

    private FeedPresenter feedPresenter;
    private FeedAdapter feedAdapter;

    public FeedFragment() { }

    public static FeedFragment newInstance()
    {
        return new FeedFragment();
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
        feedPresenter = (FeedPresenter) FragmentPresenters.getPresenter(FEED);
        feedPresenter.attachView(this);
        feedAdapter = new FeedAdapter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, view);

        personalRecyclerView.setAdapter(feedAdapter);

        int cardPadding = getResources().getDimensionPixelSize(
                R.dimen.recycler_view_item_padding);
        personalRecyclerView.addItemDecoration(new CardDecorator(cardPadding));
        personalRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        feedPresenter.init();
    }

    @Override
    public void init(List<String> data)
    {
        feedAdapter.init(data);
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy()
    {
        feedPresenter.detachView();
        super.onDestroy();
    }
}