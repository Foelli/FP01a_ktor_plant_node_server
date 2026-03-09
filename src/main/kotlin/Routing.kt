package foel.li

import foel.li.model.Priority
import foel.li.model.Task
import foel.li.model.TaskRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException

fun Application.configureRouting() {
    routing {
        staticResources("static", "static")
        route("/tasks") {
            get {
                val tasks = TaskRepository.allTasks()
                call.respond(HttpStatusCode.OK, tasks)
            }
            get("/byName/{taskName}"){
                val name = call.parameters["taskName"]
                if (name == null){
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
            }
            get("/byPriority/{priority}"){
                val priorityAsText = call.parameters["priority"]
                if (priorityAsText == null){
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                try {
                    val priority = Priority.valueOf(priorityAsText)
                    val tasks = TaskRepository.tasksByPriority(priority)

                    if (tasks.isEmpty()){
                        call.respond(HttpStatusCode.NotFound)
                        return@get
                    }
                    call.respond(tasks)
                } catch (ex: IllegalArgumentException){
                    call.respond(HttpStatusCode.BadRequest)
                }

            }
            post {
                try {
                    val task = call.receive<Task>()
                    TaskRepository.addTask(task)
                    call.respond(HttpStatusCode.Created)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: SerializationException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
            delete("/{taskName}") {
                val name = call.parameters["taskName"]
                if (name == null){
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                if (TaskRepository.removeTask(name)){
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}