import { React, useContext, useEffect, useState } from "react";
import { taskContext } from "../../Contexts/TaskContext";
import { useTasks } from "../../Hooks/TaskHooks";
import {} from "../../App.css";
import { DeleteFilled, SaveFilled, EditFilled } from "@ant-design/icons";

export default function TaskRow() {
  const [active, setActive] = useState();
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
            <>
              <ul>{task.title}</ul>
              <div className="taskButtonDiv">
                <button
                  className="taskButton"
                  onClick={() => setActive({ id: task.id, title: task.title })}
                >
                  <EditFilled />
                </button>
                <button
                  className="taskButton"
                  onClick={() => removeTask(task.id)}
                >
                  <DeleteFilled />
                </button>
              </div>
            </>
          ) : (
            <div className="taskUpdateDiv">
              <input
                className="taskUpdateInput"
                value={active.title || ""}
                onKeyDown={(e) => (e.key === "Enter" ? editTask(task.id, active, setActive) : "")}
                onChange={(e) => setActive({ id: task.id, title:  e.target.value })}
              />
              <button
                className="taskUpdateSaveButton"
                onClick={() => editTask(task.id, active, setActive)}
              >
                <SaveFilled />
              </button>
            </div>
          )}
        </div>
      ))}
    </div>
  );
}
