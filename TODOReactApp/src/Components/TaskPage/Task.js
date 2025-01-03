import { React, useState } from "react";
import {taskContext} from "../../Contexts/TaskContext";
import TaskCounter from "../TaskPage/TaskCounter";
import TaskRow from "../TaskPage//TaskRow";
import {} from "../../App.css";
import TaskInputRow from "../TaskPage//TaskInputRow";

function Task() {
  const [tasks, setTasks] = useState([]);
  return (
    <taskContext.Provider value={{ tasks, setTasks }}>
      <div className="container">
        <TaskCounter />
        <TaskInputRow />
        <TaskRow />
      </div>
    </taskContext.Provider>
  );
}

export default Task;
