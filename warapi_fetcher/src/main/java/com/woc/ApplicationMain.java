package com.woc;

import java.net.URI;
import java.util.Set;

import com.woc.warapi_client.WarApiClient;

import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;


@QuarkusMain
public class ApplicationMain implements QuarkusApplication {
  
  @Override
  public int run(String... args) throws Exception {   
    System.out.println("WarApi Data Fetcher");
    ApplicationMain.processStaticData();
    return 0;
 }

 
  public static void processStaticData(){
       WarApiClient shard3Client = QuarkusRestClientBuilder.newBuilder()
            .baseUri(URI.create("https://war-service-live-3.foxholeservices.com/api"))
            .build(WarApiClient.class);

            Set<String> mapNamesList = shard3Client.mapNamesList();
            String firstMap = "LochMorHex";
            System.out.println(mapNamesList);
            System.out.println(shard3Client.warStatus());
            System.out.println(shard3Client.mapWarReport(firstMap));
            System.out.println(shard3Client.mapStaticData(firstMap));
            System.out.println(shard3Client.mapDynamicData(firstMap));


  }
}