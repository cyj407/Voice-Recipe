package cuisineList;

import android.support.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;

public class StringRequest_utf8 extends StringRequest{

    public StringRequest_utf8(int method, String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public StringRequest_utf8(String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String s = null;
        try{
            s = new String(response.data,"utf8");
        }catch (UnsupportedEncodingException e){}

        return Response.success(s, HttpHeaderParser.parseCacheHeaders(response));
    }
}
