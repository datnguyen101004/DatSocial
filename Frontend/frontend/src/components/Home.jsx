import React, { useState, useEffect } from "react";
import axios from "axios";
import ViewBlog from "./ViewBlog"; // Import component ViewBlog
import { FaBlog, FaVideo } from "react-icons/fa"; // Import icons
import "./Css/Home.css";
import { useParams, useLocation } from "react-router-dom";

const Home = () => {
  const [posts, setPosts] = useState([]); // Blog posts
  const [videos, setVideos] = useState([]); // Video posts
  const [activeTab, setActiveTab] = useState("blog"); // Active tab
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { search } = useLocation(); // Lấy query parameters từ URL
  const params = new URLSearchParams(search);
  const searchQuery = params.get("search");

  useEffect(() => {
    const token = localStorage.getItem("jwtToken");
    console.log(searchQuery)
    // Fetch blogs
    const fetchBlogs = async () => {
      try {
        const token = localStorage.getItem("jwtToken");
        const response = await axios.get("http://localhost:8080/api/v1/blog/all", {
          headers: {
            Authorization: `Bearer ${token}`,
            "Access-Control-Allow-Origin": "*",
          },
        });
  
        if (response.status === 200) {
          const blogs = response.data.data;
  
          // Kiểm tra trạng thái `isLiked` cho từng bài viết
          const updatedBlogs = await Promise.all(
            blogs.map(async (blog) => {
              const likeStatus = await axios.get(
                `http://localhost:8080/api/v1/like/blog/${blog.id}/isLiked`,
                { headers: { Authorization: `Bearer ${token}` } }
              );
              const shareStatus = await axios.get(
                `http://localhost:8080/api/v1/share/${blog.id}/isShared`,
                { headers: { Authorization: `Bearer ${token}` } }
              );
              return { ...blog, isLiked: likeStatus.data.data, isShared: shareStatus.data.data }; // Thêm `isLiked` vào mỗi blog
            })
          );
  
          setPosts(updatedBlogs);
        }
      } catch (error) {
        setError("Hãy đăng nhập để xem các bài viết.");
        console.error("Error fetching blogs:", error);
      } finally {
        setLoading(false);
      }
    };

    // Fetch videos (mocked or API-based)
    const fetchVideos = async () => {
      try {
        // Replace with actual API for videos
        const response = await axios.get("http://localhost:8080/api/v1/videos/all", {
          headers: {
            Authorization: `Bearer ${token}`,
            "Access-Control-Allow-Origin": "*",
          },
        });
        if (response.status === 200) {
          setVideos(response.data.data);
        }
      } catch (error) {
        console.error("Error fetching videos:", error);
      }
    };

    fetchBlogs();
    fetchVideos();
  }, []);

  if (loading) {
    return <div className="home-container">Đang tải dữ liệu...</div>;
  }

  if (error) {
    return <div className="home-container">{error}</div>;
  }

  return (
    <div className="home-container">
      {/* Tabs for switching between Blog and Video */}
      <div className="tabs">
        <button
          className={activeTab === "blog" ? "active-tab" : ""}
          onClick={() => setActiveTab("blog")}
        >
          <FaBlog size={24} style={{ marginRight: "8px" }} />
          Blogs
        </button>
        <button
          className={activeTab === "video" ? "active-tab" : ""}
          onClick={() => setActiveTab("video")}
        >
          <FaVideo size={24} style={{ marginRight: "8px" }} />
          Videos
        </button>
      </div>

      {/* Content rendering based on active tab */}
      <div className="content">
        {activeTab === "blog" ? (
          <div className="posts">
            {posts.map((post) => (
              <ViewBlog
                key={post.id}
                post={post}
                setPosts={setPosts} // Truyền hàm cập nhật danh sách bài viết
              />
            ))}
          </div>
        ) : (
          <div className="videos">
            {videos.length > 0 ? (
              videos.map((video) => (
                <div key={video.id} className="video">
                  <h3>{video.title}</h3>
                  <video controls src={video.url}></video>
                </div>
              ))
            ) : (
              <p>Chưa có video nào.</p>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default Home;
