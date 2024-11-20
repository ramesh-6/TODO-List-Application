import { React, useContext, useEffect, useState } from "react";
import {} from "../App.css";
import taskContext from "../Contexts/TaskContext";

function TaskCounter() {
  const { tasks } = useContext(taskContext);
  const [totalDone, setTotalDone] = useState();
  useEffect(() => {
    setTotalDone(() => tasks.filter((task) => task.completed).length);
  }, [tasks]);
  return (
    <div className="completedTask">
      <div className="taskDone">
        Task Done <br />
        Keep it up
      </div>
      <div className="no">
        {totalDone} / {tasks.length}
      </div>
    </div>
  );
}

export default TaskCounter;
