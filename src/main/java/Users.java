import lombok.Data;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Users {

    @Data
    static class UserData {
        private String username;
        private String id;
    }

    public static void main(String[] args) {
        List<UserData> users = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 2; j++) {
                UserData userData = new UserData();
                userData.setUsername(String.format("userName%s_role%s", i, j));
                users.add(userData);
                createUserIfNotExists(userData);
            }
        }

    }

    private static void createUserIfNotExists(UserData userData) {
        new Users().getUserByUserName(userData);
        if(Objects.isNull(userData.getId())) {
            new Users().createUser(userData);
        }
    }

    public void getUserByUserName(UserData userData) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet post = new HttpGet(Utils.url + "/users?username="+userData.getUsername());

        try{
            post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            post.setHeader(HttpHeaders.ACCEPT, "application/json");
            post.setHeader(HttpHeaders.AUTHORIZATION, Utils.auth);

            HttpResponse response = client.execute(post);
            String content = EntityUtils.toString(response.getEntity());


            String jsonId = (String)((JSONObject)((JSONArray) new JSONParser().parse(content)).get(0)).get("id");
            userData.setId(jsonId);
        }
        catch(Exception e){
            System.out.println("user " + userData.getUsername()+" not found "+ e);
        }
    }

    public void createUser(UserData userData) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(Utils.url + "/users");

        JSONObject json = new JSONObject();
        json.put("username", userData.getUsername());
        try{
            post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            post.setHeader(HttpHeaders.ACCEPT, "application/json");
            post.setHeader(HttpHeaders.AUTHORIZATION, Utils.auth);
            post.setEntity(new StringEntity(json.toString(), ContentType.APPLICATION_JSON));
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == 401){
                throw new RuntimeException("Atualize o Auth para continuar ");
            } else if(response.getStatusLine().getStatusCode() != 201){
                throw new RuntimeException("Erro ao criar user "+ userData + " status code: "+ response.getStatusLine().getStatusCode());
            } else {
                System.out.println("Usuario "+ userData+" criado");
                getUserByUserName(userData);
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
