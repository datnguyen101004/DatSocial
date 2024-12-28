import React, { useEffect, useState } from "react";
import axios from "axios";
import "./Css/Navbar.css"; // Import CSS
import { Link, useNavigate } from "react-router-dom";
import { FaSearch } from "react-icons/fa";

const Navbar = ({ setIsLoggedIn }) => {
  const [user, setUser] = useState(null); // State to store user data
  const navigate = useNavigate(); // useNavigate for navigation
  const [searchQuery, setSearchQuery] = useState("");

  const token = localStorage.getItem("jwtToken");

  // Fetch user profile from API using axios
  useEffect(() => {
    const fetchUserProfile = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/v1/user/profile", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (response.data.status === 200) {
          setUser(response.data.data); // Store user data in state
        }
      } catch (error) {
        console.error("Error fetching user profile:", error);
      }
    };

    fetchUserProfile();
  }, [token]);

  const handleLogout = () => {
    // Clear tokens from localStorage
    localStorage.removeItem("jwtToken");
    localStorage.removeItem("refreshToken");

    // Update login status
    setIsLoggedIn(false); // Set logged-in status to false

    // Navigate to login page
    navigate("/login");
  };

  const handleSearch = () =>{
    navigate(`/home?search=${searchQuery}`);
  }

  // Xử lý khi nhấn phím Enter trong input tìm kiếm
  const handleKeyDown = (e) => {
    if (e.key === "Enter") { // Kiểm tra xem người dùng có nhấn Enter không
      handleSearch(e); // Gọi hàm handleSearch khi nhấn Enter
    }
  };

  return (
    <nav className="navbar">
      <a href="/home" className="navbar-brand">DatSocial</a>
      <ul className="navbar-menu">
        <li className="search-container">
          <input type="text" 
          onChange={(e)=>setSearchQuery(e.target.value)} 
          className="search-bar" placeholder="Search..." 
          onKeyDown={handleKeyDown}
          />
          <FaSearch className="search-icon" onClick={handleSearch}/>
        </li>
        <li><Link to="/addBlog">New Blog</Link></li>
        <li><Link to="/chat">Chat</Link></li>
        {user && (
          <>
            <li className="navbar-user">
              <Link to={`/profile/${user.id}`}>{user.fullName}</Link> {/* Add Link here to navigate to Profile with user id */}
            </li>
            <li>
              <button className="logout-button" onClick={handleLogout}>
                Logout
              </button>
            </li>
          </>
        )}
      </ul>
    </nav>
  );
};

export default Navbar;
