import { React, useState } from "react";
import {} from "./TaskInputRow.css";
import { useTasks } from "../../../Hooks/TaskHooks";
import { PlusSquareFilled } from "@ant-design/icons";

function TaskInputRow() {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const { postTask } = useTasks();

  const handleSubmit = () => {
    if (title.trim()) {
      postTask(title, description, setTitle, setDescription);
    }
  };

  return (
    <div className="inputRow">
      <input
        className="input titleInput"
        placeholder="Title"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        onKeyDown={(e) => e.key === "Enter" && handleSubmit()}
      />
      <input
        className="input descInput"
        placeholder="Description"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        onKeyDown={(e) => e.key === "Enter" && handleSubmit()}
      />
      <button className="inputButton" onClick={handleSubmit}>
        <PlusSquareFilled />
      </button>
    </div>
  );

}

export default TaskInputRow;
