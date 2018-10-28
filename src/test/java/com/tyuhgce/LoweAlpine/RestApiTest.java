package com.tyuhgce.LoweAlpine;

import com.tyuhgce.LoweAlpine.base.BaseTest;
import com.tyuhgce.LoweAlpine.utils.RequestParams;
import com.tyuhgce.LoweAlpine.utils.enums.HttpCode;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class RestApiTest extends BaseTest {
    @Test
    public void testHealth() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:4567/ping");
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));

        assertThat(
                IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8"),
                equalTo("pong"));
    }

    @Test
    public void testBookMethodSuccess() throws Exception {
        RequestParams requestParams = new RequestParams("CINEMA", "sun", 1);
        mainStorage.clear(requestParams);
        cachedStorage.clear(requestParams);

        HttpUriRequest request = new HttpGet("http://localhost:4567/book/CINEMA/sun/1");
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));

        assertThat(
                IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8"),
                equalTo(HttpCode.SUCCESS.getDescription()));
        mainStorage.clear(requestParams);
        cachedStorage.clear(requestParams);
    }

    @Test
    public void testBookMethodFailed() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:4567/book/CINEMA/earth/4");
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_LOCKED));

        assertThat(
                IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8"),
                equalTo(HttpCode.LOCKED.getDescription()));
    }

    @Test
    public void testGetMethodSuccess() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:4567/get/CINEMA/earth");
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));

        assert IOUtils
                .toString(httpResponse.getEntity().getContent(), "UTF-8")
                .matches("[0-9]+(?:-[0-9]+)?(,[0-9]+(?:-[0-9]+)?)*");
    }

    @Test
    public void testGetMethodFailed() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:4567/get/notexists/notexists");
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_INTERNAL_SERVER_ERROR));

        assertThat(
                IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8"),
                equalTo(HttpCode.SERVER_ERROR.getDescription()));
    }
}
