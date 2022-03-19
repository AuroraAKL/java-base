package loki.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * http 代理实现类
 * - 支持对代理响应body进行处理
 * - 支持代理请求重试
 *
 * @author zhengquan
 */
public class HttpProxy {

    private final String targetUrl;

    private HttpEntity requestHttpEntity;

    private String responseBodyString;

    /**
     * 重试策略, 是否对请求进行重试
     * 返回值 >= 0 表示进行重试
     * 返回值 < 0 表示不再进行重试, 这次结果将会是有效的. 并进行后续处理
     */
    private Supplier<Long> retryHandler;

    /**
     * 重试次数
     */
    private int retryMaxCount = 2;

    /**
     * 已经重试次数
     */
    private int retryCount;

    /**
     * 已经请求的次数
     */
    private int requestCount;

    /**
     * 返回字符串的结果处理器
     */
    private Supplier<String> stringResponseBodyHandler;

    private final static HttpClientBuilder HTTP_CLIENT_BUILDER = getConnectionBuilder();

    private static HttpClientBuilder getConnectionBuilder() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);
        connectionManager.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(2000).build());
        return HttpClients.custom().setConnectionManager(connectionManager);
    }

    public HttpProxy(@Nonnull String targetUrl) {
        this.targetUrl = targetUrl;
    }

    private void forward(HttpServletResponse httpServletResponse) throws IOException {
        HttpGet loginReq = new HttpGet(targetUrl);
        CloseableHttpClient httpClient = HTTP_CLIENT_BUILDER.build();
        do {
            CloseableHttpResponse response = httpClient.execute(loginReq);
            requestCount++;
            try {
                requestHttpEntity = response.getEntity();
                long retryTime = retryHandler == null ? -1L : retryHandler.get();
                if (retryCount > retryMaxCount || retryTime < 0) {
                    // 请求成功
                    writeResponse(response, httpServletResponse);
                    return;
                } else if (retryTime > 0) {
                    // 重试请求
                    retryCount++;
                    try {
                        Thread.sleep(retryTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                response.close();
            }
        } while (true);
    }

    public void doProxy(HttpServletResponse response) {
        try {
            this.forward(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeResponse(HttpResponse response, HttpServletResponse proxyResponse) throws IOException {
        copyHeader(response.getAllHeaders(), proxyResponse);
        if (stringResponseBodyHandler == null) {
            requestHttpEntity.writeTo(proxyResponse.getOutputStream());
            return;
        }
        String afterResponseBody = stringResponseBodyHandler.get();
        byte[] bytes = afterResponseBody.getBytes(StandardCharsets.UTF_8);
        proxyResponse.setContentLength(bytes.length);
        proxyResponse.getOutputStream().write(bytes);
    }

    private void copyHeader(Header[] headers, HttpServletResponse response) {
        for (Header header : headers) {
            response.setHeader(header.getName(), header.getValue());
        }
    }

    public String getBodyString() {
        if (responseBodyString == null) {
            try {
                responseBodyString = EntityUtils.toString(requestHttpEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseBodyString;
    }

    public long getContentLength() {
        return requestHttpEntity.getContentLength();
    }

    public void setRetryHandler(Supplier<Long> retryHandler) {
        this.retryHandler = retryHandler;
    }

    public void setStringResponseBodyHandler(Supplier<String> stringResponseBodyHandler) {
        this.stringResponseBodyHandler = stringResponseBodyHandler;
    }

    public void setRetryMaxCount(int retryMaxCount) {
        this.retryMaxCount = retryMaxCount;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public int getRequestCount() {
        return requestCount;
    }
}
