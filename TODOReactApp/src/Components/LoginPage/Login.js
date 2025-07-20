import { React, useContext, useEffect } from "react";
import { authContext } from "../../Contexts/AuthContext";
import { useLogin } from "../../Hooks/LoginHooks";
import "./Login.css";
import { useNavigate } from "react-router-dom";

function Login() {
  const { username, setUsername, password, setPassword } = useContext(authContext);
  const { login } = useLogin();
  const navigate = useNavigate();
  useEffect(() => {
  setUsername("");
  setPassword("");
  }, []);
  return (
    <div className="loginContainer">
      <div className="loginTitle">LOGIN</div>
      <div className="usernameInput">
        <input
          className="usernameInput"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
      </div>
      <div className="passwordInput">
        <input
          type="password"
          className="passwordInput"
          placeholder="Password"
          value={password}
          onKeyDown={(e) => (e.key === "Enter" ? login() : "")}
          onChange={(e) => setPassword(e.target.value)}
        />
      </div>
      <button className="loginButton" onClick={() => login()}>
        LOGIN
      </button>
      <div className="signupinLogin">
        Don't have an account?
        <button
          className="signupinLoginButton"
          onClick={() => navigate("/signup")}
        >
          Sign Up
        </button>
      </div>
    </div>
  );
}

export default Login;
