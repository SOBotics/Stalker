package in.bhargavrao.stalker.clients;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import in.bhargavrao.stalker.entities.ErrorMessage;
import in.bhargavrao.stalker.entities.Item;
import in.bhargavrao.stalker.entities.Message;
import in.bhargavrao.stalker.entities.SuccessMessage;
import in.bhargavrao.stalker.exceptions.ApiException;
import in.bhargavrao.stalker.utils.JsonUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("/users")
public class GetUsers {
    @GET
    @Path("/suspended")
    @Produces(MediaType.APPLICATION_JSON)
    public Message getAllSuspendedSoOneDay(@QueryParam("hours") Integer hours,
                                           @QueryParam("site") String site){
        if(hours==null)
            hours = 24;
        if(site==null)
            site = "stackoverflow";
        return getSuspendedFrom(hours, site);
    }

    private Message getSuspendedFrom(Integer hours, String site) {
        int i = 1;
        Integer quota = 10000;
        List<Item> timedUsers = new ArrayList<>();

        Long currentTimeInSeconds = System.currentTimeMillis() / 1000L;
        Long fromDate = currentTimeInSeconds-hours*60*60;
        JsonObject json = null;
        do  {

            try {
                json = fetchJsonOnPage(i, fromDate, site);
            } catch (ApiException e) {
                e.printStackTrace();
                return new ErrorMessage(e.getMessage());
            }
            if (json.has("items")) {
                for (JsonElement element : json.get("items").getAsJsonArray()) {
                    JsonObject object = element.getAsJsonObject();
                    if (object.has("timed_penalty_date")) {
                        Item item = new Item();
                        item.setLink(object.get("link").getAsString());
                        item.setUsername(object.get("display_name").getAsString());
                        timedUsers.add(item);
                    }
                }
            }
            quota = json.get("quota_remaining").getAsInt();
            i++;
        }while (!json.get("has_more").getAsBoolean());
        return new SuccessMessage(timedUsers, quota);
    }

    private JsonObject fetchJsonOnPage(int page, Long fromDate, String site) throws ApiException {
        String url = "https://api.stackexchange.com/2.2/users";
        String apiKey = "kmtAuIIqwIrwkXm1*p3qqA((";
        String filter = "!Ln3l_2int_VA.0Iu5wL3aW";


        JsonObject json = null;
        try {
            json = JsonUtils.get(url,
                    "sort", "creation",
                    "site", site,
                    "pagesize", "100",
                    "page", String.valueOf(page),
                    "filter", filter,
                    "order", "desc",
                    "fromdate", String.valueOf(fromDate),
                    "key", apiKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(json.has("error_id")){
            throw new ApiException(json.get("error_message").getAsString());
        }
        JsonUtils.handleBackoff(json);
        return json;
    }
}
