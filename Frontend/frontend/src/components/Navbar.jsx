import React, { useEffect, useState } from "react";
import axios from "axios";
import "./Css/Navbar.css"; // Import CSS
import { Link, useNavigate } from "react-router-dom";

const Navbar = ({ setIsLoggedIn }) => {
  const [user, setUser] = useState(null); // State to store user data
  const navigate = useNavigate(); // useNavigate for navigation

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

  return (
    <nav className="navbar">
      <a href="/home" className="navbar-brand">DatSocial</a>
      <ul className="navbar-menu">
        <li>
          <input type="text" className="search-bar" placeholder="Search..." />
        </li>
        <li><Link to="/addBlog">New Blog</Link></li>
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
