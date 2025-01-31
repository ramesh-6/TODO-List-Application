import React, { useContext, useState } from "react";
import { authContext } from "../Contexts/AuthContext";
import { loginService } from "../Service/LoginServices";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import Task from "../Components/TaskPage/Task";

export function useLogin() {
  const { username, password } = useContext(authContext);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  async function login() {
    try {
      const user = {
        username: username,
        password: password,
      };
      const JWTtoken = await loginService(user);
      if (JWTtoken) {
        sessionStorage.setItem("jwtToken", JWTtoken);
        console.log("Logging in user:", user, "and token:", JWTtoken);
        const token = sessionStorage.getItem("jwtToken");
        if (token) {
          const decodedToken = jwtDecode(token);
          console.log(decodedToken);
        }
        navigate("/tasks");
        // return <Task/>;
      } else {
        console.error("No token returned from login service.");
      }
    } catch (err) {
      console.error("Error logging in:", err);
      setError(err);
    }
  }

  return { login, error };
}
