import { React, useContext, useState } from "react";
import {} from "../App.css";
import { usePostTasks } from "../Hooks/TaskHooks";
import  taskContext  from "../Contexts/TaskContext";
import { PlusSquareFilled } from "@ant-design/icons";

function TaskInputRow() {
  const [input, setInput] = useState("");
  const { tasks, setTasks } = useContext(taskContext);
  const { postTasks } = usePostTasks({ tasks, setTasks, input, setInput });

  return (
    <div className="inputRow">
      <input
        className="input"
        placeholder="write your next task"
        value={input}
        onChange={(e) => setInput(e.target.value)}
      />
      <button className="inputButton" onClick={postTasks}>
        <PlusSquareFilled />
      </button>
    </div>
  );
}

export default TaskInputRow;
