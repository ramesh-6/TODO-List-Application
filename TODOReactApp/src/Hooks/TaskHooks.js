import { useEffect, useState } from "react";
import {
  addTask,
  deleteTask,
  getTasks,
  updateTasks,
} from "../Service/TaskServices";

export function useFetchTasks() {
  const [tasks, setTasks] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function fetchTasks() {
      try {
        const taskData = await getTasks();
        setTasks(taskData);
      } catch (err) {
        console.error("Error fetching tasks:", err);
        setError(err);
      }
    }
    fetchTasks();
  }, []);

  return { tasks, setTasks, error };
}

export function usePostTasks({ tasks, setTasks, input, setInput }) {
  const [error, setError] = useState(null);

  async function postTasks() {
    try {
      const data = [
        ...tasks,
        {
          title: input,
          completed: false,
          user: {
            id: 1,
            username: "RK",
            password: "pass",
            email: "RK@gmail.com",
          },
        },
      ];
      const taskData = await addTask(data);
      setTasks(taskData);
      setInput("");
    } catch (err) {
      console.error("Error fetching tasks:", err);
      setError(err);
    }
  }

  return { postTasks, error };
}

export function useEditTask({ tasks, setTasks }) {
  const [error, setError] = useState(null);

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

  return { editTask, error };
}

export function useDeleteTask({ tasks, setTasks }) {
  const [error, setError] = useState(null);

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

  return { removeTask, error };
}
