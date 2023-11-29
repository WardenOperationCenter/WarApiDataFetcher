package com.woc.warapi_client;

import java.util.Set;

import com.woc.warapi_client.warapi_client.responses.MapData;
import com.woc.warapi_client.warapi_client.responses.WarMapReport;
import com.woc.warapi_client.warapi_client.responses.WarStatus;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;


@Path("/worldconquest")
public interface WarApiClient {

    @Path("/war")
    @GET
    public WarStatus warStatus();

    @Path("/maps")
    @GET
    public Set<String> mapNamesList();

    @Path("/maps/{map}/static")
    @GET
    public MapData mapStaticData(@PathParam("map") String map);

    @Path("/maps/{map}/dynamic/public")
    @GET
    public MapData mapDynamicData(@PathParam("map") String map);

    @Path("/warReport/{map}")
    @GET
    public WarMapReport mapWarReport(@PathParam("map") String map);
    
    
} 