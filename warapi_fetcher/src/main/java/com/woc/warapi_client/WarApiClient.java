package com.woc.warapi_client;

import java.util.Set;

import com.woc.warapi_client.dto.MapData;
import com.woc.warapi_client.dto.WarMapReport;
import com.woc.warapi_client.dto.WarStatus;

import jakarta.enterprise.inject.Default;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@Path("/worldconquest")
@RegisterRestClient(baseUri = "https://war-service-live.foxholeservices.com/api")
@Default
public interface WarApiClient {

    @Path("/war")
    @GET
    WarStatus warStatus();

    @Path("/maps")
    @GET
    Set<String> mapNamesList();

    @Path("/maps/{map}/static")
    @GET
    MapData mapStaticData(@PathParam("map") String map);

    @Path("/maps/{map}/dynamic/public")
    @GET
    MapData mapDynamicData(@PathParam("map") String map);

    @Path("/warReport/{map}")
    @GET
    WarMapReport mapWarReport(@PathParam("map") String map);
}