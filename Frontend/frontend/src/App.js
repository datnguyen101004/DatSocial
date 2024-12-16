import "./App.css";
import Register from "./components/Register";
import Login from "./components/Login";
import "bootstrap/dist/css/bootstrap.min.css";
import Navbar from "./components/Navbar";
import Home from "./components/Home";
import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";

function App() {
  const isAuthenticated = !!localStorage.getItem("jwtToken"); // Kiểm tra token trong localStorage
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        {/* Chỉ cho phép truy cập /home nếu đã đăng nhập */}
        <Route path="/home" element={isAuthenticated ? <Home /> : <Navigate to="/login" />} />
        {/* Nếu người dùng truy cập root, điều hướng đến /login */}
        <Route path="/" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
}

export default App;
