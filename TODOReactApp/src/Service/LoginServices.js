import instance from "../Utils/Instance";

export const loginService = async (user) => {
  try {
    const response = await instance.post("/login", user);
    return response.data;
  } catch (e) {
    throw new Error(e);
  }
};
