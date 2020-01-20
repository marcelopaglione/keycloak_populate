package util;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HttpUtils {

    private HttpClient client;

    public HttpUtils(){
        client = HttpClientBuilder.create().build();
    }

    public void post(String url, JSONObject body){
        HttpPost post = new HttpPost(url);
        try {
            setHeaders(post);
            post.setEntity(new StringEntity(body.toString(), ContentType.APPLICATION_JSON));
            HttpResponse response = client.execute(post);
            handleResponse(response, url);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleResponse(HttpResponse response, String url){
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode >= 200 && statusCode <= 299) {
            System.out.println("Sucesso: " + statusCode + " " + url + " " + this) ;
        } else if (statusCode >= 300 && statusCode <= 399) {
            System.out.println("atualize o auth");
        } else if (statusCode >= 400 && statusCode <= 499) {
            System.out.println("Erro: " + statusCode + " " + url + " " + this);
        }else {
            System.out.println("Erro: "+ statusCode + " " + url + " " + this);
        }
    }

    private void setHeaders(HttpRequestBase post) {
        post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        post.setHeader(HttpHeaders.ACCEPT, "application/json");
        post.setHeader(HttpHeaders.AUTHORIZATION, Utils.auth);
    }

    public String getStringFromJsonByKey(String json, String key, Boolean isArray) {
        try {
            if(json.equals("[]")){
                return null;
            }
            if(isArray){
                return (String)((JSONObject)((JSONArray) new JSONParser().parse(json)).get(0)).get(key);
            }
            return (String)((JSONObject)(new JSONParser().parse(json))).get(key);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delete(String url){
        HttpClient client = HttpClientBuilder.create().build();
        HttpDelete delete = new HttpDelete(url);

        try{
            setHeaders(delete);
            HttpResponse response = client.execute(delete);
            handleResponse(response, url);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public String get(String url){
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);

        try{
            setHeaders(get);
            HttpResponse response = client.execute(get);
            handleResponse(response, url);
            String content = EntityUtils.toString(response.getEntity());
            return content;
        }
        catch(Exception e){
            System.out.println(e);
            return "";
        }
    }
}
