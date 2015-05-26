package org.intermine.app.util;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import android.net.Uri;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Helper to work with URI/URL
 *
 * @author Daria Komkova <Daria.Komkova @ hotmail.com>
 */
public class Uris {
    /**
     * Expands given map of parameters to string and attaches to given URI
     *
     * @param uriString Base URL string with destination
     * @param params    Map of query parameters to attach
     * @return Expanded URL with parameters
     */
    public static String expandQuery(String uriString, Map<String, ?> params) {
        return expandQuery(uriString, params, false);
    }

    /**
     * Expands given map of parameters to string and attaches to given URI
     *
     * @param uriString Base URL string with destination
     * @param params    Map of query parameters to attach
     * @param empty     Whether to attach parameters with empty values or not
     * @return Expanded URL with parameters
     */
    public static String expandQuery(String uriString, Map<String, ?> params, boolean empty) {
        if (null == params) {
            return uriString;
        }

        Uri.Builder uriBuilder = Uri.parse(uriString).buildUpon();

        for (Entry<String, ?> entrySet : params.entrySet()) {
            String key = entrySet.getKey();

            if (null != entrySet.getValue()) {
                String value = entrySet.getValue().toString();

                if ((null != value && empty) || !Strs.isNullOrEmpty(value)) {
                    uriBuilder.appendQueryParameter(key, value);
                }
            }
        }

        return uriBuilder.build().toString();
    }
}
