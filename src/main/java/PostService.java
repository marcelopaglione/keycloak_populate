
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;

public class PostService {

    static String url = "http://localhost:8080/HttpRequestSample/RequestSend.jsp";

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new PostService().doPost(String.valueOf(i));
        }
    }

    private  void doPost(String id){
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        method.setRequestHeader("Content-type",
                "text/xml; charset=ISO-8859-1");
        setParams(method, id);
        try {
            int statusCode = client.executeMethod(method);
            System.out.println("Status Code = " + statusCode);
            System.out.println("QueryString>>> " + method.getQueryString());
            System.out.println("Status Text>>>" + HttpStatus.getStatusText(statusCode));

            System.out.println(method.getResponseBodyAsString());
            byte[] res = method.getResponseBody();

            method.releaseConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setParams(PostMethod method, String id) {
        NameValuePair nvp1 = new NameValuePair("firstName", "fname" + id);
        NameValuePair nvp2 = new NameValuePair("lastName", "lname" + id);
        NameValuePair nvp3 = new NameValuePair("email", "fname" + id + "@" + "lname" + id);
        method.setQueryString(new NameValuePair[]{nvp1, nvp2, nvp3});
    }
}