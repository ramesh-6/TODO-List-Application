import { useContext, useEffect } from "react";
import { getTasks } from "../Service/TasksService";
import { taskContext } from "../Contexts/TaskContext";

export function useGetTasks() {
  const { tasks, setTasks } = useContext(taskContext);
  useEffect(() => {
    setTasks(getTasks);
  }, [tasks]);
  return tasks;
}

export function useGetTasksInitial() {
  const { tasks, setTasks } = useContext(taskContext);
  useEffect(() => {
    setTasks(getTasks);
  }, []);
  return tasks;
}
