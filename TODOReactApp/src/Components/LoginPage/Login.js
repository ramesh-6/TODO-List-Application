import { React, useContext } from "react";
import { authContext } from "../../Contexts/AuthContext";
import { useLogin } from "../../Hooks/LoginHooks";
import "./Login.css";

function Login() {
  const { username, setUsername, password, setPassword } =
    useContext(authContext);
  const { login } = useLogin();
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
        <button className="forgotButton" onClick="forgotPass()">
          Forgot?
        </button>
        {/* <button className="forgotButton" onClick={() => setActive({ id: task.id, title: task.title })}>Forgot?</button> */}
      </div>
      <button className="loginButton" onClick={() => login()}>LOGIN</button>
      <div className="signup">
        Don't have an account?
        <button className="signupButton">
          Sign Up
        </button>
      </div>
    </div>
  );
}

export default Login;
