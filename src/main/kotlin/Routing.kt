package foel.li

import foel.li.model.tasks
import foel.li.model.tasksAsTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/tasks") {
            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = tasks.tasksAsTable()
            )
        }
        route("/hello") {
            get {
                call.respondText("Heyho")
            }
            get("/minecraft") {
                call.respondText("Heyho, Minecraft Freunde")
            }
        }
    }
}