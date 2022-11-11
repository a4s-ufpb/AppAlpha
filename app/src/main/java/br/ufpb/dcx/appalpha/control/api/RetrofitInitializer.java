package br.ufpb.dcx.appalpha.control.api;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import br.ufpb.dcx.appalpha.control.service.interfaces.ChallengeApiService;
import br.ufpb.dcx.appalpha.control.service.interfaces.ThemesApiServiceInterface;
import br.ufpb.dcx.appalpha.view.activities.MainActivity;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInitializer {

    public static String URL_PROD = ApiConfig.getInstance(MainActivity.getMainContext()).getDominio();

    public static String URL_DEV = "http://192.168.0.189:8080/";

    public static String BASE_URL = URL_PROD;

    private Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(getOkHttpClient()).build();

    // ativar MiTM, para Debugger na conexao
    private static boolean desabilitar_SSL_Pinning = false;

    private static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        try {

            builder.connectTimeout(30, TimeUnit.MINUTES);
            builder.readTimeout(30, TimeUnit.MINUTES);

            if(desabilitar_SSL_Pinning) {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[] {
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                X509TrustManager trustManager = (X509TrustManager) trustAllCerts[0];

                builder.sslSocketFactory(sslSocketFactory, trustManager);
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.build();
    }

    public ThemesApiServiceInterface contextService(){
        return retrofit.create(ThemesApiServiceInterface.class);
    }

    public ChallengeApiService challengeService(){
        return retrofit.create(ChallengeApiService.class);
    }

}