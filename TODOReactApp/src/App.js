import { React } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import {} from "./App.css";
import Login from "./Components/LoginPage/Login";
import Signup from "./Components/SignupPage/Signup";
import Footer from "./Components/Footer";
import Header from "./Components/Header";
import Task from "./Components/TaskPage/Task";
import { AuthProvider } from "./Contexts/AuthContext";

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/tasks" element={<Task />} />
        </Routes>
        {/* <Header />
        <Task />
        <Footer /> */}
    </BrowserRouter>
    </AuthProvider>
  );
}
