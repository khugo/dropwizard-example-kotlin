package com.example.helloworld.cli

import com.example.helloworld.HelloWorldConfiguration
import io.dropwizard.cli.ConfiguredCommand
import io.dropwizard.setup.Bootstrap
import net.sourceforge.argparse4j.impl.Arguments
import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import org.slf4j.LoggerFactory
import java.util.*

class RenderCommand : ConfiguredCommand<HelloWorldConfiguration>("render", "Render the template data to console") {
    override fun configure(subparser: Subparser) {
        super.configure(subparser)
        subparser.addArgument("-i", "--include-default")
                .action(Arguments.storeTrue())
                .dest("include-default")
                .help("Also render the template with the default name")
        subparser.addArgument("names").nargs("*")
    }

    override fun run(bootstrap: Bootstrap<HelloWorldConfiguration>,
                     namespace: Namespace,
                     configuration: HelloWorldConfiguration) {
        val template = configuration.buildTemplate()

        if (namespace.getBoolean("include-default")!!) {
            LOGGER.info("DEFAULT => {}", template.render(Optional.empty()))
        }

        for (name in namespace.getList<String>("names")) {
            for (i in 0..999) {
                LOGGER.info("{} => {}", name, template.render(Optional.of(name)))
                Thread.sleep(1000)
            }
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RenderCommand::class.java)
    }
}
