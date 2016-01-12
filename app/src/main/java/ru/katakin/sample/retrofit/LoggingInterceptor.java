package ru.katakin.sample.retrofit;

import android.webkit.URLUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import ru.katakin.sample.util.Logger;


public class LoggingInterceptor implements Interceptor {

    private String indentString = "  ";
    private Type type = new TypeToken<HashMap<String, Object>>() {}.getType();

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Buffer buffer = new Buffer();

        Request request = chain.request();
        Logger.d(request.url().toString());
        switch (request.method()) {
            case "POST":
                RequestBody requestBody = request.body();
                if (requestBody != null && requestBody.contentLength() != 0) {

                    requestBody.writeTo(buffer);
                    Map<String, Object > requestMap = new Gson().fromJson(buffer.readUtf8(), type);
                    Logger.d(getSplitter('-', (100 - " REQUEST ".length()) / 2) + " REQUEST " + getSplitter('-', (100 - " REQUEST ".length()) / 2));
                    format(requestMap, 1);
                    Logger.d(getSplitter('-', 100));
                }
                break;
            case "GET":
                Logger.d(getSplitter('-', (100 - " REQUEST ".length()) / 2) + " REQUEST " + getSplitter('-', (100 - " REQUEST ".length()) / 2));
                format(getParametersFromUrl(request.url().toString()), 1);
                Logger.d(getSplitter('-', 100));
                break;
        }

        Logger.d(getSplitter(' ', 100));
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        if (responseBody != null && responseBody.contentLength() != 0) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            buffer = source.buffer();
            Logger.d(getSplitter('-', (100 - " RESPONSE ".length()) / 2) + " RESPONSE " + getSplitter('-', (100 - " RESPONSE ".length()) / 2));
            Map<String, Object> responseMap = new Gson().fromJson(buffer.readUtf8(), type);
            format(responseMap, 1);
            Logger.d(getSplitter('-', 100));
        }

        return response;
    }

    private Map<String, Object> getParametersFromUrl(String url) {
        Map<String, Object> map = new HashMap<String, Object>();

        if(url!=null && URLUtil.isValidUrl(url)) {
            String[] params = url.split("[&,?]");
            for (String param : params) {
                if (param.contains("=")) {
                    String name = param.split("=")[0];
                    Object value = param.split("=")[1];
                    map.put(name, value);
                }
            }
        }
        return map;
    }

    private String getSplitter(char symbol, int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(symbol);
        }
        return builder.toString();
    }

    private void format(Map<String, Object> map, int column) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            StringBuilder indent = new StringBuilder();
            for (int i = 0; i < column; i++) {
                indent.append(indentString);
            }
            if ('{' == String.valueOf(value).charAt(0)) {
                Logger.d(indent.toString() + key + " {");
                Map<String, Object > newMap = new Gson().fromJson(String.valueOf(value), type);
                format(newMap, column + 1);
                Logger.d(indent.toString() + "}");
            } else {
                if ('[' == String.valueOf(value).charAt(0)) {
                    Logger.d(indent.toString() + key + " [");
                    ArrayList newValue = (ArrayList) value;
                    for (int i = 0; i < newValue.size(); i++) {
                        Logger.d(indent.toString() + indentString + "{");
                        format((Map) newValue.get(i), column + 2);
                        if (i != newValue.size() - 1) {
                            Logger.d(indent.toString() + indentString + "},");
                        } else {
                            Logger.d(indent.toString() + indentString + "}");
                        }
                    }
                    Logger.d(indent.toString() + "]");
                } else {
                    Logger.d(indent.toString() + key + " : " + value);
                }
            }
        }
    }
}