/*
 * Copyright 2012 GitHub Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.pockethub.ui.gist;

import static android.app.Activity.RESULT_OK;
import static com.github.pockethub.RequestCodes.GIST_CREATE;
import static com.github.pockethub.RequestCodes.GIST_VIEW;
import android.content.Intent;

import com.alorma.github.sdk.bean.dto.response.Gist;
import com.alorma.github.sdk.bean.dto.response.GithubEvent;
import com.alorma.github.sdk.services.client.GithubClient;
import com.alorma.github.sdk.services.gists.UserGistsClient;
import com.github.pockethub.accounts.GitHubAccount;
import com.github.pockethub.core.ResourcePager;
import com.github.pockethub.core.gist.GistPager;
import com.google.inject.Inject;
import com.google.inject.Provider;

import org.eclipse.egit.github.core.Commit;

import com.github.pockethub.core.PageIterator;

import java.util.List;

/**
 * Fragment to display a list of Gists
 */
public class MyGistsFragment extends GistsFragment {

    @Inject
    private Provider<GitHubAccount> accountProvider;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == GIST_CREATE || requestCode == GIST_VIEW)
                && RESULT_OK == resultCode) {
            forceRefresh();
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected ResourcePager<Gist> createPager() {
        return new GistPager(store) {

            @Override
            public PageIterator<Gist> createIterator(int page, int size) {
                return new PageIterator<>(new PageIterator.GitHubRequest<List<Gist>>() {
                    @Override
                    public GithubClient<List<Gist>> execute(int page) {
                        return new UserGistsClient(getActivity(),
                                accountProvider.get().getUsername(), page);
                    }
                }, page);
            }
        };
    }
}
