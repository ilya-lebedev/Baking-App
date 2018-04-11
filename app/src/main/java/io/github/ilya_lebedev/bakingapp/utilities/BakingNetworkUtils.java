/*
 * Copyright (C) 2018 Ilya Lebedev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.ilya_lebedev.bakingapp.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * BakingNetworkUtils
 */
public class BakingNetworkUtils {

    /* Timeouts for url connection */
    private static final int TIMEOUT_CONNECT = 5000;
    private static final int TIMEOUT_READ = 10000;

    private static final String SCANNER_DELIMITER = "\\A";

    /* Base url of baking app recipe list JSON */
    private static final String BAKING_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    /**
     * Retrieves the URL to query the recipes data.
     *
     * @return URL to query Baking app service
     */
    public static URL getRecipeListUrl() {
        URL recipeListUrl = null;

        try {
            recipeListUrl = new URL(BAKING_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return recipeListUrl;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {

        /* Open connection for a given uri */
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        /* Set connection and read timeouts */
        urlConnection.setConnectTimeout(TIMEOUT_CONNECT);
        urlConnection.setReadTimeout(TIMEOUT_READ);

        try {
            /* Get a stream to read data from it */
            InputStream inputStream = urlConnection.getInputStream();

            /* Put stream inside scanner */
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter(SCANNER_DELIMITER);

            /* Read data */
            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();

            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

}
