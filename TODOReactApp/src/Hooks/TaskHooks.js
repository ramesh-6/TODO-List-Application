import { useContext, useEffect, useState } from "react";
import {
  addTask,
  deleteTask,
  getTasks,
  getTasksByUserID,
  updateTasks,
} from "../Service/TaskServices";
import { taskContext } from "../Contexts/TaskContext";

export function useTasks() {
  const { tasks, setTasks } = useContext(taskContext);
  const user = {
    id: 2,
    username: "Pio",
    password: "passpio",
    email: "pio@gmail.com",
  };
  const [error, setError] = useState(null);

  useEffect(() => {
    async function fetchTasks() {
      try {
        const taskData = await getTasksByUserID(user.id);
        setTasks(taskData);
      } catch (err) {
        console.error("Error fetching tasks:", err);
        setError(err);
      }
    }
    fetchTasks();
  }, []);

  async function postTask(input, setInput) {
    try {
      const data = [
        ...tasks,
        {
          title: input,
          completed: false,
          user: user,
        },
      ];
      const taskData = await addTask(data);
      console.log("Adding data:", input);
      setTasks(taskData);
      setInput("");
    } catch (err) {
      console.error("Error fetching tasks:", err);
      setError(err);
    }
  }

  async function toggleCheckbox(taskID) {
    try {
      const updatedChecked = tasks.find((task) => task.id === taskID);
      updatedChecked.completed = !updatedChecked.completed;
      const taskData = await updateTasks(taskID, updatedChecked);
      const taskarr = tasks.map((task) =>
        task.id === taskID ? taskData : task
      );
      setTasks(taskarr);
    } catch (err) {
      console.error("Error fetching tasks:", err);
      setError(err);
    }
  }

  async function editTask(taskID, active, setActive) {
    try {
      const updatedTask = tasks.find((task) => task.id === taskID);
      updatedTask.title = active.title;
      const taskData = await updateTasks(taskID, updatedTask);
      const taskarr = tasks.map((task) =>
        task.id === taskID ? taskData : task
      );
      setTasks(taskarr);
      setActive({});
    } catch (err) {
      console.error("Error fetching tasks:", err);
      setError(err);
    }
  }

  async function removeTask(taskID) {
    try {
      const deletedTasks = tasks.filter((task) => task.id !== taskID);
      const taskData = await deleteTask(taskID);
      setTasks(deletedTasks);
    } catch (err) {
      console.error("Error fetching tasks:", err);
      setError(err);
    }
  }

  return {
    tasks,
    setTasks,
    error,
    toggleCheckbox,
    postTask,
    editTask,
    removeTask,
  };
}
