import { React, useContext, useEffect, useState } from "react";
import taskContext from "../Contexts/TaskContext";
import { useDeleteTask, useEditTask } from "../Hooks/TaskHooks";
import {} from "../App.css";
import { DeleteFilled, SaveFilled, EditFilled } from "@ant-design/icons";

export default function TaskRow() {
  const [active, setActive] = useState();
  const { tasks, setTasks } = useContext(taskContext);
  const { removeTask } = useDeleteTask({ tasks, setTasks });
  const { editTask } = useEditTask({ tasks, setTasks });

  useEffect(() => {
    console.log("Task state:", tasks);
    console.log("Active state:", active);
  }, [tasks, active]);

  function toggleTask(index) {
    const updatedChecked = tasks.map((task) =>
      task.id === index ? { ...task, completed: !task.completed } : task
    );
    setTasks(updatedChecked);
  }

  function toggleUpdate(index, taskValue) {
    setActive({ id: index, title: taskValue });
  }

  function handleEditInputChange(index, value) {
    setActive({ id: index, title: value });
  }

  function updateTask(taskID) {
    editTask(taskID, active, setActive);
  }

  function deleteTask(taskID) {
    removeTask(taskID);
  }

  return (
    <div className="taskRow">
      {tasks.map((task) => (
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
                <button
                  className="taskButton"
                  onClick={() => deleteTask(task.id)}
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
      ))}
    </div>
  );
}
