import "./App.css";
import Register from "./components/Register";
import Login from "./components/Login";
import "bootstrap/dist/css/bootstrap.min.css";
import Navbar from "./components/Navbar";
import Home from "./components/Home";
import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import AddBlog from "./components/AddBlog";
import Profile from "./components/Profile";
import { useState, useEffect } from "react";
import Chat from "./components/Chat";

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("jwtToken");
    if (token) {
      setIsLoggedIn(true);
    } else {
      setIsLoggedIn(false);
    }
    setLoading(false); // Đảm bảo setLoading = false sau khi kiểm tra xong
  }, []);

  if (loading) {
    return <div>Loading...</div>; // Hiển thị khi đang kiểm tra trạng thái
  }


  return (
    <Router>
      {isLoggedIn && <Navbar setIsLoggedIn={setIsLoggedIn} />}
      <Routes>
      <Route
        path="/login"
        element={<Login setIsLoggedIn={setIsLoggedIn} />}
      />
        <Route
          path="/register"
          element={<Register />}
        />
        <Route
          path="/home"
          element={isLoggedIn ? <Home /> : <Navigate to="/login" />}
        />
        <Route path="/" element={isLoggedIn ? <Navigate to="/home" /> : <Navigate to="/login" />} />
        <Route
          path="/addBlog"
          element={isLoggedIn ? <AddBlog /> : <Navigate to="/login" />}
        />
        <Route
          path="/profile/:id"
          element={isLoggedIn ? <Profile /> : <Navigate to="/login" />}
        />
        <Route
          path="/chat"
          element={isLoggedIn ? <Chat /> : <Navigate to="/login" />}
        />
      </Routes>
    </Router>
  );
}

export default App;
