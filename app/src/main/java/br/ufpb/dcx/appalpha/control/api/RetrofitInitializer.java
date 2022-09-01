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
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInitializer {
    private String BASE_URL = "https://api.apps4society.dcx.ufpb.br/";

    private Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(getUnsafeOkHttpClient()).build();

    // ativar MiTM, para Debugger na conexao
    private static boolean desabilitar_SSL_Pinning = true;

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
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

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if(desabilitar_SSL_Pinning) {
                builder.sslSocketFactory(sslSocketFactory, trustManager);
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            }
			builder.connectTimeout(30, TimeUnit.MINUTES);
            builder.readTimeout(30, TimeUnit.MINUTES);
            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ThemesApiServiceInterface contextService(){
        return retrofit.create(ThemesApiServiceInterface.class);
    }

    public ChallengeApiService challengeService(){
        return retrofit.create(ChallengeApiService.class);
    }

}