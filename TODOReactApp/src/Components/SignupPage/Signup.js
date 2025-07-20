import { React, useContext, useEffect } from "react";
import { authContext } from "../../Contexts/AuthContext";
import "./Signup.css";
import { useNavigate } from "react-router-dom";
import { useLogin } from "../../Hooks/LoginHooks";

function Signup() {
  const {
    username,
    setUsername,
    password,
    setPassword,
    confirmPassword,
    setConfirmPassword,
    email,
    setEmail,
  } = useContext(authContext);
  const { register } = useLogin();
  const navigate = useNavigate();

  useEffect(() => {
  setUsername("");
  setPassword("");
  setConfirmPassword("");
  setEmail("");
  }, []);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!username) return;
    if (!email) return;
    if (!password) return;
    if (!confirmPassword) return;
    if (password !== confirmPassword) {
      alert("Passwords do not match. Please try again.");
      return;
    }
    register(username, email, password);
  };

  return (
    <div className="signupContainer">
      <div className="signupTitle">SIGN UP</div>
      <form onSubmit={handleSubmit}>
        <div className="usernameInput">
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div className="emailInput">
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div className="passwordInput">
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <div className="confirmPasswordInput">
          <input
            type="password"
            placeholder="Confirm Password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit" className="signupButton">
          REGISTER
        </button>
        <div className="logininSignup">
          Already have an account?
          <button
            type="button"
            onClick={() => navigate("/")}
            className="logininSignupButton"
          >
            Login
          </button>
        </div>
      </form>
    </div>
  );
}

export default Signup;
