import { React } from "react";
import { BrowserRouter, Routes, Route, useLocation } from "react-router-dom";
import Login from "./Components/LoginPage/Login";
import Signup from "./Components/SignupPage/Signup";
import Footer from "./Components/Footer/Footer";
import Header from "./Components/Header/Header";
import Task from "./Components/TaskPage/Task";
import { AuthProvider } from "./Contexts/AuthContext";

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <AppContent />
      </BrowserRouter>
    </AuthProvider>
  );
}

function AppContent() {
  const location = useLocation();
  const hideHeaderFooter = ["/", "/signup"].includes(location.pathname);

  return (
    <>
      {!hideHeaderFooter && <Header />}

      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/tasks" element={<Task />} />
      </Routes>

      {!hideHeaderFooter && <Footer />}
    </>
  );
}
