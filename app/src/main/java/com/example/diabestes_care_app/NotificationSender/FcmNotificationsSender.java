package com.example.diabestes_care_app.NotificationSender;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.diabestes_care_app.Ui.Patient_all.Doctor_Profile_P;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FcmNotificationsSender {

    String userFcmToken;
    String title;
    String body;
    Context mContext;
    Activity mActivity;

    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey = "AAAAVpUEzVo:APA91bFsjeSoQmspqE1ehZh6fI6uN9KWbvKB4gM31D_YTiT7ddps1b-L7dHBKsry6UUm2vf8T7peZj7WTni4mJKvfIspw6E5z5oqyYxHRPfJJjsncl_ig-vcxR0w2DbsqxcCV2SZKtsA";

    public FcmNotificationsSender(String userFcmToken, String title, String body,  Context mContext, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;

        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    public void SendNotifications() {

        requestQueue = Volley.newRequestQueue(mActivity);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", title);
            notiObject.put("body", body);

            notiObject.put("icon", "R.drawable.icon"); // enter icon that exists in drawable only
            mainObj.put("notification", notiObject);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {



                    Toast.makeText(mContext, "Is Work and send "+ userFcmToken, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // code run is got error
                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);
                    return header;
                }
            };
            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("TAG",e.getMessage());
        }


    }
}




