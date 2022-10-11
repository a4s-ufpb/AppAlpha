package br.ufpb.dcx.appalpha.control.util;

import android.content.Context;

import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/*
   Helper para obter link Imgur de uma imagem a partir de uma source.
   Criado por Julio Verne
 */

public class ImgurHelper
{
    public static String API_CLIENT_KEY = "520dd4dcbe12c2a";

    public static Retrofit retrofit_imgur = new Retrofit.Builder()
            .baseUrl("https://api.imgur.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static ImgurService service = retrofit_imgur.create(ImgurService.class);

    public class ImgurResponse
    {
        public Boolean success;
        public int status;
        public HashMap<String, Object> data;
    }

    public interface ImgurService
    {
        @Multipart
        @POST("/3/image")
        Call<ImgurResponse> sendImage(@Header("Authorization") String authorization,
                                      @Part("image") RequestBody file);
    }

    public interface ImgurHelperCompletionHandler
    {
        public void success(String link);
        public void failed(String reason);
    }

    public static void getImageLink(Context context, File fileIn, InputStream streamIn, ImgurHelperCompletionHandler handler)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    postAction_url(context, fileIn, streamIn, handler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void postAction_url(Context context, File fileIn, InputStream streamIn, ImgurHelperCompletionHandler handler)
    {
        if(fileIn == null && streamIn==null) {
            handler.failed("No source file.");
            return;
        }
        try {
            File file = fileIn;
            File fileTmp = null;
            if(file == null) {
                InputStream input = streamIn;
                try {
                    fileTmp = File.createTempFile("image", ".jpg", context.getCacheDir());
                    try (OutputStream output = new FileOutputStream(fileTmp)) {
                        byte[] buffer = new byte[4 * 1024]; // or other buffer size
                        int read;
                        while ((read = input.read(buffer)) != -1) {
                            output.write(buffer, 0, read);
                        }
                        output.flush();
                    }
                } finally {
                    input.close();
                }
                file = fileTmp;
            }

            RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);

            Response<ImgurResponse> response = service.sendImage("Client-ID "+ API_CLIENT_KEY, fbody).execute();

            if(fileTmp != null) {
                fileTmp.delete();
            }

            String link = (String)response.body().data.get("link");
            Object respObj[] = {handler, link};
            sendObject(respObj, 1);

        }catch(Exception e) {
            Object respObj[] = {handler, e.toString()};
            sendObject(respObj, 2);
        }
    }

    public static void sendObject(Object[] objects, int type)
    {
        Message msg = handlerCall.obtainMessage();
        msg.what = type;
        msg.obj = objects;
        handlerCall.sendMessage(msg);
    }

    public static final Handler handlerCall = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                ((ImgurHelperCompletionHandler)((Object[])msg.obj)[0]).success((String)((Object[])msg.obj)[1]);
            } else if(msg.what==2){
                ((ImgurHelperCompletionHandler)((Object[])msg.obj)[0]).failed((String)((Object[])msg.obj)[1]);
            }
            super.handleMessage(msg);
        }
    };

}
