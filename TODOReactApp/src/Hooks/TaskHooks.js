import { useContext, useEffect, useState } from "react";
import {
  addTask,
  deleteTask,
  getTasks,
  getTasksByUserID,
  getTasksByUsername,
  getUserByUsername,
  updateTask,
} from "../Service/TaskServices";
import { taskContext } from "../Contexts/TaskContext";
import { jwtDecode } from "jwt-decode";

export function useTasks() {
  const { tasks, setTasks } = useContext(taskContext);
  const token = sessionStorage.getItem("jwtToken");
  const decodedToken = jwtDecode(token);
  const user = { username: decodedToken.username, id: decodedToken.userId };
  const [error, setError] = useState(null);

  useEffect(() => {
    if (user) {
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
    }
  }, [token]);

  async function postTask(title, description, setTitle, setDescription) {
    try {
      const newTask = {
        title: title,
        description: description,
        completed: false,
        userID: user.id,
      };
      const createdTask = await addTask(newTask);
      console.log("Created task:", createdTask);
      setTasks(prevTasks => [...prevTasks, createdTask]);
      setTitle("");
      setDescription("");
    } catch (err) {
      console.error("Error creating task:", err);
      setError(err);
    }
}


  async function toggleCheckbox(taskID) {
    try {
      const updatedChecked = tasks.find((task) => task.id === taskID);
      updatedChecked.completed = !updatedChecked.completed;
      const taskData = await updateTask(taskID, updatedChecked);
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
      updatedTask.description = active.description;
      const taskData = await updateTask(taskID, updatedTask);
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
