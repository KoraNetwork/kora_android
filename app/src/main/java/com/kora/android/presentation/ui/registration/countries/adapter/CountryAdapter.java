package com.kora.android.presentation.ui.registration.countries.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kora.android.R;
import com.kora.android.presentation.model.Country;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.kora.android.data.network.Constants.API_BASE_URL;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> implements Filterable {

    private final Context mContext;
    private final List<Country> mOriginalCountryList;
    private final List<Country> mFilteredCountryList;
    private final CountryFilter mCountryFilter;

    private OnItemClickListener mOnItemClickListener;

    @Inject
    public CountryAdapter(final Context context) {
        mContext = context;
        mOriginalCountryList = new ArrayList<>();
        mFilteredCountryList = new ArrayList<>();
        mCountryFilter = new CountryFilter(this);
    }

    @Override
    public CountryAdapter.CountryViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
        return new CountryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CountryAdapter.CountryViewHolder holder, final int position) {
        final Country country = mFilteredCountryList.get(position);
        holder.setData(country, position);
    }

    @Override
    public int getItemCount() {
        return mFilteredCountryList.size();
    }

    @Override
    public Filter getFilter() {
        return mCountryFilter;
    }

    public void addAll(List<Country> countryList) {
        mOriginalCountryList.addAll(countryList);
        mFilteredCountryList.addAll(countryList);
        notifyDataSetChanged();
    }

    public class CountryFilter extends Filter {
        private CountryAdapter mCountryAdapter;

        private CountryFilter(final CountryAdapter countryAdapter) {
            super();
            mCountryAdapter = countryAdapter;
        }

        @Override
        protected FilterResults performFiltering(final CharSequence charSequence) {
            mFilteredCountryList.clear();
            final FilterResults filterResults = new FilterResults();
            if (charSequence.length() == 0) {
                mFilteredCountryList.addAll(mOriginalCountryList);
            } else {
                final String filterPattern = charSequence.toString().toLowerCase().trim();
                for (final Country country : mOriginalCountryList) {
                    if (country.getName().toLowerCase().contains(filterPattern)
                            && !mFilteredCountryList.contains(country)) {
                        mFilteredCountryList.add(country);
                    }
                    if (country.getPhoneCode().toLowerCase().contains(filterPattern)
                            && !mFilteredCountryList.contains(country)) {
                        mFilteredCountryList.add(country);
                    }
                }
            }
            filterResults.values = mFilteredCountryList;
            filterResults.count = mFilteredCountryList.size();
            return filterResults;
        }

        @Override
        protected void publishResults(final CharSequence constraint, final FilterResults results) {
            mCountryAdapter.notifyDataSetChanged();
        }
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view_container)
        CardView mCvContainer;
        @BindView(R.id.image_view_country_flag)
        ImageView mIvCountryFlag;
        @BindView(R.id.text_view_country_name_and_phone_code)
        TextView mTvCountryNameAndPhoneCode;

        public CountryViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Country country, final int position) {
            final Context context = mCvContainer.getContext();
            mCvContainer.setOnClickListener(v ->
                    mOnItemClickListener.onClickSelectCounrty(country, position));
            Glide.with(context)
                    .load(API_BASE_URL + country.getFlag())
                    .into(mIvCountryFlag);
            final String countryNameAndPhoneCode = country.getName() +
                    context.getString(R.string.registration_brackets, country.getPhoneCode());
            mTvCountryNameAndPhoneCode.setText(countryNameAndPhoneCode);
        }
    }

    public interface OnItemClickListener {
        void onClickSelectCounrty(final Country country, final int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
