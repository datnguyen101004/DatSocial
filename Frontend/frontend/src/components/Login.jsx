import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./Css/Login.css";

const Login = () => {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const [error, setError] = useState(""); // Lưu lỗi nếu có
  const [successMessage, setSuccessMessage] = useState(""); // Lưu thông báo thành công

  const navigate = useNavigate();
  const api = "http://localhost:8080/api/v1/auth/login";

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccessMessage("");

    try {
      const result = await axios.post(api, formData);

      const { status, message, data } = result.data;

      if (status === 200) {
        // Đăng nhập thành công
        localStorage.setItem("jwtToken", data.token);
        localStorage.setItem("refreshToken", data.refreshToken);

        setSuccessMessage("Login successful!"); // Hiển thị thông báo thành công
        setTimeout(() => navigate("/home"), 1000); // Điều hướng sau 2 giây
      } else {
        setError(message);
      }
    } catch (error) {
      if (error.response) {
        setError("Login failed. Please try again!");
      } else {
        setError("Unable to connect to the server. Please try again later.");
      }
    }
  };

  const handleBtnRegister = (e) => {
    e.preventDefault();
    navigate("/register");
  };

  return (
    <div className="Login-container">
      <h2>Login</h2>
      {error && <p className="error">{error}</p>}
      {successMessage && <p className="success">{successMessage}</p>}
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
