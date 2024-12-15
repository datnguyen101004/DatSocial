// Import các thư viện cần thiết
import React, { useState } from "react";
import axios from "axios";
import "./Css/Login.css";

const Login = () => {
  // State để lưu thông tin người dùng
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const api = "http://localhost:8080/api/v1/auth/login";

  const [error, setError] = useState(""); // Lưu lỗi nếu có

  // Xử lý khi người dùng nhập thông tin
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // Xử lý khi form được submit
  const handleSubmit = async (e) => {
    e.preventDefault();

    setError("");

    try {
      const result = await axios.post(api, formData);
      console.log(result.data);
      if (result.status === 200) {
        alert("login successful!");
      } else {
        alert("Login failed!");
      }
    } catch (error) {
      if (error.response && error.response.status === 409) {
        // Xử lý khi trạng thái HTTP là 409
        alert("This account not exists. Please try another.");
      } else {
        // Xử lý các lỗi khác
        alert("An error occurred. Please try again later.");
        console.error("Error details:", error);
      }
    }
  };

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
      </form>
    </div>
  );
};

export default Login;
