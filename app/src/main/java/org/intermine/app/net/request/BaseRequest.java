package org.intermine.app.net.request;

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

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.apache.http.client.HttpClient;
import org.intermine.app.InterMineApplication;
import org.intermine.app.net.DefaultRetryPolicy;
import org.intermine.app.net.HttpUtils;
import org.intermine.app.net.ServerErrorHandler;
import org.intermine.app.storage.Storage;
import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import javax.inject.Inject;

public abstract class BaseRequest<T> extends SpringAndroidSpiceRequest<T> {
    public final static String CONTENT_ENCODING = "UTF-8";

    @Inject
    Storage mStorage;
    private Context mContext;
    private String mUrl;
    private Map<String, ?> mUrlParams;

    public BaseRequest(Class<T> clazz, Context ctx, String url, Map<String, ?> params) {
        super(clazz);

        setContext(ctx);
        setUrl(url);
        setUrlParams(params);

        InterMineApplication app = InterMineApplication.get(ctx);
        app.inject(this);

        setRetryPolicy(new DefaultRetryPolicy());
    }

    @Override
    public RestTemplate getRestTemplate() {
        HttpClient httpClient = HttpUtils.getNewHttpClient();

        RestTemplate rtp = super.getRestTemplate();
        rtp.setErrorHandler(new ServerErrorHandler());
        rtp.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

        return rtp;
    }

    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();

        headers.setAcceptEncoding(new ContentCodingType(CONTENT_ENCODING));
        headers.setContentEncoding(new ContentCodingType(CONTENT_ENCODING));
        return headers;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public Map<String, ?> getUrlParams() {
        return mUrlParams;
    }

    public void setUrlParams(Map<String, ?> urlParams) {
        mUrlParams = urlParams;
    }

    public Storage getStorage() {
        return mStorage;
    }

    protected String getBaseUrl(String mineName) {
        return getStorage().getMineNameToUrlMap().get(mineName);
    }
}
