package io.bluephoenix.imagewall.features.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.bluephoenix.imagewall.R;

/**
 * @author Carlos A. Perez Zubizarreta
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.PersonalHolder>
{
    private List<String> data;

    /**
     * Called when RecyclerView needs a new {@link RecyclerView.ViewHolder}
     * of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @Override
    public FeedAdapter.PersonalHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.personal_item_view, parent, false);
        return new PersonalHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link RecyclerView.ViewHolder#itemView} to reflect
     * the item at the given position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(FeedAdapter.PersonalHolder holder, int position)
    {
        holder.txtCardTitle.setText(data.get(position));
       // holder.txtCardDesc.setText(data.get(position));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount()
    {
        return data.size();
    }

    /**
     * Populate the adapter with data.
     * @param data A list of data objects.
     */
    void init(List<String> data)
    {
        this.data = data;
        notifyDataSetChanged();
    }

    static class PersonalHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.imgCardImage)
        ImageView imgCardImage;

        @BindView(R.id.txtCardTitle)
        TextView txtCardTitle;

        @BindView(R.id.txtCardDesc)
        TextView txtCardDesc;

        PersonalHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
