import { React, useContext, useEffect, useState } from "react";
import { taskContext } from "./Contexts/TaskContext";
import {} from "./App.css";
import {
  DeleteFilled,
  SaveFilled,
  EditFilled,
} from "@ant-design/icons";

export default function TaskList() {
  const { tasks, updatedInputs } = useContext(taskContext);

  return tasks.map((task) => (
    <div key={task.id} className="task">
      <input
        type="checkbox"
        className="taskCheckbox"
        checked={task.completed}
        onChange={() => toggleTask(task.id)}
      />
      {updatedInputs[task.id] && !updatedInputs[task.id].update ? (
        <>
          <ul>{task.name}</ul>
          <div className="taskButtonDiv">
            <button
              className="taskButton"
              onClick={() => toggleUpdate(task.id)}
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
            value={
              (updatedInputs[task.id] && updatedInputs[task.id].name) || ""
            }
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
