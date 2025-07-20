import { React, useContext, useEffect, useState } from "react";
import { taskContext } from "../../../Contexts/TaskContext";
import { useTasks } from "../../../Hooks/TaskHooks";
import {} from "./TaskRow.css";
import { DeleteFilled, SaveFilled, EditFilled } from "@ant-design/icons";

export default function TaskRow() {
  const [active, setActive] = useState(null);
  const { tasks } = useContext(taskContext);
  const { toggleCheckbox, removeTask, editTask } = useTasks();

  useEffect(() => {
    console.log("Task state:", tasks);
    console.log("Active state:", active);
  }, [tasks, active]);

  return (
    <div className="taskRow">
      {tasks.map((task) => (
        <div key={task.id} className="task">
          <input
            type="checkbox"
            className="taskCheckbox"
            checked={task.completed}
            onChange={() => toggleCheckbox(task.id)}
          />
          {task && task.id !== active?.id ? (
            <div className="taskContent">
              <div className="taskTextWrapper">
                <div className="taskTitle">{task.title}</div>
                <div className="taskDescription">{task.description}</div>
              </div>
              <div className="taskButtonDiv">
                <button className="taskButton" onClick={() => setActive({ id: task.id, title: task.title || "", description: task.description || "" })}>
                  <EditFilled />
                </button>
                <button className="taskButton" onClick={() => removeTask(task.id)}>
                  <DeleteFilled />
                </button>
              </div>
            </div>
          ) : (
            <div className="taskContent">
              <div className="taskTextWrapper">
                <input
                  className="taskTitleInput"
                  value={active.title || ""}
                  onChange={(e) => setActive({ ...active, title: e.target.value })}
                  onKeyDown={(e) => (e.key === "Enter" ? editTask(task.id, active, setActive) : "")}
                  placeholder="Title"
                />
                <textarea
                  className="taskDescriptionTextarea"
                  value={active.description || ""}
                  onChange={(e) => setActive({ ...active, description: e.target.value })}
                  onKeyDown={(e) => (e.key === "Enter" ? editTask(task.id, active, setActive) : "")}
                  placeholder="Description"
                  rows={1}
                />
              </div>
              <div className="taskButtonDiv">
                <button className="taskButton" onClick={() => editTask(task.id, active, setActive)}>
                  <SaveFilled />
                </button>
              </div>
            </div>
          )}
        </div>
      ))}
    </div>
  );
}