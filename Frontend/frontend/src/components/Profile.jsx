import React, { useEffect, useState } from "react";
import axios from "axios";
import "./Css/Profile.css";
import { useParams} from "react-router-dom";

const Profile = () => {
  const [profileData, setProfileData] = useState(null);
  const [friendsData, setFriendsData] = useState([]); // Dữ liệu bạn bè
  const [likedBlogs, setLikedBlogs] = useState([]); // Dữ liệu bài viết đã thích
  const [sharedBlogs, setSharedBlogs] = useState([]); // Dữ liệu bài viết đã chia sẻ
  const [error, setError] = useState("");
  const [activeTab, setActiveTab] = useState("bai-viet"); // Tab mặc định

  const token = localStorage.getItem("jwtToken");
  const { id } = useParams(); // Get the user id from URL params

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/v1/user/${id}/profile`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (response.data.status === 200) {
          setProfileData(response.data.data);
        } else {
          setError(response.data.message || "Error fetching profile data.");
        }
      } catch (err) {
        setError("Network error or server unavailable.");
      }
    };

    fetchProfile();
  }, []);

  const fetchFriends = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/api/v1/user/${id}/friend/all`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.data.status === 200) {
        setFriendsData(response.data.data);
      } else {
        setError("Error fetching friends data.");
      }
    } catch (err) {
      setError("Network error while fetching friends.");
    }
  };

  const fetchLikedBlogs = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/api/v1/user/${id}/profile/like`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.data.status === 200) {
        setLikedBlogs(response.data.data.myBlog);
      } else {
        setError("Error fetching liked blogs.");
      }
    } catch (err) {
      setError("Network error while fetching liked blogs.");
    }
  };

  const fetchSharedBlogs = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/api/v1/user/${id}/profile/share`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.data.status === 200) {
        setSharedBlogs(response.data.data.myBlog);
      } else {
        setError("Error fetching shared blogs.");
      }
    } catch (err) {
      setError("Network error while fetching shared blogs.");
    }
  };

  useEffect(() => {
    if (activeTab === "ban-be") {
      fetchFriends();
    } else if (activeTab === "da-thich") {
      fetchLikedBlogs();
    } else if (activeTab === "da-chia-se") {
      fetchSharedBlogs();
    }
  }, [activeTab]);

  const renderTabContent = () => {
    switch (activeTab) {
      case "bai-viet":
        return (
          <div>
            <h3>My Blogs:</h3>
            <ul className="blog-list">
              {profileData.myBlog.map((blog) => (
                <li key={blog.id} className="blog-item">
                  <h4>{blog.title}</h4>
                  <p>{blog.content}</p>
                  <small>
                    Created At: {new Date(blog.createdAt).toLocaleString()}
                  </small>
                </li>
              ))}
            </ul>
          </div>
        );
      case "ban-be":
        return (
          <div>
            <h3>Friends:</h3>
            <ul className="friend-list">
              {friendsData.length > 0 ? (
                friendsData.map((friend) => (
                  <li key={friend.id} className="friend-item">
                    <a href={`/profile/${friend.id}`}>{friend.fullName}</a>
                  </li>
                ))
              ) : (
                <p>No friends.</p>
              )}
            </ul>
          </div>
        );
      case "da-thich":
        return (
          <div>
            <h3>Liked Blogs:</h3>
            <ul className="liked-blogs-list">
              {likedBlogs.length > 0 ? (
                likedBlogs.map((blog) => (
                  <li key={blog.id} className="liked-blog-item">
                    <h4>{blog.title}</h4>
                    <p>{blog.content}</p>
                    <p><strong>Author:</strong> {blog.author}</p>
                    <div className="blog-stats">
                      <span>Likes: {blog.likesCount}</span>
                      <span>Comments: {blog.commentsCount}</span>
                      <span>Shares: {blog.sharesCount}</span>
                    </div>
                    <small>
                      Created At: {new Date(blog.createdAt).toLocaleString()}
                    </small>
                  </li>
                ))
              ) : (
                <p>No liked blogs.</p>
              )}
            </ul>
          </div>
        );
      case "da-chia-se":
        return (
          <div>
            <h3>Shared Blogs:</h3>
            <ul className="shared-blogs-list">
              {sharedBlogs.length > 0 ? (
                sharedBlogs.map((blog) => (
                  <li key={blog.id} className="shared-blog-item">
                    <h4>{blog.title}</h4>
                    <p>{blog.content}</p>
                    <p><strong>Author:</strong> {blog.author}</p>
                    <div className="blog-stats">
                      <span>Likes: {blog.likesCount}</span>
                      <span>Comments: {blog.commentsCount}</span>
                      <span>Shares: {blog.sharesCount}</span>
                    </div>
                    <small>
                      Created At: {new Date(blog.createdAt).toLocaleString()}
                    </small>
                  </li>
                ))
              ) : (
                <p>No shared blogs.</p>
              )}
            </ul>
          </div>
        );
      default:
        return null;
    }
  };

  return (
    <div className="profile-container">
      {error ? (
        <p className="error-message">{error}</p>
      ) : profileData ? (
        <div>
          <h1>Profile</h1>
          <h2>Full Name: {profileData.fullName}</h2>
          <div className="tabs">
            <button
              className={activeTab === "bai-viet" ? "active" : ""}
              onClick={() => setActiveTab("bai-viet")}
            >
              Bài viết
            </button>
            <button
              className={activeTab === "ban-be" ? "active" : ""}
              onClick={() => setActiveTab("ban-be")}
            >
              Bạn bè
            </button>
            <button
              className={activeTab === "da-thich" ? "active" : ""}
              onClick={() => setActiveTab("da-thich")}
            >
              Đã thích
            </button>
            <button
              className={activeTab === "da-chia-se" ? "active" : ""}
              onClick={() => setActiveTab("da-chia-se")}
            >
              Đã chia sẻ
            </button>
          </div>
          <div className="tab-content">{renderTabContent()}</div>
        </div>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
};

export default Profile;
