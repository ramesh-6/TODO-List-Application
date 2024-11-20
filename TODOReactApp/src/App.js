import { React } from "react";
import taskContext from "./Contexts/TaskContext";
import TaskTitle from "./Components/TaskTitle";
import TaskCounter from "./Components/TaskCounter";
import TaskRow from "./Components/TaskRow";
import { useFetchTasks } from "./Hooks/TaskHooks";
import {} from "./App.css";
import TaskInputRow from "./Components/TaskInputRow";

export default function App() {
  const { tasks, setTasks } = useFetchTasks();

  return (
    <taskContext.Provider value={{ tasks, setTasks }}>
      <div className="container">
        <TaskTitle />
        <TaskCounter />
        <TaskInputRow />
        <TaskRow />
      </div>
    </taskContext.Provider>
  );
}
