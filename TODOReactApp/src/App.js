import { React, useState } from 'react';
import {} from './App.css';
import { DeleteFilled, SaveFilled, EditFilled, CheckSquareTwoTone, PlusSquareFilled } from '@ant-design/icons';

export default function App() {
  const [tasks, setTasks] = useState([
    { name: "Task1", checked: true, active: true },
    { name: "Task2", checked: true, active: true },
    { name: "Task3", checked: true, active: true }
  ]);
  // const [checkbox, setCehckbox] = useState({});
  const [input, setInput] = useState('');
  const [updatedInputs, setUpdatedInputs] = useState({});
  const totalDone = tasks.filter(task => task.checked).length;

  function addTask() {
    setTasks([...tasks, { name: input, checked: false, active: true }]);
    setInput('');
  }

  function toggleTask(index) {
    const updatedChecked = tasks.map((task, i) =>
      i === index ? { ...task, checked: !task.checked } : task
    );
    setTasks(updatedChecked);
  }

  function toggleUpdate(index) {
    const updatedActive = tasks.map((task, i) =>
      i === index ? { ...task, active: !task.active } : task
    );
    // Set initial value for `updatedInputs[index]` if entering edit mode
    setUpdatedInputs(prev => ({ ...prev, [index]: tasks[index].name }));
    setTasks(updatedActive);
  }

  function handleEditInputChange(index, value) {
    // Update only the specific task's editing input in `updatedInputs`
    setUpdatedInputs(prev => ({ ...prev, [index]: value }));
  }

  function updateTask(index) {
    const updatedTasks = tasks.map((task, i) =>
      i === index
        ? { ...task, name: updatedInputs[index], active: !task.active }  // Apply the updated input
        : task
    );
    setTasks(updatedTasks);

    // Remove the entry from `updatedInputs` after updating
    setUpdatedInputs(prev => {
      const { [index]: removed, ...rest } = prev;
      return rest;
    });
  }

  function deleteTask(index) {
    const deletedTasks = tasks.filter((task, i) => i !== index);
    setTasks(deletedTasks);

    // Clean up `updatedInputs` for the deleted task
    setUpdatedInputs(prev => {
      const { [index]: removed, ...rest } = prev;
      return rest;
    });
  }

  const taskList = tasks.map((task, index) => (
    <div key={index} className="task">
      <input type="checkbox" className="taskCheckbox" checked={task.checked} onChange={() => toggleTask(index)} />
      {task.active ? (
        <>
          <ul>{task.name}</ul>
          <div className="taskButtonDiv">
            <button className="taskButton" onClick={() => toggleUpdate(index)}><EditFilled /></button>
            <button className="taskButton" onClick={() => deleteTask(index)}><DeleteFilled /></button>
          </div>
        </>
      ) : (
        <div className="taskUpdateDiv">
          <input
            className="taskUpdateInput"
            value={updatedInputs[index] || ''}  // Use updatedInputs[index] for controlled input
            onChange={(e) => handleEditInputChange(index, e.target.value)}
          />
          <button className="taskUpdateSaveButton" onClick={() => updateTask(index)}><SaveFilled /></button>
        </div>
      )}
    </div>
  ));

  return (
    <>
      <div className="container">
        <div className="title"><CheckSquareTwoTone />TODO</div>
        <div className="completedTask">
          <div className="taskDone">Task Done <br />Keep it up</div>
          <div className="no">{totalDone} / {tasks.length}</div>
        </div>
        <div className="inputRow">
          <input
            className="input"
            placeholder="write your next task"
            value={input}
            onChange={(e) => setInput(e.target.value)}
          />
          <button className="inputButton" onClick={addTask}><PlusSquareFilled /></button>
        </div>
        <div className="taskRow">{taskList}</div>
      </div>
    </>
  );
}
