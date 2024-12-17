// Import các thư viện cần thiết
import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom"; // Để điều hướng
import "./Css/Login.css";

const Login = () => {
  // State để lưu thông tin người dùng
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const [error, setError] = useState(""); // Lưu lỗi nếu có
  const [message, setMessage] = useState(""); // Lưu message từ API

  const navigate = useNavigate();
  const api = "http://localhost:8080/api/v1/auth/login";

  // Xử lý khi người dùng nhập thông tin
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // Xử lý khi form được submit
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setMessage("");

    try {
      const result = await axios.post(api, formData);

      const { status, message, data } = result.data;

      if (status === 200) {
        // Đăng nhập thành công
        localStorage.setItem("jwtToken", data.token);
        localStorage.setItem("refreshToken", data.refreshToken);

        setMessage(message); // Hiển thị thông báo từ server
        alert("Login successful!");
        navigate("/home"); // Điều hướng đến trang chính
      } else {
        // Nếu status khác 0
        setError(message || "Login failed. Please try again.");
      }
    } catch (error) {
      if (error.response) {
        // Lỗi từ phía server
        setError(error.response.data.message || "An error occurred.");
      } else {
        // Lỗi khác (ví dụ: network)
        setError("Unable to connect to the server. Please try again later.");
      }
    }
  };

  const handleBtnRegister = (e)=>{
    e.preventDefault();
    navigate("/register");
  }

  return (
    <div className="Login-container">
      <h2>Login</h2>
      {error && <p className="error">{error}</p>}
      <form onSubmit={handleSubmit} className="Login-form">
        <div className="form-group">
          <label htmlFor="email">Email</label>
          <input
            type="email"
            id="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="password">Password</label>
          <input
            type="password"
            id="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>
        <button type="submit" className="login-button">
          Log in
        </button>
        <button type="button" className="signup-button" onClick={handleBtnRegister}>
          Sign Up
        </button>
      </form>
    </div>
  );
};

export default Login;
