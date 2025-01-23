import { React, useContext } from "react";
import { authContext } from "../../Contexts/AuthContext";

function Signup() {
    const { username, setUsername, password, setPassword, confirmPassword, setConfirmPassword } = useContext(authContext);
    return (
        <div className="loginContainer">
          <div className="loginTitle">SIGN UP</div>
          <div className="usernameInput">
              <input
              className="usernameInput"
              placeholder="Username"
              value={username}
              // onKeyDown={(e) => (e.key === "Enter" ? postTask(input, setInput) : "")}
              onChange={(e) => setUsername(e.target.value)}
              />
          </div>
          <div className="passwordInputs">
              <input
              type="password"
              className="passwordInput"
              placeholder="Password"
              value={password}
              // onKeyDown={(e) => (e.key === "Enter" ? postTask(input, setInput) : "")}
              onChange={(e) => setPassword(e.target.value)}
              />
              <br></br>
              <input
              type="confirmPassword"
              className="confirmpassInput"
              placeholder="Confirm Password"
              value={confirmPassword}
              // onKeyDown={(e) => (e.key === "Enter" ? postTask(input, setInput) : "")}
              onChange={(e) => setConfirmPassword(e.target.value)}
              />
          </div>
          <button className="loginButton">REGISTER</button>
          <div className="signup">Already have an account? 
              <button className="logininsignupButton"
              // onClick={() => setActive({ id: task.id, title: task.title })}
              >Login</button>
          </div>
        </div>
    );
  }
  
  export default Signup;
  