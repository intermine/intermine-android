package org.intermine.app.adapter;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.intermine.app.R;
import org.intermine.app.core.ListItems;
import org.intermine.app.util.Collections;
import org.intermine.app.util.Strs;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class ListAdapter extends BaseAdapter {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final int mAccentColor;

    private List<List<String>> mFeatures;
    private List<String> mFeaturesNames;

    public ListAdapter(Context ctx) {
        mContext = ctx;
        mAccentColor = ctx.getResources().getColor(R.color.im_green);

        mLayoutInflater = LayoutInflater.from(ctx);
        mFeatures = new ArrayList<>();
    }

    public void addListItems(ListItems listItems) {
        if (!Collections.isNullOrEmpty(listItems.getFeatures())) {
            mFeatures.addAll(listItems.getFeatures());
        }

        if (!Collections.isNullOrEmpty(listItems.getFeaturesNames())) {
            if (!Strs.isNullOrEmpty(listItems.getRootClass())) {
                String regExp = listItems.getRootClass() + " > ";
                mFeaturesNames = Collections.newArrayList();

                for (String featureName : listItems.getFeaturesNames()) {
                    mFeaturesNames.add(featureName.replace(regExp, ""));
                }
            } else {
                mFeaturesNames = new ArrayList<>(listItems.getFeaturesNames());
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null != mFeatures) {
            return mFeatures.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != mFeatures) {
            return mFeatures.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView listItem;

        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.list_item, parent, false);

            listItem = (TextView) convertView.findViewById(R.id.list_item);
            convertView.setTag(R.id.list_item, listItem);
        } else {
            listItem = (TextView) convertView.getTag(R.id.list_item);
        }

        List<String> features = (List<String>) getItem(position);
        listItem.setText(generateText(mFeaturesNames, features));
        return convertView;
    }

    protected SpannableStringBuilder generateText(List<String> featuresNames, List<String> features) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (!Collections.isNullOrEmpty(features)) {

            for (int i = 0; i < featuresNames.size(); i++) {
                String feature = features.get(i);

                if (!Strs.isNullOrEmpty(feature)) {
                    String featureName = featuresNames.get(i) + ": ";
                    Spannable featureNameSpannable = Strs.spanWithBoldFont(featureName, 0,
                            featureName.length(), mAccentColor);
                    builder.append(featureNameSpannable).append(feature);

                    if (i != featuresNames.size() - 1) {
                        builder.append(", \n");
                    }
                }
            }
        }
        return builder;
    }
}