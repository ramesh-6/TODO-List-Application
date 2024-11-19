import instance from "../Utlis/Instance";

export const getTasks = async () => {
  try {
    const response = await instance.get("/Tasks");
    return response.data;
  } catch (e) {
    throw new Error(e);
  }
};
export const addTask = async (task) => {
  try {
    const response = await instance.post("/Tasks", task);
    return response.data;
  } catch (e) {
    throw new Error(e);
  }
};
export const updateTasks = async (taskId, task) => {
  try {
    const response = await instance.put("/Task/" + taskId, task);
    return response.data;
  } catch (e) {
    throw new Error(e);
  }
};
export const deleteTask = async (taskId) => {
  try {
    const response = await instance.delete("/Task/" + taskId);
    return response.data;
  } catch (e) {
    throw new Error(e);
  }
};
