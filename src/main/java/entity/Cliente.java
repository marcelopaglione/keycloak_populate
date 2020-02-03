package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.json.simple.JSONObject;
import util.HttpUtils;
import util.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Cliente implements Runnable {

    private String clientName;
    private String id;
    private Boolean deleteIfExists = false;

    @Override
    public void run() {
        if(deleteIfExists){
            setIdByKey();
            if(Objects.nonNull(id)) {
                HttpUtils httpUtils = new HttpUtils();
                String url = Utils.url + "/clients/" + id;
                System.out.println("[DELETE] " + url);
                httpUtils.delete(url);
            }
        } else {
            createIfNotExists();
        }
    }

    private void createIfNotExists() {
        setIdByKey();
        if(Objects.isNull(id)) {
            create();
            addRoleToClientById("ROLE_USUARIO");
        }

    }

    public void addRoleToClientById(String roleName) {
        JSONObject json = new JSONObject();
        json.put("name", roleName);

        new HttpUtils().post(Utils.url + "/clients/"+id+"/roles/", json);
        //clientData.getRoles().add(roleName);
    }

    public void setIdByKey() {
        HttpUtils httpUtils = new HttpUtils();
        String response = httpUtils.get(Utils.url + "/clients?clientId=" + clientName);
        id = httpUtils.getStringFromJsonByKey(response, "id", true);
        System.out.println("[GET] " + clientName + " [ID] " + id);
    }

    public void create() {
        JSONObject json = new JSONObject();
        json.put("clientId", clientName);
        String url = Utils.url + "/clients";
        System.out.println("[POST] "+json.toString() + " [TO] "+url);
        new HttpUtils().post(url, json);
        setIdByKey();
    }

    public static List<String> getClientNames() {
        return Arrays.asList(clientNames);
    }

    public static String[] clientNames = {
            "51763522733_CLIENTE1_ENTIDADE1",
                    "15821530431_CLIENTE2_ENTIDADE2",
                    "78070376368_CLIENTE3_ENTIDADE3",
                    "35760026186_CLIENTE4_ENTIDADE4",
                    "80248730878_CLIENTE5_ENTIDADE5",
                    "81828604666_CLIENTE6_ENTIDADE6",
                    "33144432423_CLIENTE7_ENTIDADE7",
                    "14001725436_CLIENTE8_ENTIDADE8",
                    "7476546385_CLIENTE9_ENTIDADE9",
                    "16757163403_CLIENTE10_ENTIDADE10",
                    "24643823887_CLIENTE11_ENTIDADE11",
                    "74234070548_CLIENTE12_ENTIDADE12",
                    "25254731320_CLIENTE13_ENTIDADE13",
                    "37477621070_CLIENTE14_ENTIDADE14",
                    "81470883104_CLIENTE15_ENTIDADE15",
                    "88028332730_CLIENTE16_ENTIDADE16",
                    "14827553190_CLIENTE17_ENTIDADE17",
                    "25032258024_CLIENTE18_ENTIDADE18",
                    "27134717704_CLIENTE19_ENTIDADE19",
                    "41730200745_CLIENTE20_ENTIDADE20",
                    "35138430548_CLIENTE21_ENTIDADE21",
                    "35437248431_CLIENTE22_ENTIDADE22",
                    "11283144107_CLIENTE23_ENTIDADE23",
                    "75056550272_CLIENTE24_ENTIDADE24",
                    "74235770306_CLIENTE25_ENTIDADE25",
                    "72133250352_CLIENTE26_ENTIDADE26",
                    "55841244868_CLIENTE27_ENTIDADE27",
                    "80018242766_CLIENTE28_ENTIDADE28",
                    "8526830066_CLIENTE29_ENTIDADE29",
                    "34867252387_CLIENTE30_ENTIDADE30",
                    "77776657310_CLIENTE31_ENTIDADE31",
                    "70558221068_CLIENTE32_ENTIDADE32",
                    "84767034736_CLIENTE33_ENTIDADE33",
                    "58164650315_CLIENTE34_ENTIDADE34",
                    "5831767639_CLIENTE35_ENTIDADE35",
                    "7774231706_CLIENTE36_ENTIDADE36",
                    "43008816428_CLIENTE37_ENTIDADE37",
                    "70280861001_CLIENTE38_ENTIDADE38",
                    "47515163534_CLIENTE39_ENTIDADE39",
                    "67540820705_CLIENTE40_ENTIDADE40",
                    "22247065708_CLIENTE41_ENTIDADE41",
                    "30844773107_CLIENTE42_ENTIDADE42",
                    "00554186195_CLIENTE43_ENTIDADE43",
                    "78148143757_CLIENTE44_ENTIDADE44",
                    "58332114500_CLIENTE45_ENTIDADE45",
                    "61671434803_CLIENTE46_ENTIDADE46",
                    "44550254211_CLIENTE47_ENTIDADE47",
                    "67343054147_CLIENTE48_ENTIDADE48",
                    "65500468316_CLIENTE49_ENTIDADE49",
                    "10875138128_CLIENTE50_ENTIDADE50"
    };
}
