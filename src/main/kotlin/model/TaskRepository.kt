package foel.li.model

object TaskRepository {
    val tasks = mutableListOf(
        Task("Cleaning", "Clean the house", Priority.Low),
        Task("Gardening", "Mow the lawn", Priority.Medium),
        Task("Shopping", "Buy Food", Priority.High),
        Task("Dancing", "Let's Dance", Priority.Vital)
    )

    fun allTasks(): List<Task> = tasks

    /*

    // Lambda Version

    fun tasksByPriority(priority: Priority) = tasks.filter {
        it.priority == priority
        
    }*/

    fun tasksByPriority(priority: Priority): List<Task> {
        val result = mutableListOf<Task>()

        for (task in tasks) {
            if (task.priority == priority) {
                result.add(task)
            }
        }

        return result
    }

    fun taskByName(name: String): Task {
        for (task in tasks) {
            if (task.name.equals(name, ignoreCase = true)) {
                return task
            }
        }
        throw NoSuchElementException("No task with name $name")
    }


    fun addTask(task: Task) {
        if (taskByName(task.name) != null){
            throw IllegalStateException("Task already exists")
        }
        tasks.add(task)
    }
}
