import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./Css/AddBlog.css"; // Import CSS

const AddBlog = () => {
  const [formData, setFormData] = useState({ title: "", content: "" });
  const [error, setError] = useState(""); // Báo lỗi nếu có
  const navigate = useNavigate(); // Điều hướng

  const token = localStorage.getItem("jwtToken"); // Lấy token từ localStorage

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(""); // Reset lỗi trước khi gửi

    try {
      const response = await axios.post("http://localhost:8080/api/v1/blog/create",formData,{
        headers:{
            Authorization: `Bearer ${token}`,
            'Access-Control-Allow-Origin': '*',
        }
    }
      );

      if (response.data.status === 200) {
        navigate("/home"); // Chuyển đến trang Home
      } else {
        setError(response.data.message || "Something went wrong!");
      }
    } catch (err) {
      setError("Network error or server unavailable.");
    }
  };

  return (
    <div className="add-blog-container">
      <h2>Add New Blog</h2>
      {error && <p className="error-message">{error}</p>}
      <form onSubmit={handleSubmit} className="add-blog-form">
        <div className="form-group">
          <label htmlFor="title">Title:</label>
          <input
            type="text"
            id="title"
            name="title"
            value={formData.title}
            onChange={handleChange}
            placeholder="Enter blog title"
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="content">Content:</label>
          <textarea
            id="content"
            name="content"
            value={formData.content}
            onChange={handleChange}
            placeholder="Enter blog content"
            rows="5"
            required
          ></textarea>
        </div>
        <button type="submit" className="submit-button">Submit</button>
      </form>
    </div>
  );
};

export default AddBlog;