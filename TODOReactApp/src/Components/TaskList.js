import { React, useContext } from "react";
import { taskContext } from "../Contexts/TaskContext";
import {} from "./App.css";
import { DeleteFilled, SaveFilled, EditFilled } from "@ant-design/icons";

export default function TaskList() {
  const {
    tasks,
    active,
    toggleTask,
    toggleUpdate,
    handleEditInputChange,
    updateTask,
    deleteTask,
  } = useContext(taskContext);

  return tasks.map((task) => (
    <div key={task.id} className="task">
      <input
        type="checkbox"
        className="taskCheckbox"
        checked={task.completed}
        onChange={() => toggleTask(task.id)}
      />
      {task && task.id !== active?.id ? (
        <>
          <ul>{task.title}</ul>
          <div className="taskButtonDiv">
            <button
              className="taskButton"
              onClick={() => toggleUpdate(task.id, task.title)}
            >
              <EditFilled />
            </button>
            <button className="taskButton" onClick={() => deleteTask(task.id)}>
              <DeleteFilled />
            </button>
          </div>
        </>
      ) : (
        <div className="taskUpdateDiv">
          <input
            className="taskUpdateInput"
            value={active.title || ""}
            onChange={(e) => handleEditInputChange(task.id, e.target.value)}
          />
          <button
            className="taskUpdateSaveButton"
            onClick={() => updateTask(task.id)}
          >
            <SaveFilled />
          </button>
        </div>
      )}
    </div>
  ));
}
