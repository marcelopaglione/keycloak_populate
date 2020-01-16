import lombok.Data;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Clients {

    public static final int TOTAL_CLIENTS = 7;

    @Data
    static class ClientData {
        private String id;
        private String clientId;
        private List<String> roles = new ArrayList<>();
    }

    public static void main(String[] args) {
        List<ClientData> entities = new ArrayList<>();
        for (int j = 1; j <= TOTAL_CLIENTS; j++) {
            ClientData userData = new ClientData();
            userData.setClientId(String.format("client%s", j));
            entities.add(userData);
            createIfNotExists(userData);
        }

    }

    private static void createIfNotExists(ClientData entityData) {
        new Clients().setIdByKey(entityData);
        if(Objects.isNull(entityData.getId())) {
            new Clients().create(entityData);
            new Clients().addRoleToClientById(entityData.getClientId()+"Role1",entityData);
            new Clients().addRoleToClientById(entityData.getClientId()+"Role2",entityData);
        }
    }

    public void addRoleToClientById(String roleName, ClientData clientData) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(Utils.url + "/clients/"+clientData.getId()+"/roles/");
        JSONObject json = new JSONObject();
        json.put("name", roleName);
        try{
            post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            post.setHeader(HttpHeaders.ACCEPT, "application/json");
            post.setHeader(HttpHeaders.AUTHORIZATION, Utils.auth);
            post.setEntity(new StringEntity(json.toString(), ContentType.APPLICATION_JSON));
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == 401){
                throw new RuntimeException("Atualize o Auth para continuar ");
            } else if(response.getStatusLine().getStatusCode() != 201){
                throw new RuntimeException("Erro ao criar role "+ roleName + " status code: "+ response.getStatusLine().getStatusCode());
            } else {
                clientData.getRoles().add(roleName);
                System.out.println(roleName+" criado");
            }
        }
        catch(Exception e){
            System.out.println(e);
        }

    }

    public void setIdByKey(ClientData userData) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet post = new HttpGet(Utils.url + "/clients?clientId="+userData.getClientId());

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
            System.out.println("user " + userData.getClientId()+" not found ");
        }
    }

    public void create(ClientData entity) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(Utils.url + "/clients");

        JSONObject json = new JSONObject();
        json.put("clientId", entity.getClientId());
        try{
            post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            post.setHeader(HttpHeaders.ACCEPT, "application/json");
            post.setHeader(HttpHeaders.AUTHORIZATION, Utils.auth);
            post.setEntity(new StringEntity(json.toString(), ContentType.APPLICATION_JSON));
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == 401){
                throw new RuntimeException("Atualize o Auth para continuar ");
            } else if(response.getStatusLine().getStatusCode() != 201){
                throw new RuntimeException("Erro ao criar "+ entity + " status code: "+ response.getStatusLine().getStatusCode());
            } else {
                System.out.println(entity+" criado");
                setIdByKey(entity);
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
