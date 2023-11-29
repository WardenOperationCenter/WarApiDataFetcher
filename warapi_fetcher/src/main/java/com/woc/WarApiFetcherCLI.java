package com.woc;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import jakarta.inject.Inject;
import org.jboss.resteasy.reactive.common.NotImplementedYet;
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
        warApiService.fetchAndStoreStaticData(dryRun);
    }
}

@CommandLine.Command(name = "daemon", description = "Continuously fetch api data")
class DaemonFetchCommand implements Runnable {

    @Override
    public void run() {
        throw new NotImplementedYet();
    }
}