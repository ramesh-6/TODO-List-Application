import instance from "../Utils/Instance"

export const getTasks = async() => {
    try{
        const response = await instance.get("/Tasks");
        return response.data;
    } catch (e) {
        throw new Error(e);
    }
};
export const getTasksByUserID = async(userID) => {
    try{
        const response = await instance.get("/Tasks/"+userID);
        return response.data;
    } catch (e) {
        throw new Error(e);
    }
};
export const addTask = async(task) => {
    try{
        const response = await instance.post("/Tasks",task);
        return response.data;
    } catch (e) {
        throw new Error(e);
    }
};
export const updateTasks = async(taskID, task) => {
    try{
        const response = await instance.put("/Task/"+ taskID, task);
        return response.data;
    } catch (e) {
        throw new Error(e);
    }
};
export const deleteTask = async(taskID) => {
    try{
        const response = await instance.delete("/Task/"+ taskID);
        return response.data;
    } catch (e) {
        throw new Error(e);
    }
};