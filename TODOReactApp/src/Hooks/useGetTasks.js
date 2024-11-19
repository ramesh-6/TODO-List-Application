import { useContext, useEffect } from "react";
import { getTasks } from "../Service/TasksService";
import { taskContext } from "../Contexts/TaskContext";

const useGetTasks = (tasks) => {
  const { tasks, setTasks } = useContext(taskContext);
  useEffect(() => {
    setTasks(getTasks);
  }, [tasks]);
};

export default useGetTasks;
