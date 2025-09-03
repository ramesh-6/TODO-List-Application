import instance from "../Utils/Instance";

export const getTasks = async () => {
  try {
    const response = await instance.get("/tasks");
    return response.data;
  } catch (e) {
    throw new Error(e);
  }
};
export const getUserByUsername = async (username) => {
  try {
    const response = await instance.get("/user/username/" + username);
    return response.data;
  } catch (e) {
    throw new Error(e);
  }
};
export const getTasksByUserID = async (userID) => {
  try {
    const response = await instance.get("/tasks/id/" + userID);
    return response.data;
  } catch (e) {
    throw new Error(e);
  }
};
export const getTasksByUsername = async (username) => {
  try {
    const response = await instance.get("/tasks/username/" + username);
    return response.data;
  } catch (e) {
    throw new Error(e);
  }
};
export const addTask = async (task) => {
  try {
    const response = await instance.post("/task", task);
    return response.data;
  } catch (e) {
    throw new Error(e);
  }
};
export const updateTask = async (taskID, task) => {
  try {
    const response = await instance.put("/task/" + taskID, task);
    return response.data;
  } catch (e) {
    throw new Error(e);
  }
};
export const deleteTask = async (taskID) => {
  try {
    const response = await instance.delete("/task/" + taskID);
    return response.data;
  } catch (e) {
    throw new Error(e);
  }
};
