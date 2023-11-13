package org.example;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.groups.UserXtrRole;
import com.vk.api.sdk.objects.groups.responses.GetMembersFieldsResponse;
import com.vk.api.sdk.objects.users.Fields;

import java.util.HashMap;

public class ObjectVK {
    private final long APP_ID;
    private final String CODE;
    private final VkApiClient vk;
    private final UserActor actor;
    private final VkApiClient CLIENT = new VkApiClient(new HttpTransportClient());
    private HashMap<String, String> cities = new HashMap<String, String>();


    public ObjectVK() {
        var configuration = new Configuration();
        var configData = configuration.getConfigData();
        APP_ID = Long.parseLong(configData.get(0)[0]);
        CODE = configData.get(1)[0];
        TransportClient transportClient = new HttpTransportClient();
        vk = new VkApiClient(transportClient);
        actor = new UserActor(APP_ID, CODE);
    }

    public HashMap<String, String> createMapCity() {
        var offset = 0;
        var totalMembers = 0;
        var maxMembers = 1000;
        do {
            GetMembersFieldsResponse response = null;
            try {
                response = vk.groups().getMembersWithFields(actor, Fields.PERSONAL, Fields.CITY)
                        .groupId("6214974")
                        .offset(offset)
                        .count(maxMembers)
                        .execute();
            } catch (ApiException e) {
                throw new RuntimeException(e);
            } catch (ClientException e) {
                throw new RuntimeException(e);
            }

            totalMembers = response.getCount();
            offset += maxMembers;

            for (UserXtrRole memberId : response.getItems()) {
                var name = memberId.getLastName() + " " + memberId.getFirstName();
                if (memberId.getCity() != null) {
                    var city = memberId.getCity().getTitle();
                    cities.put(name, city);
                }
            }
        } while (offset < totalMembers);
        return cities;
    }

    public HashMap<String, String> getMapCity () {
        return cities;
    }
}