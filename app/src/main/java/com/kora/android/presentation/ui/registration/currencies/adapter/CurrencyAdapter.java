package com.kora.android.presentation.ui.registration.currencies.adapter;

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
import com.kora.android.presentation.model.CountryEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.kora.android.data.network.Constants.API_BASE_URL;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> implements Filterable {

    private final Context mContext;
    private final List<CountryEntity> mOriginalCountryEntityList;
    private final List<CountryEntity> mFilteredCountryEntityList;
    private final CountryFilter mCountryFilter;

    private OnItemClickListener mOnItemClickListener;

    @Inject
    public CurrencyAdapter(final Context context) {
        mContext = context;
        mOriginalCountryEntityList = new ArrayList<>();
        mFilteredCountryEntityList = new ArrayList<>();
        mCountryFilter = new CountryFilter(this);
    }

    @Override
    public CurrencyViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_currency, parent, false);
        return new CurrencyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CurrencyViewHolder holder, final int position) {
        final CountryEntity countryEntity = mFilteredCountryEntityList.get(position);
        holder.setData(countryEntity, position);
    }

    @Override
    public int getItemCount() {
        return mFilteredCountryEntityList.size();
    }

    @Override
    public Filter getFilter() {
        return mCountryFilter;
    }

    public void addAll(List<CountryEntity> countryEntityList) {
        mOriginalCountryEntityList.addAll(countryEntityList);
        mFilteredCountryEntityList.addAll(countryEntityList);
        notifyDataSetChanged();
    }

    public class CountryFilter extends Filter {
        private CurrencyAdapter mCountryAdapter;

        private CountryFilter(final CurrencyAdapter countryAdapter) {
            super();
            mCountryAdapter = countryAdapter;
        }

        @Override
        protected FilterResults performFiltering(final CharSequence charSequence) {
            mFilteredCountryEntityList.clear();
            final FilterResults filterResults = new FilterResults();
            if (charSequence.length() == 0) {
                mFilteredCountryEntityList.addAll(mOriginalCountryEntityList);
            } else {
                final String filterPattern = charSequence.toString().toLowerCase().trim();
                for (final CountryEntity countryEntity : mOriginalCountryEntityList) {
                    if (countryEntity.getCurrency().toLowerCase().contains(filterPattern)
                            && !mFilteredCountryEntityList.contains(countryEntity)) {
                        mFilteredCountryEntityList.add(countryEntity);
                    }
                }
            }
            filterResults.values = mFilteredCountryEntityList;
            filterResults.count = mFilteredCountryEntityList.size();
            return filterResults;
        }

        @Override
        protected void publishResults(final CharSequence constraint, final FilterResults results) {
            mCountryAdapter.notifyDataSetChanged();
        }
    }

    public class CurrencyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view_container)
        CardView mCvContainer;
        @BindView(R.id.image_view_country_flag)
        ImageView mIvCountryFlag;
        @BindView(R.id.text_view_country_currency)
        TextView mTvCountryCurrency;

        public CurrencyViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final CountryEntity countryEntity, final int position) {
            final Context context = mCvContainer.getContext();
            mCvContainer.setOnClickListener(v ->
                    mOnItemClickListener.onClickSelectCurrency(countryEntity, position));
            Glide.with(context)
                    .load(API_BASE_URL + countryEntity.getFlag())
                    .into(mIvCountryFlag);
            mTvCountryCurrency.setText(countryEntity.getCurrency());
        }
    }

    public interface OnItemClickListener {
        void onClickSelectCurrency(final CountryEntity countryEntity, final int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
