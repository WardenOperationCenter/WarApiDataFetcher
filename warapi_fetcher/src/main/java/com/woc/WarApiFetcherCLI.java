package com.woc;

import com.woc.entity.ShardConfEntity;
import io.quarkus.logging.Log;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import jakarta.inject.Inject;
import picocli.CommandLine;


@TopCommand
@CommandLine.Command(mixinStandardHelpOptions = true, subcommands = {StaticFetchCommand.class, DaemonFetchCommand.class})
class EntryCommand {
}

@CommandLine.Command(name = "static", description = "Fetch all static data from the api")
class StaticFetchCommand implements Runnable {

    @CommandLine.Option(names = {"-d", "--dry-run"}, description = "Perform queries and log output rather than storing in mongoDB")
    boolean dryRun = false;

    @Inject
    WarApiService warApiService;

    @Override
    public void run() {
        for (ShardConfEntity shardConf : ShardConfEntity.listAllActive()) {
            Log.info(String.format("Processing server %s", shardConf.getName()));
            warApiService.fetchAndStoreStaticData(shardConf, dryRun);
        }
    }
}

@CommandLine.Command(name = "daemon", description = "Continuously fetch api data")
class DaemonFetchCommand implements Runnable {
    @Inject
    WarApiService warApiService;

    @Override
    public void run() {
        for (ShardConfEntity shardConf : ShardConfEntity.listAllActive()) {
            Log.info(String.format("Processing server %s", shardConf.getName()));
            warApiService.fetchAndStoreDynamicData(shardConf);
        }
    }
}