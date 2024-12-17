import React, { useState, useEffect } from "react";
import axios from "axios";
import "./Css/Home.css";

const Home = () => {
  const [posts, setPosts] = useState([]); // Lưu danh sách bài viết
  const [loading, setLoading] = useState(true); // Trạng thái tải
  const [error, setError] = useState(null); // Lỗi nếu có

  useEffect(() => {
    const token = localStorage.getItem("jwtToken"); // Lấy token từ localStorage
  
    axios
      .get("http://localhost:8080/api/v1/blog/all", {
        headers: {
          Authorization: `Bearer ${token}`,
          'Access-Control-Allow-Origin': '*', // Đặt token vào header Authorization
        },
      })
      .then((response) => {
        if (response.status === 200) {
          setPosts(response.data.data); // Truy cập vào response.data.data
        }
        setLoading(false);
      })
      .catch((error) => {
        setError("Hãy đăng nhập để xem các bài viết."); // Xử lý lỗi
        setLoading(false);
        console.error("Error:", error);
      });
  }, []);

  // Hiển thị trạng thái Loading
  if (loading) {
    return <div className="home-container">Đang tải dữ liệu...</div>;
  }

  // Hiển thị lỗi nếu có
  if (error) {
    return <div className="home-container">{error}</div>;
  }

  // Hiển thị danh sách bài viết
  return (
    <div className="home-container">
      <div className="posts">
        {posts.map((post) => (
          <div key={post.id} className="post">
            <h2 className="post-title">{post.title}</h2>
            <p className="post-content">{post.content}</p>
            <div className="post-details">
              <span className="author">Tác giả: {post.author}</span>
              <span className="likes">Likes: {post.likesCount}</span>
              <span className="comments">Comments: {post.commentsCount}</span>
              <span className="shares">Shares: {post.sharesCount}</span>
              <span className="created-at">
                Ngày tạo: {new Date(post.createdAt).toLocaleDateString()}
              </span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Home;
