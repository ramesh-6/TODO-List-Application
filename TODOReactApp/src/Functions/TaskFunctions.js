export async function fetchTasks() {
    try {
      const taskData = await getTasks();
      setTasks(taskData);
    } catch (error) {
      console.error("Error fetching tasks:", error);
    }
  }