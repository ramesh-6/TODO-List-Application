import instance from "../Utils/Instance";

export const loginService = async (user) => {
  try {
    const response = await instance.post("auth/login", user);
    return response.data;
  } catch (e) {
    throw new Error(e);
  }
};

export const signupservice = async (user) => {
  try {
    const response = await instance.post("auth/register", user);
    return response.data;
  } catch (e) {
    throw new Error(e);
  }
};
