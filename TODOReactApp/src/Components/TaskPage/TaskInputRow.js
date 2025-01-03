import { React, useState } from "react";
import {} from "../../App.css";
import { useTasks } from "../../Hooks/TaskHooks";
import { PlusSquareFilled } from "@ant-design/icons";

function TaskInputRow() {
  const [input, setInput] = useState("");
  const { postTask } = useTasks();

  return (
    <div className="inputRow">
      <input
        className="input"
        placeholder="write your next task"
        value={input}
        onKeyDown={(e) => (e.key === "Enter" ? postTask(input, setInput) : "")}
        onChange={(e) => setInput(e.target.value)}
      />
      <button
        className="inputButton"
        onClick={() => postTask(input, setInput)}
      >
        <PlusSquareFilled />
      </button>
    </div>
  );
}

export default TaskInputRow;
