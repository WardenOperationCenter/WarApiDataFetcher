package com.woc.warapi_client;

import java.net.URI;
import java.util.Set;

import com.woc.warapi_client.dto.MapData;
import com.woc.warapi_client.dto.WarMapReport;
import com.woc.warapi_client.dto.WarStatus;

import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;
import jakarta.enterprise.inject.Default;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/worldconquest")
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

    static WarApiClient instantiate(String endpoint) {
        return QuarkusRestClientBuilder.newBuilder()
                .baseUri(URI.create(endpoint))
                .build(WarApiClient.class);
    }
}
