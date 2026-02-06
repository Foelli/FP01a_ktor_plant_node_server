package foel.li.model

object TaskRepository {
    val tasks = mutableListOf(
        Task("Cleaning", "Clean the house", Priority.Low),
        Task("Gardening", "Mow the lawn", Priority.Medium),
        Task("Shopping", "Buy Food", Priority.High),
        Task("Dancing", "Let's Dance", Priority.Vital)
    )

    fun allTasks(): List<Task> = tasks

    fun tasksByPriority(priority: Priority) = tasks.filter {
        it.priority == priority
    }

    fun taskByName(name: String) = tasks.first {
        it.name.equals(name, ignoreCase = true)
    }

    fun addTask(task: Task) {
        if (taskByName(task.name) != null){
            throw IllegalStateException("Task already exists")
        }
        tasks.add(task)
    }
}
