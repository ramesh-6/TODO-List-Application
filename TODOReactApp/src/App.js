import { React, useState } from 'react';
import {} from './App.css';
import { DeleteFilled, SaveFilled, EditFilled, CheckSquareTwoTone, PlusSquareFilled } from '@ant-design/icons';

export default function App() {
  const [tasks, setTasks] = useState([
    { id: 1,name: "Task1", completed: true, active: false },
    { id: 2,name: "Task2", completed: true, active: false },
    { id: 3,name: "Task3", completed: true, active: false }
  ]);
  const [update, setUpdate] = useState({});
  const [input, setInput] = useState('');
  const [updatedInputs, setUpdatedInputs] = useState({});
  const totalDone = tasks.filter(task => task.completed).length

  function addTask() {
    setTasks([...tasks, { name: input, completed: false, active: false }])
    setInput('')
  }

  function toggleTask(index) {
    const updatedChecked = tasks.map((task) =>
      task.id === index ? { ...task, completed: !task.completed } : task
    )
    setTasks(updatedChecked);
  }

  function toggleUpdate(index) {
    const updatedActive = tasks.map((task) =>
      task.id === index ? { ...task, active: !task.active } : task
    )
    setUpdatedInputs(prev => ({ ...prev, [index]: tasks.find(task => task.id===index).name}));
    setTasks(updatedActive);
  }

  function handleEditInputChange(index, value) {
    setUpdatedInputs(prev => ({ ...prev, [index]: value }));
  }

  function updateTask(index) {
    const updatedTasks = tasks.map((task) =>
      task.id === index
        ? { ...task, name: updatedInputs[index], active: !task.active }
        : task
    )
    setTasks(updatedTasks);

    // Remove the entry from `updatedInputs` after updating
    setUpdatedInputs(prev => {
      const { [index]: removed, ...rest } = prev;
      return rest;
    })
  }

  function deleteTask(index) {
    const deletedTasks = tasks.filter((task) => task.id !== index);
    setTasks(deletedTasks)

    // Clean up `updatedInputs` for the deleted task
    setUpdatedInputs(prev => {
      const { [index]: removed, ...rest } = prev
      return rest;
    });
  }

  const taskList = tasks.map((task) => (
    <div key={task.id} className="task">
      <input type="checkbox" className="taskCheckbox" checked={task.completed} onChange={() => toggleTask(task.id)} />
      {!task.active ? (
        <>
          <ul>{task.name}</ul>
          <div className="taskButtonDiv">
            <button className="taskButton" onClick={() => toggleUpdate(task.id)}><EditFilled /></button>
            <button className="taskButton" onClick={() => deleteTask(task.id)}><DeleteFilled /></button>
          </div>
        </>
      ) : (
        <div className="taskUpdateDiv">
          <input
            className="taskUpdateInput"
            value={updatedInputs[task.id] || ''}
            onChange={(e) => handleEditInputChange(task.id, e.target.value)}
          />
          <button className="taskUpdateSaveButton" onClick={() => updateTask(task.id)}><SaveFilled /></button>
        </div>
      )}
    </div>
  ))

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
  )
}