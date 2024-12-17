import React, { useEffect, useState } from "react";
import axios from "axios";
import "./Css/Profile.css";

const Profile = () => {
  const [profileData, setProfileData] = useState(null);
  const [error, setError] = useState("");
  const [editingBlog, setEditingBlog] = useState(null); // Trạng thái chỉnh sửa
  const [editForm, setEditForm] = useState({ title: "", content: "" });
  const token = localStorage.getItem('jwtToken');

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/v1/user/profile",
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                }
            }
        ); // API URL
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

  // Xử lý xóa bài viết
  const handleDelete = async (id) => {
    const jwtToken = localStorage.getItem("jwtToken");
    try {
      const response = await axios.post(
        `http://localhost:8080/api/v1/blog/delete/${id}`,
        {},//Khi dùng phương thức post nếu không gửi kèm body thì phải truyền body trống
        {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
          },
        }
      );
  
      if (response.data.status === 200) {
        setProfileData({
          ...profileData,
          myBlog: profileData.myBlog.filter((blog) => blog.id !== id),
        });
        console.log("Blog deleted successfully");
      } else {
        setError("Failed to delete blog.");
      }
    } catch (err) {
      console.error("Error deleting blog:", err);
      setError("Network error during delete operation.");
    }
  };

  // Bắt đầu chỉnh sửa
  const handleEditClick = (blog) => {
    setEditingBlog(blog.id);
    setEditForm({ title: blog.title, content: blog.content });
  };

  // Xử lý chỉnh sửa
  const handleEditSubmit = async (e, id) => {
    e.preventDefault();
    try {
      const response = await axios.post(`http://localhost:8080/api/v1/blog/edit/${id}`, {
        title: editForm.title,
        content: editForm.content,
      },{
        headers: {
            Authorization: `Bearer ${token}`,
        }
      });

      if (response.data.status === 200) {
        const updatedBlogs = profileData.myBlog.map((blog) =>
          blog.id === id ? { ...blog, ...editForm } : blog
        );
        setProfileData({ ...profileData, myBlog: updatedBlogs });
        setEditingBlog(null); // Thoát chế độ chỉnh sửa
      } else {
        setError("Failed to update blog.");
      }
    } catch (err) {
      setError("Network error during blog update.");
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
          <h3>My Blogs:</h3>
          <ul className="blog-list">
            {profileData.myBlog.map((blog) => (
              <li key={blog.id} className="blog-item">
                {editingBlog === blog.id ? (
                  // Form chỉnh sửa
                  <form
                    className="edit-form"
                    onSubmit={(e) => handleEditSubmit(e, blog.id)}
                  >
                    <input
                      type="text"
                      value={editForm.title}
                      onChange={(e) =>
                        setEditForm({ ...editForm, title: e.target.value })
                      }
                      placeholder="Title"
                    />
                    <textarea
                      value={editForm.content}
                      onChange={(e) =>
                        setEditForm({ ...editForm, content: e.target.value })
                      }
                      placeholder="Content"
                    ></textarea>
                    <button type="submit">Save</button>
                    <button type="button" onClick={() => setEditingBlog(null)}>
                      Cancel
                    </button>
                  </form>
                ) : (
                  // Hiển thị blog
                  <>
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
                    <div className="blog-actions">
                      <button onClick={() => handleEditClick(blog)}>Edit</button>
                      <button onClick={() => handleDelete(blog.id)}>Delete</button>
                    </div>
                  </>
                )}
              </li>
            ))}
          </ul>
        </div>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
};

export default Profile;
