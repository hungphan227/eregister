import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

public class Test {

    public static void main(String[] args) throws Exception {
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http")).build();
        System.out.println("---------------------------STARTED----------------------------");

        Request request = new Request(
                "GET",
                "/studentmngm/_search");
        request.setEntity(new NStringEntity(
                "{\n" +
                        "    \"query\": {\n" +
                        "        \"query_string\": {\n" +
                        "            \"query\": \"AAA Dijkstra\"\n" +
                        "        }\n" +
                        "    }\n" +
                        "}",
                ContentType.APPLICATION_JSON));
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
    }

}
