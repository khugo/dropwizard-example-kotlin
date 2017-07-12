package com.example.helloworld.tasks

import com.google.common.collect.ImmutableMultimap
import io.dropwizard.servlets.tasks.PostBodyTask
import java.io.PrintWriter

class EchoTask : PostBodyTask("echo") {

    @Throws(Exception::class)
    override fun execute(parameters: ImmutableMultimap<String, String>, body: String, output: PrintWriter) {
        output.print(body)
        output.flush()
    }
}
